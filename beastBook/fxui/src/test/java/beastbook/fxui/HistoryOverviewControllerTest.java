package beastbook.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import beastbook.client.ClientController;
import beastbook.client.RegisterController;
import beastbook.core.*;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.UserAlreadyExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HistoryOverviewControllerTest extends ApplicationTest {
  
  private HistoryOverviewController controller;
  private History history1;
  private History history2;
  private RegisterController reg = new RegisterController();
  private ClientController service;
  private final String username = "testUser";
  private final String password = "password123";
  
  @Override
  public void start(final Stage stage) throws IOException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.HistoryAlreadyExistsException,
      URISyntaxException,
      Exceptions.UserNotFoundException,
      Exceptions.PasswordIncorrectException, UserAlreadyExistException {

    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/HistoryOverview.fxml"));
    controller = new HistoryOverviewController();
    loader.setController(controller);
    reg.registerUser(username, password);
    service = new ClientController(username, password);
    controller.setService(service);
    addHistoryToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser() throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException, URISyntaxException, JsonProcessingException {
    List<Exercise> exercises1 = new ArrayList<>();
    exercises1.add(new Exercise("Benchpress", 20, 30, 40, 50, 718));
    exercises1.add(new Exercise("Chestpress", 90, 35, 22, 33, 94));
    history1 = new History("myHistory1", exercises1);

    List<Exercise> exercises2 = new ArrayList<>();
    exercises2.add(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    history2 = new History("myHistory2", exercises2);

    controller.service.addHistory(history1);
    controller.service.addHistory(history2);
  }

  @Test
  void testCorrectHistoryIsOpened() throws IOException{
    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);

    String firstKeyId = controller.service.getHistoryMap().keySet().stream().findFirst().get();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/beastbook.fxui/History.fxml"));
    HistoryController hc = new HistoryController();
    hc.setHistoryId(firstKeyId);
    loader.setController(hc);
    hc.setService(service);
    Parent root = loader.load();
    
    clickOn("#openButton");
    FxAssert.verifyThat("#title", TextMatchers.hasText(history1.getName()));
    FxAssert.verifyThat("#date", TextMatchers.hasText(history1.getDate()));

    assertEquals(718, hc.getHistoryTable().getItems().get(0).getRestTime());
    assertNotEquals(48, hc.getHistoryTable().getItems().get(0).getRestTime());

    assertEquals("Chestpress", hc.getHistoryTable().getItems().get(1).getName());
    assertNotEquals("Squat", hc.getHistoryTable().getItems().get(1).getName());
  }

  @Test
  void testDeleteHistory(){
    controller.getHistoryOverview().getColumns().get(0).setId("historyName");
    Node node = lookup("#historyName").nth(1).query();
    clickOn(node);
    clickOn("#deleteButton");
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText("History entry deleted!"));

    String firstKeyId = controller.service.getHistoryMap().keySet().stream().findFirst().get();      

    assertEquals(1, controller.service.getHistoryMap().values().size());
    assertNotEquals(2, controller.service.getHistoryMap().values().size());
    assertEquals(controller.getHistoryOverview().getItems().get(0).toString(), controller.service.getHistoryMap().get(firstKeyId));
    assertNotEquals("myHistory999;12.12.2020", controller.service.getHistoryMap().get(firstKeyId));
    controller.getHistoryOverview().getColumns().get(1).setId("historyName");
    Node node2 = lookup("#historyName").nth(1).query();
    clickOn(node2);
    FxAssert.verifyThat("#exceptionFeedback", TextMatchers.hasText(""));
  }

  @AfterEach
  void cleanUp() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException {
    service.deleteUser();
  }
}