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

public class IdHandlerTest {

    private Workout workout;
    private History history;
    private Exercise exercise;
    private IdHandler idHandler = new IdHandler();

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
      idHandler.giveId(workout);
      assertDoesNotThrow(() -> validateId(workout.getId(), Workout.class));
      assertThrows(IllegalIdException.class, () -> {
        workout.setId("rubbish");
      });

      idHandler.giveId(exercise);
      assertDoesNotThrow(() -> validateId(exercise.getId(), Exercise.class));
      assertThrows(IllegalIdException.class, () -> {
        exercise.setId("rubbish");
      });

      idHandler.giveId(history);
      assertDoesNotThrow(() -> validateId(history.getId(), History.class));
      assertThrows(IllegalIdException.class, () -> {
        history.setId("rubbish");
      });
    }

    @Test
    void testGiveAndRemoveId() throws IdNotFoundException{
      idHandler.giveId(workout);
      assertNotEquals(null, workout.getId());
      assertEquals(true, idHandler.getMap(Workout.class).containsKey(workout.getId()));
      idHandler.removeId(workout.getId(), Workout.class);
      assertEquals(false, idHandler.getMap(Workout.class).containsKey(workout.getId()));
     
      idHandler.giveId(history);
      assertNotEquals(null, history.getId());
      assertEquals(true, idHandler.getMap(History.class).containsKey(history.getId()));
      idHandler.removeId(history.getId(), History.class);
      assertEquals(false, idHandler.getMap(History.class).containsKey(history.getId()));

      idHandler.giveId(exercise);
      assertNotEquals(null, exercise.getId());
      assertEquals(true, idHandler.getMap(Exercise.class).containsKey(exercise.getId()));
      idHandler.removeId(exercise.getId(), Exercise.class);
      assertEquals(false, idHandler.getMap(Exercise.class).containsKey(exercise.getId()));
    }

    @Test
    void testLegals(){
      IdHandler.setLegals(Exercise.class);
      assertEquals(Properties.LEGAL_CHARS_EXERCISE_ID, IdHandler.legalChars);
      assertEquals(Properties.EXERCISE_ID_LENGTH, IdHandler.legalLength);

      IdHandler.setLegals(Workout.class);
      assertEquals(Properties.LEGAL_CHARS_WORKOUT_ID, IdHandler.legalChars);
      assertEquals(Properties.WORKOUT_ID_LENGTH, IdHandler.legalLength);

      IdHandler.setLegals(History.class);
      assertEquals(Properties.LEGAL_CHARS_HISTORY_ID, IdHandler.legalChars);
      assertEquals(Properties.HISTORY_ID_LENGTH, IdHandler.legalLength);
    }
    // (!) addId() is tested in PersistenceTest.
}
