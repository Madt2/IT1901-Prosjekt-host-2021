package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.IllegalArgumentException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import core.User;
import javafx.scene.text.Text;
import json.BeastBookPersistence;

public class LoginController {
    private User user = new User();

    @FXML
    private AnchorPane rootPane;

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

    @FXML
    void loadHome() throws IOException {
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void registerUser(ActionEvent event) throws IllegalArgumentException {
        String userName = username_input.getText();
        String password = password_input.getText();
        this.user = new User(userName, password);
        saveUser(user); //Skal lagre bruker som en JSON-fil
    }


    @FXML
    void loginUser(ActionEvent event) throws IllegalArgumentException, IOException {
        String userName = username_input.getText();
        String password = password_input.getText();
        if (userName != "") {
            User login = getUser(userName);
            if (Objects.isNull(login)) {
                login_error.setText("No user found");
            } else {
                if (password != "") {
                    if(!login.getPassword().equals(password))
                        login_error.setText("Wrong Password");
                    else {
                        user = login;
                        loadHome();
                    }
                } else
                    login_error.setText("No Password given!");
            }
        } else {
            login_error.setText("No username given");
        }
    }

    private void saveUser(User user) {
        BeastBookPersistence persistence = new BeastBookPersistence();
        try {
            persistence.setSaveFilePath(user.getUserName());
            persistence.saveUser(user);
        } catch (IOException e) {
            login_error.setText("User was not saved");
        }
    }


    private User getUser(String userName) {
        BeastBookPersistence persistence = new BeastBookPersistence();
        try {
            persistence.setSaveFilePath(userName);
            User user = persistence.loadUser();
            return user;
        } catch (IOException e) {
        }
       return null;
    }

    /* public static void main(String[] args) {
        LoginController controller = new LoginController();
        controller.username_input.setText("Tor");
        controller.password_input.setText("bruh");

        controller.registerUser(ActionEvent event);
    } */

}
