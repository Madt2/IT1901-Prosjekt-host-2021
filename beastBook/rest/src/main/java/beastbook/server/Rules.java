package beastbook.server;

import beastbook.core.*;
import java.util.Map;

/**
 * Class for defining what rules apply to adding Exercise, Workout and History objects.
 */
public class Rules {

  /**
   * Current rule for adding Exercise object: No Exercise with the same name can be added to same Workout.
   *
   * @param exercise to validate.
   * @param workout workout to validate Exercise for.
   * @param idHandler used to get workout used for validation,
   *                  and for getting Exercise objects in Workout to compare names to.
   * @throws Exceptions.ExerciseAlreadyExistsException if Exercise with same name exists in given workout.
   */
  public static void exerciseRules(Exercise exercise, Workout workout, IdHandler idHandler)
      throws Exceptions.ExerciseAlreadyExistsException {
    Map<String, String> map = idHandler.getMap(Exercise.class);
    for (String id : workout.getExerciseIds()) {
      if (exercise.getName().equals(map.get(id))) {
        throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
      }
    }
  }

  /**
   * Current rule for adding Workout object: No Workout with the same name can be added to same User.
   *
   * @param workout to be validated.
   * @param idHandler used to get information about existing workout names.
   * @throws Exceptions.WorkoutAlreadyExistsException if Workout with same name exists in given workout.
   */
  public static void workoutRules(Workout workout, IdHandler idHandler) throws Exceptions.WorkoutAlreadyExistsException {
    Map<String, String> map = idHandler.getMap(Workout.class);
    if (map.containsValue(workout.getName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  /**
   * Current rule for adding History object: No History with the same name and date can be added to same User.
   *
   * @param history to be valiteded.
   * @param idHandler used to get information about existing history names and dates.
   * @throws Exceptions.HistoryAlreadyExistsException
   */
  public static void historyRules(History history, IdHandler idHandler) throws Exceptions.HistoryAlreadyExistsException {
    Map<String, String> map = idHandler.getMap(History.class);
    if (map.containsValue(history.getName() + ";" + history.getDate())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName());
    }
  }
}
