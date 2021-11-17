package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller for the Login screen.
 */
public class LoginController extends AbstractController {
  @FXML
  private TextField usernameInput;

  @FXML
  private TextField passwordInput;

  @FXML
  private Text loginError;

  @FXML
  private Button registerButton;

  @FXML
  private Button loginButton;

  /**
  * Registers user, saves user to file. Retrieves username and password input from gui.
  *
  * @param event when register button is clicked
  * @throws IllegalArgumentException if username or password is invalid
  */
  @FXML
  void registerUser(ActionEvent event) throws IllegalArgumentException {
    String userName = usernameInput.getText();
    String password = passwordInput.getText();
    if (!userName.equals("") && !password.equals("")) {
      try {
        User user = new User(userName, password);
        service.createUser(user);
        loginUser(event);
      } catch (Exception e) {
        loginError.setText(e.getMessage());
      }
    }
  }

  /**
  * Login for user. Validation for user-input for existing user and correct password.
   * Also validates for empty input.
  *
  * @param event when log in button is clicked
  * @throws IllegalArgumentException if username or password is not valid
  */
  @FXML
  void loginUser(ActionEvent event) throws IllegalArgumentException {
    String userName = usernameInput.getText();
    String password = passwordInput.getText();
    User user = new User("user", "user");
    try {
      validateLogin(userName, password);
      user = service.queryUser(userName);
      super.loadHome(event);
    } catch (Exception e) {
      loginError.setText(e.getMessage());
    }
  }

  private void validateLogin(
      String userName, String password)
      throws IOException, IllegalArgumentException {
    if (userName.equals("")) {
      throw new IllegalArgumentException("No username given");
    }
    if (password.equals("")) {
      throw new IllegalArgumentException("No Password given!");
    }
    try {
      User login = service.queryUser(userName);
      if (!login.getPassword().equals(password)) {
        throw new IllegalArgumentException("Wrong Password");
      }
      //TODO fix handling not found user
    } /*catch (IOException e) {
      throw new IOException("No user found!");
    }*/ catch (IllegalArgumentException e) {
      throw e;
    }
  }
}