package beastbook.fxui;

import static beastbook.core.Properties.*;

import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * Controller for the CreateWorkout screen.
 */
public class CreateWorkoutController extends AbstractController {

  @FXML
  private TableView<Exercise> workoutTable = new TableView<>();

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TextField exerciseNameInput;

  @FXML
  private TextField repGoalInput;

  @FXML
  private TextField weightInput;

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
  private Button deleteButton;

  @FXML
  private Button addExerciseButton;

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

  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
  private List<Exercise> exercises = new ArrayList<>();
  private Workout workout = new Workout();

  public static final String WRONG_INPUT_BORDER_COLOR = "-fx-text-box-border: #B22222;"
          + "-fx-focus-color: #B22222";
  public static final String CORRECT_INPUT_BORDER_COLOR = "";

  /**
   * Initializes the CreateWorkout scene with the listeners for validation of input fields.
   */
  public void initialize() {
    updateTable();
    exerciseNameInput.setOnKeyTyped(event ->
            new StringValidator(exerciseTitle, exerciseNameInput, exceptionFeedback));
    repGoalInput.setOnKeyTyped(event ->
            new IntValidator(repGoalTitle, repGoalInput, exceptionFeedback));
    weightInput.setOnKeyTyped(event ->
            new DoubleValidator(weightTitle, weightInput, exceptionFeedback));
    setsInput.setOnKeyTyped(event ->
            new IntValidator(setsTitle, setsInput, exceptionFeedback));
    restInput.setOnKeyTyped(event ->
            new IntValidator(restTimeTitle, restInput, exceptionFeedback));
  }

  /**
   * Sets the workout table columns. Clears the columns first, to avoid duplicate columns.
   * After the columns are created, they are added to the table view
   * The exercises to the workout appears in to the table view if a saved workout is loaded.
   */
  public void updateTable() {
    workoutTable.getColumns().clear();
    exerciseNameColumn = new TableColumn<>("Exercise name");
    exerciseNameColumn.setCellValueFactory(
            new PropertyValueFactory<>("name")
    );
    repGoalColumn = new TableColumn<>("Rep goal");
    repGoalColumn.setCellValueFactory(
            new PropertyValueFactory<>("repGoal")
    );
    weightColumn = new TableColumn<>("Weight");
    weightColumn.setCellValueFactory(
            new PropertyValueFactory<>("weight")
    );
    setsColumn = new TableColumn<>("Nr of sets");
    setsColumn.setCellValueFactory(
            new PropertyValueFactory<>("sets")
    );
    restTimeColumn = new TableColumn<>("Rest time (sec)");
    restTimeColumn.setCellValueFactory(
            new PropertyValueFactory<>("restTime")
    );
    workoutTable.getColumns().add(exerciseNameColumn);
    workoutTable.getColumns().add(repGoalColumn);
    workoutTable.getColumns().add(weightColumn);
    workoutTable.getColumns().add(setsColumn);
    workoutTable.getColumns().add(restTimeColumn);
    setColumnProperties();
    workoutTable.getItems().setAll(exercises);
  }

  /**
  * Sets different properties to the columns.
  * Width, not reorderable and not resizable functionality is set.
  */
  private void setColumnProperties() {
    exerciseNameColumn.setPrefWidth(210);        
    repGoalColumn.setPrefWidth(65);
    weightColumn.setPrefWidth(65);
    setsColumn.setPrefWidth(64);
    restTimeColumn.setPrefWidth(94);
  
    exerciseNameColumn.setReorderable(false);
    repGoalColumn.setReorderable(false);
    weightColumn.setReorderable(false);
    setsColumn.setReorderable(false);
    restTimeColumn.setReorderable(false);

    repGoalColumn.setResizable(false);
    weightColumn.setResizable(false);
    setsColumn.setResizable(false);
    restTimeColumn.setResizable(false);
  }
  
  TableView<Exercise> getWorkoutTable() {
    return workoutTable;
  }

