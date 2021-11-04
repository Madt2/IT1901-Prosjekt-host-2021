package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
  private User user;

  @FXML
  public void initialize() {
    updateTable();
    title.setText(workoutName);
  }

  /**
  * Sets the workout table columns and adds them to the table view.
  */
  public void updateTable() {
    workoutTable.getColumns().clear();
    workoutTable.setEditable(true);
    
    exerciseNameColumn = new TableColumn<Exercise, String>("Exercise name:");
    exerciseNameColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, String>("exerciseName")
    );
    
    repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal:");
    repGoalColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, Integer>("repGoal")
    );

    weightColumn = new TableColumn<Exercise, Double>("Weight:");
    weightColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, Double>("weight")
    );

    setsColumn = new TableColumn<Exercise, Integer>("Nr of sets:");
    setsColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, Integer>("sets")
    );

    repsPerSetColumn = new TableColumn<Exercise, Integer>("Reps per set:");
    repsPerSetColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, Integer>("repsPerSet")
    );
        
    restTimeColumn = new TableColumn<Exercise, Integer>("Rest time in sec:");
    restTimeColumn.setCellValueFactory(
      new PropertyValueFactory<Exercise, Integer>("restTime")
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
  * Makes the cells in workout table editable.
  * If a cell is edited, the new value will overwrite
  * the old value in both the GUI and the user database.
  * If the value is in wrong format, an exeption will be thrown
  * and a red text will appear in the GUI with feedback.
  * Sets the exercises from the workout to the table view.
  */

  private void tableWithString(TableColumn<Exercise, String> javaFxTag, String errorMessage) {
    javaFxTag.setCellFactory(TextFieldTableCell.forTableColumn());
    javaFxTag.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setExerciseName(event.getNewValue());
        user.saveUser();
        emptyExceptionFeedback();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }

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
        user.getWorkout(workoutName).updateExercise(exercise);
        user.updateWorkout(user.getWorkout(workoutName));
        user.saveUser();
        emptyExceptionFeedback();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }

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
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText(errorMessage);
        workoutTable.refresh();
      }
    });
  }

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

  @Override
  void loadOverview(ActionEvent event, String username) throws IOException {
    super.loadOverview(event, user.getUserName());
  }

  void setUser(String username) throws IOException {
    this.user = user.loadUser(username);
  }

  void setWorkoutName(String workoutName) {
    this.workoutName = workoutName;
  }

  // SOURCE for the following two static classes: 
  // https://stackoverflow.com/questions/56376182/javafx-exception-handling-in-an-editable-textfieldtablecell
    
  /**
  * Extended version of IntegerStringConverter. Used to catch NumberFormatException, as 
  * the IntegerStringConverter do not handle the NumberFormatException by default.
  * Returns null which is catched by editTable()
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
  * Returns null which is catched by editTable()
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