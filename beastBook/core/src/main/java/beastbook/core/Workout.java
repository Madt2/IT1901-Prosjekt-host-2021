package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Workout class used by User class. Contains name and exercises List.
 */
public class Workout {
  private String name;
  private List<Exercise> exercises = new ArrayList<>();
    
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

  /**
  * Method for setting name of workout.
  *
  * @param name name of workout.
  */
  public void setName(String name) {
    this.name = name;
  }

  /**
  * Workout name getter.
  *
  * @return workout name.
  */
  public String getName() {
    return name;
  }

  /**
  * Adds an exorcise to workout.
  *
  * @param exercise exercise object to add to workout.
  */
  public void addExercise(Exercise exercise) {
    for (Exercise e : exercises) {
      if (e.getExerciseName().equals(exercise.getExerciseName())) {
        throw new IllegalArgumentException(
          exercise.getExerciseName() + " is already added as an exercise!"
        );
      }
    }
    exercises.add(exercise);
  }

  /**
   * Removed exercise object from exercises List.
   *
   * @param exercise exercise to remove
   */
  public void removeExercise(Exercise exercise) {
    if (exercises.contains(exercise)) {
      exercises.remove(exercise);
    } else {
      throw new IllegalArgumentException(
        exercise.getExerciseName() + " was not found in workout!"
      );
    }
  }

  /**
  * Getter for exercises.
  *
  * @return returns the list of exercises in workout.
  */
  public List<Exercise> getExercises() {
    return new ArrayList<>(exercises);
  }

  public Workout copy(Workout workout) {
    Workout copy = new Workout(workout.getName());
    for (Exercise e : workout.getExercises()) {
      copy.addExercise(e);
    }
    return copy;
  }

  /**
  * toString for workout. Returns object in more readable format.
  *
  * @return (name of workout): [list of exercises]
  */
  @Override
  public String toString() {
    return getName() + ": " + getExercises();
  }
}