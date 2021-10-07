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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
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

  /*  @FXML
    private TableColumn<Exercise, String> exerciseName;

    @FXML
    private TableColumn<Exercise, Integer> repGoal;

    @FXML
    private TableColumn<Exercise, String> weight;

    @FXML
    private TableColumn<Exercise, String> sets;

    @FXML
    private TableColumn<Exercise, String> restTime;
    */
    @FXML
    private TableView<Exercise> workout_table;

    @FXML
    private Button back_button;

    @FXML
    private Text title;

    @FXML
    private TextField date_input;

    private TableColumn<Exercise, String> exerciseNameColumn;
    private TableColumn<Exercise, Integer> repGoalColumn;
    private TableColumn<Exercise, Double> weightColumn;
    private TableColumn<Exercise, Integer> setsColumn;
    private TableColumn<Exercise, Integer> restTimeColumn;

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
        System.out.println(workout.getExercises());
        
        exerciseNameColumn = new TableColumn<Exercise, String>("Workout name:");
        exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));

        repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal:");
        repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

        weightColumn = new TableColumn<Exercise, Double>("Weight:");
        weightColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Double>("weight"));

        setsColumn = new TableColumn<Exercise, Integer>("Nr of sets:");
        setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

        restTimeColumn = new TableColumn<Exercise, Integer>("Rest time:");
        restTimeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("restTime"));

        workout_table.getColumns().add(exerciseNameColumn);
        workout_table.getColumns().add(repGoalColumn);
        workout_table.getColumns().add(weightColumn);
        workout_table.getColumns().add(setsColumn);
        workout_table.getColumns().add(restTimeColumn);

        exerciseNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        exerciseNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,String>>(){
            @Override
            public void handle(CellEditEvent<Exercise, String> event){
                Exercise exercise = event.getRowValue();
                exercise.setExerciseName(event.getNewValue());
            }
        });

        repGoalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        repGoalColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                Exercise exercise = event.getRowValue();
                exercise.setRepGoal(event.getNewValue());
            }
        });
        
        weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        weightColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Double>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Double> event){
                Exercise exercise = event.getRowValue();
                exercise.setWeight(event.getNewValue());
            }
        });
        
        /*
        setsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        setsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                Exercise exercise = event.getRowValue();
                exercise.setSets(event.getNewValue());
            }
        });
        */
        restTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        restTimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                Exercise exercise = event.getRowValue();
                exercise.setRestTime(event.getNewValue());
            }
        });

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
       
        workout_table.getItems().setAll(workout.getExercises());
        
    }

    private int getMaxSetColumns(){
        int currentMax = 0;

        for (Exercise e : workout.getExercises()) {
            if(e.getSets() > currentMax){
                currentMax = e.getSets();
            }
        }
        return currentMax;
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
