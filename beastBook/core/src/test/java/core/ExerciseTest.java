package core;

import org.junit.jupiter.api.*;

public class ExerciseTest {
    private Exercise exercise;

    @BeforeEach
    void init() {
        exercise = new Exercise("Bench Press", 25, 100, 5, 120);
    }

    @Test
    @DisplayName("Tests if Constructor works as intended")
    void testExercise() {
        Assertions.assertEquals("Bench Press", exercise.getExerciseName());
        Assertions.assertEquals(25, exercise.getRepGoal());
        Assertions.assertEquals(100, exercise.getWeight());
        Assertions.assertEquals(5, exercise.getSets());
        Assertions.assertEquals(120, exercise.getRestTime());
    }

    @Test
    @DisplayName("Tests if illegal arguments trigger exceptions")
    void testValidation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            exercise.setRepGoal(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            exercise.setSets(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            exercise.setWeight(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            exercise.setRestTime(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            exercise.setExerciseName("");
        });

    }
}
