package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.io.IOException;
import javafx.scene.control.MenuItem;

public class HistoryController extends AbstractController{
  @FXML
  private MenuItem logout_button;

  @FXML
  private Button back_button;

  @FXML
  private ListView<?> history_overview;

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
