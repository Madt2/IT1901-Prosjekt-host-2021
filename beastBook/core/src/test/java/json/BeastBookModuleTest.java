package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import core.Workout;
import core.Exercise;
import json.internal.BeastBookModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BeastBookModuleTest {

    private static ObjectMapper mapper;

    private final static String UserWithOneWorkout = """
            {
                "userName": "Test"
                "password": "password"
                "workouts": [
                    {
                    "name": "Legday"
                    "exercises": [
                           {
                            "exerciseName": "Squat",
                            "repGoal": 25
                            "weight": 100
                            "sets": 5
                            "restTime": 90
                            },
                            {
                            "exerciseName": "Leg Press",
                            "repGoal": 30
                            "weight": 250
                            "sets": 3
                            "restTime": 60
                           }
                        ]
                    }
                ]
            }
            """;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BeastBookModule());
    }

    static void checkWorkout(Workout workout, String name, List<Exercise> exercises) {
        assertEquals(name, workout.getName());
        assertEquals(exercises, workout.getExercises());
    }

    static void checkExercise(Exercise exercise, String exerciseName, int repGoal, double weight, int sets, int restTime) {
        assertEquals(exerciseName, exercise.getExerciseName());
        assertEquals(repGoal, exercise.getRepGoal());
        assertEquals(weight, exercise.getWeight());
        assertEquals(sets, exercise.getSets());
        assertEquals(restTime, exercise.getRestTime());
    }

    @Test
    public void testSerializers() {
        User user = new User("Test", "password");
        Workout workout = new Workout("Legday");
        Exercise squat = new Exercise("Squat", 25, 100, 5, 90);
        Exercise legPress = new Exercise("Leg Press", 30, 250, 3, 60);
        workout.addExercise(squat);
        workout.addExercise(legPress);
        user.addWorkout(workout);
        try {
            assertEquals(UserWithOneWorkout.replaceAll("\\s+", ""), mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void testDeserializers() {
        try {
            User user = mapper.readValue(UserWithOneWorkout, User.class);
            assertEquals( "Test", user.getUserName());
            assertEquals("password", user.getPassword());
            assertTrue(user.getWorkouts().size() == 1);

            List<Exercise> testList = new ArrayList<>();
            Exercise squat = new Exercise("Squat", 25, 100, 5, 90);
            Exercise legPress = new Exercise("Leg Press", 30, 250, 3, 60);
            testList.add(squat);
            testList.add(legPress);

            checkWorkout(user.getWorkouts().get(0), "Legday", testList);

            checkExercise(user.getWorkouts().get(0).getExercises().get(0), "Squat", 25, 100, 5, 90);
            checkExercise(user.getWorkouts().get(0).getExercises().get(1), "Leg Press", 30, 250, 3, 60);
        } catch (JsonProcessingException e) {
            fail();
        }
    }


    @Test
    public void testSerializersDeserializers() {
        User user = new User("Test", "password");
        Workout workout = new Workout("Legday");
        Exercise squat = new Exercise("Squat", 25, 100, 5, 90);
        Exercise legPress = new Exercise("Leg Press", 30, 250, 3, 60);
        workout.addExercise(squat);
        workout.addExercise(legPress);
        user.addWorkout(workout);
        try {
            String json = mapper.writeValueAsString(user);
            User user2 = mapper.readValue(json, User.class);
            assertEquals( "Test", user2.getUserName());
            assertEquals("password", user2.getPassword());
            assertTrue(user2.getWorkouts().size() == 1);
            List<Exercise> testList = new ArrayList<>();
            testList.add(squat);
            testList.add(legPress);

            checkWorkout(user2.getWorkouts().get(0), "Legday", testList);

            checkExercise(user2.getWorkouts().get(0).getExercises().get(0), "Squat", 25, 100, 5, 90);
            checkExercise(user2.getWorkouts().get(0).getExercises().get(1), "Leg Press", 30, 250, 3, 60);

        } catch (JsonProcessingException e) {
            fail();
        }
    }

}
