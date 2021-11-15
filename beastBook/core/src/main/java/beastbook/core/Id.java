package beastbook.core;

import java.util.ArrayList;
import java.util.List;

public class Id {
  private List<String> workoutIDs = new ArrayList<>();
  private List<String> exerciseIDs = new ArrayList<>();

  public boolean hasWorkoutID(String id) {
    for (String ID : workoutIDs) {
      if (ID.equals(id)) {
        return true;
      }
    }
    return false;
    //Todo can be replaced with return workoutIDs.contains(id);? problem with string and contains?
  }

  public boolean hasExerciseID(String id) {
    for (String ID : exerciseIDs) {
      if (ID.equals(id)) {
        return true;
      }
    }
    return false;
    //Todo can be replaced with return exerciseIDs.contains(id);? problem with string and contains?
  }

  public void addWorkoutID(String id) {
    if (hasWorkoutID(id)) {
      throw new IllegalArgumentException("User already have ID " + id + " stored in workouts");
    }
    workoutIDs.add(id);
  }

  public void removeWorkoutID(String id) {
    if (!hasWorkoutID(id)) {
      throw new IllegalArgumentException("User does not have ID " + id + " stored in workouts");
    }
    //Todo might be problem with remove!
    workoutIDs.remove(id);
  }

  public void addExerciseID(String id) {
    if (hasExerciseID(id)) {
      throw new IllegalArgumentException("User already have ID " + id + " stored in exercises");
    }
    exerciseIDs.add(id);
  }

  public void removeExerciseID(String id) {
    if (!hasExerciseID(id)) {
      throw new IllegalArgumentException("User does not have ID " + id + " stored in exercises");
    }
    //Todo might be problem with remove!
    exerciseIDs.remove(id);
  }

  public List<String> getWorkoutIDs() {
    return new ArrayList<>(workoutIDs);
  }

  public List<String> getExerciseIDs() {
    return new ArrayList<>(exerciseIDs);
  }

}
