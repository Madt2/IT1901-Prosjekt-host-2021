package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextInputControlMatchers;

import core.Exercise;
import core.Workout;
import core.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;


public class CreateControllerTest extends ApplicationTest{

    private CreateController controller;
    private User user;
    
    //@FXML
    //private TableView<Exercise> workout_table;
   
    
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Create.fxml"));
        controller = new CreateController();
        loader.setController(controller);
        final Parent root = loader.load();
        user = new User();
        controller.setUser(user);

        //controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }  
    
    @Test
    void testSetupTableView() {
/*
        //controller = new CreateController();
        Exercise e1 = new Exercise("Benchpress", 25, 70, 5, 60);
        //workout_table = controller.getWorkoutTable();
        controller.getWorkout().addExercise(e1);
        controller.setTable();
                
                // Workout name, row 0
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
        */
    }

    
    @Test
    
    void testInputFieldsAddsToWorkoutObject() {

        //workout_table = controller.getWorkoutTable();
        
        clickOn("#titleInput", MouseButton.PRIMARY).write("My workout");
        clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Benchpress");
        clickOn("#repsInput", MouseButton.PRIMARY).write("30");
        clickOn("#weigthInput", MouseButton.PRIMARY).write("80");
        clickOn("#setsInput", MouseButton.PRIMARY).write("3");
        clickOn("#restInput", MouseButton.PRIMARY).write("60");
        clickOn("#addButton", MouseButton.PRIMARY);
        
        // Adds to row
        Assertions.assertEquals(60, controller.getWorkoutTable().getItems().get(0).getRestTime());

        // Adds to object
        Assertions.assertEquals(1, controller.getWorkout().getExercises().size());
       
        // Add another object 
        clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Leg press");
        clickOn("#repsInput", MouseButton.PRIMARY).write("20");
        clickOn("#weigthInput", MouseButton.PRIMARY).write("150");
        clickOn("#setsInput", MouseButton.PRIMARY).write("5");
        clickOn("#restInput", MouseButton.PRIMARY).write("40");
        clickOn("#addButton", MouseButton.PRIMARY);
        Assertions.assertEquals(2, controller.getWorkout().getExercises().size());
    }
   
    @Test
    void testWrongInputFails() {
       /*
        CreateController newController = new CreateController();
        newController = controller;
        workout_table = newController.getWorkoutTable();
        */
        //workout_table = controller.getWorkoutTable();
        // Should not another object because of wrong format
        clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Deadlift");
        clickOn("#repsInput", MouseButton.PRIMARY).write("50");
        clickOn("#weigthInput", MouseButton.PRIMARY).write("Not a Double");
        clickOn("#setsInput", MouseButton.PRIMARY).write("Not a Integer");
        clickOn("#restInput", MouseButton.PRIMARY).write("40");
        clickOn("#addButton", MouseButton.PRIMARY);
        Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
        Assertions.assertNotEquals("", controller.getExceptionFeedback().toString());
        }
        

}