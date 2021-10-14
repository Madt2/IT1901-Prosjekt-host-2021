package core;

import java.util.ArrayList;
import java.util.List;

public class Workout {
  private String name;
  private List<Exercise> exercises = new ArrayList<>();
    
  /**
  * Contructor for workout with name parameter.
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
  * @param name name of workout.
  */
  public void setName(String name) {
    this.name = name;
  }

  /**
  * Workout name getter.
  * @return workout name.
  */
  public String getName() {
    return name;
  }

  /**
  * Adds an exorcise to workout.
  * @param exercise exercise object to add to workout.
  */
  public void addExercise(Exercise exercise) {
    exercises.add(exercise);
  }

  /**
  * Getter for exercises.
  * @return returns the list of exercises in workout.
  */
  public List<Exercise> getExercises() {
    return new ArrayList<>(exercises);
  }

  /**
  * toString for workout. Returns object in more readable format.
  * @return (name of workout): [list of exercises]
  */
  @Override
  public String toString() {
    return getName() + ": " + getExercises();
  }
}

