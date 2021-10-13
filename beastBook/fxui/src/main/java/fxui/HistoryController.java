package fxui;

import core.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;

public class HistoryController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuItem logout_button;

    @FXML
    private Button back_button;

    @FXML
    private ListView<?> history_overview;

    private User user;

    @FXML
    void loadHome(ActionEvent event) throws IOException{
        HomeScreenController homeScreenController = new HomeScreenController();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("HomeScreen.fxml"));
        fxmlLoader.setController(homeScreenController);
        homeScreenController.setUser(user);

        AnchorPane pane =  fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadLogin(ActionEvent event) throws IOException{
        LoginController loginController = new LoginController();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("Login.fxml"));
        fxmlLoader.setController(loginController);

        AnchorPane pane =  fxmlLoader.load();
        rootPane.getChildren().setAll(pane);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
