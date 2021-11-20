package beastbook.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import beastbook.client.ClientController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
  private History history1;
  private History history2;
  
  @Override
  public void start(final Stage stage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/HistoryOverview.fxml"));
    controller = new HistoryOverviewController();
    loader.setController(controller);
    controller.setService(new ClientController("Test", "Test"));
    addHistoryToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser(){
    List<Exercise> exercises1 = new ArrayList<>();
    exercises1.add(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    exercises1.add(new Exercise("Chestpress", 90, 35, 22, 33, 94));
    history1 = new History("myHistory1", exercises1);

    List<Exercise> exercises2 = new ArrayList<>();
    exercises2.add(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    history2 = new History("myHistory2", exercises2);

    controller.service.addHistory(history1);
    controller.service.addHistory(history2);
  }

  @Test
  void testCorrectHistoryIsOpened(){
    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);
    clickOn("#openButton");
    // might be wrong:
    FxAssert.verifyThat("#title", TextMatchers.hasText(history1.getName()));
    FxAssert.verifyThat("#date", TextMatchers.hasText(history1.getDate()));
  }

  @Test
  void testDeleteHistory(){
/*    Map<String,String> historyMap = controller.service.getHistoryMap();
    String historyLine = controller.getHistoryOverview().getItems().get(0);
    String serviceH1 = null;
    Optional<String> firstKey = historyMap.keySet().stream().findFirst();
    if (firstKey.isPresent()) {
      serviceH1 = historyMap.get(firstKey);
    }
    Assertions.assertEquals(historyLine, serviceH1);

    assertEquals(2, controller.service.getWorkoutMap().values().size());
    controller.getHistoryOverview().getColumns().get(0).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);
    clickOn("#deleteButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("History entry deleted!"));
    assertEquals(controller.getHistoryOverview().getItems(), controller.service.getWorkoutMap().values());
    assertEquals(1, controller.service.getWorkoutMap().values().size());

    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node2 = lookup("#historyName").nth(1).query();
    clickOn(node2);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));*/
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}