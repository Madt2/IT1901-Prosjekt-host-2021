package beastbook.fxui;

import beastbook.client.ClientController;
import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WorkoutControllerTest extends ApplicationTest{
  private WorkoutController wc;
  private Workout workout1;
  private Workout workout2;

  @Override
  public void start(Stage stage) throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      Exceptions.WorkoutAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/Workout.fxml"));
    wc = new WorkoutController();
    loader.setController(wc);
    wc.setService(new ClientController("Test", "Test"));
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
      Exceptions.ExerciseAlreadyExistsException {
    workout1 = new Workout("Pull workout");
    workout2 = new Workout("LEGS");
    List<Exercise> exerciseList1 = new ArrayList<>();
    List<Exercise> exerciseList2 = new ArrayList<>();

    exerciseList1.add(new Exercise("Benchpress", 20, 30, 40, 0, 50));
    exerciseList1.add(new Exercise("Biceps curl", 20, 20, 20, 0, 20));

    exerciseList2.add(new Exercise("Leg press", 25, 50, 75, 0,  100));
    exerciseList2.add(new Exercise("Deadlift", 20, 20, 20, 0, 20));

    wc.service.addWorkout(workout1, exerciseList1);
    wc.service.addWorkout(workout2, exerciseList2);
  }

  @Test
  void testEditSelectedCell() throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.ExerciseNotFoundException {
    Map<String,String> exerciseMap = wc.service.getExerciseMap();
    //String exercise1 = wc.getWorkoutTable().getItems().get(0).getName();
    String serviceE1 = null;
    Optional<String> firstKey = exerciseMap.keySet().stream().findFirst();
    if (firstKey.isPresent()) {
      serviceE1 = exerciseMap.get(firstKey);
    }

    Assertions.assertEquals("Benchpress",  wc.getWorkoutTable().getItems().get(0).getName());     //wc.user.getWorkout("Pull workout").getExercises().get(0).getExerciseName());
    wc.getWorkoutTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    doubleClickOn(node, MouseButton.PRIMARY).write("Pull ups");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    
    Assertions.assertNotEquals("Benchpress", wc.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    Assertions.assertEquals("Pull ups", wc.getWorkoutTable().getSelectionModel().getSelectedItem().getName());
    Map<String,String> workoutMap = wc.service.getWorkoutMap();
    String eId = null;
    for (String id: workoutMap.keySet()) {
      if (workoutMap.get(id).equals("Pull ups")) {
        eId = id;
      }
    }
    wc.service.getExercise(eId);
    Assertions.assertEquals("Pull ups",wc.service.getExercise(eId).getName());
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
    doubleClickOn(node, MouseButton.PRIMARY).write("-50");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    Assertions.assertNotEquals(-50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());

    String workout1Id = wc.service.getWorkout(workout1.getId()).getExerciseIds().get(0);
    int repGoalExercise1 = wc.service.getExercise(workout1Id).getRepGoal();

    Assertions.assertNotEquals(-50, repGoalExercise1);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Rep Goal must be more than 0! Value was not changed!"));

    Assertions.assertEquals(20, repGoalExercise1);
    Assertions.assertEquals(20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());

    wc.getWorkoutTable().getColumns().get(1).setId("repGoal");
    node = lookup("#repGoal").nth(1).query();
    doubleClickOn(node, MouseButton.PRIMARY).write("50");
    press(KeyCode.ENTER).release(KeyCode.ENTER);

    Assertions.assertNotEquals(-20, repGoalExercise1);
    Assertions.assertNotEquals(-20, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());

    Assertions.assertEquals(50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
    Assertions.assertEquals(50, repGoalExercise1);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));

    wc.getWorkoutTable().getColumns().get(1).setId("repGoal");
    node = lookup("#repGoal").nth(1).query();
    sleep(1000);
    doubleClickOn(node, MouseButton.PRIMARY).write("Five");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    
    sleep(1000);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Rep Goal must be a number. Value was not changed!"));
    Assertions.assertEquals(50, repGoalExercise1);
    Assertions.assertEquals(50, wc.getWorkoutTable().getSelectionModel().getSelectedItem().getRepGoal());
  }

  @Test
  void testAddToHistory(){
    Assertions.assertEquals(0, wc.service.getHistoryMap().size());
    clickOn("#saveButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout was successfully added to history!"));
    Assertions.assertEquals(1, wc.service.getHistoryMap().size());

    wc.getWorkoutTable().getColumns().get(4).setId("repsPerSet");
    Node node = lookup("#repsPerSet").nth(1).query();
    doubleClickOn(node, MouseButton.PRIMARY).write("12");
    press(KeyCode.ENTER).release(KeyCode.ENTER);
    clickOn("#saveButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("History overwritten!"));
    Assertions.assertEquals(1, wc.service.getHistoryMap().size());
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    if (file.delete()) {
      System.out.println("Successfully deleted testworkout");
    } else {
      throw new IllegalStateException();
    }
  }
}
