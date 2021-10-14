package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.io.IOException;
import core.Workout;
import core.Exercise;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import json.BeastBookPersistence;

public class CreateController {
  @FXML
  private AnchorPane rootPane;

  @FXML
  private MenuBar menuBar;

  @FXML
  private TableView<Exercise> workout_table = new TableView<Exercise>();

  @FXML
  private Text exceptionFeedback;

  @FXML
  private Button back_button;

  @FXML
  private TextField exerciseNameInput;

  @FXML
  private TextField repsInput;

  @FXML
  private TextField weigthInput;

  @FXML
  private TextField setsInput;

  @FXML
  private TextField restInput;

  @FXML
  private TextField titleInput;

  @FXML
  private Button createButton;

  @FXML
  private Button loadButton;

  @FXML
  private Button addExercise;

  private Workout workout = new Workout();
  private Exercise exercise;
  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
  private User user;

  public void initialize() {
    setTable();
  } 

  /**
  * Sets the workout table columns. Clears the columns first, to avoid duplicate columns.
  * After the columns are created, they are added to the table view. 
  */
  public void setTable() {     
    workout_table.getColumns().clear();
         
    exerciseNameColumn = new TableColumn<Exercise, String>("Exercise name");
    exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
        
    repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal");
    repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

    weightColumn = new TableColumn<Exercise, Double>("Weight");
    weightColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Double>("weight"));

    setsColumn = new TableColumn<Exercise, Integer>("Nr of sets");
    setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

    restTimeColumn = new TableColumn<Exercise, Integer>("Rest time");
    restTimeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("restTime"));
       
    workout_table.getColumns().add(exerciseNameColumn);
    workout_table.getColumns().add(repGoalColumn);
    workout_table.getColumns().add(weightColumn);
    workout_table.getColumns().add(setsColumn);
    workout_table.getColumns().add(restTimeColumn);
    setColumnsSize();
    workout_table.getItems().setAll(workout.getExercises());
  }

  /**
  * Resizes the width of the columns.
  */
  private void setColumnsSize() {
    exerciseNameColumn.setPrefWidth(100);        
    repGoalColumn.setPrefWidth(75);
    weightColumn.setPrefWidth(75);
    setsColumn.setPrefWidth(75);
    restTimeColumn.setPrefWidth(75);
  }

  /**
  * Return the exercise on the specified row. Mainly used for test reasons.
  *
  * @param row the row you want to have access to / get an Exercise object from. int row 0 is the first row,  int row 1 is the second row and so on. 
  * 
  * @return the Exercise object on the the requested row 
  */
  public Exercise getTable(int row) {
    return workout_table.getItems().get(row);
  }

  /**
  *
  * @return the workout. Mainly used for test reasons
  */
  public Workout getWorkout() {
    return workout;
  }
    
  /**
  *
  * @return the the workout table
  */
  public TableView<Exercise> getWorkoutTable() {
    return workout_table;
  }

  /**
  *
  *  Runs when the "Add exercise" button is clicked. If all the input fields are in the correct format, a Exercise object is made with the
  *  input fields data. The exercise object is then added to the workout object and its list over exercises, this is then connected to the signed in user. 
  *  The workout table is then "reloaded" with the new exercise added to the list.
  *  
  *  If the input fields are not in the correct format, the method catches the Exepction. A text with red color appears on the screen with 
  *  a message to the user saying that the exercise could not be added (because of wrong inputs). The text disappears when a exercise is added successfully. 
  */

  @FXML
  void addExercise() {
    this.workout.setName(titleInput.getText());
    try {
      exercise = new Exercise(exerciseNameInput.getText(), 
      Integer.parseInt(repsInput.getText()),
      Double.parseDouble(weigthInput.getText()),
      Integer.parseInt(setsInput.getText()),
      Integer.parseInt(restInput.getText()));
            
      this.workout.addExercise(exercise);
      workout_table.getItems().add(exercise);   
      exceptionFeedback.setText("");
      createButton.setDisable(false);
      emptyInputFields();
    } catch (NumberFormatException i) {
      exceptionFeedback.setText("Value can not be in string format, must be number"); 
    } catch (Exception e) {
      exceptionFeedback.setText(e.getMessage());
    }   
  }
  
  /**
  * Empties all the input fields. Should be called when a exercise is successfully added to the workout
  */
  private void emptyInputFields() {
    exerciseNameInput.setText("");
    repsInput.setText("");
    weigthInput.setText("");
    setsInput.setText("");
    restInput.setText("");
  }

  /**
  * Loads a workout using title input in GUI
  * If no title input is given, an error message is displayed in GUI
  * If no file found with given title, an error message is displayed in GUI
  *
  * @param event When Load Workout button is clicked in GUI, loadWorkout() is fired
  */
  @FXML
  void loadWorkout(ActionEvent event) {     
    if (titleInput.getText().equals("") || titleInput.getText() == null) {
      exceptionFeedback.setText("Missing Title!");
      return;
    }
    try {
      workout = user.getWorkout(titleInput.getText());
      setTable();
      exceptionFeedback.setText("");
    } catch (Exception e) {
      exceptionFeedback.setText("Workout not found!");
    }
  }

  /**
  * Creates a workout and saves it as a file with input given in GUI
  * If no title input is given, an error message is displayed in GUI
  * If an error occurs in saveWorkout, an error message is displayed in GUI
  *
  * @param event When Create Workout button is clicked in GUI, createWorkout() is fired
  */
  @FXML
  void createWorkout(ActionEvent event) {
    workout.setName(titleInput.getText());
    if (titleInput.getText() == null || titleInput.getText().equals("")) {
      exceptionFeedback.setText("Input title is empty, please enter name to workout");
    } else {
      try {
        user.addWorkout(workout);
        BeastBookPersistence persistence = new BeastBookPersistence();
        persistence.setSaveFilePath(user.getUserName());
        persistence.saveUser(user);
        exceptionFeedback.setText("Workout saved!");
      } catch (Exception e) {
        System.err.println(e);
        exceptionFeedback.setText("Save Workout failed!");
      }
    }
  }

  @FXML
  void loadHome(ActionEvent event) throws IOException {
    HomeScreenController homeScreenController = new HomeScreenController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("HomeScreen.fxml"));
    fxmlLoader.setController(homeScreenController);
    homeScreenController.setUser(user);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  @FXML
  void loadLogin(ActionEvent event) throws IOException {
    LoginController loginController = new LoginController();
    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));
    fxmlLoader.setController(loginController);
    AnchorPane pane =  fxmlLoader.load();
    rootPane.getChildren().setAll(pane);
  }

  void setUser(User user) {
    this.user = user;
  }
}