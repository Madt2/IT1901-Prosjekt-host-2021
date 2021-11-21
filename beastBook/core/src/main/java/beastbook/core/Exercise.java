package beastbook.core;

import static beastbook.core.Properties.maxDoubleLength;
import static beastbook.core.Properties.maxIntLength;

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

  /**
  * Checks if name is valid, valid is not blank.
  *
  * @param name Name of the exercise
  */
  private void validateName(String name) throws IllegalArgumentException {
    boolean isTooLong = name.length() >= Properties.maxStringLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
              "Exercise Name can not be longer than " + Properties.maxStringLength + " characters!"
      );
    }
    name = name.trim();
    boolean isBlank = name.length() <= 0;
    if (isBlank) {
      throw new IllegalArgumentException("Exercise Name can not be blank!");
    }
  }

  /**
  * Checks if repGoal is valid, valid is more than 0.
  *
  * @param repGoal Number of repetitions to be performed
  */
  private void validateRepGoal(int repGoal) throws IllegalArgumentException {
    boolean isTooLow = (repGoal <= 0);
    if (isTooLow) {
      throw new IllegalArgumentException("Rep Goal must be more than 0!");
    }
    boolean isTooLong = String.valueOf(repGoal)
            .length() > maxIntLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
              "Rep Goal can not be longer than " + maxIntLength + " characters!"
      );
    }
  }

  /**
  * Checks if weight is valid, valid is more than 0.
  *
  * @param weight Weight to be used for the exercise
  */
  private void validateWeight(double weight) throws IllegalArgumentException {
    boolean isTooLow = (weight <= 0);
    if (isTooLow) {
      throw new IllegalArgumentException("Working Weight must be more than 0!");
    }
    boolean isTooLong = String.valueOf(weight).length() > maxDoubleLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
              "Working Weight can not be longer than " + maxDoubleLength + " characters!"
      );
    }
  }

  /**
  * Checks if sets is valid, valid is more than 0.
  *
  * @param sets Number of sets to be performed
  */
  private void validateSets(int sets) throws IllegalArgumentException {
    boolean isTooLow = (sets <= 0);
    if (isTooLow) {
      throw new IllegalArgumentException(
              "Sets must be more than 0!"
      );
    }
    boolean isTooLong = String.valueOf(sets)
            .length() >= maxIntLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
              "Sets can not be longer than " + maxIntLength + " characters!"
      );
    }
  }

  private void validateRepsPerSet(int repsPerSet) throws IllegalArgumentException {
    boolean isTooLow = (repsPerSet < 0);
    if (isTooLow) {
      throw new IllegalArgumentException("Reps Per Set must be more than or equal to 0!");
    }
    boolean isTooLong = String.valueOf(repsPerSet)
            .length() >= maxIntLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
              "Reps Per Set can not be longer than " + maxIntLength + " characters!"
      );
    }
  }

  /**
  * Checks if restTime is valid, valid is more than 0.
  *
  * @param restTime How many seconds of rest between each set
  */
  private void validateRestTime(int restTime) throws IllegalArgumentException {
    boolean isTooLow = (restTime <= 0);
    if (isTooLow) {
      throw new IllegalArgumentException("Rest Time must be more than 0!");
    }
    boolean isTooLong = (String.valueOf(restTime).length() >= maxIntLength);
    if (isTooLong) {
      throw new IllegalArgumentException("Rest Time can not be longer than " 
      + maxIntLength + " characters!");
    }
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
    validateName(name);
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