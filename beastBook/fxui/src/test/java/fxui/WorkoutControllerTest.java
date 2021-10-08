package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import core.Exercise;
import core.Workout;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class WorkoutControllerTest extends ApplicationTest{
    
    private WorkoutController wc;

    @FXML
    private TableView<Exercise> workout_table;

    private Exercise e1;
    private Exercise e2;
    private Exercise e3;
    private Exercise e4;
    //private Workout w1;

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Workout.fxml"));
        final Parent root = loader.load();
        wc = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setup(){
        e1 = new Exercise("Deadlift", 8, 100, 3, 180);
        e2 = new Exercise("Reverse flyes", 12, 20, 3, 120);
        e3 = new Exercise("Wide rows", 12, 20, 3, 120);
        e4 = new Exercise("Dumbell shrugs", 12, 30, 3, 120);

        //w1 = new Workout("Back");
        workout_table = wc.getWorkoutTable();
        wc.getWorkout().addExercise(e1);
        wc.getWorkout().addExercise(e2);
        wc.getWorkout().addExercise(e3);
        wc.getWorkout().addExercise(e4);
        //w1.addExercise(e1);
    }

    @Test
    void testSetupTableView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                wc.setTable();
                
                // Workout name, row 0
                System.out.println(wc.getTable(0));
                Assertions.assertEquals("Deadlift", wc.getTable(0).getExerciseName());
                wc.getTable(0).setExerciseName("Dumbell deadlift");
                Assertions.assertNotEquals("Deadlift", wc.getTable(0).getExerciseName());
                Assertions.assertEquals("Dumbell deadlift", wc.getTable(0).getExerciseName());
                
                // Rep goal, row 0
                Assertions.assertEquals(8, wc.getTable(0).getRepGoal());
                wc.getTable(0).setRepGoal(5);
                Assertions.assertNotEquals(8, wc.getTable(0).getRepGoal());
                Assertions.assertEquals(5, wc.getTable(0).getRepGoal());
        
                /*
                // Weight, row 0
                Assertions.assertEquals(70, wc.getTable(0).getWeight());
                wc.getTable(0).setWeight(100);
                Assertions.assertNotEquals(70, wc.getTable(0).getWeight());
                Assertions.assertEquals(100, wc.getTable(0).getWeight());
        
                // Time to check row 1 (second row)
                
                
                // Sets, row 1
                Assertions.assertEquals(3, wc.getTable(1).getSets());
                wc.getTable(1).setSets(5);
                Assertions.assertNotEquals(3, wc.getTable(1).getSets());
                Assertions.assertEquals(5, wc.getTable(1).getSets());
        
                // Rest time, row 1
                Assertions.assertEquals(60, wc.getTable(1).getRestTime());
                wc.getTable(1).setRestTime(120);
                Assertions.assertNotEquals(60, wc.getTable(1).getRestTime());
                Assertions.assertEquals(120, wc.getTable(1).getRestTime());
                */
            }
        });
    }
}
