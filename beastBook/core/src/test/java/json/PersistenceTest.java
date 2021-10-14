package json;

import core.Exercise;
import core.User;
import core.Workout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersistenceTest {
  private BeastBookPersistence beastBookPersistence = new BeastBookPersistence();

  @Test
  public void testSerializersAndDeserializers() {
    User user = new User("utest", "utest");
    Workout w = new Workout("wtest");
    Exercise e1 = new Exercise("etest1", 1, 1, 1, 1);
    Exercise e2 = new Exercise("etest2", 2, 2, 2, 2);
    Exercise e3 = new Exercise("etest3", 3, 3, 3, 3);

    w.addExercise(e1);
    w.addExercise(e2);
    w.addExercise(e3);
    user.addWorkout(w);

    try{
      beastBookPersistence.setSaveFilePath(user.getUserName());
      beastBookPersistence.saveUser(user);
      User user2 = beastBookPersistence.loadUser();

      Assertions.assertEquals(user.getUserName(), user2.getUserName());
      Assertions.assertEquals(user.getPassword(), user2.getPassword());
      assertTrue(user2.getWorkouts().size()==1);

      Workout w2 = user2.getWorkouts().get(0);
      for (int i = 0; i < w2.getExercises().size(); i++) {
        assertEquals(w.getExercises().get(i).getExerciseName(), w2.getExercises().get(i).getExerciseName());
        assertEquals(w.getExercises().get(i).getRepGoal(), w2.getExercises().get(i).getRepGoal());
        assertEquals(w.getExercises().get(i).getWeight(), w2.getExercises().get(i).getWeight());
        assertEquals(w.getExercises().get(i).getSets(), w2.getExercises().get(i).getSets());
        assertEquals(w.getExercises().get(i).getRestTime(), w2.getExercises().get(i).getRestTime());
      }
    } catch (IOException e) {
        Assertions.fail();
    }
  }
}