  /**
   * Creates an exercise object based on the input in the input fields.
   * Adds the exercise to the table view and to the user's data.
   * Will show an error in the GUI if the exercise could not be added/created.
   */
  @FXML
  void addExercise() {
    if (exceptionFeedback.getText().equals("") && !checkForEmptyInputFields()) {
      try {
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
          weight = Double.parseDouble(weightInput.getText());
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
        String name = exerciseNameInput.getText();
        int repsPerSet = 0;
        Exercise exercise = new Exercise(name, repGoal, weight, sets, repsPerSet, rest);
    
        for (Exercise e : exercises) {
          if (e.getName().equals(exercise.getName())) {
            exceptionFeedback.setText("Could not add exercise because it is already added!");
            return;
          }
        }       
        exercises.add(exercise);
        exceptionFeedback.setText("Exercise added and saved to the workout!");
        updateTable();
        createButton.setDisable(false);
        emptyInputFields();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage());
      }
    } else if (checkForEmptyInputFields()) {
      exceptionFeedback.setText("Input missing in one or more fields");
    } else if (exceptionFeedback.getText().equals("Could not add exercise because it is already added!")) {
      return; // Do nothing. Do not change the exception feedback to the one underneath
    } else {
      exceptionFeedback.setText("Wrong input, exercise was not created");
    }
  }

  /**
  *  Empties all the input fields when an exercise is created successfully.
  */
  private void emptyInputFields() {
    exerciseNameInput.setText("");
    repGoalInput.setText("");
    weightInput.setText("");
    setsInput.setText("");
    restInput.setText("");
  }

  private boolean checkForEmptyInputFields() {
    int counter = 0;
    if (exerciseNameInput.getText().equals("")) {
      exerciseNameInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (repGoalInput.getText().equals("")) {
      repGoalInput.setStyle(WRONG_INPUT_BORDER_COLOR);
      counter++;
    }
    if (weightInput.getText().equals("")) {
      weightInput.setStyle(WRONG_INPUT_BORDER_COLOR);
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
    return counter > 1;
  }

  /**
  * Loads a workout using title input in GUI.
  * If no title input is given, an error message is displayed in GUI.
  * If no file found with given title, an error message is displayed in GUI.
  *
  * @param event the event the "Load workout" button is clicked.
  */
  @FXML
  void loadWorkout(ActionEvent event) {
    if (titleInput.getText().equals("") || titleInput.getText() == null) {
      exceptionFeedback.setText("Missing Title!");
      return;
    }
    String name = titleInput.getText();
    try {
      String id = null;
      Map<String, String> workoutMap = service.getWorkoutMap();
      Iterator it = workoutMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        if (entry.getValue().equals(name)) {
          id = entry.getKey().toString();
          break;
        }
      }
      workout = service.getWorkout(id);
      exercises = new ArrayList<>();
      for (String exerciseId : workout.getExerciseIds()) {
        Exercise e = service.getExercise(exerciseId);
        if (e != null) {
          exercises.add(e);
        }
      }
      createButton.setVisible(false);
      titleInput.setDisable(true);
      loadButton.setDisable(true);
      updateTable();
    } catch (Exceptions.IllegalIdException e) {
      exceptionFeedback.setText("Invalid workout!");
    } catch (Exceptions.WorkoutNotFoundException | Exceptions.BadPackageException
        | Exceptions.ServerException | URISyntaxException | JsonProcessingException
        | Exceptions.ExerciseNotFoundException e) {
      exceptionFeedback.setText(e.getMessage());
    }
  }

  /**
  * Creates a workout to the user based on the exercises added and the title input.
  * If no title input is given, an error message is displayed in GUI.
  * If an error occurs in saveWorkout, an error message is displayed in GUI.
  */
  @FXML
  void createWorkout() {
    if (titleInput.getText() == null || titleInput.getText().equals("")) {
      exceptionFeedback.setText("Input title is empty, please enter name of workout");
    } else {
      try {
        workout.setName(titleInput.getText());
        service.addWorkout(workout, exercises);
        exceptionFeedback.setText("Workout saved!");
        emptyInputFields();
        titleInput.setText("");
        createButton.setDisable(true);
        workout = new Workout();
        exercises = new ArrayList<>();
        updateTable();
      } catch (IllegalArgumentException | Exceptions.BadPackageException 
        | Exceptions.ServerException | URISyntaxException 
        | Exceptions.WorkoutNotFoundException | Exceptions.ExerciseAlreadyExistsException 
        | JsonProcessingException | Exceptions.WorkoutAlreadyExistsException | Exceptions.IllegalIdException i) {
        exceptionFeedback.setText(i.getMessage());
      }
    }
  }

  /**
   * Listener for when an exercise is clicked in the table view.
   */
  @FXML
  private void exerciseSelectedListener() {
    Exercise selectedExercise = workoutTable.getSelectionModel().getSelectedItem();
    deleteButton.setDisable(selectedExercise == null);
  }

  /**
   * Deletes the selected exercise from the workout.
   */
  @FXML
  void deleteExercise() {
    Exercise selectedExercise = workoutTable.getSelectionModel().getSelectedItem();
    try {
      if (workout.getId() != null) {
        if (selectedExercise.getId() != null) {
          service.removeExercise(selectedExercise.getId());
        }
      }
      exercises.remove(selectedExercise);
      exceptionFeedback.setText("The exercise '" + selectedExercise.getName() + "' was deleted!");
      updateTable();
      deleteButton.setDisable(true);
    } catch (Exceptions.BadPackageException | JsonProcessingException
        | Exceptions.IllegalIdException | Exceptions.ServerException
        | URISyntaxException e) {
      exceptionFeedback.setText(e.getMessage());
    }
  }

  TextField getWeightInput() {
    return this.weightInput;
  }
  /**
  * Validates if int given as input is allowed.
  * If int is not accepted, border for input field is given red colour.
  */

  public class IntValidator {
    /**
    * IntValidators method for validating.
    *
    * @param title title of the inputfield
    * @param field input-field to be validated
    * @param exception exception text to be changed if error found
    */
    public IntValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      try {
        int num = Integer.parseInt(text);
        if (num <= 0) {
          exception.setText(title.getText().replace(":", "") + " must be more than 0");
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        } else if (String.valueOf(num).length() > maxIntLength) {
          exception.setText(
                title.getText().replace(":", "") + " can not be longer than "
                + maxIntLength + " characters!"
          );
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        } else {
          exceptionFeedback.setText("");
          field.setStyle(CORRECT_INPUT_BORDER_COLOR);
        }
      } catch (NumberFormatException e) {
        exception.setText(
            title.getText().replace(":", "")
            + " must be a number and can not exceed "
            + maxIntLength + " characters!"
        );
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      }
    }
  }

  /**
  * Validates if double given as input is allowed.
  * If double is not accepted, border for input field is given red colour.
  */
  public class DoubleValidator {
    /**
    * DoubleValidators method for validating.
    *
    * @param title title of the inputfield
    * @param field input-field to be validated
    * @param exception exception text to be changed if error found
    */
    public DoubleValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      try {
        double num = Double.parseDouble(text);
        if (num <= 0) {
          exception.setText(title.getText().replace(":", "") + " must be more than 0");
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        } else if (String.valueOf(num).length() > maxDoubleLength) {
          exception.setText(
                title.getText().replace(":", "") + " can not be longer than "
                + maxDoubleLength + " characters! (including the '.' and decimals)!"
          );
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        } else {
          exceptionFeedback.setText("");
          field.setStyle(CORRECT_INPUT_BORDER_COLOR);
        }
      } catch (NumberFormatException e) {
        exception.setText(
                title.getText().replace(":", "")
                + " must be a number and can not exceed "
                + maxDoubleLength + " characters (including the '.' and decimals)!"
        );
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      }
    }
  }

  /**
  * Validates if String given as input is allowed.
  * If string is not accepted, border for input field is given red colour.
  */
  public class StringValidator {
    /**
    * StringValidators method for validating.
    *
    * @param title title of the inputfield
    * @param field input-field to be validated
    * @param exception exception text to be changed if error found
    */
    public StringValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      text = text.trim();
      if (text.length() <= 0) {
        exception.setText(
                title.getText().replace(":", "")
                + " can not be blank"
        );
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      } else if (text.length() > maxStringLength) {
        exception.setText(
                title.getText().replace(":", "")
                + " must be less than "
                + maxStringLength
                + " characters!"
        );
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      } else {
        exceptionFeedback.setText("");
        field.setStyle(CORRECT_INPUT_BORDER_COLOR);
      }
    }
  }
}