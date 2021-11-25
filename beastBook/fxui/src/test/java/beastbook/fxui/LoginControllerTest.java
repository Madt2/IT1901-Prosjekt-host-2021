package beastbook.fxui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import beastbook.client.ClientController;
import beastbook.client.RegisterController;
import beastbook.core.Exercise;
import beastbook.core.Workout;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.ExerciseAlreadyExistsException;
import beastbook.core.Exceptions.IllegalIdException;
import beastbook.core.Exceptions.PasswordIncorrectException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.UserAlreadyExistException;
import beastbook.core.Exceptions.UserNotFoundException;
import beastbook.core.Exceptions.WorkoutAlreadyExistsException;
import beastbook.core.Exceptions.WorkoutNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTest extends ApplicationTest {

  private ClientController service;
  private RegisterController reg;
  private LoginController lc;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/beastbook.fxui/Login.fxml"));
    lc = new LoginController();
    loader.setController(lc);
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  void testLoginWithNoInput() { //Test without existing users
    clickOn("#loginButton");
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("No username given"));
  }

  @Test
  void testLoginWithUsernameInput() {  //Test without existing users
    clickOn("#usernameInput").write("test");
    clickOn("#loginButton");
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("No Password given!"));
  }

  @Test 
  void testLoginWithPasswordInput() {  //Test without existing users
    clickOn("#passwordInput").write("test");
    clickOn("#loginButton");
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("No username given"));
  }

  @Test 
  void testLoginWithWrongInput() {  //Test without existing users
    clickOn("#usernameInput").write("test");
    clickOn("#passwordInput").write("test");
    clickOn("#loginButton");
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("Could not find user: test"));
  }

  @Test 
  @Order(1)
  void testRegisterUser() throws JsonProcessingException, UserNotFoundException, BadPackageException, ServerException, URISyntaxException, PasswordIncorrectException {  //Test without existing users
    clickOn("#usernameInput").write("correct");
    clickOn("#passwordInput").write("correct");
    clickOn("#registerButton");
    clickOn("#deleteUserButton");
  }

  @Test
  @Order(2)
  void testLoginWithCorrectUsername() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, UserNotFoundException, PasswordIncorrectException, UserAlreadyExistException {  //Test correct username and wrong password input
    RegisterController reg = new RegisterController();
    reg.registerUser("correct", "correct");
    service = new ClientController("correct", "correct");
    clickOn("#usernameInput").write("correct");
    clickOn("#passwordInput").write("wrong");
    clickOn("#loginButton");
    
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("Password incorrect!"));
    service.deleteUser();
  }

  @Test
  void testLoginWithWrongUsernameCorrectPassword() {
    clickOn("#usernameInput").write("wrong");
    clickOn("#passwordInput").write("correct");
    clickOn("#loginButton");
    FxAssert.verifyThat("#loginError", TextMatchers.hasText("Could not find user: wrong"));
  }

  @Test
  void testLoginExistingUser() throws UserNotFoundException, BadPackageException, ServerException, URISyntaxException, PasswordIncorrectException, UserAlreadyExistException, IllegalArgumentException, WorkoutAlreadyExistsException, ExerciseAlreadyExistsException, WorkoutNotFoundException, IllegalIdException, IOException {
    RegisterController reg = new RegisterController();
    reg.registerUser("correct", "correct");
    service = new ClientController("correct", "correct");
    lc.setService(service);

    Workout Workout = new Workout("ExistingW");
    lc.service.addWorkout(Workout, List.of(new Exercise("Benchpress", 20, 40, 59, 23, 23), new Exercise("Squats", 30, 40, 20, 90, 20)));

    clickOn("#usernameInput").write("correct");
    clickOn("#passwordInput").write("correct");
    clickOn("#loginButton");
    clickOn("#workoutsButton");
  
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/beastbook.fxui/WorkoutOverview.fxml"));
    WorkoutOverviewController woc = new WorkoutOverviewController();
    loader.setController(woc);
    woc.setService(service);
    Parent root = loader.load();
    
    assertEquals("ExistingW", woc.getWorkoutOverview().getItems().get(0));
    assertNotEquals("WORKOUT", woc.getWorkoutOverview().getItems().get(0));

    service.deleteUser();
  }
}