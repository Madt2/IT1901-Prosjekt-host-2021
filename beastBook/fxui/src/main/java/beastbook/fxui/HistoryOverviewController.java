package beastbook.fxui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    historyMap = service.getHistoryMap();
    loadTable();
  }

  private void loadTable() {
    historyOverview.getColumns().clear();
    historyNameColumn = new TableColumn<>("Workout name:");
    historyNameColumn.setCellValueFactory(name -> new SimpleStringProperty(name.getValue()));
    historyDateColumn = new TableColumn<>("Date:");
    historyDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    historyOverview.getColumns().add(historyDateColumn);
    historyOverview.getColumns().add(historyNameColumn);
////    List<List<String>> body = new ArrayList<>();
////    List<String> name = new ArrayList<>();
////    List<String> date = new ArrayList<>();
///
    List<List<String>> historyData = new ArrayList<>();
//
//    //String[][] =
//    // H-d4D = {"minøkt2", "12.12.2021"};
//    // TODO Bannlyse muligheten til å skrive '§' siden denne brukes til å splitte!
//    //"historyName§23.01.2000"
//  /*
    for (String s : historyMap.values()) {
      //String[] splittedItems = s.split("§");
      List<String> split = List.of(s.split("§"));
      //name.add(splittedItems[0]);
      //date.add(splittedItems[1]);
      historyData.add(split);
      }
//    }
//    body.add(name);
//    body.add(date);
//    newArray[0][0] = "element 1";
//    newArray[0][1] = "verdi 1";
//    newArray[1][0] = "element 2";
//    newArray[1][1] = "verdi 2";*/
//
//    //System.out.println(historyData);
//    //System.out.println(historyData.iterator().next());
//    //historyOverview.getItems().setAll(body.iterator().next());
//    historyOverview.getItems().addAll(historyData.iterator().
//    //historyOverview.getItems().setAll(historyData.iterator().next().get(0), historyData.iterator().next().get(1));

      setColumnsSize();

  }

  private void setColumnsSize() {
    historyNameColumn.setPrefWidth(129);
    historyNameColumn.setResizable(false);
   /* historyDateColumn.setPrefWidth(129);
    historyDateColumn.setResizable(false);*/
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
      String name =  historyOverview.getSelectionModel().getSelectedItem();
      Iterator it = historyMap.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        if (entry.getValue().equals(name)) {
          selectedHistoryId = entry.getKey().toString();
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

  TableView<String> getHistoryOverview() {
    return historyOverview;
  }

  private String getSelectedHistoryId() {
    return selectedHistoryId;
  }

}