package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TableViewMatchers;
import org.testfx.matcher.control.TextMatchers;

import core.Exercise;
import core.Workout;
import core.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import json.BeastBookPersistence;

public class WorkoutControllerTest extends ApplicationTest{
    
    private WorkoutController wc;
    private User user;

    @Override
    public void start(final Stage stage) throws IOException {
        wc = new WorkoutController();
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Workout.fxml"));
        loader.setController(wc);
        user = new User();
        user.setUserName("test");
        addWorkoutsToUser();
        wc.setUser(user);
        final Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
   
    @Test
    void testEditSelectedCell() throws IOException{
        Assertions.assertEquals("Benchpress", user.getWorkout("Pull workout").getExercises().get(0).getExerciseName());
        wc.getWorkoutTable().getColumns().get(0).setId("exerciseName");
        Node node = lookup("#exerciseName").nth(1).query();
        doubleClickOn(node, MouseButton.PRIMARY).write("Biceps curl");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //saveUserState();
        
        //FxAssert.verifyThat("#exerciseName", LabeledMatchers.hasText("Hei"));
        Assertions.assertEquals("Biceps curl", user.getWorkout("Pull workout").getExercises().get(0).getExerciseName());

    
    }

    

    private void addWorkoutsToUser(){
        Workout workout1 = new Workout("Pull workout");
        workout1.addExercise(new Exercise("Benchpress", 20, 30, 40, 50));
        workout1.addExercise(new Exercise("Leg press", 25, 50, 75, 100));
        workout1.addExercise(new Exercise("Deadlift", 20, 20, 20, 20));
        workout1.addExercise(new Exercise("Biceps curl", 20, 20, 20, 20));
        user.addWorkout(workout1);

        wc.setWorkout(workout1);
    }
    
    private void saveUserState() throws IOException {
        BeastBookPersistence persistence = new BeastBookPersistence();
        persistence.setSaveFilePath(user.getUserName());
        persistence.saveUser(user);
    }

}
