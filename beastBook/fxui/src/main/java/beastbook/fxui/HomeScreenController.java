package beastbook.fxui;

import beastbook.core.User;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller for the Home screen.
 */
public class HomeScreenController extends AbstractController {
/*  @FXML
  private Button logoutButton;

  @FXML
  private Button createButton;

  @FXML
  private Button workoutsButton;


  @FXML
  void initialize() {
      System.out.println(getUsername());
  }
*/
  @FXML
  void deleteUser(ActionEvent event){
    try{
      service.deleteUser();
      super.loadLogin(event);
    } catch (IOException e) {
      //TODO ExceptionFeedback?
      System.out.println("Could not delete user");
    }
  }
}

