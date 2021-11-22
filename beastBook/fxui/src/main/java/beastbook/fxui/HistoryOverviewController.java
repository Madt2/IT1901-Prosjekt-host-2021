package beastbook.fxui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the HistoryOverview screen.
 */
public class HistoryOverviewController extends AbstractController {

  @FXML
  private Button openButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Text exceptionFeedback;

  @FXML
  private TableView<HistoryData> historyOverview = new TableView<>();
  private TableColumn<HistoryData, String> historyNameColumn;
  private TableColumn<HistoryData, String> historyDateColumn;
  private String selectedHistoryId;
  private Map<String,String> historyMap;

  @FXML
  public void initialize() {
    historyMap = service.getHistoryMap();
    loadTable();
  }

  private void loadTable() {
    historyOverview.getColumns().clear();
    List<HistoryData> historyData = new ArrayList<>();
    for (String s : historyMap.values()) {
      String[] strings = s.split(";");
      historyData.add(new HistoryData(strings[0], strings[1]));
    }
    historyNameColumn = new TableColumn<>("Name:");
    historyNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
    historyDateColumn = new TableColumn<>("Date:");
    historyDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
    historyOverview.getColumns().add(historyDateColumn);
    historyOverview.getColumns().add(historyNameColumn);
    historyOverview.getItems().setAll(historyData);
    setColumnProperties();
  }

  /**
  * Sets different properties to the columns.
  * Width, not reorderable and not resizable functionality is set.
  */
  private void setColumnProperties() {
    historyDateColumn.setPrefWidth(83);
    historyNameColumn.setPrefWidth(165);

    historyDateColumn.setResizable(false);
    historyNameColumn.setResizable(false);
    
    historyDateColumn.setReorderable(false);
    historyNameColumn.setReorderable(false);
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
      historyController.setService(service);
      historyController.setHistoryId(getSelectedHistoryId());
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root, 600, 500);
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(scene);
    } catch (Exception e) {
      e.printStackTrace();
      openButton.setDisable(true);
      deleteButton.setDisable(true);
      exceptionFeedback.setText("No history entry is selected!");
    }
  }

  @FXML
  private void historySelectedListener() {
    try {
      HistoryData selected =  historyOverview.getSelectionModel().getSelectedItem();
      String name = selected.getName();
      String date = selected.getDate();
      String data = name + ";" + date;
      for (Map.Entry<String, String> stringStringEntry : historyMap.entrySet()) {
        Map.Entry<String, String> entry = stringStringEntry;
        if (entry.getValue().equals(data)) {
          selectedHistoryId = entry.getKey();
          break;
        }
      }
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
    service.removeHistory(selectedHistoryId);
    historyMap = service.getHistoryMap();
    loadTable();
    exceptionFeedback.setText("History entry deleted!");
    openButton.setDisable(true);
    deleteButton.setDisable(true);
  }

  TableView<HistoryData> getHistoryOverview() {
    return historyOverview;
  }

  private String getSelectedHistoryId() {
    return selectedHistoryId;
  }

  /**
   * Class for handling history map values inside an own object.
   * Used to insert values into table view with the HistoryData fields.
   */
  public static class HistoryData {
    private final String name;
    private final String date;

    public HistoryData(String name, String date){
      this.name = name;
      this.date = date;
    }

    public String getName() {
      return name;
    }

    public String getDate() {
      return date;
    }
  }
}