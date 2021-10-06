package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import java.io.IOException;

import core.Exercise;
import core.Workout;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;

public class WorkoutController {

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private MenuItem logout_button;

    @FXML
    private TableColumn<Exercise, String> exerciseName;

    @FXML
    private TableColumn<Exercise, String> repGoal;

    @FXML
    private TableColumn<Exercise, String> weight;

    @FXML
    private TableColumn<Exercise, String> sets;

    @FXML
    private TableColumn<Exercise, String> restTime;
    
    @FXML
    private TableView<Exercise> workout_table;

    @FXML
    private Button back_button;

    @FXML
    private Text title;

    @FXML
    private TextField date_input;

    //@FXML
    public void initialize(){
        title.setText("BRAGE");
        exerciseName.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
        repGoal.setCellValueFactory(new PropertyValueFactory<Exercise, String>("repGoal"));
        weight.setCellValueFactory(new PropertyValueFactory<Exercise, String>("weight"));
        sets.setCellValueFactory(new PropertyValueFactory<Exercise, String>("sets"));
        restTime.setCellValueFactory(new PropertyValueFactory<Exercise, String>("restTime"));

       // System.out.println(exerciseName);

    }
    
    public void setTable(Workout workout) {
        initialize();
        //title.setText("BRAGE");
        //title.setText(workout.getName());
 //       exerciseName.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
    //    repGoal.setCellValueFactory(new PropertyValueFactory<Exercise, String>("repGoal"));
      //  weight.setCellValueFactory(new PropertyValueFactory<Exercise, String>("weight"));
       // sets.setCellValueFactory(new PropertyValueFactory<Exercise, String>("sets"));
        //restTime.setCellValueFactory(new PropertyValueFactory<Exercise, String>("restTime"));
        workout_table.getItems().setAll(workout.getExercises());
    }

    @FXML
    void loadLogin(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("Login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadOverview(ActionEvent event) throws IOException{
        AnchorPane pane =  FXMLLoader.load(getClass().getResource("WorkoutOverview.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
