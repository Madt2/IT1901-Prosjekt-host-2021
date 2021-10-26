package beastbook.fxui;

import beastbook.core.User;
import beastbook.json.BeastBookPersistence;
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

  private BeastBookPersistence persistence = new BeastBookPersistence();

  /**
  * Loads home in gui.
  *
   * @throws IOException if loading home screen fails
  */
  @Override
  void loadHome() throws IOException {
    super.loadHome();
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
    if (!userName.equals("")) {
      if (!password.equals("")) {
        User login = getUser(userName);
        if (Objects.isNull(login)) {
          loginError.setText("No user found");
        } else {
          if (!login.getPassword().equals(password)) {
            loginError.setText("Wrong Password");
          } else {
            user = login;
            super.loadHome();
          }
        }
      } else {
        loginError.setText("No Password given!");
      }
    } else {
      loginError.setText("No username given");
    }
  }

  /**
  * Help method for registerUser. Uses persistence to save.
  *
  * @param user user to save.
  */
  private void saveUser(User user) {
    try {
      persistence.setSaveFilePath(user.getUserName());
      persistence.saveUser(user);
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
      persistence.setSaveFilePath(userName);
      User user = persistence.loadUser();
      return user;
    } catch (IOException e) {
      return null;
    }
  }
}