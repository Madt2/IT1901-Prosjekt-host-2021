package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;

import java.io.IOException;
import java.lang.Integer;

import core.Workout;
import core.Exercise;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import json.BeastBookPersistence;

public class CreateExerciseController {
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
  private TextField repGoalInput;

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

  @FXML
  private Text exerciseTitle;

  @FXML
  private Text repGoalTitle;

  @FXML
  private Text weightTitle;

  @FXML
  private Text setsTitle;

  @FXML
  private Text restTimeTitle;

  private Workout workout = new Workout();
  private Exercise exercise = new Exercise();
  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
  private User user;
  private static final String WRONG_INPUT_BORDER_COLOR = "-fx-text-box-border: #B22222; -fx-focus-color: #B22222";
  private static final String CORRECT_INPUT_BORDER_COLOR = "";  

  public void initialize() {
    updateTable();
    exerciseNameInput.setOnKeyTyped(event -> {
      new StringValidator(exerciseTitle, exerciseNameInput, exceptionFeedback);
    });
    repGoalInput.setOnKeyTyped(event -> {
      new IntValidator(repGoalTitle, repGoalInput, exceptionFeedback);
    });
    weigthInput.setOnKeyTyped(event -> {
      new DoubleValidator(weightTitle, weigthInput, exceptionFeedback);
    });
    setsInput.setOnKeyTyped(event -> {
      new IntValidator(setsTitle, setsInput, exceptionFeedback);
    });
    restInput.setOnKeyTyped(event -> {
      new IntValidator(restTimeTitle, restInput, exceptionFeedback);
    });
  } 

  /**
  * Sets the workout table columns. Clears the columns first, to avoid duplicate columns.
  * After the columns are created, they are added to the table view. 
  */
  public void updateTable() {     
    workout_table.getColumns().clear();
         
    exerciseNameColumn = new TableColumn<Exercise, String>("Exercise name");
    exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
        
    repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal");
    repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

    weightColumn = new TableColumn<Exercise, Double>("Weight");
    weightColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Double>("weight"));

    setsColumn = new TableColumn<Exercise, Integer>("Nr of sets");
    setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

    restTimeColumn = new TableColumn<Exercise, Integer>("Rest time in sec");
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
    setsColumn.setPrefWidth(80);
    restTimeColumn.setPrefWidth(110);
  }

  /**
  * Return the exercise on the specified row. Mainly used for test reasons.
  *
  * @param row the row you want to have access to / get an Exercise object from. int row 0 is the first row,  int row 1 is the second row and so on. 
  * 
  * @return the Exercise object on the the requested row 
  */
  Exercise getTable(int row) {
    return workout_table.getItems().get(row);
  }

  /**
  *
  * @return the workout. Mainly used for test reasons
  */
  Workout getWorkout() {
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
    if (exceptionFeedback.getText().equals("") && !checkForEmptyInputFields()) {
      try {

        String name = exerciseNameInput.getText(); 
        int repGoal;
        double weight;
        int sets;
        int rest;
        try {
          repGoal = Integer.parseInt(repGoalInput.getText());
        } catch (NumberFormatException a) {
          throw new IllegalArgumentException("Rep Goal must be a number!");
        }
        try {
          weight = Double.parseDouble(weigthInput.getText());
        } catch (NumberFormatException b) {
          throw new IllegalArgumentException("Working Weight must be a number");
        }
        try {
          sets = Integer.parseInt(setsInput.getText());
        } catch (NumberFormatException c) {
          throw new IllegalArgumentException("Sets must be a number");
        }
        try {
          rest = Integer.parseInt(restInput.getText());
        } catch (NumberFormatException d) {
          throw new IllegalArgumentException("Rest Time must be a number");
        }
        exercise = new Exercise(name,repGoal,weight,sets,rest);
        
        this.workout.addExercise(exercise);
        workout_table.getItems().add(exercise);
        exercise = new Exercise();   
        exceptionFeedback.setText("");
        createButton.setDisable(false);
        emptyInputFields();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage()); 
      } catch (Exception e) {
        exceptionFeedback.setText(e.getMessage());
        //exceptionFeedback.setText("exercise catch");
      }  
    } else if (checkForEmptyInputFields()) {
      exceptionFeedback.setText("Input missing in a field");
    } else {
      exceptionFeedback.setText("Wrong input, exercise was not created");
    }
  }

  /**
  * Empties all the input fields. Should be called when a exercise is successfully added to the workout
  */
  private void emptyInputFields() {
    exerciseNameInput.setText("");
    repGoalInput.setText("");
    weigthInput.setText("");
    setsInput.setText("");
    restInput.setText("");
  }

  private boolean checkForEmptyInputFields(){
    int counter = 0;
    if (exerciseNameInput.getText().equals("")) {
      exerciseNameInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (repGoalInput.getText().equals("")) {
      repGoalInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (weigthInput.getText().equals("")) {
      weigthInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (setsInput.getText().equals("")) {
      setsInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (restInput.getText().equals("")) {
      restInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if(counter > 1){
      return true;
    }
    return false;
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
      updateTable();
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
  public class IntValidator {
    public IntValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      try {
        int num = Integer.parseInt(text);
        if (num <= 0) {
          exception.setText(title.getText().replace(":", "") + " must be more than 0");
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        }
        else {
          exceptionFeedback.setText("");
          field.setStyle(CORRECT_INPUT_BORDER_COLOR);
        }
      } catch (NumberFormatException e) {
        exception.setText(title.getText().replace(":", "") + " must be a number and can not exceed " + Integer.MAX_VALUE);
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      }
    }
  }
  public class DoubleValidator {
    public DoubleValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      try {
        double num = Double.parseDouble(text);
        if (num <= 0) {
          exception.setText(title.getText().replace(":", "") + " must be more than 0");
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        }
        else {
          exceptionFeedback.setText("");
          field.setStyle(CORRECT_INPUT_BORDER_COLOR);
        }
      } catch (NumberFormatException e) {
        exception.setText(title.getText().replace(":", "") + " must be a number and can not exceed " + Double.MAX_VALUE);
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      }
    }
  }

  public class StringValidator {
    public StringValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      text = text.trim();
      if ((text.length() <= 0) || (text.equals(""))){
      exception.setText(title.getText().replace(":", "") + " can not be blank");
      field.setStyle(WRONG_INPUT_BORDER_COLOR);
      } else {
        exceptionFeedback.setText("");
        field.setStyle(CORRECT_INPUT_BORDER_COLOR);
      }
    }
  }
}