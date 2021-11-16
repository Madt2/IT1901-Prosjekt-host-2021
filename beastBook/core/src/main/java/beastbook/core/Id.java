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
  private List<String> historyIDs = new ArrayList<>();
  private HashMap<String, String> exerciseNameIDMap = new HashMap<>();
  private HashMap<String, String> workoutNameIDMap = new HashMap<>();
  private HashMap<String, String> historyNameIDMap = new HashMap<>();

  /**
   * Checks if exerciseID is in use.
   *
   * @param id to check
   * @return true if ID is in reference list, false otherwise.
   */
  public boolean hasExerciseID(String id) {
    return exerciseIDs.contains(id);
  }

  /**
   * Checks if workoutID is in use.
   *
   * @param id to check.
   * @return true if ID is in reference list, false otherwise.
   */
  public boolean hasWorkoutID(String id) {
    return workoutIDs.contains(id);
  }

  public boolean hasHistoryID(String id) {
    return historyIDs.contains(id);
  }

  public List<String> getWorkoutIDs() {
    return new ArrayList<>(workoutIDs);
  }

  public List<String> getExerciseIDs() {
    return new ArrayList<>(exerciseIDs);
  }

  public List<String> getHistoryIDs() {
    return new ArrayList<>(historyIDs);
  }

  public String getExerciseIDName(String exerciseID) {
    return exerciseNameIDMap.get(exerciseID);
  }

  public String getWorkoutIDName(String workoutID) {
    return exerciseNameIDMap.get(workoutID);
  }

  public String getHistoryIDName(String historyID) {
    return historyNameIDMap.get(historyID);
  }

  /**
   * Adds exerciseID to list of exerciseIDs in use.
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
   * Adds workoutID to list of workoutIDs in use.
   *
   * @param id to add.
   * @throws IllegalArgumentException if ID is in use already.
   */
  public void addWorkoutID(String id) throws IllegalArgumentException {
    if (hasWorkoutID(id)) {
      throw new IllegalArgumentException("User already has ID " + id + " stored in workouts");
    }
    workoutIDs.add(id);
  }

  /**
   * Adds historyID to list of historyIDs in use.
   *
   * @param id to add
   * @throws IllegalArgumentException if ID is in use already.
   */
  public void addHistoryID(String id) throws IllegalArgumentException {
    if (hasHistoryID(id)) {
      throw new IllegalArgumentException("User already has ID " + id + " stored in histories");
    }
    historyIDs.add(id);
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
   * Adds an entry to map over workoutID to workoutNames.
   *
   * @param workoutID value to map.
   * @param workoutName key to map.
   */
  public void addWorkoutIDEntry(String workoutID, String workoutName) {
    workoutNameIDMap.put(workoutID, workoutName);
  }

  public void addHistoryIDEntry(String historyID, String historyName) {
    historyNameIDMap.put(historyID, historyName);
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
    exerciseIDs.remove(id);
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
    workoutIDs.remove(id);
  }

  public void removeHistoryID(String id) throws IllegalArgumentException {
    if (!hasHistoryID(id)) {
      throw new IllegalArgumentException("User does not have ID " + id + " stored in histories");
    }
    historyIDs.remove(id);
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
   * Removes entry for mapping of exerciseID.
   *
   * @param workoutID entry to remove.
   */
  public void removeWorkoutIDEntry(String workoutID) {
    workoutNameIDMap.remove(workoutID);
  }

  public void removeHistoryIDEntry(String historyID) {
    historyNameIDMap.remove(historyID);
  }

  /**
   * Checks if ID given is valid as exerciseID.
   *
   * @param id to be checked.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  public static void validateExerciseID(String id) throws IllegalArgumentException {
    if (id.length() != 2) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
    }
    final String legalChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    if (!(legalChars.contains(String.valueOf(id.charAt(0)))) &&
          legalChars.contains(String.valueOf(id.charAt(1)))) {
      throw new IllegalArgumentException("ID does not use correct characters!");
    }
  }

  /**
   * Checks if ID given is valid as workoutID.
   *
   * @param id to be checked.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  public static void validateWorkoutID(String id) throws IllegalArgumentException {
    if (id.length() != 2) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
    }
    final String legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (!(legalChars.contains(String.valueOf(id.charAt(0)))) &&
          legalChars.contains(String.valueOf(id.charAt(1)))) {
      throw new IllegalArgumentException("ID does not use correct characters!");
    }
  }
}
