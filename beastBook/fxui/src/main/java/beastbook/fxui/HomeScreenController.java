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

  @FXML
  private Button historyButton;

  private User user;

  @Override
  void loadHistory(ActionEvent event, String username) throws IOException {
    super.loadHistory(event, user.getUserName());
  }

  @Override
  void loadCreate(ActionEvent event, String username) throws IOException {
    super.loadCreate(event, user.getUserName());
  }

  @Override
  void loadOverview(ActionEvent event, String username) throws IOException {
    super.loadOverview(event, user.getUserName());
  }

  @Override
  void loadLogin(ActionEvent event) throws IOException {
    super.loadLogin(event);
  }

  public void setUser(String username) throws IOException {
    this.user = user.loadUser(username);
  }
}

