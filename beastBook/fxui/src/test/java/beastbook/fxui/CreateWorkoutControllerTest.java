package beastbook.fxui;

import beastbook.client.ClientController;
import beastbook.client.RegisterController;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.ExerciseAlreadyExistsException;
import beastbook.core.Exceptions.IllegalIdException;
import beastbook.core.Exceptions.PasswordIncorrectException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.UserAlreadyExistException;
import beastbook.core.Exceptions.UserNotFoundException;
import beastbook.core.Exceptions.WorkoutAlreadyExistsException;
import beastbook.core.Exceptions.WorkoutNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateWorkoutControllerTest extends ApplicationTest{
  private CreateWorkoutController controller;
  private RegisterController reg = new RegisterController();
  private Workout workout;
  private ClientController service;
  private final String username = "testUser";
  private final String password = "password123";

  @Override
  public void start(final Stage stage) throws IOException, 
  UserNotFoundException, 
  BadPackageException, 
  ServerException, 
  URISyntaxException, 
  PasswordIncorrectException, WorkoutAlreadyExistsException, ExerciseAlreadyExistsException, WorkoutNotFoundException, IllegalIdException, UserAlreadyExistException {
    
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/Create.fxml"));
    controller = new CreateWorkoutController();
    reg.registerUser(username, password);
    loader.setController(controller);
    service = new ClientController(username, password);
    controller.setService(service);
    addWorkoutsToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }  

  private void addWorkoutsToUser() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, WorkoutAlreadyExistsException, ExerciseAlreadyExistsException, WorkoutNotFoundException, IllegalIdException{
    workout = new Workout("testWorkout");
    Exercise exercise1 = new Exercise("Benchpress", 10, 20, 30, 0, 40);
    Exercise exercise2 = new Exercise("Squat", 40, 30, 20, 0, 10);
    service.addWorkout(workout, List.of(exercise1, exercise2));
  }
      
  @Test
  void testAddExercise() {        
    clickOn("#titleInput").write("My workout");
    clickOn("#exerciseNameInput").write("Benchpress");
    clickOn("#repGoalInput").write("30");
    clickOn("#weightInput").write("80");
    clickOn("#setsInput").write("3");
    clickOn("#restInput").write("60");
    clickOn("#addExerciseButton");
    
    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);
    assertEquals("Benchpress", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName()); 
    assertNotEquals("Squat", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName()); 

    clickOn("#exerciseNameInput").write("Leg press"); 
    clickOn("#repGoalInput").write("20");
    clickOn("#weightInput").write("150");
    clickOn("#setsInput").write("5");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");

    controller.getWorkoutTable().getColumns().get(2).setId("setsInput");
    Node node2 = lookup("#setsInput").nth(2).query();
    clickOn(node2);
    assertEquals(5, controller.getWorkoutTable().getSelectionModel().getSelectedItem().getSets()); 
    assertNotEquals(33, controller.getWorkoutTable().getSelectionModel().getSelectedItem().getSets()); 
  }

  @Test 
  void testSaveWorkout() throws IOException {
    clickOn("#titleInput").write("saveTest");
    clickOn("#exerciseNameInput").write("Deadlift");
    clickOn("#repGoalInput").write("50");
    clickOn("#weightInput").write("20");
    clickOn("#setsInput").write("3");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");
    clickOn("#createButton");
    clickOn("#backButton");
    clickOn("#workoutsButton");
    
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml"));
    WorkoutOverviewController woc = new WorkoutOverviewController();
    loader.setController(woc);
    woc.setService(service);
    Parent root = loader.load();
    assertEquals("saveTest", woc.getWorkoutOverview().getItems().get(1));
    assertNotEquals("WorkoutName", woc.getWorkoutOverview().getItems().get(1));

  }
   
  @Test
  void testWrongFormatInputFails() {
    clickOn("#exerciseNameInput").write("Deadlift");
    clickOn("#repGoalInput").write("50");
    clickOn("#weightInput").write("Double");
    clickOn("#setsInput").write("3");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be a number"));
    assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
    assertThrows(IndexOutOfBoundsException.class, () -> {
      controller.getWorkoutTable().getItems().get(0);
    });
  }

  @Test
  void testWrongFormatWorksAfterChanged() {
    clickOn("#exerciseNameInput").write("Deadlift");
    clickOn("#repGoalInput").write("50");
    clickOn("#weightInput").write("Double");
    clickOn("#setsInput").write("3");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be a number"));
    assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weightInput").write("-20");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0"));
    assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());

    doubleClickOn("#weightInput").write("");
    doubleClickOn("#weightInput").write("20");
    assertEquals(CreateWorkoutController.CORRECT_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
    clickOn("#addExerciseButton");

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Exercise added and saved to the workout!"));
    controller.getWorkoutTable().getColumns().get(2).setId("weight");
    Node node = lookup("#weight").nth(1).query();
    clickOn(node);

    assertEquals(20, controller.getWorkoutTable().getSelectionModel().getSelectedItem().getWeight()); 
    assertNotEquals(58, controller.getWorkoutTable().getSelectionModel().getSelectedItem().getWeight()); 
  }
        
        
  @Test
  void testIllegalArgumentFails() {
    clickOn("#exerciseNameInput").write("Deadlift");
    clickOn("#repGoalInput").write("50");
    clickOn("#weightInput").write("-20");
    clickOn("#setsInput").write("50");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Working Weight must be more than 0!"));
    assertEquals(CreateWorkoutController.WRONG_INPUT_BORDER_COLOR, controller.getWeightInput().getStyle());
  }

  @Test
  void testCanNotSaveWorkoutWithoutName(){
    clickOn("#exerciseNameInput").write("Squat");
    clickOn("#repGoalInput").write("50");
    clickOn("#weightInput").write("20");
    clickOn("#setsInput").write("50");
    clickOn("#restInput").write("40");
    clickOn("#addExerciseButton");
    clickOn("#createButton");

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Input title is empty, please enter name of workout"));
  }

  @Test
  void testCanNotOverwriteWorkout(){
    clickOn("#titleInput").write(workout.getName());
    clickOn("#exerciseNameInput").write("B");
    clickOn("#repGoalInput").write("1");
    clickOn("#weightInput").write("2");
    clickOn("#setsInput").write("3");
    clickOn("#restInput").write("4");
    clickOn("#addExerciseButton");
    clickOn("#createButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout with name: testWorkout Already exists!"));
  }
    
  @Test
  void testWorkoutIsNotLoaded() throws IOException{  
    clickOn("#titleInput").write("rubbishWorkout");
    clickOn("#loadButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Invalid workout!"));
  }

  @Test
  void testWorkoutIsLoaded(){
    clickOn("#titleInput").write("testWorkout");
    clickOn("#loadButton");

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);

    assertEquals("Benchpress", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    assertNotEquals("Situps", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
  }

  @Test
  void testDeleteExercise() throws IOException {
    clickOn("#titleInput").write("testWorkout");
    clickOn("#loadButton");

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);
    clickOn("#deleteButton");

    controller.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node2 = lookup("#exerciseName").nth(1).query();
    clickOn(node2);
    assertEquals("Squat", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    assertNotEquals("Benchpress", controller.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
  }
 
  @AfterEach
  void cleanUp() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException {
    service.deleteUser();
  }
}