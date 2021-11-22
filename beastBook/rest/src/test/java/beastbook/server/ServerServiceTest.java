package beastbook.server;

import beastbook.core.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import static beastbook.core.Properties.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerServiceTest {

  private static final String username = "mock";
  private static final String password = "mock";
  private static final User mockUser = new User(username, password);
  private static ServerService serverService = new ServerService(mockUser);
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
  static void setup() throws Exceptions.ServerException {
    File file = new File(System.getProperty("user.home") + "/" + mockUser.getUsername());
    if (file.exists()) {
      serverService.deleteUser();
    }
  }

  @Test
  @Order(1)
  void testCreateUser() throws Exceptions.UserAlreadyExistException, Exceptions.ServerException {
    serverService.createUser();
    assertThrows(Exceptions.UserAlreadyExistException.class, () -> serverService.createUser());
  }

  @Test
  @Order(2)
  void testLogin() throws Exceptions.UserAlreadyExistException, Exceptions.ServerException, Exceptions.UserNotFoundException, Exceptions.PasswordIncorrectException {
    serverService.createUser();
    serverService.login();
    User user = new User("doesNotExist", "test");
    serverService = new ServerService(user);
    assertThrows(Exceptions.UserNotFoundException.class, () -> serverService.login());
    user = new User("test", "wrongPassword");
    serverService = new ServerService(user);
    assertThrows(Exceptions.PasswordIncorrectException.class, () -> serverService.login());
    serverService = new ServerService(mockUser);
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
  @Order(3)
  void testGetters() throws Exceptions.ServerException {
    assertThrows(IllegalArgumentException.class, () -> serverService.getMapping(NoClass.class));
    exerciseMap = serverService.getMapping(Exercise.class);
    workoutMap = serverService.getMapping(Workout.class);
    historyMap = serverService.getMapping(History.class);

    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getExercise(notValidExerciseId));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getWorkout(notValidWorkoutId));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getHistory(notValidHistoryId));

    final String exerciseIdNotInSystem = mockIdGenerator(Exercise.class, serverService.getMapping(Exercise.class));
    final String workoutIdNotInSystem = mockIdGenerator(Workout.class, serverService.getMapping(Workout.class));
    final String historyIdNotInSystem = mockIdGenerator(History.class, serverService.getMapping(History.class));
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> serverService.getExercise(exerciseIdNotInSystem));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.getWorkout(workoutIdNotInSystem));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> serverService.getHistory(historyIdNotInSystem));
  }

  @Test
  @Order(4)
  void testAddObjects() throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException, Exceptions.HistoryAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryNotFoundException, Exceptions.IllegalIdException, Exceptions.IdNotFoundException {
    //add objects:
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.addExercise(mockExercise, notValidWorkoutId));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.addExercise(mockExercise, mockIdGenerator(Workout.class, serverService.getMapping(Workout.class))));
    final String returnedWorkoutId = serverService.addWorkout(mockWorkout);
    serverService.addExercise(mockExercise, returnedWorkoutId);
    serverService.addHistory(mockHistory);

    //add object with same name:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addWorkout(mockWorkout));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addExercise(mockExercise, returnedWorkoutId));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addHistory(mockHistory));

    //get id for added objects:
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    returnedExercise = serverService.getExercise(exerciseId);
    returnedWorkout = serverService.getWorkout(workoutId);
    returnedHistory = serverService.getHistory(historyId);

    assertEqualWithoutIdExercise(mockExercise, returnedExercise);
    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
    assertEqualWithoutIdHistory(mockHistory, returnedHistory);

    //add with id:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addExercise(returnedExercise, returnedWorkoutId));
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> serverService.addWorkout(returnedWorkout));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> serverService.addHistory(returnedHistory));
  }

  @Test
  @Order(5)
  void testUpdateObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseNotFoundException {
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateExercise(mockExercise));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateWorkout(mockWorkout));


    Exercise falseExercise = mockExercise;
    falseExercise.setId(mockIdGenerator(Exercise.class, serverService.getMapping(Exercise.class)));
    Workout falseWorkout = mockWorkout;
    falseWorkout.setId(mockIdGenerator(Workout.class, serverService.getMapping(Workout.class)));
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> serverService.updateExercise(mockExercise));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.updateWorkout(mockWorkout));

    Workout updatedWorkout = returnedWorkout;
    updatedWorkout.setName("newName");
    serverService.updateWorkout(updatedWorkout);
    Workout loadedWorkout = serverService.getWorkout(updatedWorkout.getId());
    assertEqualWithoutIdWorkout(loadedWorkout, updatedWorkout);
    assertTrue(loadedWorkout.getId().equals(updatedWorkout.getId()));

    Exercise updatedExercise = returnedExercise;
    updatedExercise.setName("newName");
    updatedExercise.setRepGoal(2);
    updatedExercise.setSets(2);
    updatedExercise.setRepsPerSet(2);
    updatedExercise.setWeight(2);
    updatedExercise.setRestTime(2);
    serverService.updateExercise(updatedExercise);
    Exercise loadedExercise = serverService.getExercise(updatedExercise.getId());
    assertEqualWithoutIdExercise(loadedExercise, updatedExercise);
    assertTrue(loadedExercise.getId().equals(returnedExercise.getId()));
    assertTrue(loadedExercise.getWorkoutID().equals(returnedExercise.getWorkoutID()));
  }

  @Test
  @Order(6)
  void testDeleteObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseAlreadyExistsException {
    serverService.deleteHistory(returnedHistory.getId());
    assertTrue(serverService.getMapping(History.class).size() == 0);
    serverService.deleteHistory(returnedHistory.getId());

    serverService.deleteExercise(returnedExercise.getId());
    assertTrue(serverService.getMapping(Exercise.class).size() == 0);
    serverService.deleteExercise(returnedExercise.getId());

    serverService.addExercise(mockExercise, returnedWorkout.getId());
    serverService.deleteWorkout(returnedWorkout.getId());
    assertTrue(serverService.getMapping(Exercise.class).size() == 0);
    assertTrue(serverService.getMapping(Workout.class).size() == 0);
    serverService.deleteWorkout(returnedWorkout.getId());
  }

  @AfterAll
  static void cleanUp() throws Exceptions.ServerException {
    serverService.deleteUser();
  }

}
