package beastbook.core;

import static beastbook.core.Validation.*;

/**
 * Exercise class used in Workout class. Creates an Exercise object containing name, rep goal,
 * weight used, amount of sets, reps per set and rest time for exercise. It also has a unique ID
 * to identify it, as well as an ID to reference the workout object it belongs to.
 */
public class Exercise implements IdClasses {
  private String id;
  private String workoutID;
  private String name;
  private int repGoal;
  private double weight;
  private int sets;
  private int repsPerSet;
  private int restTime;

  /**
  * Creates an Exercise using given input.
  *
  * @param name Name of the exercise
  * @param repGoal Number of repetitions to be performed
  * @param weight Weight to be used for the exercise
  * @param sets Number of sets to be performed
  * @param restTime How much rest between sets in seconds
   * @throws IllegalArgumentException when params are illegal.
  */
  public Exercise(String name, int repGoal, double weight, int sets, int repsPerSet, int restTime) throws IllegalArgumentException {
    setName(name);
    setRepGoal(repGoal);
    setWeight(weight);
    setSets(sets);
    setRepsPerSet(repsPerSet);
    setRestTime(restTime);
  }

  public void setId(String id) throws Exceptions.IllegalIdException {
    Id.validateId(id, Exercise.class);
    this.id = id;
  }

  public void setWorkoutID(String id) throws Exceptions.IllegalIdException {
    Id.validateId(id, Workout.class);
    this.workoutID = id;
  }

  public void setName(String name) throws IllegalArgumentException {
    validateExerciseName(name);
    this.name = name;
  }

  public void setRepGoal(int repGoal) throws IllegalArgumentException {
    validateRepGoal(repGoal);
    this.repGoal = repGoal;
  }

  public void setWeight(double weight) throws IllegalArgumentException {
    validateWeight(weight);
    this.weight = weight;
  }

  public void setSets(int sets) throws IllegalArgumentException {
    validateSets(sets);
    this.sets = sets;
  }

  public void setRepsPerSet(int repsPerSet) throws IllegalArgumentException {
    validateRepsPerSet(repsPerSet);
    this.repsPerSet = repsPerSet;
  }

  public void setRestTime(int restTime) throws IllegalArgumentException {
    validateRestTime(restTime);
    this.restTime = restTime;
  }

  public String getId() {
    return id;
  }

  public String getWorkoutID() {
    return workoutID;
  }

  public String getName() {
    return this.name;
  }

  public int getRepGoal() {
    return this.repGoal;
  }

  public double getWeight() {
    return this.weight;
  }

  public int getSets() {
    return this.sets;
  }

  public int getRepsPerSet() {
    return repsPerSet;
  }

  public int getRestTime() {
    return restTime;
  }
}