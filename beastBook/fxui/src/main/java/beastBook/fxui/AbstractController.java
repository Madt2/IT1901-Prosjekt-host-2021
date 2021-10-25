package beastBook.fxui;

import java.io.IOException;
import beastBook.core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public abstract class AbstractController {
  @FXML
  protected AnchorPane rootPane;

  protected User user;
    
  @FXML
  void loadHome() throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("HomeScreen.fxml"));
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadLogin() throws IOException {
    LoginController loginController = new LoginController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));
    fxmlLoader.setController(loginController);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadCreate() throws IOException {
    CreateExerciseController createController = new CreateExerciseController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Create.fxml"));
    fxmlLoader.setController(createController);
    createController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadHistory() throws IOException {
    HistoryController historyController = new HistoryController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("History.fxml"));
    fxmlLoader.setController(historyController);
    historyController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadWorkouts() throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadOverview() throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }
}
