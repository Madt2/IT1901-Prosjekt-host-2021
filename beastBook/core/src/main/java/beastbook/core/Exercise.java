package beastbook.core;

import static beastbook.core.Validation.*;

/**
 * Exercise class used in Workout class. Creates an Exercise object containing name, rep goal,
 * weight used, amount of sets, reps per set and rest time for exercise. It also has a unique ID
 * to identify it, as well as an ID to reference the workout object it belongs to.
 */
public class Exercise implements IdClasses {
  private String id;
  private String workoutId;
  private String name;
  private int repGoal;
  private double weight;
  private int sets;
  private int repsPerSet;
  private int restTime;

  /**
  * Creates an Exercise using given input.
  *
  * @param name Name of the Exercise
  * @param repGoal Number of repetitions to be performed
  * @param weight Weight to be used for the exercise
  * @param sets Number of sets to be performed
  * @param restTime How much rest between sets in seconds
  * @throws IllegalArgumentException when validation for params fail.
  */
  public Exercise(String name, int repGoal, double weight, int sets, int repsPerSet, int restTime)
      throws IllegalArgumentException {
    setName(name);
    setRepGoal(repGoal);
    setWeight(weight);
    setSets(sets);
    setRepsPerSet(repsPerSet);
    setRestTime(restTime);
  }

  /**
   * Sets id for Exercise.
   *
   * @param id to set.
   * @throws Exceptions.IllegalIdException when id is invalid as exercise id.
   */
  public void setId(String id) throws Exceptions.IllegalIdException {
    validateId(id, Exercise.class);
    this.id = id;
  }

  /**
   * Sets workoutId for Exercise.
   *
   * @param id workoutId to set.
   * @throws Exceptions.IllegalIdException when id is invalid as workout id.
   */
  public void setWorkoutId(String id) throws Exceptions.IllegalIdException {
    validateId(id, Workout.class);
    this.workoutId = id;
  }

  /**
   * Sets name for Exercise with Validation.
   *
   * @param name of Exercise
   * @throws IllegalArgumentException if validation fails.
   */
  public void setName(String name) throws IllegalArgumentException {
    validateExerciseName(name);
    this.name = name;
  }

  /**
   * Sets rep goal for Exercise with Validation.
   *
   * @param repGoal to set.
   * @throws IllegalArgumentException if validation fails.
   */
  public void setRepGoal(int repGoal) throws IllegalArgumentException {
    validateRepGoal(repGoal);
    this.repGoal = repGoal;
  }

  /**
   * Sets weight for Exercise with Validation.
   *
   * @param weight to set.
   * @throws IllegalArgumentException if validation fails.
   */
  public void setWeight(double weight) throws IllegalArgumentException {
    validateWeight(weight);
    this.weight = weight;
  }

  /**
   * Sets number of sets for Exercise with Validation.
   *
   * @param sets to set.
   * @throws IllegalArgumentException if validation fails.
   */
  public void setSets(int sets) throws IllegalArgumentException {
    validateSets(sets);
    this.sets = sets;
  }

  /**
   * Sets number of reps per set for Exercise with Validation.
   *
   * @param repsPerSet to set.
   * @throws IllegalArgumentException if validation fails.
   */
  public void setRepsPerSet(int repsPerSet) throws IllegalArgumentException {
    validateRepsPerSet(repsPerSet);
    this.repsPerSet = repsPerSet;
  }

  /**
   * Sets rest time between sets for Exercise with Validation.
   *
   * @param restTime to set.
   * @throws IllegalArgumentException if validation fails.
   */
  public void setRestTime(int restTime) throws IllegalArgumentException {
    validateRestTime(restTime);
    this.restTime = restTime;
  }

  public String getId() {
    return id;
  }

  public String getWorkoutId() {
    return workoutId;
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