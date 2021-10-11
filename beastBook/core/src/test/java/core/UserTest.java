package core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class UserTest {

    private User testUser;
    private Workout testWorkout;

    @BeforeEach
    void setup() {
        testUser = new User("Test", "Test");
        testWorkout = new Workout("testWorkout");
        testWorkout.addExercise(new Exercise("test1", 1, 1, 1, 1));
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
    }

    @Test
    void testRemoveWorkout() {

    }

}
