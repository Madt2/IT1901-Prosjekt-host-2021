package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.Workout;
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
  private Button backButton;

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

  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
//  private List<String> workoutIds = new ArrayList<>();
//  private List<String> workoutNames = new ArrayList<>();
  private List<Exercise> exercises = new ArrayList<>();
  private Workout workout = new Workout();
  //TODO use loadedWorkout boolean
//  private boolean loadedWorkout = (workout.getId() != null);

  public static final String WRONG_INPUT_BORDER_COLOR = "-fx-text-box-border: #B22222;"
          + "-fx-focus-color: #B22222";
  public static final String CORRECT_INPUT_BORDER_COLOR = "";

  /**
   * Initializes the CreateWorkout scene with the listeners for validation of input fields.
   */
  public void initialize() {
/*    workoutIds = service.queryUser(getUsername()).getWorkoutIDs();
    for (String id : workoutIds) {
      String name = service.queryWorkoutName(id, getUsername());
      if (name != null) {
        workoutNames.add(name);
      }
    }*/
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
  * After the columns are created, they are added to the table view. 
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
    restTimeColumn = new TableColumn<>("Rest time in sec");
    restTimeColumn.setCellValueFactory(
            new PropertyValueFactory<>("restTime")
    );
    workoutTable.getColumns().add(exerciseNameColumn);
    workoutTable.getColumns().add(repGoalColumn);
    workoutTable.getColumns().add(weightColumn);
    workoutTable.getColumns().add(setsColumn);
    workoutTable.getColumns().add(restTimeColumn);
    setColumnsSize();

    workoutTable.getItems().setAll(exercises);
  }

  /**
  * Resizes the width of the columns.
  */
  private void setColumnsSize() {
    exerciseNameColumn.setPrefWidth(100);        
    repGoalColumn.setPrefWidth(75);
    weightColumn.setPrefWidth(75);
    setsColumn.setPrefWidth(80);
    restTimeColumn.setPrefWidth(100);
  }

  /**
  * Return the exercise on the specified row. Mainly used for test reasons.
  *
  * @param row the row you want to have access to / get an Exercise object from.
  *
  * @return the Exercise object on the requested row
  */
  Exercise getTable(int row) {
    return workoutTable.getItems().get(row);
  }
    
  /**
  * Gets the workout table.
  *
  * @return the workout table
  */
  TableView<Exercise> getWorkoutTable() {
    return workoutTable;
  }

  /**
  *  Runs when the "Add exercise" button is clicked.
  *  If all the input fields are in the correct format,
  *  an Exercise object is made with the input fields data.
  *  The exercise object is then added to the workout object and
  *  its list over exercises, this is then connected to the signed-in user.
  *  The workout table is then "reloaded" with the new exercise added to the list.
  *  If the input fields are not in the correct format, the method catches the Exception.
  *  A text with red color appears on the screen with a message to the user
  *  saying that the exercise could not be added (because of wrong inputs).
  *  The text disappears when an exercise is added successfully.
  */
  @FXML
  void addExercise() throws IllegalArgumentException {
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
        //TODO fix this entire block underneath
        if (workout.getId() != null) {
          for (Exercise e : exercises) {
            if (e.getName().equals(exercise.getName())) {
              exercises.remove(e);
              exercises.add(exercise);
              exceptionFeedback.setText("Exercise overwritten!");
            }
          }
          service.addExercise(exercise, workout.getId());
        } else {
          for (Exercise e : exercises) {
            if (e.getName().equals(exercise.getName())) {
              exercises.remove(e);
              exercises.add(exercise);
              exceptionFeedback.setText("Exercise overwritten!");
            }
          }
        }
        if (!exceptionFeedback.getText().equals("Exercise overwritten!")) {
          exercises.add(exercise);
          exceptionFeedback.setText("Exercise added!");
        }
        updateTable();
        exceptionFeedback.setText("");
        createButton.setDisable(false);
        emptyInputFields();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage());
      }
    } else if (checkForEmptyInputFields()) {
      exceptionFeedback.setText("Input missing in a field");
    } else {
      exceptionFeedback.setText("Wrong input, exercise was not created");
    }
  }

  /**
  * Empties all the input fields.
  * Should be called when an exercise is successfully added to the workout
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
  * @param event When Load Workout button is clicked in GUI, loadWorkout() is fired.
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
      for (String eId : workout.getExerciseIDs()) {
        Exercise e = service.getExercise(eId);
        if (e != null) {
          exercises.add(e);
        }
      }
      createButton.setVisible(false);
      //titleInput.setVisible(false);
      titleInput.setDisable(true);
      loadButton.setDisable(true);

    } catch (Exception e) {
      exceptionFeedback.setText("Workout not found!");
    }
    updateTable();
  }

  /**
  * Creates a workout and saves it as a file with input given in GUI.
  * If no title input is given, an error message is displayed in GUI.
  * If an error occurs in saveWorkout, an error message is displayed in GUI.
  */
  @FXML
  void createWorkout() {
    if (titleInput.getText() == null || titleInput.getText().equals("")) {
      exceptionFeedback.setText("Input title is empty, please enter name of workout");
    }
    else {
      try {
        //hvis den er loadet
        if (workout.getId() != null) {
          //service.updateWorkout(workout, getUsername());
          exceptionFeedback.setText("Workout overwritten!");
        } else {
          workout.setName(titleInput.getText());
          if (!service.addWorkout(workout, exercises)) {
            exceptionFeedback.setText("Workout with name " + workout.getName() + " already exists. Please choose another name or delete: " + workout.getName());
          } else {
            service.addWorkout(workout, exercises);
            exceptionFeedback.setText("Workout saved!");
            emptyInputFields();
            titleInput.setText("");
            createButton.setDisable(true);
            workout = new Workout();
            exercises = new ArrayList<>();
            updateTable();
          }
        }
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage());
      } catch (Exception e) {
        exceptionFeedback.setText("Save Workout failed!");
      }
    }
  }

  @FXML
  private void exerciseSelectedListener() {
    Exercise selectedExercise = workoutTable.getSelectionModel().getSelectedItem();
    deleteButton.setDisable(selectedExercise == null);
  }

  @FXML
  void deleteExercise() {
    Exercise selectedExercise = workoutTable.getSelectionModel().getSelectedItem();
    try {
      if (workout.getId() != null) {
        System.out.println("Exercise id = " + selectedExercise.getId());
        System.out.println("Exercises IDs til workout" + workout.getExerciseIDs());
        if (selectedExercise.getId() != null){
          service.removeExercise(selectedExercise);
        }
        exercises.remove(selectedExercise);
        System.out.println("ID" + workout.getId());
        System.out.println("Exercise id = " + selectedExercise.getId());

        System.out.println("Name" + workout.getName());
        System.out.println("Workouten henta fra service: " +service.getWorkout(workout.getId()));

      } else {
        exercises.remove(selectedExercise);
      }

      exceptionFeedback.setText("The exercise '"
              + selectedExercise.getName() + "' was deleted!");
      updateTable();
      deleteButton.setDisable(true);
    } catch (Exception e) {
      exceptionFeedback.setText("The exercise '"
              + selectedExercise.getName() + "' does not exist");
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
        } else if (String.valueOf(num).length() >= Exercise.maxIntLength) {
          exception.setText(
                title.getText().replace(":", "") + " can not be longer than "
                + Exercise.maxIntLength + " characters!"
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
            + Exercise.maxIntLength
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
        } else if (String.valueOf(num).length() >= Exercise.maxDoubleLength) {
          exception.setText(
                title.getText().replace(":", "") + " can not be longer than "
                + Exercise.maxDoubleLength + " characters!"
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
                + Exercise.maxDoubleLength
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
      } else if (text.length() >= Exercise.maxStringLength) {
        exception.setText(
                title.getText().replace(":", "")
                + " must be less than "
                + Exercise.maxStringLength
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