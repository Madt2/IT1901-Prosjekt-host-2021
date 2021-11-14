package beastbook.fxui;

import beastbook.core.User;
import beastbook.core.History;
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
import java.io.IOException;

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
  private TableView<History> historyOverview = new TableView<>();
  private TableColumn<History, String> historyNameColumn;
  private TableColumn<History, String> historyDateColumn;
  private String selectedHistoryName;
  private String selectedHistoryDate;

  @FXML
  public void initialize() throws IOException {
    user = user.loadUser(user.getUserName());
    loadTable();
  }

  private void loadTable() {
    historyOverview.getColumns().clear();
    historyNameColumn = new TableColumn<History, String>("Workout name:");
    historyNameColumn.setCellValueFactory(new PropertyValueFactory<History, String>("name"));
    historyDateColumn = new TableColumn<History, String>("Date:");
    historyDateColumn.setCellValueFactory((new PropertyValueFactory<History, String>("date")));
    historyOverview.getColumns().add(historyDateColumn);
    historyOverview.getColumns().add(historyNameColumn);
    historyOverview.getItems().setAll(user.getHistories());
    setColumnsSize();
  }

  private void setColumnsSize() {
    historyNameColumn.setPrefWidth(129);
    historyNameColumn.setResizable(false);
    historyDateColumn.setPrefWidth(129);
    historyDateColumn.setResizable(false);
  }

  @FXML
  void loadHistory(ActionEvent event) throws IOException {
    try {
      exceptionFeedback.setText("");
      HistoryController historyController = new HistoryController();
      FXMLLoader fxmlLoader = new FXMLLoader(
          this.getClass().getResource("/beastbook.fxui/History.fxml")
      );
      fxmlLoader.setController(historyController);
      historyController.setUser(user);
      historyController.setHistoryName(getHistoryName());
      historyController.setHistoryDate(getHistoryDate());
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
  private void historySelectedListener() throws Exception {
    try {
      selectedHistoryName = historyOverview.getSelectionModel().getSelectedItem().getName();
      selectedHistoryDate = historyOverview.getSelectionModel().getSelectedItem().getDate();
      if (selectedHistoryName != null) {
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
    user.removeHistory(getHistoryName(), getHistoryDate());
    loadTable();
    exceptionFeedback.setText("History entry deleted!");
    user.saveUser();
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }

  TableView<History> getHistoryOverview() {
    return historyOverview;
  }

  private String getHistoryDate() {
    return selectedHistoryDate;
  }
  
  public String getHistoryName() {
    return selectedHistoryName;
  }

  public void setUser(User user) {
    this.user = user;
  }
}