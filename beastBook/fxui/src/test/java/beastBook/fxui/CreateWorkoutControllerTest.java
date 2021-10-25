package beastBook.fxui;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import beastBook.core.Exercise;
import beastBook.core.Workout;
import beastBook.core.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class CreateWorkoutControllerTest extends ApplicationTest{
  private CreateWorkoutController controller;
  private User user;
    
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Create.fxml"));
    controller = new CreateWorkoutController();
    loader.setController(controller);
    final Parent root = loader.load();
    user = new User();
    controller.setUser(user);
    user.setUserName("test");
    stage.setScene(new Scene(root));
    stage.show();
  }  
      
  @Test
  void testInputFieldsAddsToWorkoutObject() {        
    clickOn("#titleInput", MouseButton.PRIMARY).write("My workout");
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Benchpress");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("30");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("80");
    clickOn("#setsInput", MouseButton.PRIMARY).write("3");
    clickOn("#restInput", MouseButton.PRIMARY).write("60");
    clickOn("#addButton", MouseButton.PRIMARY);
       
    Assertions.assertEquals(60, controller.getWorkoutTable().getItems().get(0).getRestTime()); //Adds to row
    Assertions.assertEquals(1, controller.getWorkout().getExercises().size()); //Adds to object
       
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Leg press"); // Add another object 
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("20");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("150");
    clickOn("#setsInput", MouseButton.PRIMARY).write("5");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

    Assertions.assertEquals(2, controller.getWorkout().getExercises().size());
  }
   
  @Test
  void testWrongFormatInputFails() {
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Deadlift");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("50");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("Double");
    clickOn("#setsInput", MouseButton.PRIMARY).write("3");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

    Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be a number"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
  }

  @Test
  void testWrongFormatWorksAfterChanged() {
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Deadlift");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("50");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("Double");
    clickOn("#setsInput", MouseButton.PRIMARY).write("3");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

    Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be a number"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("-20");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("");
    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("20");
    Assertions.assertEquals(CreateWorkoutController.CORRECT_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
    clickOn("#addButton", MouseButton.PRIMARY);
    
    Assertions.assertEquals(1, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));
  }
        
        
  @Test
  void testIllegalArgumentFails() {
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Deadlift");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("50");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("-20");
    clickOn("#setsInput", MouseButton.PRIMARY).write("50");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

    Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0!"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
  }

  @Test
  void canNotSaveWorkoutWithoutName(){
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Squat");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("50");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("20");
    clickOn("#setsInput", MouseButton.PRIMARY).write("50");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);
    clickOn("#createButton", MouseButton.PRIMARY);

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Input title is empty, please enter name to workout"));
    Assertions.assertEquals(0, user.getWorkouts().size());
  }
    
  @Test
  void workoutIsNotLoaded(){
    Workout workout = new Workout("testWorkout");
    workout.addExercise(new Exercise("Benchpress", 20, 20, 20, 20));
     
    clickOn("#titleInput", MouseButton.PRIMARY).write("testWorkout");
    clickOn("#loadButton", MouseButton.PRIMARY);

    Assertions.assertEquals(null, controller.getWorkout());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout not found!"));
  }

  @Test
  void workoutIsLoaded(){
    Workout workout = new Workout("testWorkout");
    workout.addExercise(new Exercise("Benchpress", 20, 30, 40, 50));
    user.addWorkout(workout);

    clickOn("#titleInput", MouseButton.PRIMARY).write("testWorkout");
    clickOn("#loadButton", MouseButton.PRIMARY);

    Assertions.assertEquals("Benchpress", controller.getTable(0).getExerciseName());
    Assertions.assertEquals(20, controller.getTable(0).getRepGoal());
    Assertions.assertEquals(30, controller.getTable(0).getWeight());
    Assertions.assertEquals(40, controller.getTable(0).getSets());
    Assertions.assertEquals(50, controller.getTable(0).getRestTime());
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}