package beastbook.fxui;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HistoryOverviewControllerTest extends ApplicationTest {
  
  private HistoryOverviewController controller;
  private User user;
  
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/HistoryOverview.fxml"));
    controller = new HistoryOverviewController();
    loader.setController(controller);
    user = new User("Test", "123");
    controller.setUser(user);
    controller.user.saveUser();
    addHistoryToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser(){
    Workout workout = new Workout("testWorkout");
    workout.addExercise(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    History history = new History(workout, "13.11.2021");
    controller.user.addHistory(history);
  }
    
  //TODO
  @Test
  void test(){

  }
}