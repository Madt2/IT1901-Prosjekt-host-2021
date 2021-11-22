package beastbook.fxui;

import beastbook.client.ClientController;
import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class WorkoutOverviewControllerTest extends ApplicationTest{
  private WorkoutOverviewController woc;
    
  @Override
  public void start(final Stage stage) throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      Exceptions.WorkoutAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml"));
    woc = new WorkoutOverviewController();
    loader.setController(woc);
    woc.setService(new ClientController("Test","Test"));
    addWorkoutsToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  private void addWorkoutsToUser() throws Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, JsonProcessingException, Exceptions.ExerciseAlreadyExistsException {
    Workout workout1 = new Workout("Pull workout");
    Workout workout2 = new Workout("LEGS");
    List<Exercise> exerciseList1 = new ArrayList<>();
    List<Exercise> exerciseList2 = new ArrayList<>();

    exerciseList1.add(new Exercise("Benchpress", 20, 30, 40, 0, 50));
    exerciseList1.add(new Exercise("Biceps curl", 20, 20, 20, 0, 20));

    exerciseList2.add(new Exercise("Leg press", 25, 50, 75, 0,  100));
    exerciseList2.add(new Exercise("Deadlift", 20, 20, 20, 0, 20));

    woc.service.addWorkout(workout1, exerciseList1);
    woc.service.addWorkout(workout2, exerciseList2);
  }

  @Test
  void testOpenWorkout() {
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);
    Assertions.assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());
    clickOn("#openButton");
    FxAssert.verifyThat("#title", TextMatchers.hasText("Pull workout"));
  }

  @Test
  void testDeleteWorkout() throws IOException {
    // 2 workouts left
    Map<String,String> workoutMap = woc.service.getWorkoutMap();
    String workout1 = woc.getWorkoutOverview().getItems().get(0);
    String serviceW1 = null;
    Optional<String> firstKey = workoutMap.keySet().stream().findFirst();
    if (firstKey.isPresent()) {
      serviceW1 = workoutMap.get(firstKey);
    }
    Assertions.assertEquals(workout1, serviceW1);
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);
    Assertions.assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem());
    clickOn("#deleteButton");

    // 1 workout left
    Assertions.assertEquals(1, woc.service.getWorkoutMap().values().size());
    Assertions.assertEquals(1, woc.getWorkoutOverview().getItems().size());
    Optional<String> newFirstKey = workoutMap.keySet().stream().findFirst();
    if (newFirstKey.isPresent()) {
      serviceW1 = workoutMap.get(newFirstKey);
    }
    Assertions.assertEquals(workout1, serviceW1);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout deleted!"));
    // Tries to delete once again, without selecting a workout. Will not delete
    clickOn("#deleteButton");
    Assertions.assertEquals(woc.getWorkoutOverview().getItems().get(0), serviceW1);
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}