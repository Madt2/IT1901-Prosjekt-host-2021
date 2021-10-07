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

    @FXML
    private TableView<Exercise> workout_table;

    @FXML
    private Button back_button;

    @FXML
    private Text title;

    @FXML
    private TextField date_input;

    @FXML
    private Text exceptionFeedback;

    private TableColumn<Exercise, String> exerciseNameColumn;
    private TableColumn<Exercise, Integer> repGoalColumn;
    private TableColumn<Exercise, Double> weightColumn;
    private TableColumn<Exercise, Integer> setsColumn;
    private TableColumn<Exercise, Integer> restTimeColumn;
    private TableColumn<Exercise, String> setColumnX;

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
        
        exerciseNameColumn = new TableColumn<Exercise, String>("Workout name:");
        exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));

        repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal:");
        repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

        weightColumn = new TableColumn<Exercise, Double>("Weight:");
        weightColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Double>("weight"));

        setsColumn = new TableColumn<Exercise, Integer>("Nr of sets:");
        setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

        setColumnX = new TableColumn<Exercise, String>("Rep pr set:");
        setColumnX.setCellValueFactory(new PropertyValueFactory<Exercise, String>("repPrSet"));
        
        restTimeColumn = new TableColumn<Exercise, Integer>("Rest time:");
        restTimeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("restTime"));

        workout_table.getColumns().add(exerciseNameColumn);
        workout_table.getColumns().add(repGoalColumn);
        workout_table.getColumns().add(weightColumn);
        workout_table.getColumns().add(setsColumn);
        workout_table.getColumns().add(restTimeColumn);

        editTable();
        setColumnsSize();
    }

    // TODO NumberFormatException is not catched. Must find a solution 

    private void editTable(){

        exerciseNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        exerciseNameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,String>>(){

            @Override
            public void handle(CellEditEvent<Exercise, String> event){
                try{
                    Exercise exercise = event.getRowValue();
                    exercise.setExerciseName(event.getNewValue());
                    emptyExceptionFeedback();
                }

                catch(NumberFormatException i){
                    exceptionFeedback.setText("Value can not be in string format, must be number");
                }

                catch (Exception e) {
                    exceptionFeedback.setText(e.getMessage() + " . Value was not changed.");
                }
            }
        });

        repGoalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        repGoalColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){

            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                try{
                    Exercise exercise = event.getRowValue();
                    exercise.setRepGoal(event.getNewValue());
                    emptyExceptionFeedback();
                }

                catch(NumberFormatException i){
                    exceptionFeedback.setText("Value can not be in string format, must be number");
                }

                catch (Exception e) {
                    exceptionFeedback.setText(e.getMessage() + " Value was not changed.");
                }
                
            }
        });
       
        weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        weightColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Double>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Double> event){
                try{
                    Exercise exercise = event.getRowValue();
                    exercise.setWeight(event.getNewValue());
                    emptyExceptionFeedback();
                }

                catch(NumberFormatException i){
                    exceptionFeedback.setText("Value can not be in string format, must be number");
                }

                catch (Exception e) {
                    exceptionFeedback.setText(e.getMessage() + " Value was not changed.");
                }
            }
        });
         
        setsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        setsColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                try{
                    Exercise exercise = event.getRowValue();
                    exercise.setSets(event.getNewValue());
                    emptyExceptionFeedback();
                }

                catch(NumberFormatException i){
                    exceptionFeedback.setText("Value can not be in string format, must be number");
                }

                catch (Exception e) {
                    exceptionFeedback.setText(e.getMessage() + " Value was not changed.");
                }
            }
        });
   
        restTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        restTimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Exercise,Integer>>(){
            @Override
            public void handle(CellEditEvent<Exercise, Integer> event){
                try{
                    Exercise exercise = event.getRowValue();
                    exercise.setRestTime(event.getNewValue());
                    emptyExceptionFeedback();
                }

                catch(NumberFormatException i){
                    exceptionFeedback.setText("Value can not be in string format, must be number");
                }

                catch (Exception e) {
                    exceptionFeedback.setText(e.getMessage() + " Value was not changed.");

                }
            }
        });
       
        workout_table.getItems().setAll(workout.getExercises());
    }

    private void emptyExceptionFeedback(){
        this.exceptionFeedback.setText("");
    }

    private void setColumnsSize(){
        exerciseNameColumn.setPrefWidth(100);        
        repGoalColumn.setPrefWidth(75);
        weightColumn.setPrefWidth(75);
        setsColumn.setPrefWidth(75);
        restTimeColumn.setPrefWidth(75);
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
