package beastbook.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.*;
import beastbook.core.Exceptions.IllegalIdException;

public class ExerciseTest {
  private Exercise exercise;

  @BeforeEach
  void setup() {
      exercise = new Exercise("Bench press", 25, 100, 5, 0, 120);
  }

  @Test
  void testConstructor() {
    assertEquals("Bench press", exercise.getName());
    assertEquals(25, exercise.getRepGoal());
    assertEquals(100, exercise.getWeight());
    assertEquals(5, exercise.getSets());
    assertEquals(120, exercise.getRestTime());
  }

  @Test
  void testInvalidInputsThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      exercise.setRepGoal(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      exercise.setSets(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      exercise.setWeight(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      exercise.setRestTime(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      exercise.setName("");
    });
    assertThrows(IllegalIdException.class, () -> {
      exercise.setId("111");
    });
    assertThrows(IllegalIdException.class, () -> {
      exercise.setId("2G");
    });
  }
}