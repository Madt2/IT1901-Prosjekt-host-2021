package beastbook.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import beastbook.client.ClientController;
import beastbook.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
  private History history;
  
  @Override
  public void start(final Stage stage) throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      Exceptions.HistoryAlreadyExistsException {
    final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/beastbook.fxui/History.fxml"));
    controller = new HistoryController();
    loader.setController(controller);
    controller.setService(new ClientController("Test", "Test"));
    addHistoryToUser();
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  } 

  private void addHistoryToUser() throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException, URISyntaxException, JsonProcessingException {
    List<Exercise> exercises1 = new ArrayList<>();
    exercises1.add(new Exercise("Benchpress", 20, 30, 40, 50, 50));
    history = new History("myHistory1", exercises1);
    //controller.service.addWorkout(history);
    controller.service.addHistory(history);
  }

  @Test
  void testCorrectHistoryIsShown() throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.HistoryNotFoundException {
    FxAssert.verifyThat("#title", TextMatchers.hasText(history.getName()));
    FxAssert.verifyThat("#date", TextMatchers.hasText(history.getDate()));

    controller.getHistoryTable().getColumns().get(0).setId("exerciseName");
    Node node = lookup("#exerciseName").nth(1).query();
    clickOn(node);
    List<Exercise> exercises = controller.service.getHistory(history.getId()).getSavedExercises();
    assertEquals(exercises, controller.getHistoryTable().getItems());
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/test");
    file.delete();
  }
}