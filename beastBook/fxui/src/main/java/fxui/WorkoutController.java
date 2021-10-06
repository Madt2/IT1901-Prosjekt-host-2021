package fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

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
    private TableColumn<Exercise, Integer> repGoal;

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

    private Workout workout = new Workout();


    //@FXML
    public void initialize(){
        // STATIC: (fyfy)
        setWorkout(WorkoutOverviewController.clickedWorkout);
        setTable();
        title.setText(workout.getName());
    }

    public void setWorkout(Workout workout){
        this.workout = workout;
    }
    
    public Workout getWorkout(){
        return this.workout;
    }

    public void setTable() {
        
        
        workout_table.setEditable(true);
        
        exerciseName.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
        exerciseName.setCellFactory(TextFieldTableCell.forTableColumn());
        exerciseName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,String>>(){
            @Override
            public void handle(CellEditEvent<Exercise, String> event){
                Exercise exercise = event.getRowValue();
                exercise.setExerciseName(event.getNewValue());
            }
        });

       // repGoal.setCellValueFactory(new PropertyValueFactory<Exercise, String>("repGoal"));
       // repGoal.setCellFactory(TextFieldTableCell.forTableColumn());
        repGoal.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));
        repGoal.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
       
       
        weight.setCellValueFactory(new PropertyValueFactory<Exercise, String>("weight"));

        sets.setCellValueFactory(new PropertyValueFactory<Exercise, String>("sets"));
        /*
        for (Exercise el : workout.getExercises()) {
            for (int i = 0; i < el.getSets(); i++) {
                TableColumn<Exercise, String> setColumn = new TableColumn<Exercise, String>("Set " + (i+1) + ":");
                setColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,String>>(){
                    @Override
                    public void handle(CellEditEvent<Exercise, String> event){
                        //Exercise exercise = event.getRowValue();
                        //exercise.setExerciseName(event.getNewValue());
                        
                    }
                });
                
                workout_table.getColumns().add(setColumn);
            }
        }
        */
        restTime.setCellValueFactory(new PropertyValueFactory<Exercise, String>("restTime"));
      
       
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
