package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Controller for the Workout screen.
 */
public class WorkoutController extends AbstractController {
  @FXML
  private TableView<Exercise> workoutTable;

  @FXML
  private Text title;

  @FXML
  private TextField dateInput;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private Button saveButton;

  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> repsPerSetColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
  public static final String WRONG_INPUT_BORDER_COLOR =
        "-fx-text-box-border: #B22222;"
        + "-fx-focus-color: #B22222";
  public static final String CORRECT_INPUT_BORDER_COLOR = "";
  private String workoutName;

  /**
   * Initializes the Workout screen with the correct User from File,
   * and updates the table based on given Workout.
   *
   * @throws IOException if an error occurs when loading user.
   */
  @FXML
  public void initialize() throws IOException {
    user = user.loadUser(user.getUserName());
    updateTable();
    title.setText(workoutName);
  }

  /**
  * Sets the workout table columns and adds them to the table view. 
  */
  public void updateTable() {
    workoutTable.getColumns().clear();
    workoutTable.setEditable(true);
    
    exerciseNameColumn = new TableColumn<>("Exercise name:");
    exerciseNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("exerciseName")
    );
    
    repGoalColumn = new TableColumn<>("Rep goal:");
    repGoalColumn.setCellValueFactory(
      new PropertyValueFactory<>("repGoal")
    );

    weightColumn = new TableColumn<>("Weight:");
    weightColumn.setCellValueFactory(
      new PropertyValueFactory<>("weight")
    );

    setsColumn = new TableColumn<>("Nr of sets:");
    setsColumn.setCellValueFactory(
      new PropertyValueFactory<>("sets")
    );

    repsPerSetColumn = new TableColumn<>("Reps per set:");
    repsPerSetColumn.setCellValueFactory(
      new PropertyValueFactory<>("repsPerSet")
    );
        
    restTimeColumn = new TableColumn<>("Rest time in sec:");
    restTimeColumn.setCellValueFactory(
      new PropertyValueFactory<>("restTime")
    );

