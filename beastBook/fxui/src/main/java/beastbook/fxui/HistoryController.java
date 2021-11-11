package beastbook.fxui;

import beastbook.core.User;
import javafx.fxml.FXML;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.io.IOException;

public class HistoryController extends AbstractController{

  @FXML
  private TableView<Exercise> historyTable;

  @FXML
  private Text title;

  private TableColumn<Exercise, String> exerciseNameColumn;
  private TableColumn<Exercise, Integer> repGoalColumn;
  private TableColumn<Exercise, Double> weightColumn;
  private TableColumn<Exercise, Integer> setsColumn;
  private TableColumn<Exercise, Integer> repsPerSetColumn;
  private TableColumn<Exercise, Integer> restTimeColumn;

  private String historyName;
  private String historyDate;

  @FXML
  public void initialize() throws IOException {
      user = user.loadUser(user.getUserName());
      System.out.println(user.getHistories());
      setTable();
      title.setText("Name: " + historyName + " - Date :" + historyDate);
  }

  public void setTable() {
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
    setColumnsSize();

    historyTable.getItems().setAll(user.getWorkout(historyName).getExercises());
  }


  private void setColumnsSize() {
    exerciseNameColumn.setPrefWidth(100);
    repGoalColumn.setPrefWidth(75);
    weightColumn.setPrefWidth(75);
    setsColumn.setPrefWidth(75);
    repsPerSetColumn.setPrefWidth(80);
    restTimeColumn.setPrefWidth(106);
  }

  public void setHistoryName(String historyName) {
    this.historyName = historyName;
  }

  public void setHistoryDate(String historyDate) {
    this.historyDate = historyDate;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
