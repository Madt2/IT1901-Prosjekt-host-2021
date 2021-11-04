package beastbook.fxui;

import beastbook.core.User;
import java.io.IOException;
import java.util.Objects;
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

  private User user;

  /**
  * Loads home in gui.
  *
   * @throws IOException if loading home screen fails
  */
  @Override
  void loadHome(ActionEvent event, String username) throws IOException {
    super.loadHome(event, username);
  }

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
        user = new User(userName, password);
        saveUser(user); //Skal lagre bruker som en JSON-fil
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
  * @throws IOException if loading home screen fails
  */
  @FXML
  void loginUser(ActionEvent event) throws IllegalArgumentException, IOException {
    String userName = usernameInput.getText();
    String password = passwordInput.getText();
    if (userName.equals("")) {
      loginError.setText("No username given");
      return;
    }
    if (password.equals("")) {
      loginError.setText("No Password given!");
      return;
    }
    User login = user.loadUser(userName);
    if (Objects.isNull(login)) {
      loginError.setText("No user found");
      return;
    }
    if (!login.getPassword().equals(password)) {
      loginError.setText("Wrong Password");
      return;
    }
    user = login;
    super.loadHome(event, user.getUserName());
  }

  /**
  * Help method for registerUser. Uses persistence to save.
  *
  * @param user user to save.
  */
  private void saveUser(User user) {
    try {
      user.saveUser();
    } catch (IOException e) {
      loginError.setText("User was not saved");
    }
  }

  /**
  * Help method for loadUser. Uses persistence to load user.
  *
  * @param userName username for user to get.
  * @return user if user found. null if no user found.
  */
  private User getUser(String userName) {
    try {
      User user = new User();
      user = user.loadUser(userName);
      return user;
    } catch (IOException e) {
      return null;
    }
  }
}