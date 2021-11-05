package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;

import javafx.event.ActionEvent;
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

  @Override
  void loadCreate(ActionEvent event) throws IOException {
    super.loadCreate(event);
  }

  @Override
  void loadOverview(ActionEvent event) throws IOException {
    super.loadOverview(event);
  }

  @Override
  void loadLogin(ActionEvent event) throws IOException {
    super.loadLogin(event);
  }

  void setUser(User user) {
    this.user = user;
  }
}

