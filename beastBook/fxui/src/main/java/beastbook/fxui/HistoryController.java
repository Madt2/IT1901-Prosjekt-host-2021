package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * Controller for unimplemented History scene.
 */
public class HistoryController extends AbstractController {

  @FXML
  private Button backButton;

  @FXML
  private ListView<?> historyOverview;

  private User user;

  @Override
  void loadHome(ActionEvent event, String username) throws IOException {
    super.loadHome(event, user.getUserName());
  }

  @Override
  void loadLogin(ActionEvent event) throws IOException {
    super.loadLogin(event);
  }

  public void setUser(String username) throws IOException {
    this.user = user.loadUser(username);
  }
}
