package beastBook.fxui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import java.io.File;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTest extends ApplicationTest {
  LoginController lc;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
    lc = new LoginController();
    loader.setController(lc);
    final Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setup() {
    lc = new LoginController();
  }

  @Test
  void testLoginWithNoInput() { //Test without existing users
    clickOn("#login_button");
    FxAssert.verifyThat("#login_error", TextMatchers.hasText("No username given"));
  }

    @Test 
    void testLoginWithUsernameInput() {  //Test without existing users
      clickOn("#username_input").write("test");
      clickOn("#login_button");
      FxAssert.verifyThat("#login_error", TextMatchers.hasText("No Password given!"));
  }

  @Test 
  void testLoginWithPasswordInput() {  //Test without existing users
    clickOn("#password_input").write("test");
    clickOn("#login_button");
    FxAssert.verifyThat("#login_error", TextMatchers.hasText("No username given"));
  }

  @Test 
  void testLoginWithWrongInput() {  //Test without existing users
    clickOn("#username_input").write("test");
    clickOn("#password_input").write("test");
    clickOn("#login_button");
    FxAssert.verifyThat("#login_error", TextMatchers.hasText("No user found"));
  }

  //Test register user
  @Test 
  @Order(1)
  void testRegisterUser() {  //Test without existing users
    clickOn("#username_input").write("correct");
    clickOn("#password_input").write("correct");
    clickOn("#register_button");
  }

    
  @Test
  void testLoginWithCorrectUsername() {  //Test correct username and wrong password input
    clickOn("#username_input").write("correct");
    clickOn("#password_input").write("wrong");
    clickOn("#login_button");
    FxAssert.verifyThat("#login_error", TextMatchers.hasText("Wrong Password"));
  }

  @Test
  void testLoginWithWrongUsernameCorrectPassword() {
    clickOn("#username_input").write("wrong");
    clickOn("#password_input").write("correct");
    clickOn("#login_button");
    FxAssert.verifyThat("#login_error", TextMatchers.hasText("No user found"));
  }

  @Test
  void testLoginExistingUser() {
    clickOn("#username_input").write("correct");
    clickOn("#password_input").write("correct");
    clickOn("#login_button");
    FxAssert.verifyThat("#create_button", LabeledMatchers.hasText("Create Workout"));
  }

  @AfterAll
  static void cleanUp() {
    File file = new File(System.getProperty("user.home") + "/correct");
    file.delete();
  }
}
