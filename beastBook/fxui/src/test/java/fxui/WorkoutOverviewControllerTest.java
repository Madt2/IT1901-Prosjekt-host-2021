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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;


public class WorkoutOverviewControllerTest extends ApplicationTest{
    
    private WorkoutOverviewController woc;
    private User user = new User();
    
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
        woc = new WorkoutOverviewController();
        loader.setController(woc);
        woc.setUser(user);
        addWorkoutsToUser();
        final Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @Test
    void testClickedRow1IsCorrectWorkout(){
        WorkoutController wc = new WorkoutController();
        wc.setUser(user);

        woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
        Node node = lookup("#workoutName").nth(1).query();
        clickOn(node);

        wc.setWorkout(user.getWorkouts().get(0));

        Assertions.assertEquals(user.getWorkouts().get(0), woc.getWorkout());
        Assertions.assertNotEquals(user.getWorkouts().get(1), woc.getWorkout());

        Assertions.assertEquals("Pull workout", wc.getWorkout().getName());
        Assertions.assertEquals("Benchpress", wc.getWorkout().getExercises().get(0).getExerciseName());
        Assertions.assertEquals(20, wc.getWorkout().getExercises().get(0).getRepGoal());
        Assertions.assertEquals(30, wc.getWorkout().getExercises().get(0).getWeight());
        Assertions.assertEquals(40, wc.getWorkout().getExercises().get(0).getSets());
        Assertions.assertEquals(50, wc.getWorkout().getExercises().get(0).getRestTime());
    }

    @Test
    void testClickedRow2IsCorrectWorkout(){
        WorkoutController wc = new WorkoutController();
        wc.setUser(user);

        woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
        Node node2 = lookup("#workoutName").nth(2).query();
        clickOn(node2);

        wc.setWorkout(user.getWorkouts().get(1));

        Assertions.assertEquals(user.getWorkouts().get(1), woc.getWorkout());
        Assertions.assertNotEquals(user.getWorkouts().get(0), woc.getWorkout());
    }

    private void addWorkoutsToUser(){
        Workout workout1 = new Workout("Pull workout");
        Workout workout2 = new Workout("LEGS");
        workout1.addExercise(new Exercise("Benchpress", 20, 30, 40, 50));
        workout1.addExercise(new Exercise("Leg press", 25, 50, 75, 100));
        workout1.addExercise(new Exercise("Deadlift", 20, 20, 20, 20));
        workout1.addExercise(new Exercise("Biceps curl", 20, 20, 20, 20));

        user.addWorkout(workout1);
        user.addWorkout(workout2);
    }
}