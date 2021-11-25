package beastbook.fxui;

import beastbook.client.ClientController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Abstract controller for all controllers in fxui.
 * Used for loading scenes.
 */
public abstract class AbstractController {
  private String username;
  private String password;
  protected ClientController service;

  @FXML
  void loadHome(ActionEvent event) throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/HomeScreen.fxml")
    );
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setService(service);
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
    createController.setService(service);
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
    workoutOverviewController.setService(service);
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
    historyOverviewController.setService(service);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  void setService(ClientController service) {
    this.service = service;
  }

  void setUsername(String username) {
    this.username = username;
  }

  void setPassword(String password) {
    this.password = password;
  }

  String getUsername() {
    return this.username;
  }

  String getPassword() {
    return this.password;
  }
}
