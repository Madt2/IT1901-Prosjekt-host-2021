package core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

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
    void constructorTest() {
        Workout workout = new Workout("testConstructor1");
        Assertions.assertEquals(workout.getName(), "testConstructor1");
        workout = new Workout();
        workout.setName("testConstructor2");
        Assertions.assertEquals("testConstructor2", workout.getName());
    }

    @Test
    void addWorkoutTest() {
        testWorkout.addExercise(exercise1);
        Assertions.assertEquals(exercise1, testWorkout.getExercises().get(0));
    }

    @Test
    void saveAndLoadWorkoutTest() {
        //Save one exercise in testWorkout, then tests save and load.
        testWorkout.addExercise(exercise1);
        try {testWorkout.saveWorkout();}
        catch (FileNotFoundException e) {System.err.println(e);}
        testWorkout = new Workout("testWorkout");
        try {testWorkout.loadWorkout("testWorkout");}
        catch (FileNotFoundException e) {System.err.println(e);}
        Assertions.assertEquals(1, testWorkout.getExercises().size());

        //Save another exercise in testWorkout, then tests save and load.
        testWorkout.addExercise(exercise2);
        try {testWorkout.saveWorkout();}
        catch (FileNotFoundException e) {System.err.println(e);}
        testWorkout = new Workout("testWorkout");
        try {testWorkout.loadWorkout("TestWorkout");}
        catch (FileNotFoundException e) {System.err.println(e);}
        Assertions.assertEquals(2, testWorkout.getExercises().size());

        //Cleanup:
        File file = new File("TestWorkout");
        file.delete();

        //tests if load throws exception if it loads a workout file that does not exist.
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            testWorkout.loadWorkout("doesnotexist");
        });
    }

}
