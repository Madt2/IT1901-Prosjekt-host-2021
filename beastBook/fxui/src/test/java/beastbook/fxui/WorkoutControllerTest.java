package beastbook.fxui;

import beastbook.client.ClientController;
import beastbook.client.RegisterController;
import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.HistoryNotFoundException;
import beastbook.core.Exceptions.IllegalIdException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.UserAlreadyExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WorkoutControllerTest extends ApplicationTest{

  private WorkoutController wc;
  private Workout workout1;
  private RegisterController reg = new RegisterController();
  private ClientController service;
  private final String username = "testUser";
  private final String password = "password123";

  @Override
  public void start(Stage stage) throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      Exceptions.WorkoutAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException, UserAlreadyExistException {

    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/Workout.fxml"));
    wc = new WorkoutController();
    loader.setController(wc);
    reg.registerUser(username, password);
    service = new ClientController(username, password);
    wc.setService(service);
    addWorkoutsToUser();
    Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  private void addWorkoutsToUser() throws Exceptions.WorkoutAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException {

    workout1 = new Workout("Pull workout");
    List<Exercise> exerciseList1 = new ArrayList<>();
    exerciseList1.add(new Exercise("Benchpress", 20, 30, 40, 0, 50));
    exerciseList1.add(new Exercise("Biceps curl", 20, 20, 20, 0, 20));
    exerciseList1.add(new Exercise("Leg press", 25, 50, 75, 0,  100));
    exerciseList1.add(new Exercise("Deadlift", 20, 20, 20, 0, 20));

    wc.service.addWorkout(workout1, exerciseList1);

    String firstKeyId = wc.service.getWorkoutMap().keySet().stream().findFirst().get();
    wc.setWorkoutId(firstKeyId);
  }

  @Test
  void testEditSelectedCell() throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      Exceptions.ExerciseNotFoundException, InterruptedException, IOException, UserAlreadyExistException {
    
    assertEquals("Benchpress",  wc.getWorkoutTable().getItems().get(0).getName());     
    assertNotEquals("Squat",  wc.getWorkoutTable().getItems().get(0).getName());  

    wc.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    doubleClickOn(node).write("Pull ups");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    
    assertNotEquals("Benchpress", wc.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    assertEquals("Pull ups", wc.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    
    wc.getWorkoutTable().getColumns().get(5).setId("restTime");
    Node node2 = lookup("#restTime").nth(2).query();
    doubleClickOn(node2).write("6000");
    press(KeyCode.ENTER).release(KeyCode.ENTER);

    assertNotEquals(20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRestTime());
    assertEquals(6000, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRestTime());
  }

  @Test
  void testExceptionFeedback() throws Exceptions.WorkoutNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.ExerciseNotFoundException {
    wc.getWorkoutTable().getColumns().get(1).setId("repGoal");
    Node node = lookup("#repGoal").nth(1).query();
    doubleClickOn(node).write("-50");
    press(KeyCode.ENTER).release(KeyCode.ENTER);

    assertNotEquals(-50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Rep Goal must be more than 0! Value was not changed!"));
    assertEquals(20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());

    wc.getWorkoutTable().getColumns().get(1).setId("repGoal");
    node = lookup("#repGoal").nth(1).query();
    doubleClickOn(node).write("50");
    press(KeyCode.ENTER).release(KeyCode.ENTER);

    assertNotEquals(-20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
    assertEquals(50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));

    wc.getWorkoutTable().getColumns().get(1).setId("repGoal");
    node = lookup("#repGoal").nth(1).query();
    sleep(1000);
    doubleClickOn(node).write("Five");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    sleep(1000);

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Rep Goal must be a number. Value was not changed!"));
    assertEquals(50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
    assertNotEquals(-20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
  }

  @Test
  void testAddToHistory() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, HistoryNotFoundException, IllegalIdException{
    assertEquals(0, wc.service.getHistoryMap().size());
    assertNotEquals(1, wc.service.getHistoryMap().size());

    clickOn("#saveButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout was successfully added to history!"));

    assertEquals(1, wc.service.getHistoryMap().size());
    assertNotEquals(0, wc.service.getHistoryMap().size());

    wc.getWorkoutTable().getColumns().get(4).setId("repsPerSet");
    Node node = lookup("#repsPerSet").nth(1).query();
    doubleClickOn(node).write("12");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    clickOn("#saveButton");

    String firstKeyId = wc.service.getHistoryMap().keySet().stream().findFirst().get();
    String name = wc.service.getHistory(firstKeyId).getName();
    String date = wc.service.getHistory(firstKeyId).getDate();

    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("History with name: " + name + ";" + date +  " Already exists!"));
    assertEquals(1, wc.service.getHistoryMap().size());
    assertNotEquals(2, wc.service.getHistoryMap().size());
  }

  @AfterEach
  void cleanUp() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException {
    service.deleteUser();
  }
}
