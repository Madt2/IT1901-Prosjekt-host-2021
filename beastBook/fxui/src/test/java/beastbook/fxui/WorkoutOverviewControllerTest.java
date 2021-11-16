package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;
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

public class WorkoutOverviewControllerTest extends ApplicationTest{
  private WorkoutOverviewController woc;
  private User user = new User("Test", "123");
    
  @Override
  public void start(final Stage stage) throws IOException {
    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml"));
    woc = new WorkoutOverviewController();
    loader.setController(woc);
    woc.setUser(user);
    addWorkoutsToUser();
    woc.user.saveUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  void testOpenWorkout() {
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);
    Assertions.assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem().getName());
    clickOn("#openButton");
    FxAssert.verifyThat("#title", TextMatchers.hasText("Pull workout"));
  }

  @Test
  void testDeleteWorkout() throws IOException {
    // 2 workouts left
    Assertions.assertEquals(woc.getWorkoutOverview().getItems().get(0).getName(), woc.user.getWorkouts().get(0).getName());
    woc.getWorkoutOverview().getColumns().get(0).setId("workoutName");
    Node node = lookup("#workoutName").nth(1).query();
    clickOn(node);
    Assertions.assertEquals("Pull workout", woc.getWorkoutOverview().getSelectionModel().getSelectedItem().getName());
    clickOn("#deleteButton");
    
    // 1 workout left
    Assertions.assertEquals(1, woc.user.getWorkouts().size());
    Assertions.assertEquals(1, woc.getWorkoutOverview().getItems().size());
    Assertions.assertEquals(woc.getWorkoutOverview().getItems().get(0).getName(), woc.user.getWorkouts().get(0).getName());
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("Workout deleted!"));
    // Tries to delete once again, without selecting a workout. Will not delete
    clickOn("#deleteButton");
    Assertions.assertEquals(woc.getWorkoutOverview().getItems().get(0).getName(), woc.user.getWorkouts().get(0).getName());
  }
 
  private void addWorkoutsToUser() {
    Workout workout1 = new Workout("Pull workout");
    Workout workout2 = new Workout("LEGS");
    workout1.addExercise(new Exercise("Benchpress", 20, 30, 40, 0, 50));
    workout1.addExercise(new Exercise("Leg press", 25, 50, 75, 0,  100));
    workout1.addExercise(new Exercise("Deadlift", 20, 20, 20, 0, 20));
    workout1.addExercise(new Exercise("Biceps curl", 20, 20, 20, 0, 20));
    woc.user.addWorkout(workout1);
    woc.user.addWorkout(workout2);
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}