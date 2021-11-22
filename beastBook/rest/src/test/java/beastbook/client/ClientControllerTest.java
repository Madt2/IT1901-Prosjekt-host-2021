package beastbook.client;

import beastbook.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import static beastbook.core.Properties.*;
import static beastbook.core.Properties.mockIdGenerator;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientControllerTest {

  private static ClientController clientController; 
  private final Exercise mockExercise = new Exercise("mockExercise", 1, 1, 1, 1, 1);
  private final Workout mockWorkout = new Workout("mockWorkout");
  private final History mockHistory = new History("mockHistory", List.of(mockExercise));
  private Map<String, String> exerciseMap;
  private Map<String, String> workoutMap;
  private Map<String, String> historyMap;
  private Exercise returnedExercise;
  private Workout returnedWorkout;
  private History returnedHistory;

  @BeforeAll
  static void setup() throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.PasswordIncorrectException, JsonProcessingException, Exceptions.UserAlreadyExistException, Exceptions.UserNotFoundException {
    RegisterController registerController = new RegisterController();
    try {
      clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
      clientController.deleteUser();
      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
    } catch (Exceptions.UserNotFoundException e) {
      registerController.registerUser(mockUser.getUsername(), mockUser.getPassword());
    }
    clientController = new ClientController(mockUser.getUsername(), mockUser.getPassword());
  }

  @Test
  @Order(1)
  void testLogin() {
    assertThrows(Exceptions.UserNotFoundException.class, () -> new ClientController("doesNotExist", "test"));
    assertThrows(Exceptions.PasswordIncorrectException.class, () -> new ClientController("test", "incorrectPassword"));
  }

  private void assertEqualWithoutIdExercise(Exercise exercise1, Exercise exercise2) {
    assertTrue(exercise1.getWeight() == exercise2.getWeight());
    assertTrue(exercise1.getRepGoal() == exercise2.getRepGoal());
    assertTrue(exercise1.getSets() == exercise2.getSets());
    assertTrue(exercise1.getRepsPerSet() == exercise2.getRepsPerSet());
    assertTrue(exercise1.getRestTime() == exercise2.getRestTime());
  }

  private void assertEqualWithoutIdWorkout(Workout workout1, Workout workout2) {
    assertTrue(workout1.getName().equals(workout2.getName()));
    assertTrue(workout1.getExerciseIDs().equals(workout2.getExerciseIDs()));
  }

  private void assertEqualWithoutIdHistory(History history1, History history2) {
    assertTrue(history1.getName().equals(history2.getName()));
    assertTrue(history1.getDate().equals(history2.getDate()));
    assertTrue(history1.getSavedExercises().size() == history2.getSavedExercises().size());
    for (int i = 0; i < history1.getSavedExercises().size(); i++) {
      Exercise exercise1 = history1.getSavedExercises().get(i);
      Exercise exercise2 = history1.getSavedExercises().get(i);
      assertEqualWithoutIdExercise(exercise1, exercise2);
    }
  }

  @Test
  @Order(2)
  void testGetters() throws Exceptions.ServerException {
    exerciseMap = clientController.getExerciseMap();
    workoutMap = clientController.getWorkoutMap();
    historyMap = clientController.getHistoryMap();

    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getExercise(notValidExerciseId));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getWorkout(notValidWorkoutId));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.getHistory(notValidHistoryId));

    final String exerciseIdNotInSystem = mockIdGenerator(Exercise.class, clientController.getExerciseMap());
    final String workoutIdNotInSystem = mockIdGenerator(Workout.class, clientController.getWorkoutMap());
    final String historyIdNotInSystem = mockIdGenerator(History.class, clientController.getHistoryMap());
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.getExercise(exerciseIdNotInSystem));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.getWorkout(workoutIdNotInSystem));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> clientController.getHistory(historyIdNotInSystem));
  }

  @Test
  @Order(3)
  void testAddObjects() throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException, Exceptions.HistoryAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryNotFoundException, Exceptions.IllegalIdException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
    //add objects:
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.addExercise(mockExercise, notValidWorkoutId));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.addExercise(mockExercise, mockIdGenerator(Workout.class, clientController.getWorkoutMap())));
    clientController.addWorkout(mockWorkout, List.of(mockExercise));
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);
    clientController.addHistory(mockHistory);

    //add object with same name:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addWorkout(mockWorkout, List.of(mockExercise)));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(mockExercise, returnedWorkoutId));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addHistory(mockHistory));

    //get id for added objects:
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    returnedExercise = clientController.getExercise(exerciseId);
    returnedWorkout = clientController.getWorkout(workoutId);
    returnedHistory = clientController.getHistory(historyId);

    assertEqualWithoutIdExercise(mockExercise, returnedExercise);
    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
    assertEqualWithoutIdHistory(mockHistory, returnedHistory);

    //add with id:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(returnedExercise, returnedWorkoutId));
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(returnedWorkout, List.of(mockExercise)));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> clientController.addHistory(returnedHistory));
  }

  @Test
  @Order(4)
  void testUpdateObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseNotFoundException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(mockExercise));
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(mockWorkout));

    Exercise falseExercise = mockExercise;
    falseExercise.setId(mockIdGenerator(Exercise.class, clientController.getExerciseMap()));
    Workout falseWorkout = mockWorkout;
    falseWorkout.setId(mockIdGenerator(Workout.class, clientController.getWorkoutMap()));
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> clientController.updateExercise(mockExercise));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.updateWorkout(mockWorkout));

    Workout updatedWorkout = returnedWorkout;
    updatedWorkout.setName("newName");
    clientController.updateWorkout(updatedWorkout);
    Workout loadedWorkout = clientController.getWorkout(updatedWorkout.getId());
    assertEqualWithoutIdWorkout(loadedWorkout, updatedWorkout);
    assertTrue(loadedWorkout.getId().equals(updatedWorkout.getId()));

    Exercise updatedExercise = returnedExercise;
    updatedExercise.setName("newName");
    updatedExercise.setRepGoal(2);
    updatedExercise.setSets(2);
    updatedExercise.setRepsPerSet(2);
    updatedExercise.setWeight(2);
    updatedExercise.setRestTime(2);
    clientController.updateExercise(updatedExercise);
    Exercise loadedExercise = clientController.getExercise(updatedExercise.getId());
    assertEqualWithoutIdExercise(loadedExercise, updatedExercise);
    assertTrue(loadedExercise.getId().equals(returnedExercise.getId()));
    assertTrue(loadedExercise.getWorkoutID().equals(returnedExercise.getWorkoutID()));
  }

  @Test
  @Order(5)
  void testDeleteObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseAlreadyExistsException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
    clientController.removeHistory(returnedHistory.getId());
    assertTrue(clientController.getHistoryMap().size() == 0);
    clientController.removeHistory(returnedHistory.getId());

    clientController.removeExercise(returnedExercise.getId());
    assertTrue(clientController.getExerciseMap().size() == 0);
    clientController.removeExercise(returnedExercise.getId());

    clientController.addExercise(mockExercise, returnedWorkout.getId());
    clientController.removeWorkout(returnedWorkout.getId());
    assertTrue(clientController.getWorkoutMap().size() == 0);
    assertTrue(clientController.getExerciseMap().size() == 0);
    clientController.removeWorkout(returnedWorkout.getId());
  }

  @Test
  @Order(6)
  void testSetIp() {
    clientController.setIpAddress("127.0.0.1");
    clientController.getExerciseMap();
  }

  @AfterAll
  static void cleanUp() throws Exceptions.ServerException, Exceptions.BadPackageException, URISyntaxException, JsonProcessingException {
    clientController.deleteUser();
  }
  
}
