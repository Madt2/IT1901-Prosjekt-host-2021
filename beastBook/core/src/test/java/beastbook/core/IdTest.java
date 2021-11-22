package beastbook.core;

import static beastbook.core.Validation.validateId;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import beastbook.core.Exceptions.IdNotFoundException;
import beastbook.core.Exceptions.IllegalIdException;

public class IdTest {

    private Workout workout;
    private History history;
    private Exercise exercise;
    private Id id = new Id();

    @BeforeEach
    void setup(){
      List<Exercise> exerciseList = new ArrayList<>();
      exercise = new Exercise("Benchpress", 20, 30, 40, 50, 60);
      exerciseList.add(exercise);
      workout = new Workout();
      history = new History("HistoryTest", exerciseList); 
    }

    @Test
    void testValidateId(){
      id.giveId(workout);
      assertDoesNotThrow(() -> validateId(workout.getId(), Workout.class));
      assertThrows(IllegalIdException.class, () -> {
        workout.setId("rubbish");
      });

      id.giveId(exercise);
      assertDoesNotThrow(() -> validateId(exercise.getId(), Exercise.class));
      assertThrows(IllegalIdException.class, () -> {
        exercise.setId("rubbish");
      });

      id.giveId(history);
      assertDoesNotThrow(() -> validateId(history.getId(), History.class));
      assertThrows(IllegalIdException.class, () -> {
        history.setId("rubbish");
      });
    }

    @Test
    void testGiveAndRemoveId() throws IdNotFoundException{
      id.giveId(workout);
      assertNotEquals(null, workout.getId());
      assertEquals(true, id.getMap(Workout.class).containsKey(workout.getId()));
      id.removeId(workout.getId(), Workout.class);
      assertEquals(false, id.getMap(Workout.class).containsKey(workout.getId()));
     
      id.giveId(history);
      assertNotEquals(null, history.getId());
      assertEquals(true, id.getMap(History.class).containsKey(history.getId()));
      id.removeId(history.getId(), History.class);
      assertEquals(false, id.getMap(History.class).containsKey(history.getId()));

      id.giveId(exercise);
      assertNotEquals(null, exercise.getId());
      assertEquals(true, id.getMap(Exercise.class).containsKey(exercise.getId()));
      id.removeId(exercise.getId(), Exercise.class);
      assertEquals(false, id.getMap(Exercise.class).containsKey(exercise.getId()));
    }

    @Test
    void testLegals(){
      Id.setLegals(Exercise.class);
      assertEquals(Properties.LEGAL_CHARS_EXERCISE_ID, Id.legalChars);
      assertEquals(Properties.EXERCISE_ID_LENGTH, Id.legalLength);

      Id.setLegals(Workout.class);
      assertEquals(Properties.LEGAL_CHARS_WORKOUT_ID, Id.legalChars);
      assertEquals(Properties.WORKOUT_ID_LENGTH, Id.legalLength);

      Id.setLegals(History.class);
      assertEquals(Properties.LEGAL_CHARS_HISTORY_ID, Id.legalChars);
      assertEquals(Properties.HISTORY_ID_LENGTH, Id.legalLength);
    }
    // (!) addId() is tested in PersistenceTest.
}
