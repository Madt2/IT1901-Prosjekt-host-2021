package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import core.Exercise;
import core.Workout;
import core.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


public class WorkoutOverviewControllerTest extends ApplicationTest{
    
    private WorkoutOverviewController woc;
    private User user = new User();

    @FXML
    private TableView<Workout> workout_overview;
    
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
        woc = new WorkoutOverviewController();
        loader.setController(woc);
        user.setUserName("Brage");
        user.setPassword("12345");
        woc.setUser(user);
        final Parent root = loader.load();
        

        stage.setScene(new Scene(root));
        stage.show();
    }
    /*
    @BeforeEach
    void setup(){
        e1 = new Exercise("Deadlifts", 8, 100, 3, 180);
        w1 = new Workout("Back");
        w2 = new Workout("Push");
        
        w1.addExercise(e1);
        woc.addToAllWorkouts(w1);
        woc.addToAllWorkouts(w2);
    }*/
    /*
    @Test
    void testSetupTableView() {
        woc = new WorkoutOverviewController();
      
        w1 = new Workout("Back");
        w2 = new Workout("Push");
        user.addWorkout(w1);
        user.addWorkout(w2);
        e1 = new Exercise("Deadlifts", 8, 100, 3, 180);
        
        w1.addExercise(e1);
        woc.addToAllWorkouts(w1);
        woc.addToAllWorkouts(w2);
        woc.setTable();

        Assertions.assertEquals("Back", woc.getTable(0).getName());
        woc.getTable(0).setName("Biceps");
        Assertions.assertNotEquals("Back", woc.getTable(0).getName());
        Assertions.assertEquals("Biceps", woc.getTable(0).getName());
        Assertions.assertEquals("Push", woc.getTable(1).getName());

        Workout w3 = new Workout("Core");

        woc.addToAllWorkouts(w3);
        woc.setTable();
        Assertions.assertEquals("Core", woc.getTable(2).getName());
    }*/
    
    @Test
    void testClickedWorkoutIsCorrect(){
       
        /*Platform.setImplicitExit(false);
        WorkoutOverviewController newController = new WorkoutOverviewController();
        newController = woc;*/
        /*
        Workout w1 = new Workout("Workout 1");
        Workout w2 = new Workout("Workout 2");
        Workout w3 = new Workout("Workout 3");
        */
        /*
        user.addWorkout(w1);
        user.addWorkout(w2);
        user.addWorkout(w3);
        */
        /*

        workout_overview = woc.getWorkoutOverview();
        woc.setTable();
        */
        //clickOn("#workout_overview").write("HEI!");

    }
}