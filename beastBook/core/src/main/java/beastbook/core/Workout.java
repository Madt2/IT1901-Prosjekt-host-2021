package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout class used by User class. Contains name and exercises List.
 */
public class Workout {
  private String name;
  private String id;
  private List<String> exerciseIDs = new ArrayList<>();
    
  /**
  * Contructor for workout with name parameter.
  *
  * @param name name of the workout.
  */
  public Workout(String name) {
    setName(name);
  }

  /**
  * Contructor for workout with no set name.
  */
  public Workout() {}

  private void validateID(String id) throws IllegalArgumentException {
    if (id.length() != 2) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
    }
    final String legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (!(legalChars.contains(String.valueOf(id.charAt(0)))) &&
            legalChars.contains(String.valueOf(id.charAt(1)))) {
      throw new IllegalArgumentException("ID does not use correct characters!");
    }
  }

  /**
  * Method for setting name of workout.
  *
  * @param name name of workout.
  */
  public void setName(String name) {
    this.name = name;
  }

  public void setID(String id) throws IllegalArgumentException {
    validateID(id);
    this.id = id;
  }

  /**
  * Workout name getter.
  *
  * @return workout name.
  */
  public String getName() {
    return name;
  }

  public String getID() {
    return id;
  }

  /**
   * Getter for exercises.
   *
   * @return returns the list of exercises in workout.
   */
  public List<String> getExerciseIDs() {
    return new ArrayList<>(exerciseIDs);
  }

  /**
  * Adds an exorcise to workout.
  *
  * @param exerciseID exerciseID to add to workout.
  */
  public void addExercise(String exerciseID) throws IllegalArgumentException {
    for (String ID : exerciseIDs) {
      if (ID.equals(exerciseID)) {
        throw new IllegalArgumentException("Exercise is already added!");
      }
    }
    exerciseIDs.add(exerciseID);
  }

  /**
   * Removed exercise object from exercises List.
   *
   * @param exerciseID to remove from workout.
   * @throws IllegalArgumentException when exerciseID does not exist in workout.
   */
  public void removeExercise(String exerciseID) throws IllegalArgumentException {
    //Todo might be a issue with remove where it looks for spesific object and not string object equal to ID string!
    for (String ID : exerciseIDs) {
      if (ID.equals(exerciseID)) {
        exerciseIDs.remove(exerciseID);
        return;
      }
    }
    throw new IllegalArgumentException("Exercise was not found in workout!");
  }
}