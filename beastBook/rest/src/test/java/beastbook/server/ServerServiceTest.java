package beastbook.server;

import beastbook.core.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

import static beastbook.core.Properties.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerServiceTest {
  private static ServerService serverService;
  private final Exercise mockExercise = new Exercise("mockExercise", 1, 1, 1, 1, 1);
  private final Workout mockWorkout = new Workout("mockWorkout");
  private final History mockHistory = new History("mockHistory", List.of(mockExercise));
  private User user;

  @BeforeEach
  void setup() throws Exceptions.ServerException, Exceptions.UserAlreadyExistException {
    serverService = new ServerService(mockUser);
    user = new User("test", "test");
    try {
      serverService.createUser();
    } catch (Exceptions.UserAlreadyExistException e) {
      serverService.deleteUser();
      serverService.createUser();
    }
  }

  @Test
  void testCreateUser() {
    user = new User("test", "test");
    assertThrows(Exceptions.UserAlreadyExistException.class, () -> serverService.createUser());
  }

  @Test
  void testLogin() throws Exceptions.ServerException,
      Exceptions.UserNotFoundException, Exceptions.PasswordIncorrectException {
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
    for (int i = 0; i < workout1.getExerciseIds().size(); i++) {
      assertEquals(workout1.getExerciseIds().get(i) , workout2.getExerciseIds().get(i));
    }
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
  void testGetters() throws Exceptions.ServerException {
    assertThrows(IllegalArgumentException.class, () -> serverService.getMapping(NoClass.class));
    serverService.getMapping(Exercise.class);
    serverService.getMapping(Workout.class);
    serverService.getMapping(History.class);

    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getExercise(notValidExerciseId));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getWorkout(notValidWorkoutId));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.getHistory(notValidHistoryId));

    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> serverService.getExercise("ab"));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.getWorkout("AB"));
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> serverService.getHistory("ABC"));
  }

  @Test
  void testAddObjects() throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException,
      Exceptions.HistoryAlreadyExistsException, Exceptions.ExerciseNotFoundException,
      Exceptions.HistoryNotFoundException, Exceptions.IllegalIdException, Exceptions.IdNotFoundException {
    //add objects:
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.addExercise(mockExercise, notValidWorkoutId));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.addExercise(mockExercise, ("AB")));
    final String returnedWorkoutId = serverService.addWorkout(mockWorkout);
    serverService.addExercise(mockExercise, returnedWorkoutId);
    serverService.addHistory(mockHistory);

    //add object with same name:
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> serverService.addWorkout(mockWorkout));
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addExercise(mockExercise, returnedWorkoutId));
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> serverService.addHistory(mockHistory));

    //get id for added objects:
    Map<String, String> exerciseMap = serverService.getMapping(Exercise.class);
    Map<String, String> workoutMap = serverService.getMapping(Workout.class);
    Map<String, String> historyMap = serverService.getMapping(History.class);
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = serverService.getExercise(exerciseId);
    Workout returnedWorkout = serverService.getWorkout(workoutId);
    History returnedHistory = serverService.getHistory(historyId);

    assertEqualWithoutIdExercise(mockExercise, returnedExercise);
    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
    assertEqualWithoutIdHistory(mockHistory, returnedHistory);

    //add with id:
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> serverService.addExercise(returnedExercise, returnedWorkoutId));
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> serverService.addWorkout(returnedWorkout));
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> serverService.addHistory(returnedHistory));
  }

  @Test
  void testUpdateObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException,
      Exceptions.WorkoutNotFoundException, Exceptions.ExerciseNotFoundException,
      Exceptions.WorkoutAlreadyExistsException, Exceptions.ExerciseAlreadyExistsException {

    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateExercise(mockExercise));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateWorkout(mockWorkout));

    final String returnedWorkoutId = serverService.addWorkout(mockWorkout);
    serverService.addExercise(mockExercise, returnedWorkoutId);

    Exercise falseExercise = new Exercise("Bench press", 20, 30, 40, 50, 60);
    Workout falseWorkout = new Workout("test");
    falseExercise.setId("dd");
    falseWorkout.setId("GG");
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateExercise(new Exercise("Bench press", 20, 30, 40, 50, 60)));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.updateWorkout(new Workout("test")));
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> serverService.updateExercise(falseExercise));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> serverService.updateWorkout(falseWorkout));

    Map<String, String> exerciseMap = serverService.getMapping(Exercise.class);
    Map<String, String> workoutMap = serverService.getMapping(Workout.class);

    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = serverService.getExercise(exerciseId);
    Workout returnedWorkout = serverService.getWorkout(workoutId);

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
    assertTrue(loadedExercise.getWorkoutId().equals(returnedExercise.getWorkoutId()));
  }

  @Test
  void testDeleteObjects() throws Exceptions.ServerException, Exceptions.IllegalIdException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryNotFoundException, Exceptions.HistoryAlreadyExistsException {

    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.deleteExercise("not valid"));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.deleteWorkout("not valid"));
    assertThrows(Exceptions.IllegalIdException.class, () -> serverService.deleteHistory("not valid"));

    final String returnedWorkoutId = serverService.addWorkout(mockWorkout);
    serverService.addExercise(mockExercise, returnedWorkoutId);
    serverService.addHistory(mockHistory);

    Map<String, String> exerciseMap = serverService.getMapping(Exercise.class);
    Map<String, String> workoutMap = serverService.getMapping(Workout.class);
    Map<String, String> historyMap = serverService.getMapping(History.class);

    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    String historyId = historyMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = serverService.getExercise(exerciseId);
    Workout returnedWorkout = serverService.getWorkout(workoutId);
    History returnedHistory = serverService.getHistory(historyId);

    serverService.deleteHistory(returnedHistory.getId());
    assertTrue(serverService.getMapping(History.class).size() == 0);
    serverService.deleteHistory(returnedHistory.getId());

    serverService.deleteExercise(returnedExercise.getId());
    assertTrue(serverService.getMapping(Exercise.class).size() == 0);
    serverService.deleteExercise(returnedExercise.getId());

    serverService.addExercise(new Exercise("Bench press", 20, 30, 40, 50, 60), returnedWorkout.getId());
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