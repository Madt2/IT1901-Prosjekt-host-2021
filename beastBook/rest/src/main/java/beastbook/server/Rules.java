package beastbook.server;

import beastbook.core.*;
import java.util.Map;

public class Rules {

  public static void exerciseRules(Exercise exercise, Workout workout, IdHandler ids)
      throws Exceptions.ExerciseAlreadyExistsException {
    Map<String, String> map = ids.getMap(Exercise.class);
    for (String id : workout.getExerciseIds()) {
      if (exercise.getName().equals(map.get(id))) {
        throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
      }
    }
  }

  public static void workoutRules(Workout workout, IdHandler ids) throws Exceptions.WorkoutAlreadyExistsException {
    Map<String, String> map = ids.getMap(Workout.class);
    if (map.containsValue(workout.getName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  public static void historyRules(History history, IdHandler ids) throws Exceptions.HistoryAlreadyExistsException {
    Map<String, String> map = ids.getMap(History.class);
    if (map.containsValue(history.getName() + ";" + history.getDate())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName());
    }
  }
}
