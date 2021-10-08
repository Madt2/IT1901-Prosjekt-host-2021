package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import core.Exercise;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CreateControllerTest extends ApplicationTest{

    private CreateController controller = new CreateController();
    
    @FXML
    private TableView<Exercise> workout_table;
   
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Create.fxml"));
        final Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }   

    @Test
    void testTableView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                Exercise e1 = new Exercise("Benchpress", 25, 70, 5, 60);
                workout_table = controller.getWorkoutTable();
                controller.getWorkout().addExercise(e1);
                controller.setTable();
                
                // Workout name, row 0
                System.out.println(controller.getTable(0));
                Assertions.assertEquals("Benchpress", controller.getTable(0).getExerciseName());
                controller.getTable(0).setExerciseName("Squat");
                Assertions.assertNotEquals("Benchpress", controller.getTable(0).getExerciseName());
                Assertions.assertEquals("Squat", controller.getTable(0).getExerciseName());
                
                // Rep goal, row 0
                Assertions.assertEquals(25, controller.getTable(0).getRepGoal());
                controller.getTable(0).setRepGoal(30);
                Assertions.assertNotEquals(25, controller.getTable(0).getRepGoal());
                Assertions.assertEquals(30, controller.getTable(0).getRepGoal());
        
                // Weight, row 0
                Assertions.assertEquals(70, controller.getTable(0).getWeight());
                controller.getTable(0).setWeight(100);
                Assertions.assertNotEquals(70, controller.getTable(0).getWeight());
                Assertions.assertEquals(100, controller.getTable(0).getWeight());
        
                // Time to check row 1 (second row)
                Exercise e2 = new Exercise("Squat", 18, 100, 3, 60);
                controller.getWorkout().addExercise(e2);
                controller.setTable();
                
                // Sets, row 1
                Assertions.assertEquals(3, controller.getTable(1).getSets());
                controller.getTable(1).setSets(5);
                Assertions.assertNotEquals(3, controller.getTable(1).getSets());
                Assertions.assertEquals(5, controller.getTable(1).getSets());
        
                // Rest time, row 1
                Assertions.assertEquals(60, controller.getTable(1).getRestTime());
                controller.getTable(1).setRestTime(120);
                Assertions.assertNotEquals(60, controller.getTable(1).getRestTime());
                Assertions.assertEquals(120, controller.getTable(1).getRestTime());
            }
        });
    }
}