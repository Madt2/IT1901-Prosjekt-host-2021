package beastbook.fxui;

import beastbook.core.Exceptions;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

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
    } catch (IOException | Exceptions.BadPackageException
        | Exceptions.ServerException | URISyntaxException e) {
      exceptionFeedback.setText(e.getMessage());
    }
  }
}

