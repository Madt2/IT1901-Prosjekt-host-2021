package beastbook.fxui;

import beastbook.core.User;
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
  protected User user;

  @FXML
  void loadHome(ActionEvent event) throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/HomeScreen.fxml")
    );
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setUser(user);
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
    createController.setUser(user);
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
    workoutOverviewController.setUser(user);
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
    historyOverviewController.setUser(user);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }
}