    workoutTable.getColumns().add(exerciseNameColumn);
    workoutTable.getColumns().add(repGoalColumn);
    workoutTable.getColumns().add(weightColumn);
    workoutTable.getColumns().add(setsColumn);
    workoutTable.getColumns().add(repsPerSetColumn);
    workoutTable.getColumns().add(restTimeColumn);
    editTable();
    setColumnsSize();
  }
  
  /**
   * Makes the column with string format editable and saves the updated data to the user/GUI.
   * Also prints a message to the GUI if a input is in wrong format.
   *
   * @param column the column which have data in string format
   * @param errorMessage the messeage which the user will recive if input is wrong
   */
  private void tableWithString(TableColumn<Exercise, String> column, String errorMessage) {
    column.setCellFactory(TextFieldTableCell.forTableColumn());
    column.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setExerciseName(event.getNewValue());
        user.saveUser();
        emptyExceptionFeedback();
        saveButton.setDisable(false);
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }

  /**
   * Makes the column with integer format editable and saves the updated data to the user/GUI.
   * Also prints a message to the GUI if a input is in wrong format.
   *
   * @param column the column which have data in string format
   * @param errorMessage the messeage which the user will recive if input is wrong
   */
  private void tableWithInteger(TableColumn<Exercise, Integer> column, String errorMessage) {
    column.setCellFactory(TextFieldTableCell.forTableColumn(
            new CustomIntegerStringConverter())
    );
    column.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        if (column.equals(repGoalColumn)) {
          exercise.setRepGoal(event.getNewValue());
        }
        if (column.equals(setsColumn)) {
          exercise.setSets(event.getNewValue());
        }
        if (column.equals(repsPerSetColumn)) {
          exercise.setRepsPerSet(event.getNewValue());
        }
        if (column.equals(restTimeColumn)) {
          exercise.setRestTime(event.getNewValue());
        }
        user.saveUser();
        emptyExceptionFeedback();
        saveButton.setDisable(false);
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }

  /**
   * Makes the column with double format editable and saves the updated data to the user/GUI.
   * Also prints a message to the GUI if a input is in wrong format.
   *
   * @param column the column which have data in string format
   * @param errorMessage the messeage which the user will recive if input is wrong
   */
  private void tableWithDouble(TableColumn<Exercise, Double> column, String errorMessage) {
    column.setCellFactory(TextFieldTableCell.forTableColumn(
            new CustomDoubleStringConverter())
    );
    column.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setWeight(event.getNewValue());
        user.saveUser();
        emptyExceptionFeedback();
        saveButton.setDisable(false);
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }
  
  /**
  * Saves the workout to the history overview. 
  * If the user already have saved the workout to history the same day, 
  * the history will be overwritten with current data from the table view.
  */
  @FXML
  void saveHistory() {
    try {
      boolean overwritten = false;
      for (History h : user.getHistories()) {
        if (h.getName().equals(workoutName) && h.getDate().equals(user.getDate())) {
          user.removeHistory(h.getSavedWorkout().getName(), h.getDate());
          user.addHistory(user.getWorkout(workoutName));
          exceptionFeedback.setText("History overwritten!");
          overwritten = !overwritten;
        }
      }
      if (!overwritten) {
        user.addHistory(user.getWorkout(workoutName));
        exceptionFeedback.setText("Workout was successfully added to history!");
      }
      saveButton.setDisable(true);
      user.saveUser();
    } catch (IOException e) {
      exceptionFeedback.setText("User could not save the workout to history!");
    }
  }

  /**
  * Loads the workout data from the user in to the tableview. 
  * Also sets up the methods to listen after edited cells.
  */
  private void editTable() {
    tableWithString(exerciseNameColumn, "Wrong input, please try again. Value was not changed!");
    tableWithInteger(repGoalColumn, "Rep Goal must be a number. Value was not changed!");
    tableWithDouble(weightColumn, "Working Weight must be a number. Value was not changed!");
    tableWithInteger(setsColumn, "Sets must be a number. Value was not changed!");
    tableWithInteger(repsPerSetColumn, "Reps per set must be a number. Value was not changed!");
    tableWithInteger(restTimeColumn, "Rest time must be a number. Value was not changed!");
    workoutTable.getItems().setAll(user.getWorkout(workoutName).getExercises());
  }
    
  TableView<Exercise> getWorkoutTable() {
    return workoutTable;
  }

  Exercise getTable(int row) {
    return workoutTable.getItems().get(row);
  }

  private void emptyExceptionFeedback() {
    this.exceptionFeedback.setText("");
  }

  private void setColumnsSize() {
    exerciseNameColumn.setPrefWidth(100);        
    repGoalColumn.setPrefWidth(75);
    weightColumn.setPrefWidth(75);
    setsColumn.setPrefWidth(75);
    repsPerSetColumn.setPrefWidth(80);
    restTimeColumn.setPrefWidth(106);
  }

  void setUser(User user) {
    this.user = user;
  }

  void setWorkoutName(String workoutName) {
    this.workoutName = workoutName;
  }

  // SOURCE for the following two static classes: 
  // https://stackoverflow.com/questions/56376182/javafx-exception-handling-in-an-editable-textfieldtablecell
    
  /**
  * Extended version of IntegerStringConverter. Used to catch NumberFormatException, as 
  * the IntegerStringConverter do not handle the NumberFormatException by default.
  * Returns null which is caught by editTable()
  */
  public static class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
      try {
        return converter.toString(object);
      } catch (NumberFormatException e) {
        return null;
      }
    }

    @Override
    public Integer fromString(String string) {
      try {
        return converter.fromString(string);
      } catch (NumberFormatException e) {
        return null;
      }
    }
  }

  /**
  * Extended version of DoubleStringConverter. Used to catch NumberFormatException, as 
  * the DoubleStringConverter do not handle the NumberFormatException by default.
  * Returns null which is caught by updateTable()
  */
  public static class CustomDoubleStringConverter extends DoubleStringConverter {
    private final DoubleStringConverter converter = new DoubleStringConverter();

    @Override
    public String toString(Double object) {
      try {
        return converter.toString(object);
      } catch (NumberFormatException e) {
        return null;
      }
    }

    @Override
    public Double fromString(String string) {
      try {
        return converter.fromString(string);
      } catch (NumberFormatException e) {
        return null;
      }
    }
  }
}