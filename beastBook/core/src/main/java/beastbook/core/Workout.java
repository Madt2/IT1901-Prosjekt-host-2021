package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout class that creates a workout. It has a name, a unique ID to identify it,
 * and a list of IDs to reference exercise objects.
 */
public class Workout {
  private String name;
  private String id;
  private List<String> exerciseIDs = new ArrayList<>();
    
  /**
  * Contructor for workout with name parameter.
  *
  * @param name of the workout.
  */
  public Workout(String name) {
    setName(name);
  }

  /**
  * Contructor for workout with no set name.
  */
  public Workout() {}

  /**
   * Checks if ID given is valid as workoutID.
   *
   * @param id to be checked.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  private void validateWorkoutID(String id) throws IllegalArgumentException {
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
   * Checks if ID given is valid as exerciseID.
   *
   * @param id to be checked.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  private void validateExerciseID(String id) throws IllegalArgumentException {
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
  * Method for setting name of workout.
  *
  * @param name name of workout.
  */
  public void setName(String name) {
    this.name = name;
  }

  public void setID(String id) throws IllegalArgumentException {
    validateWorkoutID(id);
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
  * @throws IllegalArgumentException when workout already have reference to exercise,
  *                                  or if ID is wrong formatted.
  */
  public void addExercise(String exerciseID) throws IllegalArgumentException {
    for (String ID : exerciseIDs) {
      if (ID.equals(exerciseID)) {
        throw new IllegalArgumentException("Exercise is already added!");
      }
    }
    validateExerciseID(exerciseID);
    exerciseIDs.add(exerciseID);
  }

  /**
   * Method to get a copy of a workout,
   * used to save History objects with new reference.
   *
   * @param workout The Workout to be copied
   * @return a copy of the Workout
   */
  public Workout copy(Workout workout) {
    Workout copy = new Workout(workout.getName());
    for (Exercise e : workout.getExercises()) {
      copy.addExercise(e);
    }
    return copy;
  }

  /**
   * Removes reference to exercise object from exerciseIDs List.
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