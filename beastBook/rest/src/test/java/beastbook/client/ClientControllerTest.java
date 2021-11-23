package beastbook.client;

import beastbook.core.*;
import beastbook.server.ServerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import static beastbook.core.Properties.*;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

/*  @LocalServerPort
  private int port;*/

  private static ClientController clientController;
  private final Exercise mockExercise = new Exercise("mockExercise", 1, 1, 1, 1, 1);
  private final Workout mockWorkout = new Workout("mockWorkout");
  private final History mockHistory = new History("mockHistory", List.of(mockExercise));

  @BeforeEach
  void setup() throws Exceptions.ServerException, Exceptions.UserNotFoundException,
      Exceptions.BadPackageException, URISyntaxException,
      Exceptions.PasswordIncorrectException, JsonProcessingException,
      Exceptions.UserAlreadyExistException {
    try {
      RegisterController registerController = new RegisterController();
      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
    } catch (Exceptions.UserAlreadyExistException e) {
      clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
      clientController.deleteUser();
      RegisterController registerController = new RegisterController();
      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
    }
    clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
  }

  private void assertEqualWithoutIdExercise(Exercise exercise1, Exercise exercise2) {
    assertEquals(exercise1.getWeight(), exercise2.getWeight());
    assertEquals(exercise1.getRepGoal(), exercise2.getRepGoal());
    assertEquals(exercise1.getSets(), exercise2.getSets());
    assertEquals(exercise1.getRepsPerSet(), exercise2.getRepsPerSet());
    assertEquals(exercise1.getRestTime(), exercise2.getRestTime());
  }

  private void assertEqualWithoutIdWorkout(Workout workout1, Workout workout2) {
    assertEquals(workout1.getName(), workout2.getName());
    for (int i = 0; i < workout1.getExerciseIds().size(); i++) {
      assertEquals(workout1.getExerciseIds().get(i) , workout2.getExerciseIds().get(i));
    }
  }

  private void assertEqualWithoutIdHistory(History history1, History history2) {
    assertEquals(history1.getName(), history2.getName());
    assertEquals(history1.getDate(), history2.getDate());
    assertEquals(history1.getSavedExercises().size(), history2.getSavedExercises().size());
    for (int i = 0; i < history1.getSavedExercises().size(); i++) {
      Exercise exercise1 = history1.getSavedExercises().get(i);
      Exercise exercise2 = history1.getSavedExercises().get(i);
      assertEqualWithoutIdExercise(exercise1, exercise2);
    }
  }

  @Test
  void testGetters() {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getExercise("fakeId"));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getWorkout("fakeId"));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getHistory("fakeId"));

    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.getExercise("ab"));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.getWorkout("AB"));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> clientController.getHistory("ABC"));
  }

  @Test
  void testAddObjects() throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException,
      Exceptions.HistoryAlreadyExistsException, Exceptions.ExerciseNotFoundException,
      Exceptions.HistoryNotFoundException, Exceptions.IllegalIdException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
    //add objects:
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.addExercise(mockExercise, "fakeId"));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.addExercise(mockExercise, ("AB")));
    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);
    clientController.addHistory(mockHistory);

    //add object with same name:
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(mockWorkout, List.of(mockExercise)));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(mockExercise, returnedWorkoutId));
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> clientController.addHistory(mockHistory));

    //get id for added objects:
    Map<String, String> exerciseMap = clientController.getExerciseMap();
    Map<String, String> workoutMap = clientController.getWorkoutMap();
    Map<String, String> historyMap = clientController.getHistoryMap();
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = clientController.getExercise(exerciseId);
    Workout returnedWorkout = clientController.getWorkout(workoutId);
    History returnedHistory = clientController.getHistory(historyId);

    assertEqualWithoutIdExercise(mockExercise, returnedExercise);
    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
    assertEqualWithoutIdHistory(mockHistory, returnedHistory);

    //add with id:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(returnedExercise, returnedWorkoutId));
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(returnedWorkout, List.of(mockExercise)));
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> clientController.addHistory(returnedHistory));
  }

  @Test
  void testUpdateObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException,
      Exceptions.WorkoutNotFoundException, Exceptions.ExerciseNotFoundException,
      Exceptions.ExerciseAlreadyExistsException,
      Exceptions.BadPackageException, URISyntaxException, JsonProcessingException, Exceptions.WorkoutAlreadyExistsException {

    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(mockExercise));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(mockWorkout));

    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);


    Exercise falseExercise = new Exercise("Bench press", 20, 30, 40, 50, 60);
    Workout falseWorkout = new Workout("test");
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(falseExercise));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(falseWorkout));
/*

    falseExercise = new Exercise("Bench press", 20, 30, 40, 50, 60);
    falseWorkout = new Workout("test");
    falseExercise.setId("dd");
    falseWorkout.setId("GG");
    Exercise falseExerciseWithValidId = falseExercise;
    Workout falseWorkoutWithValidId = falseWorkout;
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.updateExercise(falseExerciseWithValidId));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.updateWorkout(falseWorkoutWithValidId));
*/

    Map<String, String> exerciseMap = clientController.getExerciseMap();
    Map<String, String> workoutMap = clientController.getWorkoutMap();

    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);

    Workout updatedWorkout = clientController.getWorkout(workoutId);
    updatedWorkout.setName("newName");
    clientController.updateWorkout(updatedWorkout);
    Workout loadedWorkout = clientController.getWorkout(updatedWorkout.getId());
    assertEqualWithoutIdWorkout(loadedWorkout, updatedWorkout);
    assertEquals(loadedWorkout.getId(), updatedWorkout.getId());

    Exercise updatedExercise = clientController.getExercise(exerciseId);;
    updatedExercise.setName("newName");
    updatedExercise.setRepGoal(2);
    updatedExercise.setSets(2);
    updatedExercise.setRepsPerSet(2);
    updatedExercise.setWeight(2);
    updatedExercise.setRestTime(2);
    clientController.updateExercise(updatedExercise);
    Exercise loadedExercise = clientController.getExercise(updatedExercise.getId());
    assertEqualWithoutIdExercise(loadedExercise, updatedExercise);
    assertEquals(loadedExercise.getId(), updatedExercise.getId());
    assertEquals(loadedExercise.getWorkoutId(), updatedExercise.getWorkoutId());
  }

  @Test
  void testDeleteObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException,
      Exceptions.ExerciseNotFoundException, Exceptions.HistoryNotFoundException,
      Exceptions.HistoryAlreadyExistsException, Exceptions.BadPackageException,
      URISyntaxException, JsonProcessingException {

    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeExercise("notValid"));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeWorkout("notValid"));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeHistory("notValid"));

    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    System.out.println(returnedWorkoutId);
    clientController.addExercise(mockExercise, returnedWorkoutId);
    clientController.addHistory(mockHistory);

    Map<String, String> exerciseMap = clientController.getExerciseMap();
    Map<String, String> workoutMap = clientController.getWorkoutMap();
    Map<String, String> historyMap = clientController.getHistoryMap();

    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = clientController.getExercise(exerciseId);
    Workout returnedWorkout = clientController.getWorkout(workoutId);
    History returnedHistory = clientController.getHistory(historyId);

    clientController.removeHistory(returnedHistory.getId());
    assertTrue(clientController.getHistoryMap().size() == 0);
    clientController.removeHistory(returnedHistory.getId());

    clientController.removeExercise(returnedExercise.getId());
    assertTrue(clientController.getExerciseMap().size() == 0);
    clientController.removeExercise(returnedExercise.getId());

    clientController.addExercise(new Exercise("Bench press", 20, 30, 40, 50, 60), returnedWorkout.getId());
    clientController.removeWorkout(returnedWorkout.getId());
    assertTrue(clientController.getExerciseMap().size() == 0);
    assertTrue(clientController.getWorkoutMap().size() == 0);
    clientController.removeWorkout(returnedWorkout.getId());
  }

  @AfterAll
  static void cleanUp() throws Exceptions.ServerException, Exceptions.BadPackageException,
      URISyntaxException, JsonProcessingException {
    clientController.deleteUser();
  }


