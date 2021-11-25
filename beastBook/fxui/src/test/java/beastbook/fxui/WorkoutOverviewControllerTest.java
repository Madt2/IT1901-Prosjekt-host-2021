package beastbook.fxui;

import beastbook.client.ClientController;
import beastbook.client.RegisterController;
import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.ExerciseNotFoundException;
import beastbook.core.Exceptions.IllegalIdException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.UserAlreadyExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.List;
import java.util.ArrayList;

public class WorkoutOverviewControllerTest extends ApplicationTest{
  private WorkoutOverviewController woc;
  private RegisterController reg = new RegisterController();
  private ClientController service;
  private final String username = "testUser";
  private final String password = "password123";

    
  @Override
  public void start(final Stage stage) throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      Exceptions.WorkoutAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException, UserAlreadyExistException {

    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml"));
    woc = new WorkoutOverviewController();
    reg.registerUser(username, password);
    loader.setController(woc);
    service = new ClientController(username, password);
    woc.setService(service);
    addWorkoutsToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  private void addWorkoutsToUser() throws Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, JsonProcessingException, Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException {
    Workout workout1 = new Workout("Pull workout");
    List<Exercise> exerciseList1 = new ArrayList<>();
    exerciseList1.add(new Exercise("Benchpress", 20, 30, 40, 0, 50));
    exerciseList1.add(new Exercise("Biceps curl", 20, 600, 20, 0, 70));
    
    Workout workout2 = new Workout("LEGS");
    List<Exercise> exerciseList2 = new ArrayList<>();
    exerciseList2.add(new Exercise("Leg press", 25, 50, 75, 0,  100));
    exerciseList2.add(new Exercise("Deadlift", 20, 20, 20, 0, 20));

    woc.service.addWorkout(workout1, exerciseList1);
    woc.service.addWorkout(workout2, exerciseList2);   
  }

  @Test
  void testOpenWorkout() throws BadPackageException, ServerException, URISyntaxException, ExerciseNotFoundException, IllegalIdException, IOException {
    
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);

    assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());
    assertNotEquals("workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());

    clickOn("#openButton");
    FxAssert.verifyThat("#title", TextMatchers.hasText("Pull workout"));
    
    String firstKeyId = woc.service.getWorkoutMap().keySet().stream().findFirst().get();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/beastbook.fxui/Workout.fxml"));
    WorkoutController wc = new WorkoutController();
    wc.setWorkoutId(firstKeyId);
    loader.setController(wc);
    wc.setService(service);
    Parent root = loader.load();

    assertEquals("Biceps curl", wc.getWorkoutTable().getItems().get(1).getName());
    assertNotEquals("Hammer curl", wc.getWorkoutTable().getItems().get(1).getName());

    assertEquals(70, wc.getWorkoutTable().getItems().get(1).getRestTime());
    assertNotEquals(280, wc.getWorkoutTable().getItems().get(1).getRestTime());

    assertEquals(600,  wc.getWorkoutTable().getItems().get(1).getWeight());
    assertNotEquals(25,  wc.getWorkoutTable().getItems().get(1).getWeight());
  }

  @Test
  void testDeleteWorkout() throws IOException {
   
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);
    assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());
    assertNotEquals("Push workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());
    clickOn("#deleteButton");

    assertEquals(1, woc.getWorkoutOverview().getItems().size());
    assertNotEquals(2, woc.getWorkoutOverview().getItems().size());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout deleted!"));

    clickOn("#backButton");
    clickOn("#createButton");
    clickOn("#titleInput").write("Pull workout");
    clickOn("#loadButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Invalid workout!"));
  }

  @AfterEach
  void cleanUp() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException {
    service.deleteUser();
  }
}