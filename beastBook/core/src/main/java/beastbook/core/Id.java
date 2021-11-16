package beastbook.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Id class. This class keeps track of what IDs are in use for workout and exercise objects,
 * and contains a map of (exerciseID : exerciseName) and (workoutID : workoutName).
 */
public class Id {
  private List<String> exerciseIDs = new ArrayList<>();
  private List<String> workoutIDs = new ArrayList<>();
  private HashMap<String, String> exerciseNameIDMap = new HashMap<String, String>();
  private HashMap<String, String> workoutNameIDMap = new HashMap<String, String>();

  /**
   * Checks if workoutID is in use.
   *
   * @param id to check.
   * @return true if ID is in reference list, false otherwise.
   */
  public boolean hasWorkoutID(String id) {
    for (String ID : workoutIDs) {
      if (ID.equals(id)) {
        return true;
      }
    }
    return false;
    //Todo can be replaced with return workoutIDs.contains(id);? problem with string and contains?
  }

  /**
   * Checks if exerciseID is in use.
   *
   * @param id to check
   * @return true if ID is in reference list, false otherwise.
   */
  public boolean hasExerciseID(String id) {
    for (String ID : exerciseIDs) {
      if (ID.equals(id)) {
        return true;
      }
    }
    return false;
    //Todo can be replaced with return exerciseIDs.contains(id);? problem with string and contains?
  }

  public List<String> getWorkoutIDs() {
    return new ArrayList<>(workoutIDs);
  }

  public List<String> getExerciseIDs() {
    return new ArrayList<>(exerciseIDs);
  }

  public String getExerciseIDName(String exerciseID) {
    return exerciseNameIDMap.get(exerciseID);
  }

  public String getWorkoutIDName(String workoutID) {
    return exerciseNameIDMap.get(workoutID);
  }

  /**
   * Adds workoutID to list over workoutIDs in use.
   *
   * @param id to add.
   * @throws IllegalArgumentException if ID is in use already.
   */
  public void addWorkoutID(String id) throws IllegalArgumentException {
    if (hasWorkoutID(id)) {
      throw new IllegalArgumentException("User already have ID " + id + " stored in workouts");
    }
    workoutIDs.add(id);
  }

  /**
   * Removes workoutID from list over workoutIDs in use.
   *
   * @param id to remove.
   * @throws IllegalArgumentException when ID does not exist in reference list.
   */
  public void removeWorkoutID(String id) throws IllegalArgumentException {
    if (!hasWorkoutID(id)) {
      throw new IllegalArgumentException("User does not have ID " + id + " stored in workouts");
    }
    //Todo might be problem with remove!
    workoutIDs.remove(id);
  }

  /**
   * Adds exerciseID to list over exerciseIDs in use.
   *
   * @param id to add
   * @throws IllegalArgumentException if ID is in use already.
   */
  public void addExerciseID(String id) throws IllegalArgumentException {
    if (hasExerciseID(id)) {
      throw new IllegalArgumentException("User already have ID " + id + " stored in exercises");
    }
    exerciseIDs.add(id);
  }

  /**
   * Removes exerciseID from list over exerciseIDs in use.
   *
   * @param id to remove.
   * @throws IllegalArgumentException when ID does not exist in reference list.
   */
  public void removeExerciseID(String id) throws IllegalArgumentException {
    if (!hasExerciseID(id)) {
      throw new IllegalArgumentException("User does not have ID " + id + " stored in exercises");
    }
    //Todo might be problem with remove!
    exerciseIDs.remove(id);
  }

  /**
   * Adds an entry to map over exerciseID to exerciseNames.
   *
   * @param exerciseID value to map.
   * @param exerciseName key to map.
   */
  public void addExerciseIDEntry(String exerciseID, String exerciseName) {
    exerciseNameIDMap.put(exerciseID, exerciseName);
  }

  /**
   * Removes entry for mapping of exerciseID.
   *
   * @param exerciseID entry to remove.
   */
  public void removeExerciseIDEntry(String exerciseID) {
    exerciseNameIDMap.remove(exerciseID);
  }

  /**
   * Adds an entry to map over workoutID to workoutNames.
   *
   * @param workoutID value to map.
   * @param workoutName key to map.
   */
  public void addWorkoutIDEntry(String workoutID, String workoutName) {
    workoutNameIDMap.put(workoutID, workoutName);
  }

  /**
   * Removes entry for mapping of exerciseID.
   *
   * @param workoutID entry to remove.
   */
  public void removeWorkoutIDEntry(String workoutID) {
    workoutIDs.remove(workoutID);
  }
}
