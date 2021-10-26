package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * Abstract controller for all controllers in fxui.
 * Used for loading scenes.
 */
public abstract class AbstractController {
  @FXML
  protected AnchorPane rootPane;

  protected User user;
    
  @FXML
  void loadHome() throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/HomeScreen.fxml")
    );
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadLogin() throws IOException {
    LoginController loginController = new LoginController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/Login.fxml")
    );
    fxmlLoader.setController(loginController);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadCreate() throws IOException {
    CreateWorkoutController createController = new CreateWorkoutController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/Create.fxml")
    );
    fxmlLoader.setController(createController);
    createController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadHistory() throws IOException {
    HistoryController historyController = new HistoryController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/History.fxml")
    );
    fxmlLoader.setController(historyController);
    historyController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadWorkouts() throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml")
    );
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadOverview() throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(
        this.getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml")
    );
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }
}
