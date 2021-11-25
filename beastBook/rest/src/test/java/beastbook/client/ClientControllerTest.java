package beastbook.client;

import beastbook.core.*;
import beastbook.core.Exceptions.BadPackageException;
import beastbook.core.Exceptions.ExerciseAlreadyExistsException;
import beastbook.core.Exceptions.ExerciseNotFoundException;
import beastbook.core.Exceptions.HistoryAlreadyExistsException;
import beastbook.core.Exceptions.HistoryNotFoundException;
import beastbook.core.Exceptions.IllegalIdException;
import beastbook.core.Exceptions.ServerException;
import beastbook.core.Exceptions.WorkoutAlreadyExistsException;
import beastbook.core.Exceptions.WorkoutNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import static beastbook.core.Properties.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClientControllerTest {
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
  void testAddExercise() throws Exceptions.WorkoutAlreadyExistsException, Exceptions.ServerException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException, Exceptions.ExerciseAlreadyExistsException, Exceptions.ExerciseNotFoundException, JsonProcessingException, BadPackageException, URISyntaxException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.addExercise(mockExercise, "fakeId"));
    assertThrows(Exceptions.WorkoutNotFoundException.class, () -> clientController.addExercise(mockExercise, ("AB")));
    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);
    
    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(mockExercise, returnedWorkoutId));

    Map<String, String> exerciseMap = clientController.getExerciseMap();
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = clientController.getExercise(exerciseId);
    assertEqualWithoutIdExercise(mockExercise, returnedExercise);

    assertThrows(Exceptions.ExerciseAlreadyExistsException.class, () -> clientController.addExercise(returnedExercise, returnedWorkoutId));
  }

  @Test
  void testAddWorkout() throws Exceptions.WorkoutAlreadyExistsException, Exceptions.ServerException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException, JsonProcessingException, BadPackageException, URISyntaxException, ExerciseAlreadyExistsException {
    clientController.addWorkout(mockWorkout, List.of());
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(mockWorkout, List.of(mockExercise)));
    Map<String, String> workoutMap = clientController.getWorkoutMap();
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    Workout returnedWorkout = clientController.getWorkout(workoutId);
    assertEqualWithoutIdWorkout(mockWorkout, returnedWorkout);
    assertThrows(Exceptions.WorkoutAlreadyExistsException.class, () -> clientController.addWorkout(returnedWorkout, List.of(mockExercise)));
  }

  @Test
  void testAddHistory() throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.HistoryNotFoundException, Exceptions.HistoryAlreadyExistsException, JsonProcessingException, BadPackageException, URISyntaxException {
    clientController.addHistory(mockHistory);
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> clientController.addHistory(mockHistory));
    Map<String, String> historyMap = clientController.getHistoryMap();
    String historyId = historyMap.keySet().stream().toList().get(0);
    History returnedHistory = clientController.getHistory(historyId);
    assertEqualWithoutIdHistory(mockHistory, returnedHistory);
    assertThrows(Exceptions.HistoryAlreadyExistsException.class, () -> clientController.addHistory(returnedHistory));
  }

  @Test
  void testUpdateWorkout() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, WorkoutAlreadyExistsException, ExerciseAlreadyExistsException, WorkoutNotFoundException, IllegalIdException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(mockWorkout));
    clientController.addWorkout(mockWorkout, List.of());
    Workout falseWorkout = new Workout("test");
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateWorkout(falseWorkout));

    Map<String, String> workoutMap = clientController.getWorkoutMap();
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    Workout updatedWorkout = clientController.getWorkout(workoutId);
    updatedWorkout.setName("newName");
    clientController.updateWorkout(updatedWorkout);
    Workout loadedWorkout = clientController.getWorkout(updatedWorkout.getId());
    assertEqualWithoutIdWorkout(loadedWorkout, updatedWorkout);
    assertEquals(loadedWorkout.getId(), updatedWorkout.getId());
  }

  @Test
  void testUpdateExercise() throws JsonProcessingException, WorkoutNotFoundException, BadPackageException, ServerException, URISyntaxException, ExerciseAlreadyExistsException, IllegalIdException, ExerciseNotFoundException, WorkoutAlreadyExistsException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(mockExercise));
    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);

    Exercise falseExercise = new Exercise("Bench press", 20, 30, 40, 50, 60);
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.updateExercise(falseExercise));
    Map<String, String> exerciseMap = clientController.getExerciseMap();
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    
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
  void testDeleteExercise() throws JsonProcessingException, WorkoutNotFoundException, BadPackageException, ServerException, URISyntaxException, ExerciseAlreadyExistsException, IllegalIdException, ExerciseNotFoundException, WorkoutAlreadyExistsException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeExercise("notValid"));
    clientController.addWorkout(mockWorkout, List.of());
    String returnedWorkoutId = clientController.getWorkoutMap().keySet().stream().toList().get(0);
    clientController.addExercise(mockExercise, returnedWorkoutId);

    Map<String, String> exerciseMap = clientController.getExerciseMap();
    String exerciseId = exerciseMap.keySet().stream().toList().get(0);
    Exercise returnedExercise = clientController.getExercise(exerciseId);

    clientController.removeExercise(returnedExercise.getId());
    assertTrue(clientController.getExerciseMap().size() == 0);
    clientController.removeExercise(returnedExercise.getId());
  }

  @Test
  void testDeleteWorkout() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, WorkoutAlreadyExistsException, ExerciseAlreadyExistsException, WorkoutNotFoundException, IllegalIdException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeWorkout("notValid"));
    clientController.addWorkout(mockWorkout, List.of());
    Map<String, String> workoutMap = clientController.getWorkoutMap();
    String workoutId = workoutMap.keySet().stream().toList().get(0);
    Workout returnedWorkout = clientController.getWorkout(workoutId);

    clientController.addExercise(new Exercise("Bench press", 20, 30, 40, 50, 60), returnedWorkout.getId());
    clientController.removeWorkout(returnedWorkout.getId());
    assertTrue(clientController.getExerciseMap().size() == 0);
    assertTrue(clientController.getWorkoutMap().size() == 0);
    clientController.removeWorkout(returnedWorkout.getId());
  }

  @Test
  void testDeleteHistory() throws JsonProcessingException, BadPackageException, ServerException, URISyntaxException, HistoryAlreadyExistsException, HistoryNotFoundException, IllegalIdException {
    assertThrows(Exceptions.IllegalIdException.class, () -> clientController.removeHistory("notValid"));
    clientController.addHistory(mockHistory);
    Map<String, String> historyMap = clientController.getHistoryMap();
    String historyId = historyMap.keySet().stream().toList().get(0);
    History returnedHistory = clientController.getHistory(historyId);

    clientController.removeHistory(returnedHistory.getId());
    assertTrue(clientController.getHistoryMap().size() == 0);
    clientController.removeHistory(returnedHistory.getId());
  }

  @AfterAll
  static void cleanUp() throws Exceptions.ServerException, Exceptions.BadPackageException,
      URISyntaxException, JsonProcessingException {
    clientController.deleteUser();
  }
}
