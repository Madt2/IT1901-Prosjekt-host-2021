package beastbook.json;

import beastbook.core.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static beastbook.core.Properties.mockUser;
import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
  private static BeastBookPersistence beastBookPersistence = new BeastBookPersistence(mockUser);;


  @BeforeAll
  static void setup() throws IOException, Exceptions.ServerException {
    File file = new File(System.getProperty("user.home") + "/" + mockUser.getUsername());
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
    User user = new User("doesNotExist", mockUser.getPassword());
    beastBookPersistence = new BeastBookPersistence(user);
    assertThrows(Exceptions.UserNotFoundException.class, () -> beastBookPersistence.validateUser());
    user = new User(mockUser.getUsername(), "wrongPassword");
    beastBookPersistence = new BeastBookPersistence(user);
    assertThrows(Exceptions.PasswordIncorrectException.class, () -> beastBookPersistence.validateUser());
  }

  private void assertEqualExercise(Exercise exercise1, Exercise exercise2) {
    assertTrue(exercise1.getId().equals(exercise2.getId()));
    assertTrue(exercise1.getWorkoutId().equals(exercise2.getWorkoutId()));
    assertTrue(exercise1.getWeight() == exercise2.getWeight());
    assertTrue(exercise1.getRepGoal() == exercise2.getRepGoal());
    assertTrue(exercise1.getSets() == exercise2.getSets());
    assertTrue(exercise1.getRepsPerSet() == exercise2.getRepsPerSet());
    assertTrue(exercise1.getRestTime() == exercise2.getRestTime());
  }

  private void assertEqualWorkout(Workout workout1, Workout workout2) {
    assertTrue(workout1.getId().equals(workout2.getId()));
    assertTrue(workout1.getName().equals(workout2.getName()));
    assertTrue(workout1.getExerciseIds().equals(workout2.getExerciseIds()));
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
  void testSaveLoadDeleteExercise() throws IOException, Exceptions.ExerciseNotFoundException, Exceptions.UserAlreadyExistException, Exceptions.IdNotFoundException, Exceptions.IllegalIdException, Exceptions.ServerException {
    beastBookPersistence.createUser();
    IdHandler ids = new IdHandler();
    Exercise exerciseNoId = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    Workout workout = new Workout("workoutTest");
    Exercise exerciseWithId = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(exerciseNoId));

    Workout workoutWithId = (Workout) ids.giveId(workout);
    ids.giveId(exerciseWithId);
    exerciseWithId.setWorkoutId(workoutWithId.getId());
    beastBookPersistence.saveIdObject(exerciseWithId);
    Exercise loadedExercise = beastBookPersistence.getExercise(exerciseWithId.getId());
    assertEqualExercise(loadedExercise, exerciseWithId);
    File file = new File(System.getProperty("user.home") + "/" + mockUser.getUsername() + "/Exercises/" + exerciseWithId.getId());
    assertTrue(file.exists());
    System.out.println(exerciseWithId.getId());
    beastBookPersistence.deleteIdObject(exerciseWithId.getId(), Exercise.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.ExerciseNotFoundException.class, () -> beastBookPersistence.getExercise(exerciseWithId.getId()));
  }

  @Test
  void testSaveLoadDeleteWorkout() throws Exceptions.WorkoutNotFoundException, IOException, Exceptions.UserAlreadyExistException, Exceptions.IdNotFoundException, Exceptions.ServerException {
    beastBookPersistence.createUser();
    IdHandler ids = new IdHandler();
    Workout workoutTemplate = new Workout("testWorkout");
    final Workout workoutNoId = workoutTemplate;
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(workoutNoId));

    final Workout workoutWithId = (Workout) ids.giveId(workoutTemplate);
    beastBookPersistence.saveIdObject(workoutWithId);
    beastBookPersistence.getWorkout(workoutWithId.getId());
    Workout loadedWorkout = beastBookPersistence.getWorkout(workoutNoId.getId());
    assertEqualWorkout(loadedWorkout, workoutWithId);

    File file = new File(System.getProperty("user.home") + "/" + mockUser.getUsername() + "/Workouts/" + workoutWithId.getId());
    assertTrue(file.exists());
    beastBookPersistence.deleteIdObject(workoutWithId.getId(),Workout.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> beastBookPersistence.getWorkout(workoutWithId.getId()));
  }

  @Test
  void testSaveLoadDeleteHistory() throws Exceptions.HistoryNotFoundException, IOException, Exceptions.IdNotFoundException, Exceptions.IllegalIdException, Exceptions.ServerException {
    assertDoesNotThrow(() -> beastBookPersistence.createUser());
    IdHandler ids = new IdHandler();
    Exercise exercise = new Exercise("Benchpress", 20, 20, 20, 20, 20);
    Workout workout = new Workout("testWorkout");
    ids.giveId(exercise);
    ids.giveId(workout);
    exercise.setWorkoutId(workout.getId());
    History historyTemplate = new History("testHistory", List.of(exercise));
    final History historyNoId = historyTemplate;
    assertThrows(Exceptions.IdNotFoundException.class, () -> beastBookPersistence.saveIdObject(historyNoId));

    final History historyWithId = (History) ids.giveId(historyTemplate);
    beastBookPersistence.saveIdObject(historyWithId);
    History loadedHistory = beastBookPersistence.getHistory(historyWithId.getId());
    assertEqualHistory(loadedHistory, historyWithId);

    File file = new File(System.getProperty("user.home") + "/" + mockUser.getUsername() + "/Histories/" + historyWithId.getId());
    assertTrue(file.exists());
    beastBookPersistence.deleteIdObject(historyNoId.getId(),History.class);
    assertFalse(file.exists());
    assertThrows(Exceptions.HistoryNotFoundException.class, () -> beastBookPersistence.getHistory(historyWithId.getId()));
  }

  @Test
  void testSaveAndGetIds() throws Exceptions.IdHandlerNotFoundException, IOException, Exceptions.UserAlreadyExistException {
    beastBookPersistence.createUser();
    final IdHandler idHandler = new IdHandler();
    Workout workout = new Workout("testWorkout");
    Exercise exercise = new Exercise("testExercise", 1 ,1, 1, 1, 1);
    History history = new History("testHistory", List.of(exercise));
    idHandler.giveId(exercise);
    idHandler.giveId(workout);
    idHandler.giveId(history);
    beastBookPersistence.saveIdHandler(idHandler);
    IdHandler idHandlerLoad = beastBookPersistence.getIdHandler();
    for (String s : idHandler.getMap(Exercise.class).keySet()) {
      idHandler.getMap(Exercise.class).get(s).equals(idHandlerLoad.getMap(Exercise.class).get(s));
    }
    for (String s : idHandler.getMap(Workout.class).keySet()) {
      idHandler.getMap(Workout.class).get(s).equals(idHandlerLoad.getMap(Workout.class).get(s));
    }
    for (String s : idHandler.getMap(History.class).keySet()) {
      idHandler.getMap(History.class).get(s).equals(idHandlerLoad.getMap(History.class).get(s));
    }
  }

  @AfterEach
  void cleanUp() throws IOException, Exceptions.ServerException {
    beastBookPersistence.deleteUserDir();
  }
}