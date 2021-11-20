package beastbook.fxui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Controller for the Home screen.
 */
public class HomeScreenController extends AbstractController {

  @FXML
  private Text exceptionFeedback;

  @FXML
  void deleteUser(ActionEvent event){
    try{
      service.deleteUser();
      super.loadLogin(event);
    } catch (IOException e) {
      //TODO ExceptionFeedback?
      exceptionFeedback.setText("Could not delete user!");
    }
  }
}

