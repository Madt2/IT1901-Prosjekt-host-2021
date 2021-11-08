package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateWorkoutControllerTest extends ApplicationTest{
  private CreateWorkoutController controller;
  private User user = new User("Test", "123");
  private Workout workout = new Workout();
  private Exercise exercise1 = new Exercise();
  private Exercise exercise2 = new Exercise();
    
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/Create.fxml"));
    controller = new CreateWorkoutController();
    loader.setController(controller);
    controller.setUser(user);
    user.saveUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }  

  @BeforeEach
  void setup(){
    user.getWorkouts().clear();
    workout = new Workout("testWorkout");
    exercise1 = new Exercise("Benchpress", 10, 20, 30, 0, 40);
    exercise2 = new Exercise("Squat", 40, 30, 20, 0, 10);
    workout.addExercise(exercise1);
    workout.addExercise(exercise2);
    user.addWorkout(workout);
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
    //Assertions.assertEquals(1, controller.getWorkout().getExercises().size()); //Adds to object
       
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Leg press"); // Add another object 
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("20");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("150");
    clickOn("#setsInput", MouseButton.PRIMARY).write("5");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

   // Assertions.assertEquals(2, controller.getWorkout().getExercises().size());
  }
   
  @Test
  void testWrongFormatInputFails() {
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("Deadlift");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("50");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("Double");
    clickOn("#setsInput", MouseButton.PRIMARY).write("3");
    clickOn("#restInput", MouseButton.PRIMARY).write("40");
    clickOn("#addButton", MouseButton.PRIMARY);

    //Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
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

   // Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be a number"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("-20");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("");
    doubleClickOn("#weigthInput", MouseButton.PRIMARY).write("20");
    Assertions.assertEquals(CreateWorkoutController.CORRECT_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
    clickOn("#addButton", MouseButton.PRIMARY);
    
    //Assertions.assertEquals(1, controller.getWorkout().getExercises().size());
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

    //Assertions.assertEquals(0, controller.getWorkout().getExercises().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0!"));
    Assertions.assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
  }

  @Test
  void testCanNotSaveWorkoutWithoutName(){
    user.removeWorkout(workout);

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
  void testCanNotCreateWorkoutWithSameName(){
    clickOn("#titleInput", MouseButton.PRIMARY).write(workout.getName());
    clickOn("#exerciseNameInput", MouseButton.PRIMARY).write("B");
    clickOn("#repGoalInput", MouseButton.PRIMARY).write("1");
    clickOn("#weigthInput", MouseButton.PRIMARY).write("2");
    clickOn("#setsInput", MouseButton.PRIMARY).write("3");
    clickOn("#restInput", MouseButton.PRIMARY).write("4");
    clickOn("#addButton", MouseButton.PRIMARY);
    clickOn("#createButton", MouseButton.PRIMARY);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("User already has workout testWorkout saved! Workout was not created, please choose another name."));
    Assertions.assertEquals("Benchpress", user.getWorkout("testWorkout").getExercises().get(0).getExerciseName());
    Assertions.assertNotEquals("Squat", user.getWorkout("testWorkout").getExercises().get(0).getExerciseName());
  }
    
  @Test
  void testWorkoutIsNotLoaded(){   
    user.removeWorkout(workout);  

    clickOn("#titleInput", MouseButton.PRIMARY).write("testWorkout");
    clickOn("#loadButton", MouseButton.PRIMARY);

    //Assertions.assertEquals(null, controller.getWorkout().getName());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout not found!"));
  }

  @Test
  void testWorkoutIsLoaded(){
    clickOn("#titleInput", MouseButton.PRIMARY).write("testWorkout");
    clickOn("#loadButton", MouseButton.PRIMARY);

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);

    Exercise loadedExercise = controller.getWorkoutTable().getSelectionModel().getSelectedItem();
    Assertions.assertEquals(exercise1, loadedExercise);
  }

  @Test
  void testDeleteExercise() throws InterruptedException{
    clickOn("#titleInput", MouseButton.PRIMARY).write("testWorkout");
    clickOn("#loadButton", MouseButton.PRIMARY);

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);
    clickOn("#deleteButton");

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Assertions.assertEquals(1, user.getWorkout("testWorkout").getExercises().size());
    Node node2 = lookup("#exerciseName").nth(1).query();
    clickOn(node2);

    Exercise lastExercise = controller.getWorkoutTable().getSelectionModel().getSelectedItem();
    Assertions.assertEquals(exercise2, lastExercise);

    clickOn("#deleteButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Could not delete exercise '" + lastExercise.getExerciseName() 
    + "', at least one exercise has to be in every workout!"));
  }
 

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}