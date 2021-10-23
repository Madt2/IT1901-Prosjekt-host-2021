package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
  void loadHistory() throws IOException {
    super.loadHistory();
  }

  @Override
  void loadCreate() throws IOException {
    super.loadCreate();
  }

  @Override
  void loadWorkouts() throws IOException {
    super.loadWorkouts();
  }

  @Override
  void loadLogin() throws IOException {
    super.loadLogin();
  }

  public void setUser(User user) {
    this.user = user;
  }
}

