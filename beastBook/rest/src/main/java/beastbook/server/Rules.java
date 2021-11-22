package beastbook.server;

import beastbook.core.*;

import java.util.Map;

public class Rules {

  public static void ExerciseRules(Exercise exercise, Workout workout, Id ids) throws Exceptions.ExerciseAlreadyExistsException {
    Map map = ids.getMap(Exercise.class);
    for (String id : workout.getExerciseIDs()) {
      if (exercise.getName().equals(map.get(id))) {
        throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
      }
    }
  }

  public static void WorkoutRules(Workout workout, Id ids) throws Exceptions.WorkoutAlreadyExistsException {
    Map map = ids.getMap(Workout.class);
    if (map.containsValue(workout.getName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  public static void HistoryRules(History history, Id ids) throws Exceptions.HistoryAlreadyExistsException {
    Map map = ids.getMap(Workout.class);
    if (map.containsKey(history.getName())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName());
    }
  }

}
