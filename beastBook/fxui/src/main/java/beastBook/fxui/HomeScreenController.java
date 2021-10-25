package beastBook.fxui;

import beastBook.core.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the Home screen.
 */
public class HomeScreenController extends AbstractController {
  @FXML
  private Button logoutButton;

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

