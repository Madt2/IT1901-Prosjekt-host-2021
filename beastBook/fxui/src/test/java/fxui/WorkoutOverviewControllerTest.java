package fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import core.Exercise;
import core.Workout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class WorkoutOverviewControllerTest extends ApplicationTest{
    
    private WorkoutOverviewController woc;

    @FXML
    private TableView<Workout> workout_overview;
   
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader loader = new FXMLLoader(this.getClass().getResource("WorkoutOverview.fxml"));
        final Parent root = loader.load();
        woc = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void testTableView() {
        Exercise e1 = new Exercise("Deadlifts", 8, 100, 3, 180);
        Workout w1 = new Workout("Back");
        Workout w2 = new Workout("Push");
        w1.addExercise(e1);
        woc.getWorkout().addExercise(e1);
        woc.getAllWorkouts().add(w1);
        woc.getAllWorkouts().add(w2);
        woc.setTable();

        Assertions.assertEquals("Back", woc.getTable(0).getName());
        woc.getTable(0).setName("Biceps");
        Assertions.assertNotEquals("Back", woc.getTable(0).getName());
        Assertions.assertEquals("Biceps", woc.getTable(0).getName());

        Assertions.assertEquals("Push", woc.getTable(1).getName());


        Workout w3 = new Workout("Core");
        woc.getAllWorkouts().add(w3);
        woc.setTable();

        Assertions.assertEquals("Core", woc.getTable(2).getName());
    }
}
