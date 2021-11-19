package beastbook.fxui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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
  private String selectedWorkoutId;
  private Map<String,String> workoutMap;

  public void initialize() throws IOException {
    workoutMap = service.getWorkoutMap();
    loadTable();
  } 
    
  /**
  * Creates a table view with a column for workout name and
  * adds the users workouts to the table view.
  */
  private void loadTable() {
    workoutOverview.getColumns().clear();
    workoutNameColumn = new TableColumn<>("Workout name:");
    workoutNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    workoutOverview.getColumns().add(workoutNameColumn);
    workoutOverview.getItems().setAll(workoutMap.values());
    setColumnsSize();
  }

  @FXML
  private void workoutSelectedListener() throws Exception {
    //TODO is try/catch needed?
    try {
      String name = workoutOverview.getSelectionModel().getSelectedItem();
      Iterator it = workoutMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        if (entry.getValue().equals(name)) {
          selectedWorkoutId = entry.getKey().toString();
          break;
        }
      }
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
      workoutController.setService(service);
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
    if (selectedWorkoutId != null) {
      service.removeWorkout(selectedWorkoutId);
      workoutMap = service.getWorkoutMap();
      exceptionFeedback.setText("Workout deleted!");
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    } else {
      exceptionFeedback.setText("Workout not deleted, could not find workout!");
    }
    loadTable();

  }
}
