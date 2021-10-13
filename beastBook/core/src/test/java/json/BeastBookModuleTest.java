package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import core.Workout;
import core.Exercise;
import json.internal.BeastBookModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class BeastBookModuleTest {

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BeastBookModule());
    }

    private final static String UserWithOneWorkout = """
            {
                "username": "Test",
                "password": "password",
                "workouts": [
                    {
                    "name": "Legday",
                    "exercises": [
                           {
                            "exerciseName": "Squat",
                            "repGoal": "25",
                            "weight": "100.0",
                            "sets": "5",
                            "restTime": "90"
                            },
                            {
                            "exerciseName": "LegPress",
                            "repGoal": "30",
                            "weight": "250.0",
                            "sets": "3",
                            "restTime": "60"
                           }
                        ]
                    }
                ]
            }
            """;



    static void checkWorkout(Workout workout, String name, List<Exercise> exercises) {
        assertEquals(name, workout.getName());
        for (int i = 0; i < workout.getExercises().size(); i++) {
            assertEquals(exercises.get(i).getExerciseName(), workout.getExercises().get(i).getExerciseName());
            assertEquals(exercises.get(i).getRepGoal(), workout.getExercises().get(i).getRepGoal());
            assertEquals(exercises.get(i).getWeight(), workout.getExercises().get(i).getWeight());
            assertEquals(exercises.get(i).getSets(), workout.getExercises().get(i).getSets());
            assertEquals(exercises.get(i).getRestTime(), workout.getExercises().get(i).getRestTime());
        }
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
        Exercise legPress = new Exercise("LegPress", 30, 250, 3, 60);
        workout.addExercise(squat);
        workout.addExercise(legPress);
        user.addWorkout(workout);
        try {
            Assertions.assertEquals(UserWithOneWorkout.replaceAll("\\s+", ""),mapper.writeValueAsString(user));
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
            Exercise legPress = new Exercise("LegPress", 30, 250, 3, 60);
            testList.add(squat);
            testList.add(legPress);

            checkWorkout(user.getWorkouts().get(0), "Legday", testList);

            checkExercise(user.getWorkouts().get(0).getExercises().get(0), "Squat", 25, 100, 5, 90);
            checkExercise(user.getWorkouts().get(0).getExercises().get(1), "LegPress", 30, 250, 3, 60);
        } catch (JsonProcessingException e) {
            fail();
        }
    }


    @Test
    public void testSerializersDeserializers() {
        User user = new User("Test", "password");
        Workout workout = new Workout("Legday");
        Exercise squat = new Exercise("Squat", 25, 100, 5, 90);
        Exercise legPress = new Exercise("LegPress", 30, 250, 3, 60);
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
            checkExercise(user2.getWorkouts().get(0).getExercises().get(1), "LegPress", 30, 250, 3, 60);

        } catch (JsonProcessingException e) {
            fail();
        }
    }

}
