package beastbook.fxui;

import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the WorkoutOverview screen.
 */
public class WorkoutOverviewController extends AbstractController {
  @FXML
  private AnchorPane rootPane;

  @FXML
  private Button backButton;

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<String> workoutOverview = new TableView<>();
  private TableColumn<String, String> workoutNameColumn;
  private List<String> workoutNames = new ArrayList<>();
  private List<String> workoutIds = new ArrayList<>();
  private String selectedWorkoutId;

  public void initialize() throws IOException {
    workoutIds = service.queryUser(getUsername()).getWorkoutIDs();
    for (String id : workoutIds) {
      String name = service.queryWorkoutName(id, getUsername());
      if (name != null) {
        workoutNames.add(name);
      }
    }
    loadTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  private void loadTable() {
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<String, String>("Workout name:");
    workoutNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(workoutNames);
    setColumnsSize();
  }

  @FXML
  private void workoutSelectedListener() throws Exception {
    try {
      int i = workoutOverview.getSelectionModel().getFocusedIndex();
      selectedWorkoutId = workoutIds.get(i);
      if (selectedWorkoutId != null) {
        exceptionFeedback.setText("");
        openButton.setDisable(false);
        deleteButton.setDisable(false);
      }
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }

  String getWorkoutName() {
    return selectedWorkoutId;
  }

  TableView<String> getWorkoutOverview() {
    return workoutOverview;
  }

  private void setColumnsSize() {
    workoutNameColumn.setPrefWidth(150);    
  }

  @FXML
  void loadWorkout(ActionEvent event) throws IOException {
    try {
      exceptionFeedback.setText("");
      WorkoutController workoutController = new WorkoutController();
      FXMLLoader fxmlLoader = new FXMLLoader(
              this.getClass().getResource("/beastbook.fxui/Workout.fxml")
      );
      fxmlLoader.setController(workoutController);
      workoutController.setWorkoutId(selectedWorkoutId);
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root, 600, 500);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No workout is selected!");
    }
  }

  @FXML
  void deleteWorkout() throws IllegalStateException {
    //TODO handle eventual deletion error
    int i = workoutOverview.getSelectionModel().getFocusedIndex();
    service.deleteWorkout(workoutIds.get(i),getUsername());
    workoutNames.remove(workoutNames.get(i));
    workoutIds.remove(i);
    loadTable();
    exceptionFeedback.setText("Workout deleted!");
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }
}
