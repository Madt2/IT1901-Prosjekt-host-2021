package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Abstract controller for all controllers in fxui.
 * Used for loading scenes.
 */
public abstract class AbstractController {
  @FXML
  protected AnchorPane rootPane;

  private User user;
    
  @FXML
  void loadHome(ActionEvent event, String username) throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/HomeScreen.fxml")
    );
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setUser(username);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadCreate(ActionEvent event, String username) throws IOException {
    CreateWorkoutController createController = new CreateWorkoutController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/Create.fxml")
    );
    fxmlLoader.setController(createController);
    createController.setUser(username);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadHistory(ActionEvent event, String username) throws IOException {
    HistoryController historyController = new HistoryController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/History.fxml")
    );
    fxmlLoader.setController(historyController);
    historyController.setUser(username);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadWorkouts(ActionEvent event, String username) throws IOException {
    WorkoutController workoutController = new WorkoutController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/Workout.fxml")
    );
    fxmlLoader.setController(workoutController);
    workoutController.setUser(username);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  void loadOverview(ActionEvent event, String username) throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(
            this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml")
    );
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(username);
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root, 600, 500);
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(scene);
  }
}