//  private static ClientController clientController;
//  private final Exercise mockExercise = new Exercise("mockExercise", 1, 1, 1, 1, 1);
//  private final Workout mockWorkout = new Workout("mockWorkout");
//  private final History mockHistory = new History("mockHistory", List.of(mockExercise));
//  private Map<String, String> exerciseMap;
//  private Map<String, String> workoutMap;
//  private Map<String, String> historyMap;
//  private Exercise returnedExercise;
//  private Workout returnedWorkout;
//  private History returnedHistory;
//
//  @BeforeAll
//  static void setup() throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.PasswordIncorrectException, JsonProcessingException, Exceptions.UserAlreadyExistException, Exceptions.UserNotFoundException {
//    RegisterController registerController = new RegisterController();
//    try {
//      clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
//      clientController.deleteUser();
//      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
//    } catch (Exceptions.UserNotFoundException | NullPointerException e) {
//      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
//    }
//    clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
//  }
//
//  @Test
//  @Order(1)
//  void testLogin() {
//    assertThrows(Exceptions.UserNotFoundException.class, () -> new ClientController("doesNotExist", "test"));
//    assertThrows(Exceptions.PasswordIncorrectException.class, () -> new ClientController("test", "incorrectPassword"));
//  }
//
//  private void assertEqualWithoutIdExercise(Exercise exercise1, Exercise exercise2) {
//    assertTrue(exercise1.getWeight() == exercise2.getWeight());
//    assertTrue(exercise1.getRepGoal() == exercise2.getRepGoal());
//    assertTrue(exercise1.getSets() == exercise2.getSets());
//    assertTrue(exercise1.getRepsPerSet() == exercise2.getRepsPerSet());
//    assertTrue(exercise1.getRestTime() == exercise2.getRestTime());
//  }
//
//  private void assertEqualWithoutIdWorkout(Workout workout1, Workout workout2) {
//    assertTrue(workout1.getName().equals(workout2.getName()));
//    assertTrue(workout1.getExerciseIds().equals(workout2.getExerciseIds()));
//  }
//
//  private void assertEqualWithoutIdHistory(History history1, History history2) {
//    assertTrue(history1.getName().equals(history2.getName()));
//    assertTrue(history1.getDate().equals(history2.getDate()));
//    assertTrue(history1.getSavedExercises().size() == history2.getSavedExercises().size());
//    for (int i = 0; i < history1.getSavedExercises().size(); i++) {
//      Exercise exercise1 = history1.getSavedExercises().get(i);
//      Exercise exercise2 = history1.getSavedExercises().get(i);
//      assertEqualWithoutIdExercise(exercise1, exercise2);
//    }
//  }
//
//  @Test
//  @Order(2)
//  void testGetters() throws Exceptions.ServerException {
//    exerciseMap = clientController.getExerciseMap();
//    workoutMap = clientController.getWorkoutMap();
//    historyMap = clientController.getHistoryMap();
//
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getExercise(notValidExerciseId));
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getWorkout(notValidWorkoutId));
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getHistory(notValidHistoryId));
//
//    final String exerciseIdNotInSystem = mockIdGenerator(Exercise.class, clientController.getExerciseMap());
//    final String workoutIdNotInSystem = mockIdGenerator(Workout.class, clientController.getWorkoutMap());
//    final String historyIdNotInSystem = mockIdGenerator(History.class, clientController.getHistoryMap());
//    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.getExercise(exerciseIdNotInSystem));
//    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.getWorkout(workoutIdNotInSystem));
//    assertThrows(Exceptions.HistoryNotFoundException.class, () -> clientController.getHistory(historyIdNotInSystem));
//  }
//
//  @Test
//  @Order(3)
//  void testAddObjects() throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException, Exceptions.HistoryAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryNotFoundException, Exceptions.IllegalIdException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
//    //add objects:
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.addExercise(mockExercise, notValidWorkoutId));
//    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.addExercise(mockExercise, mockIdGenerator(Workout.class, clientController.getWorkoutMap())));
//    clientController.addWorkout(mockWorkout, List.of(mockExercise));
//    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
//    clientController.addExercise(mockExercise, returnedWorkoutId);
//    clientController.addHistory(mockHistory);
//
//    //add object with same name:
//    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addWorkout(mockWorkout, List.of(mockExercise)));
//    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(mockExercise, returnedWorkoutId));
//    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addHistory(mockHistory));
//
//    //get id for added objects:
//    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
//    String workoutId = workoutMap.keySet().stream().toList().get(0);
//    String historyId = historyMap.keySet().stream().toList().get(0);
//    returnedExercise = clientController.getExercise(exerciseId);
//    returnedWorkout = clientController.getWorkout(workoutId);
//    returnedHistory = clientController.getHistory(historyId);
//
//    assertEqualWithoutIdExercise(mockExercise, returnedExercise);
//    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
//    assertEqualWithoutIdHistory(mockHistory, returnedHistory);
//
//    //add with id:
//    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(returnedExercise, returnedWorkoutId));
//    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(returnedWorkout, List.of(mockExercise)));
//    assertThrows(Exceptions.HistoryNotFoundException.class, () -> clientController.addHistory(returnedHistory));
//  }
//
//  @Test
//  @Order(4)
//  void testUpdateObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseNotFoundException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(mockExercise));
//    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(mockWorkout));
//
//    Exercise falseExercise = mockExercise;
//    falseExercise.setId(mockIdGenerator(Exercise.class, clientController.getExerciseMap()));
//    Workout falseWorkout = mockWorkout;
//    falseWorkout.setId(mockIdGenerator(Workout.class, clientController.getWorkoutMap()));
//    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.updateExercise(mockExercise));
//    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.updateWorkout(mockWorkout));
//
//    Workout updatedWorkout = returnedWorkout;
//    updatedWorkout.setName("newName");
//    clientController.updateWorkout(updatedWorkout);
//    Workout loadedWorkout = clientController.getWorkout(updatedWorkout.getId());
//    assertEqualWithoutIdWorkout(loadedWorkout, updatedWorkout);
//    assertTrue(loadedWorkout.getId().equals(updatedWorkout.getId()));
//
//    Exercise updatedExercise = returnedExercise;
//    updatedExercise.setName("newName");
//    updatedExercise.setRepGoal(2);
//    updatedExercise.setSets(2);
//    updatedExercise.setRepsPerSet(2);
//    updatedExercise.setWeight(2);
//    updatedExercise.setRestTime(2);
//    clientController.updateExercise(updatedExercise);
//    Exercise loadedExercise = clientController.getExercise(updatedExercise.getId());
//    assertEqualWithoutIdExercise(loadedExercise, updatedExercise);
//    assertTrue(loadedExercise.getId().equals(returnedExercise.getId()));
//    assertTrue(loadedExercise.getWorkoutId().equals(returnedExercise.getWorkoutId()));
//  }
//
//  @Test
//  @Order(5)
//  void testDeleteObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseAlreadyExistsException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
//    clientController.removeHistory(returnedHistory.getId());
//    assertTrue(clientController.getHistoryMap().size() == 0);
//    clientController.removeHistory(returnedHistory.getId());
//
//    clientController.removeExercise(returnedExercise.getId());
//    assertTrue(clientController.getExerciseMap().size() == 0);
//    clientController.removeExercise(returnedExercise.getId());
//
//    clientController.addExercise(mockExercise, returnedWorkout.getId());
//    clientController.removeWorkout(returnedWorkout.getId());
//    assertTrue(clientController.getWorkoutMap().size() == 0);
//    assertTrue(clientController.getExerciseMap().size() == 0);
//    clientController.removeWorkout(returnedWorkout.getId());
//  }
//
//  @Test
//  @Order(6)
//  void testSetIp() {
//    clientController.setIpAddress("127.0.0.1");
//    clientController.getExerciseMap();
//  }
//
//  @AfterAll
//  static void cleanUp() throws Exceptions.ServerException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
//    clientController.deleteUser();
//  }
  
}
