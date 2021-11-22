package beastbook.json;

import beastbook.core.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
  private static final String username = "test";
  private static final String password = "test";
  private static final User user = new User(username, password);
  private static BeastBookPersistence beastBookPersistence = new BeastBookPersistence(user);;


  @BeforeAll
  static void setup() throws IOException {
    File file = new File(System.getProperty("user.home") + "/" + user.getUsername());
    if (file.exists()) {
      beastBookPersistence.deleteUserDir();
    }
  }

  @Test
  void testCreateUser() throws Exceptions.UserAlreadyExistException, IOException {
    beastBookPersistence.createUser();
    assertThrows(Exceptions.UserAlreadyExistException.class, () -> beastBookPersistence.createUser());
  }

  @Test
  void testSaveUser() throws Exceptions.UserAlreadyExistException, IOException, Exceptions.UserNotFoundException {
    assertThrows(Exceptions.UserNotFoundException.class, () -> beastBookPersistence.saveUser());
    beastBookPersistence.createUser();
    beastBookPersistence.saveUser();
  }

  @Test
  void testValidateUser() throws Exceptions.UserAlreadyExistException, IOException, Exceptions.UserNotFoundException, Exceptions.PasswordIncorrectException {
    beastBookPersistence.createUser();
    beastBookPersistence.validateUser();
    User user = new User("doesNotExist", "test");
    beastBookPersistence = new BeastBookPersistence(user);
    assertThrows(Exceptions.UserNotFoundException.class, () -> beastBookPersistence.validateUser());
    user = new User("test", "wrongPassword");
    beastBookPersistence = new BeastBookPersistence(user);
    assertThrows(Exceptions.PasswordIncorrectException.class, () -> beastBookPersistence.validateUser());
  }

  private void assertEqualExercise(Exercise exercise1, Exercise exercise2) {
    assertTrue(exercise1.getId().equals(exercise2.getId()));
    assertTrue(exercise1.getWorkoutID().equals(exercise2.getWorkoutID()));
    assertTrue(exercise1.getWeight() == exercise2.getWeight());
    assertTrue(exercise1.getRepGoal() == exercise2.getRepGoal());
    assertTrue(exercise1.getSets() == exercise2.getSets());
    assertTrue(exercise1.getRepsPerSet() == exercise2.getRepsPerSet());
    assertTrue(exercise1.getRestTime() == exercise2.getRestTime());
  }

  private void assertEqualWorkout(Workout workout1, Workout workout2) {
    assertTrue(workout1.getId().equals(workout2.getId()));
    assertTrue(workout1.getName().equals(workout2.getName()));
    assertTrue(workout1.getExerciseIDs().equals(workout2.getExerciseIDs()));
  }

  private void assertEqualHistory(History history1, History history2) {
    assertTrue(history1.getId().equals(history2.getId()));
    assertTrue(history1.getName().equals(history2.getName()));
    assertTrue(history1.getDate().equals(history2.getDate()));
    assertTrue(history1.getSavedExercises().size() == history2.getSavedExercises().size());
    for (int i = 0; i < history1.getSavedExercises().size(); i++) {
      Exercise exercise1 = history1.getSavedExercises().get(i);
      Exercise exercise2 = history1.getSavedExercises().get(i);
      assertEqualExercise(exercise1, exercise2);
    }
  }

  @Test
  void testSaveLoadDeleteExercise() throws IOException, Exceptions.ExerciseNotFoundException, Exceptions.UserAlreadyExistException, Exceptions.IdNotFoundException, Exceptions.IllegalIdException {
    beastBookPersistence.createUser();
    Id ids = new Id();
    Exercise exerciseNoId = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    Workout workout = new Workout("workoutTest");
    Exercise exerciseWithId = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(exerciseNoId));

    Workout workoutWithId = (Workout) ids.giveId(workout);
    ids.giveId(exerciseWithId);
    exerciseWithId.setWorkoutID(workoutWithId.getId());
    beastBookPersistence.saveIdObject(exerciseWithId);
    Exercise loadedExercise = beastBookPersistence.getExercise(exerciseWithId.getId());
    assertEqualExercise(loadedExercise, exerciseWithId);
    File file = new File(System.getProperty("user.home") + "/" + username + "/Exercises/" + exerciseWithId.getId());
    assertTrue(file.exists());
    System.out.println(exerciseWithId.getId());
    beastBookPersistence.deleteIdObject(exerciseWithId.getId(), Exercise.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> beastBookPersistence.getExercise(exerciseWithId.getId()));
  }

  @Test
  void testSaveLoadDeleteWorkout() throws Exceptions.WorkoutNotFoundException, IOException, Exceptions.UserAlreadyExistException, Exceptions.IdNotFoundException {
    beastBookPersistence.createUser();
    Id ids = new Id();
    Workout workoutTemplate = new Workout("testWorkout");
    final Workout workoutNoId = workoutTemplate;
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(workoutNoId));

    final Workout workoutWithId = (Workout) ids.giveId(workoutTemplate);
    beastBookPersistence.saveIdObject(workoutWithId);
    beastBookPersistence.getWorkout(workoutWithId.getId());
    Workout loadedWorkout = beastBookPersistence.getWorkout(workoutNoId.getId());
    assertEqualWorkout(loadedWorkout, workoutWithId);

    File file = new File(System.getProperty("user.home") + "/" + username + "/Workouts/" + workoutWithId.getId());
    assertTrue(file.exists());
    beastBookPersistence.deleteIdObject(workoutWithId.getId(),Workout.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> beastBookPersistence.getWorkout(workoutWithId.getId()));
  }

  @Test
  void testSaveLoadDeleteHistory() throws Exceptions.HistoryNotFoundException, IOException, Exceptions.IdNotFoundException, Exceptions.IllegalIdException {
    assertDoesNotThrow(() -> beastBookPersistence.createUser());
    Id ids = new Id();
    Exercise exercise = new Exercise("Benchpress", 20, 20, 20, 20, 20);
    Workout workout = new Workout("testWorkout");
    ids.giveId(exercise);
    ids.giveId(workout);
    exercise.setWorkoutID(workout.getId());
    History historyTemplate = new History("testHistory", List.of(exercise));
    final History historyNoId = historyTemplate;
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(historyNoId));

    final History historyWithId = (History) ids.giveId(historyTemplate);
    beastBookPersistence.saveIdObject(historyWithId);
    History loadedHistory = beastBookPersistence.getHistory(historyWithId.getId());
    assertEqualHistory(loadedHistory, historyWithId);

    File file = new File(System.getProperty("user.home") + "/" + username + "/Histories/" + historyWithId.getId());
    assertTrue(file.exists());
    beastBookPersistence.deleteIdObject(historyNoId.getId(),History.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> beastBookPersistence.getHistory(historyWithId.getId()));
  }

  @Test
  void testSaveAndGetIds() throws Exceptions.IdHandlerNotFoundException, IOException, Exceptions.UserAlreadyExistException {
    beastBookPersistence.createUser();
    final Id ids = new Id();
    Workout workout = new Workout("testWorkout");
    Exercise exercise = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    History history = new History("testHistory", List.of(exercise));
    ids.giveId(exercise);
    ids.giveId(workout);
    ids.giveId(history);
    beastBookPersistence.saveIds(ids);
    Id idsLoad = beastBookPersistence.getIds();
    for (String s : ids.getMap(Exercise.class).keySet()) {
      ids.getMap(Exercise.class).get(s).equals(idsLoad.getMap(Exercise.class).get(s));
    }
    for (String s : ids.getMap(Workout.class).keySet()) {
      ids.getMap(Workout.class).get(s).equals(idsLoad.getMap(Workout.class).get(s));
    }
    for (String s : ids.getMap(History.class).keySet()) {
      ids.getMap(History.class).get(s).equals(idsLoad.getMap(History.class).get(s));
    }
  }

  @AfterEach
  void cleanUp() throws IOException {
    beastBookPersistence.deleteUserDir();
  }
}