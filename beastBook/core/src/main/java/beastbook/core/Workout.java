package beastbook.core;

import static beastbook.core.Validation.validateId;

import java.util.ArrayList;
import java.util.List;


/**
 * Workout class that creates a workout. It has a name, a unique Id to identify it,
 * and a list of Ids to reference exercise objects.
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

  /**
   * Sets name of Workout
   *
   * @param name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets workoutId for Workout.
   *
   * @param id to set.
   * @throws Exceptions.IllegalIdException if id is invalid as workoutId.
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
  * Adds an exerciseId to Workout.
  *
  * @param exerciseId exerciseId to add to Workout.
  * @throws Exceptions.IllegalIdException if Id is not valid exerciseId.
  * @throws Exceptions.ExerciseAlreadyExistsException when workout already have reference to Exercise.
  */
  public void addExercise(String exerciseId) throws Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException {
    validateId(exerciseId, Exercise.class);
    if (exerciseIds.contains(exerciseId)) {
      throw new Exceptions.ExerciseAlreadyExistsException(name);
    }
    exerciseIds.add(exerciseId);
  }

  /**
   * Removes reference to exercise object from exerciseIDs List.
   *
   * @param exerciseId of Exercise remove from workout.
   * @throws Exceptions.ExerciseNotFoundException when exerciseID does not exist in workout.
   */
  public void removeExercise(String exerciseId) throws Exceptions.ExerciseNotFoundException {
    if (!exerciseIds.remove(exerciseId)) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }
}



