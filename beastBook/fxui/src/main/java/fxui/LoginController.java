package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.Objects;
import core.User;
import javafx.scene.text.Text;
import json.BeastBookPersistence;

public class LoginController extends AbstractController{
  @FXML
  private TextField username_input;

  @FXML
  private TextField password_input;

  @FXML
  private Text login_error;

  @FXML
  private Button register_button;

  @FXML
  private Button login_button;

  //private User registerUser = new User();

  /**
  * Loads home in gui.
  * @throws IOException
  */
  @Override
  void loadHome() throws IOException {
    super.loadHome();
  }

  /**
  * Registers user, saves user to file. Retrieves username and password input from gui.
  *
  * @param event
  * @throws IllegalArgumentException
  */
  @FXML
  void registerUser(ActionEvent event) throws IllegalArgumentException {
    String userName = username_input.getText();
    String password = password_input.getText();
    if (!userName.equals("") && !password.equals("")) {
      try {
        user = new User(userName, password);
        saveUser(user); //Skal lagre bruker som en JSON-fil
      } catch (Exception e) {
        login_error.setText(e.getMessage());
      }
    }
  }

  /**
  * Login for user. Validation for user-input for existing user and correct password. Also validates for empty input.
  *
  * @param event
  * @throws IllegalArgumentException
  * @throws IOException
  */
  @FXML
  void loginUser(ActionEvent event) throws IllegalArgumentException, IOException {
    String userName = username_input.getText();
    String password = password_input.getText();
    if (!userName.equals("")) {
      if (!password.equals("")) {
        User login = getUser(userName);
        if (Objects.isNull(login)) {
          login_error.setText("No user found");
        } else {
          if (!login.getPassword().equals(password)) {
            login_error.setText("Wrong Password");
          } else {
            user = login;
            super.loadHome();
          }
        }
      } else {
        login_error.setText("No Password given!");
      }
    } else {
      login_error.setText("No username given");
    }
  }

  /**
  * Help method for registerUser. Uses persistence to save.
  *
  * @param user user to save.
  */
  private void saveUser(User user) {
    BeastBookPersistence persistence = new BeastBookPersistence();
    try {
      persistence.setSaveFilePath(user.getUserName());
      persistence.saveUser(user);
    } catch (IOException e) {
      login_error.setText("User was not saved");
    }
  }

  /**
  * Help method for loadUser. Uses persistence to load user.
  *
  * @param userName username for user to get.
  * @return
  */
  private User getUser(String userName) {
    BeastBookPersistence persistence = new BeastBookPersistence();
    try {
      persistence.setSaveFilePath(userName);
      User user = persistence.loadUser();
      return user;
    } catch (IOException e) {
      return null;
    }
  }
}
