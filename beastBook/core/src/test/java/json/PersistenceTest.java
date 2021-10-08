package json;

import core.Exercise;
import core.User;
import core.Workout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class PersistenceTest {

    private BeastBookPersistence beastBookPersistence = new BeastBookPersistence();

    @Test
    public void testSerializersAndDeserializers() {

        User uSerialize = new User("utest", "utest");
        Workout w = new Workout("wtest");
        Exercise e1 = new Exercise("etest1", 1, 1, 1, 1);
        Exercise e2 = new Exercise("etest2", 2, 2, 2, 2);
        Exercise e3 = new Exercise("etest3", 3, 3, 3, 3);

        w.addExercise(e1);
        w.addExercise(e2);
        w.addExercise(e3);
        uSerialize.addWorkout(w);

        try{
            StringWriter writer = new StringWriter();
            beastBookPersistence.writeUser(uSerialize, writer);

            String json = writer.toString();
            String testJson = """
            {
                "userName": "utest"
                "password": "utest"
                "workouts": [
                    {
                    "name": "wtest"
                    "exercises": [
                           {
                            "exerciseName": "etest1",
                            "repGoal": 1
                            "weight": 1
                            "sets": 1
                            "restTime": 1
                            },
                            {
                            "exerciseName": "etest2",
                            "repGoal": 2
                            "weight": 2
                            "sets": 2
                            "restTime": 2
                           }
                           {
                            "exerciseName": "etest3",
                            "repGoal": 3
                            "weight": 3
                            "sets": 3
                            "restTime": 3
                           }
                        ]
                    }
                ]
            }
            """;
            Assertions.assertTrue(json.equals(testJson));

            User uDeserialize = beastBookPersistence.readUser(new StringReader(json));
            Assertions.assertEquals(uDeserialize.getUserName(), uSerialize.getUserName());
            Assertions.assertEquals(uDeserialize.getPassword(), uSerialize.getPassword());
            for (int i = 0; i < uSerialize.getWorkouts().size(); i++) {
                Workout uSerializeWI = uSerialize.getWorkouts().get(i);
                Workout uDeserializeWI = uDeserialize.getWorkouts().get(i);
                Assertions.assertEquals(uDeserializeWI, uSerializeWI);
                for (int j = 0; j < uSerializeWI.getExercises().size(); j++) {
                    Exercise uSerializeEJ = uSerializeWI.getExercises().get(j);
                    Exercise uDeserializeEJ = uDeserializeWI.getExercises().get(j);
                    Assertions.assertEquals(uDeserializeEJ, uSerializeEJ);
                }
            }

        } catch (IOException e) {
            Assertions.fail();
        }
    }


}
