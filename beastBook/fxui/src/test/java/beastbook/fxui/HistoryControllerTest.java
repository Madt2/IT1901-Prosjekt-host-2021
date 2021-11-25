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

public class HistoryControllerTest extends ApplicationTest {
  
  private HistoryController controller;
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
      Exceptions.HistoryAlreadyExistsException, UserAlreadyExistException {

    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/History.fxml"));
    controller = new HistoryController();
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
    exercises1.add(new Exercise("Benchpress", 37, 128, 994, 2, 763));
    History history = new History("myHistory1", exercises1);
    controller.service.addHistory(history);

    String firstKeyId = controller.service.getHistoryMap().keySet().stream().findFirst().get();
    controller.setHistoryId(firstKeyId);
  }

  @Test
  void testCorrectHistoryIsShown() throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.HistoryNotFoundException {

    History history = controller.service.getHistory(controller.getHistoryId());
    FxAssert.verifyThat("#title", TextMatchers.hasText(history.getName()));
    FxAssert.verifyThat("#date", TextMatchers.hasText(history.getDate()));
    
    controller.getHistoryTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);
  
    assertEquals(controller.service.getHistory(history.getId()).getSavedExercises().get(0).getName(), controller.getHistoryTable().getSelectionModel().getSelectedItem().getName());
    assertNotEquals("situps", controller.getHistoryTable().getSelectionModel().getSelectedItem().getName());

    assertEquals(controller.service.getHistory(history.getId()).getSavedExercises().get(0).getRestTime(), controller.getHistoryTable().getSelectionModel().getSelectedItem().getRestTime());
    assertNotEquals(20, controller.getHistoryTable().getSelectionModel().getSelectedItem().getRestTime());
  } 

  @AfterEach
  void cleanUp() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException {
    service.deleteUser();
  }
}