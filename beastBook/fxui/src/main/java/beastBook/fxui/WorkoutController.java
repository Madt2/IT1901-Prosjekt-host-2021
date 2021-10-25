package beastBook.fxui;

import beastBook.core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.io.IOException;
import beastBook.core.Exercise;
import beastBook.core.Workout;
import javafx.scene.control.MenuItem;
import beastBook.json.BeastBookPersistence;

public class WorkoutController extends AbstractController{
  @FXML
  private MenuItem logOutButton;

  @FXML
  private TableView<Exercise> workoutTable;

  @FXML
  private Button backButton;

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
  private Workout workout = new Workout();
  public static final String WRONG_INPUT_BORDER_COLOR = "-fx-text-box-border: #B22222; -fx-focus-color: #B22222";
  public static final String CORRECT_INPUT_BORDER_COLOR = "";
  BeastBookPersistence persistence = new BeastBookPersistence();

  @FXML
  public void initialize() {
    updateTable();
    title.setText(workout.getName());
  }

  public void setWorkout(Workout workout) {
    this.workout = workout;
  }
    
  public Workout getWorkout() {
    return this.workout;
  }

  /**
  * Sets the workout table columns and adds them to the table view.
  */
  public void updateTable() {
    workoutTable.getColumns().clear();
    workoutTable.setEditable(true);    
    
    exerciseNameColumn = new TableColumn<Exercise, String>("Exercise name:");
    exerciseNameColumn.setCellValueFactory(new PropertyValueFactory<Exercise, String>("exerciseName"));
    
    repGoalColumn = new TableColumn<Exercise, Integer>("Rep goal:");
    repGoalColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repGoal"));

    weightColumn = new TableColumn<Exercise, Double>("Weight:");
    weightColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Double>("weight"));

    setsColumn = new TableColumn<Exercise, Integer>("Nr of sets:");
    setsColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("sets"));

    repsPerSetColumn = new TableColumn<Exercise, Integer>("Reps per set:");
    repsPerSetColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("repsPerSet"));
        
    restTimeColumn = new TableColumn<Exercise, Integer>("Rest time in sec:");
    restTimeColumn.setCellValueFactory(new PropertyValueFactory<Exercise, Integer>("restTime"));

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
  private void editTable() {        
    exerciseNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    exerciseNameColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setExerciseName(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch (IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Wrong input, please try again. Value was not changed!");
        workoutTable.refresh();
      }
    });

    repGoalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
    repGoalColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setRepGoal(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch(IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Rep Goal must be a number. Value was not changed!");
        workoutTable.refresh();
      }
    });
   
    weightColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
    weightColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setWeight(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch(IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Working Weight must be a number. Value was not changed!");
        workoutTable.refresh();
      }
    });
     
    setsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
    setsColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setSets(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch(IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Sets must be a number. Value was not changed!");
        workoutTable.refresh();
      }
    });

    repsPerSetColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
    repsPerSetColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setRepsPerSet(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch(IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Reps per set must be a number. Value was not changed!");
        workoutTable.refresh();
      }
    });

    restTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
    restTimeColumn.setOnEditCommit(event -> {
      try {
        Exercise exercise = event.getRowValue();
        exercise.setRestTime(event.getNewValue());
        saveUserState();
        emptyExceptionFeedback();
      } catch(IllegalArgumentException i) {
        exceptionFeedback.setText(i.getMessage() + " Value was not changed!");
        workoutTable.refresh();
      } catch (Exception e) {
        exceptionFeedback.setText("Rest time must be a number. Value was not changed!");
        workoutTable.refresh();
        }
    });
    workoutTable.getItems().setAll(workout.getExercises());
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
    restTimeColumn.setPrefWidth(110);
  }

  @Override
  void loadLogin() throws IOException {
    super.loadLogin();
  }

  @Override
  void loadOverview() throws IOException {
    super.loadOverview();
  }

  void setUser(User user) {
    this.user = user;
  }

  /**
  * Updates the exercises/workouts for a user in file format
  */
  private void saveUserState() throws IOException {
    persistence.setSaveFilePath(user.getUserName());
    persistence.saveUser(user);
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
  /*
  public class IntValidator {
    public IntValidator(Text title, TextField field, Text exception) {
      String text = field.getText();
      try {
        int num = Integer.parseInt(text);
        if (num <= 0) {
          exception.setText(title.getText().replace(":", "") + " must be more than 0");
          field.setStyle(WRONG_INPUT_BORDER_COLOR);
        } else {
          exceptionFeedback.setText("");
          field.setStyle(CORRECT_INPUT_BORDER_COLOR);
        }
      } catch (NumberFormatException e) {
        exception.setText(title.getText().replace(":", "") + " must be a number and can not exceed " + Integer.MAX_VALUE);
        field.setStyle(WRONG_INPUT_BORDER_COLOR);
      }
    }
  }
  */
}