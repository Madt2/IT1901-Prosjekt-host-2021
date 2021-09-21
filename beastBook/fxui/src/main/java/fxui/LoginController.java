package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

public class LoginController {

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
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
