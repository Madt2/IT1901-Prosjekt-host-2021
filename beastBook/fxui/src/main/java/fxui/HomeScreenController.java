package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.control.MenuItem;

public class HomeScreenController extends AbstractController{
  @FXML
  private MenuItem logout_button;

  @FXML
  private Button create_button;

  @FXML
  private Button workouts_button;

  @FXML
  private Button history_button;

  @Override
  void loadHistory(ActionEvent event) throws IOException {
    super.loadHistory(event);
  }

  @Override
  void loadCreate(ActionEvent event) throws IOException {
    super.loadCreate(event);
  }

  @Override
  void loadWorkouts(ActionEvent event) throws IOException {
    super.loadWorkouts(event);
  }

  @Override
  void loadLogin(ActionEvent event) throws IOException {
    super.loadLogin(event);
  }

  public void setUser(User user) {
    this.user = user;
  }
}

