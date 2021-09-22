package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import javafx.fxml.FXMLLoader;
import core.User;

public class LoginController {
    private User user;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField username_input;

    @FXML
    private TextField password_input;

    @FXML
    private Button register_button;

    @FXML
    private Button login_button;

    @FXML
    void loadHome(ActionEvent event) throws IOException {
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
    void loginUser(ActionEvent event) throws IllegalArgumentException {
        String userName = username_input.getText();
        String password = password_input.getText();
        for (String u:userBase) {
            if (u.equals(userName)) {
                user = getUser(u); //Skal hente bruker objekt fra JSON-fil
                break;
            }
        }
        if (user.getUserName() != userName) {
            throw new IllegalArgumentException("No such user found!");
        } 
    }

    /* public static void main(String[] args) {
        LoginController controller = new LoginController();
        controller.username_input.setText("Tor");
        controller.password_input.setText("bruh");

        controller.registerUser(ActionEvent event);
    } */

}
