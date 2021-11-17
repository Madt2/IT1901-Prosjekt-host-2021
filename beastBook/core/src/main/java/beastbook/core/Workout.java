package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout class that creates a workout. It has a name, a unique ID to identify it,
 * and a list of IDs to reference exercise objects.
 */
public class Workout implements IIdClases {
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
  * Method for setting name of workout.
  *
  * @param name name of workout.
  */
  public void setName(String name) {
    this.name = name;
  }

  public void setId(String id) throws IllegalArgumentException {
    Id.validateID(id, Workout.class);
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

  public String getId() {
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
    Id.validateID(exerciseID, Exercise.class);
    exerciseIDs.add(exerciseID);
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