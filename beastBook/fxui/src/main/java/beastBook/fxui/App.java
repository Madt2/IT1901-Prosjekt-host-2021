package beastBook.fxui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App.
 */
public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    LoginController loginController = new LoginController();

    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));

    fxmlLoader.setController(loginController);
        
    Parent parent = fxmlLoader.load();
    stage.setScene(new Scene(parent, 600, 500));
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}