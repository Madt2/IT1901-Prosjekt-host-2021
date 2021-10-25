package beastBook.fxui;

import beastBook.core.User;
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

  @Override
  void loadHome() throws IOException {
    super.loadHome();
  }

  @Override
  void loadLogin() throws IOException {
    super.loadLogin();
  }

  public void setUser(User user) {
    this.user = user;
  }
}
