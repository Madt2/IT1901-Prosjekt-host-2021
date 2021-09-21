package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class CreateController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<?> workout_table;

    @FXML
    private Button back_button;

    @FXML
    private TextField exercise_input;

    @FXML
    private TextField rep_input;

    @FXML
    private TextField weight_input;

    @FXML
    private TextField set_input;

    @FXML
    private TextField rest_input;

    @FXML
    private TextField title_input;

    @FXML
    private Button save_button;

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
