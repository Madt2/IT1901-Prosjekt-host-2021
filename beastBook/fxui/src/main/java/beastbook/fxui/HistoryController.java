package beastbook.fxui;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


/**
 * Controller for the History screen.
 */
public class HistoryController extends AbstractController {

  @FXML
  private TableView<Exercise> historyTable;

  @FXML
  private Text title;

  @FXML
  private Text date;

  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> repsPerSetColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;
  private String historyId;

  /**
   * Initialize method to load user and set the table view with its data.
   * Also sets name and date of the workout in text fields.
   *
   */
  @FXML
  public void initialize() throws IOException {
    History history = service.queryHistory(historyId, getUsername());
    setTable(history);
    title.setText(history.getName());
    date.setText(history.getDate());
  }

  /**
   * Adds columns for each exercise to the table view and loads data into the table view.
   *
   */
  public void setTable(History history) {
    historyTable.getColumns().clear();
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

    historyTable.getColumns().add(exerciseNameColumn);
    historyTable.getColumns().add(repGoalColumn);
    historyTable.getColumns().add(weightColumn);
    historyTable.getColumns().add(setsColumn);
    historyTable.getColumns().add(repsPerSetColumn);
    historyTable.getColumns().add(restTimeColumn);
    customizeHistoryTable();
    historyTable.getItems().setAll(history.getSavedExercises());
  }

  /**
   * Changes the width of the columns in the table view.
   *
   */
  private void customizeHistoryTable() {
    exerciseNameColumn.setPrefWidth(100);
    repGoalColumn.setPrefWidth(75);
    weightColumn.setPrefWidth(75);
    setsColumn.setPrefWidth(75);
    repsPerSetColumn.setPrefWidth(80);
    restTimeColumn.setPrefWidth(106);
  }

  TableView<Exercise> getHistoryTable() {
    return historyTable;
  }

  public void setHistoryId(String historyId) {
    this.historyId = historyId;
  }
}
