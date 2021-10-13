package core;

import org.junit.jupiter.api.*;

public class WorkoutTest {

    private Workout testWorkout;
    private Exercise exercise1;
    private Exercise exercise2;


    @BeforeEach
    void setup() {
        testWorkout = new Workout("testWorkout");
        exercise1 = new Exercise("test1", 1, 1, 1, 1);
        exercise2 = new Exercise("test2", 2, 2, 2, 2);
    }

    @AfterEach
    void cleanUp() {
        testWorkout = null;
    }



    @Test
    void addWorkoutTest() {
        testWorkout.addExercise(exercise1);
        Assertions.assertEquals(exercise1, testWorkout.getExercises().get(0));
    }


}
