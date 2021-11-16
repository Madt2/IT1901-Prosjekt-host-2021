package beastbook.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {
  /*
  private User testUser;
  private Workout testWorkout;

  @BeforeEach
  void setup() {
    testUser = new User("Test", "Test");
    testWorkout = new Workout("testWorkout");
    testWorkout.addExercise(new Exercise("test1", 1, 1, 1, 0, 1));
  }

  @Test
  void constructorTest() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      User user = new User("", "");
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      User user = new User("ts", "nts");
    });
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      User user = new User("nts", "ts");
    });
  }

  @Test
  void testAddWorkout() {
    testUser.addWorkout(testWorkout);
    Assertions.assertTrue(testUser.getWorkouts().contains(testWorkout));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testUser.addWorkout(testWorkout);
    });
  }

  @Test
  void testUpdateWorkout() {
    testUser.addWorkout(testWorkout);
    Workout workout = new Workout("temp");
    workout.addExercise(new Exercise("test2",1,1,1,1,1));
    workout.setName("testWorkout");
    testUser.updateWorkout(workout);
    Assertions.assertTrue(testUser.getWorkouts().contains(workout));
    Assertions.assertNotEquals(testWorkout, testUser.getWorkout("testWorkout"));
  }

  @Test
  void testRemoveWorkout() {
    testUser.addWorkout(testWorkout);
    testUser.removeWorkout(testWorkout);
    Assertions.assertFalse(testUser.getWorkouts().contains(testWorkout));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testUser.removeWorkout(testWorkout);
    });
  }*/
}
