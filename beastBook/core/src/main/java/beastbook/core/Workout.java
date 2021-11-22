package beastbook.core;

import static beastbook.core.Validation.validateId;

import java.util.ArrayList;
import java.util.List;


/**
 * Workout class that creates a workout. It has a name, a unique ID to identify it,
 * and a list of IDs to reference exercise objects.
 */
public class Workout implements IdClasses {
  private String id;
  private String name;
  private List<String> exerciseIds = new ArrayList<>();
    
  /**
  * Constructor for workout with name parameter.
  *
  * @param name of the workout.
  */
  public Workout(String name) {
    setName(name);
  }

  /**
  * Constructor for workout with no set name.
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
  public void setId(String id) throws Exceptions.IllegalIdException {
    validateId(id, Workout.class);
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public List<String> getExerciseIds() {
    return new ArrayList<>(exerciseIds);
  }

  /**
  * Adds an exercise to workout.
  *
  * @param exerciseId exerciseID to add to workout.
  * @throws IllegalArgumentException when workout already have reference to exercise,
  *                                  or if ID is wrong formatted.
  */
  public void addExercise(String exerciseId) throws Exceptions.IllegalIdException {
    if (exerciseIds.contains(exerciseId)) {
      throw new IllegalArgumentException("Exercise is already added!");
    }
    validateId(exerciseId, Exercise.class);
    exerciseIds.add(exerciseId);
  }

  /**
   * Removes reference to exercise object from exerciseIDs List.
   *
   * @param exerciseId to remove from workout.
   * @throws IllegalArgumentException when exerciseID does not exist in workout.
   */
  public void removeExercise(String exerciseId) throws IllegalArgumentException {
    if (!exerciseIds.remove(exerciseId)) {
      throw new IllegalArgumentException("Exercise was not found in workout!");
    }
  }
}