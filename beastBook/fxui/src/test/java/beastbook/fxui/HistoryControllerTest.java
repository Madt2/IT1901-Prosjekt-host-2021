package beastbook.fxui;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HistoryControllerTest extends ApplicationTest {
  
  private HistoryController controller;
  private History history;
  
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/History.fxml"));
    controller = new HistoryController();
    loader.setController(controller);
    User user = new User("Test", "123");
    controller.setUser(user);
    addHistoryToUser();
    controller.user.saveUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser(){
    Workout workout = new Workout("testWorkout");
    workout.addExercise(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    History history = new History(workout, "13.11.2021");
    controller.user.addWorkout(workout);
    controller.user.addHistory(history);
  }
  //TODO
  @Test
  void testCorrectHistoryIsShown(){
    FxAssert.verifyThat("#title", TextMatchers.hasText(controller.user.getHistory(history.getName(), history.getDate()).getName()));
    FxAssert.verifyThat("#date", TextMatchers.hasText(controller.user.getHistory(history.getName(), history.getDate()).getDate()));
  }
}