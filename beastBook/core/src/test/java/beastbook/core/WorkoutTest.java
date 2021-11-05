package beastbook.core;

import org.junit.jupiter.api.*;

public class WorkoutTest {
  private Workout testWorkout;
  private Exercise exercise1;


  @BeforeEach
  void setup() {
    testWorkout = new Workout("testWorkout");
    exercise1 = new Exercise("test1", 1, 1, 1, 0,1);
  }

  @AfterEach
  void cleanUp() {
    testWorkout = null;
  }

  @Test
  void addExerciseTest() {
    testWorkout.addExercise(exercise1);
    Assertions.assertEquals(exercise1, testWorkout.getExercises().get(0));
  }
}
