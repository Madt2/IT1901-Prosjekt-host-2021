package beastbook.fxui;

import beastbook.core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


/**
 * Controller for the Home screen.
 */
public class HomeScreenController extends AbstractController {
  @FXML
  private Button logoutButton;

  @FXML
  private Button createButton;

  @FXML
  private Button workoutsButton;

  void setUser(User user) {
    this.user = user;
  }
}

