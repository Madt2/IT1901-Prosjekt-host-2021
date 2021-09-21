package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class WorkoutOverviewController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button back_button;

    @FXML
    private ListView<?> workout_overview;

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("HomeScreen.fxml"));
        rootPane.getChildren().setAll(pane);
    }


}
