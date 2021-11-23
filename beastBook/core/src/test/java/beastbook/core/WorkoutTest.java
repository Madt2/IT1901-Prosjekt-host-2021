package beastbook.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import beastbook.core.Exceptions.IllegalIdException;

public class WorkoutTest {
  
  private Workout testWorkout;
  private Exercise exercise;

  @BeforeEach
  void setup() throws IllegalIdException {
    testWorkout = new Workout("testWorkout");
    exercise = new Exercise("ex", 50, 50, 30, 20, 20);
    exercise.setId("9d");
  }

  @Test
  void testSetCorrectIdToWorkout() throws IllegalIdException{
    testWorkout.setId("TK");
    assertEquals("TK", testWorkout.getId());    

    testWorkout.setId("PM");
    assertEquals("PM", testWorkout.getId());   
  }

  @Test
  void testIncorrectWorkoutIdFails(){
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("k3");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("  ");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("--");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("2P");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("K$R$#34");
    });
    Assertions.assertThrows(IllegalIdException.class, () -> {
      testWorkout.setId("K ");
    });
  }

  @Test
  void testAddAndRemoveExercise() throws IllegalIdException, Exceptions.ExerciseNotFoundException, Exceptions.ExerciseAlreadyExistsException {
    testWorkout.addExercise(exercise.getId());
   
    assertEquals(1, testWorkout.getExerciseIds().size());
    assertEquals(exercise.getId(), testWorkout.getExerciseIds().get(0));

    testWorkout.removeExercise(exercise.getId());
    assertEquals(0, testWorkout.getExerciseIds().size());
  }

  @Test
  void addAndRemoveDuplicateExerciseTest() throws IllegalIdException, Exceptions.ExerciseNotFoundException, Exceptions.ExerciseAlreadyExistsException {
    testWorkout.addExercise(exercise.getId());
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testWorkout.addExercise(exercise.getId());
    });

    testWorkout.removeExercise(exercise.getId());
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testWorkout.removeExercise(exercise.getId());
    });
  }
}