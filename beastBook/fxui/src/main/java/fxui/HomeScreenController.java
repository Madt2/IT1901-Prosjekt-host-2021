package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.control.MenuItem;

public class HomeScreenController {
  @FXML
  private AnchorPane rootPane;

  @FXML
  private MenuItem logout_button;

  @FXML
  private Button create_button;

  @FXML
  private Button workouts_button;

  @FXML
  private Button history_button;

  private User user;

  @FXML
  void loadHistory(ActionEvent event) throws IOException {
    HistoryController historyController = new HistoryController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("History.fxml"));
    fxmlLoader.setController(historyController);
    historyController.setUser(user);

    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadCreate(ActionEvent event) throws IOException {
    CreateController createController = new CreateController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Create.fxml"));
    fxmlLoader.setController(createController);
    createController.setUser(user);

    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadWorkouts(ActionEvent event) throws IOException {
    WorkoutOverviewController workoutOverviewController = new WorkoutOverviewController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
    fxmlLoader.setController(workoutOverviewController);
    workoutOverviewController.setUser(user);

    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadLogin(ActionEvent event) throws IOException {
    LoginController loginController = new LoginController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));
    fxmlLoader.setController(loginController);

    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  public void setUser(User user) {
    this.user = user;
  }
}

