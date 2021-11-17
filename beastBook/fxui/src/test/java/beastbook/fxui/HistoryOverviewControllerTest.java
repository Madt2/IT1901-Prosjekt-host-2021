package beastbook.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HistoryOverviewControllerTest extends ApplicationTest {
  
  private HistoryOverviewController controller;
  private User user;
  private History history;
  private History history2;
  
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/HistoryOverview.fxml"));
    controller = new HistoryOverviewController();
    loader.setController(controller);
    user = new User("Test", "123");
    addHistoryToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser(){
    Workout workout1 = new Workout("testWorkout");
   // workout1.addExercise(new Exercise("Benchpress", 20, 30, 40, 50, 50));
   // workout1.addExercise(new Exercise("Chestpress", 90, 35, 22, 33, 94));
   // history = new History(workout1, "13.11.2021");

    Workout workout2 = new Workout("test2");
   // workout2.addExercise(new Exercise("Benchpress", 20, 30, 40, 50, 50));
   // history2 = new History(workout2, "14.11.2021");
   
   // controller.user.addHistory(history);
   // controller.user.addHistory(history2);
  }
    
  
  @Test
  void testCorrectHistoryIsOpened(){
    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);
    clickOn("#openButton");
  //  FxAssert.verifyThat("#title", TextMatchers.hasText(controller.user.getHistory(history.getName(), history.getDate()).getName()));
  //  FxAssert.verifyThat("#date", TextMatchers.hasText(controller.user.getHistory(history.getName(), history.getDate()).getDate()));
  }

  @Test
  void testDeleteHistory(){
  //  assertEquals(2, controller.user.getHistories().size());
    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);
    clickOn("#deleteButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("History entry deleted!"));
  //  assertEquals(controller.getHistoryOverview().getItems(), controller.user.getHistories());
  //  assertEquals(1, controller.user.getHistories().size());

    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node2 = lookup("#historyName").nth(1).query();
    clickOn(node2);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}