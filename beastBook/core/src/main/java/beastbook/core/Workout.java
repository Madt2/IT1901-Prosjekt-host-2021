package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout class that creates a workout. It has a name, a unique ID to identify it,
 * and a list of IDs to reference exercise objects.
 */
public class Workout implements IdClasses {
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

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets id for this workout object.
   *
   * @param id to set
   * @throws IllegalArgumentException if id is in wrong format.
   */
  public void setId(String id) throws IllegalArgumentException {
    Id.validateID(id, Workout.class);
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

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
    if (exerciseIDs.contains(exerciseID)) {
      throw new IllegalArgumentException("Exercise is already added!");
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
    if (!exerciseIDs.remove(exerciseID)) {
      throw new IllegalArgumentException("Exercise was not found in workout!");
    }
  }
}