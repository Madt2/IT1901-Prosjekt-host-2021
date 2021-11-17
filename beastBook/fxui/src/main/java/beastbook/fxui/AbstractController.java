package beastbook.fxui;

import beastbook.client.ClientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Abstract controller for all controllers in fxui.
 * Used for loading scenes.
 */
public abstract class AbstractController {
  protected String username;
  protected ClientService service;

  @FXML
  void loadHome(ActionEvent event) throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/HomeScreen.fxml")
    );
    fxmlLoader.setController(homeScreenController);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadLogin(ActionEvent event) throws IOException {
    LoginController loginController = new LoginController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/Login.fxml")
    );
    fxmlLoader.setController(loginController);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadCreate(ActionEvent event) throws IOException {
    CreateWorkoutController createController = new CreateWorkoutController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/Create.fxml")
    );
    fxmlLoader.setController(createController);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadOverview(ActionEvent event) throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml")
    );
    fxmlLoader.setController(workoutOverviewController);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadHistoryOverview(ActionEvent event) throws IOException {
    HistoryOverviewController historyOverviewController = new HistoryOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/HistoryOverview.fxml")
    );
    fxmlLoader.setController(historyOverviewController);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  void setUsername(String username) {
    this.username = username;
  }

  String getUsername() {
    return this.username;
  }
}
