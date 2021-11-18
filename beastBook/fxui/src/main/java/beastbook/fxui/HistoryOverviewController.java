package beastbook.fxui;

import beastbook.core.History;
import beastbook.core.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Controller for the HistoryOverview screen.
 */
public class HistoryOverviewController extends AbstractController {
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
  private TableView<String> historyOverview = new TableView<>();
  private TableColumn<String, String> historyNameColumn;
  private TableColumn<String, String> historyDateColumn;
  private String selectedHistoryId;
  private Map<String,String> historyMap;

  @FXML
  public void initialize() throws IOException {
    historyIds = service.queryUser(getUsername()).getHistoryIDs();
    for (String id : historyIds) {
      String name = service.queryHistoryName(id, getUsername());
      if (name != null) {
        historyNames.add(name);
      }
    }
    loadTable();
  }

  private void loadTable() {
    historyOverview.getColumns().clear();
    historyNameColumn = new TableColumn<>("Workout name:");
    historyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    historyDateColumn = new TableColumn<>("Date:");
    historyDateColumn.setCellValueFactory((new PropertyValueFactory<>("date")));
    historyOverview.getColumns().add(historyDateColumn);
    historyOverview.getColumns().add(historyNameColumn);
    historyOverview.getItems().setAll(historyNames);
    setColumnsSize();
  }

  private void setColumnsSize() {
    historyNameColumn.setPrefWidth(129);
    historyNameColumn.setResizable(false);
    historyDateColumn.setPrefWidth(129);
    historyDateColumn.setResizable(false);
  }

  @FXML
  void loadHistory(ActionEvent event) {
    try {
      exceptionFeedback.setText("");
      HistoryController historyController = new HistoryController();
      FXMLLoader fxmlLoader = new FXMLLoader(
          this.getClass().getResource("/beastbook.fxui/History.fxml")
      );
      fxmlLoader.setController(historyController);
      historyController.setHistoryId(getSelectedHistoryId());
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root, 600, 500);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No history entry is selected!");
    }
  }

  @FXML
  private void historySelectedListener() {
    try {
      int i = historyOverview.getSelectionModel().getFocusedIndex();
      selectedHistoryId = historyIds.get(i);
      if (selectedHistoryId != null) {
        exceptionFeedback.setText("");
        openButton.setDisable(false);
        deleteButton.setDisable(false);
      }
    } catch (Exception e) {
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    }
  }
  
  @FXML
  void deleteHistory() throws IllegalStateException, IOException {
    int i = historyOverview.getSelectionModel().getFocusedIndex();
    service.deleteWorkout(historyIds.get(i),getUsername());
    historyNames.remove(historyNames.get(i));
    historyIds.remove(i);
    loadTable();
    exceptionFeedback.setText("History entry deleted!");
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }

  TableView<String> getHistoryOverview() {
    return historyOverview;
  }

  private String getSelectedHistoryId() {
    return selectedHistoryId;
  }

}