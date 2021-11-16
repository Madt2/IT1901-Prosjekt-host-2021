package beastbook.fxui;

import beastbook.core.History;
import beastbook.core.User;
import java.io.IOException;
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

  /**
  * Sets the column for the tableview and fills the history data from the user into the table view.
  */
  private void loadTable() {
    historyOverview.getColumns().clear();

    historyNameColumn = new TableColumn<>("Workout name:");
    historyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    historyDateColumn = new TableColumn<>("Date:");
    historyDateColumn.setCellValueFactory((new PropertyValueFactory<>("date")));

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

  /**
  * Loads the history which has been selected after Open button is clicked.
  *
  * @param event the event when open button is clicked
  */
  @FXML
  void loadHistory(ActionEvent event) {
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

  /**
  * Listener which registeres if a history is clicked on in the table view.
  */
  @FXML
  private void historySelectedListener() {
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

  /**
  * Deletes the selected history from the tableview and the user when Delete button is clicked.
  */
  @FXML
  void deleteHistory() {
    try {
      user.removeHistory(getHistoryName(), getHistoryDate());
      loadTable();
      exceptionFeedback.setText("History entry deleted!");
      user.saveUser();
      openButton.setDisable(true);
      deleteButton.setDisable(true);
    } catch (IOException e) {
      exceptionFeedback.setText("User could not delete the history entry!");
    }
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