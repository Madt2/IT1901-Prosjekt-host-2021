package beastbook.core;

/**
 * Exercise class used in Workout class. Creates an Exercise object containing name, rep goal,
 * weight used, amount of sets, reps per set and rest time for exercise.
 */
public class Exercise {
  public final int maxStringLength = 20;
  public final int maxIntLength = 5;
  public final int maxDoubleLength = 7;
  private String exerciseName;
  private int repGoal;
  private double weight;
  private int sets;
  private int repsPerSet;
  private int restTime;

  /**
  * Creates an Exercise using given input.
  *
  * @param exerciseName Name of the exercise
  * @param repGoal Number of repetitions to be performed
  * @param weight Weight to be used for the exercise
  * @param sets Number of sets to be performed
  * @param restTime How much rest between sets in seconds
  */
  public Exercise(String exerciseName, int repGoal, double weight, int sets, int restTime) {
    setExerciseName(exerciseName);
    setRepGoal(repGoal);
    setWeight(weight);
    setSets(sets);
    setRestTime(restTime);
  }

  /**
   * Exercise constructor for empty Exercise object.
   */
  public Exercise() {}

  /**
  * Checks if name is valid, valid is not blank.
  *
  * @param name Name of the exercise
  */
  private void validateExerciseName(String exerciseName) {
    if ((exerciseName.length() >= maxStringLength)) {
      throw new IllegalArgumentException("Exercise Name can not be longer than " + maxStringLength + " characters!");
    }
    exerciseName = exerciseName.trim();
    if ((exerciseName.length() <= 0) || (exerciseName.equals(""))) {
      throw new IllegalArgumentException("Exercise Name can not be blank!");
    }

  /**
  * Checks if repGoal is valid, valid is more than 0.
  *
  * @param repGoal Number of repetitions to be performed
  */
  private void validateRepGoal(int repGoal) {
    if (repGoal <= 0) {
      throw new IllegalArgumentException("Rep Goal must be more than 0!");
    }
    if (String.valueOf(repGoal).length() >= maxIntLength) {
      throw new IllegalArgumentException("Rep Goal can not be longer than " + maxIntLength + " characters!");
    }
  }

  /**
  * Checks if weight is valid, valid is more than 0.
  *
  * @param weight Weight to be used for the exercise
  */
  private void validateWeight(double weight) {
    if (weight <= 0) {
      throw new IllegalArgumentException("Working Weight must be more than 0!");
    }
    if (String.valueOf(weight).length() >= maxDoubleLength) {
      throw new IllegalArgumentException("Working Weight can not be longer than " + maxDoubleLength + " characters!");
    }
  }

  /**
  * Checks if sets is valid, valid is more than 0.
  *
  * @param sets Number of sets to be performed
  */
  private void validateSets(int sets) {
    if (sets <= 0) {
      throw new IllegalArgumentException("Sets must be more than 0!");
    }
    if (String.valueOf(sets).length() >= maxIntLength) {
      throw new IllegalArgumentException("Sets can not be longer than " + maxIntLength + " characters!");
    }
  }

  private void validateRepsPerSet(int repsPerSet) {
    if (repsPerSet <= 0) {
      throw new IllegalArgumentException("Reps Per Set must be more than 0!");
    }
    if (String.valueOf(repsPerSet).length() >= maxIntLength) {
      throw new IllegalArgumentException("Reps Per Set can not be longer than " + maxIntLength + " characters!");
    }
  }

  /**
  * Checks if restTime is valid, valid is more than 0.
  *
  * @param restTime How many seconds of rest between each set
  */
  private void validateRestTime(int restTime) {
    if (restTime <= 0) {
      throw new IllegalArgumentException("Rest Time must be more than 0!");
    }
    if (String.valueOf(restTime).length() >= maxIntLength) {
      throw new IllegalArgumentException("Rest Time can not be longer than " + maxIntLength + " characters!");
    }
  }
  
  public void setExerciseName(String exerciseName) {
    validateExerciseName(exerciseName);
    this.exerciseName = exerciseName;
  }

  public String getExerciseName() {
    return this.exerciseName;
  }

  public void setRepGoal(int repGoal) {
    validateRepGoal(repGoal);
    this.repGoal = repGoal;
  }

  public int getRepGoal() {
    return this.repGoal;
  }

  public void setWeight(double weight) {
    validateWeight(weight);
    this.weight = weight;
  }

  public double getWeight() {
    return this.weight;
  }

  public void setSets(int sets) {
    validateSets(sets);
    this.sets = sets;
  }

  public int getSets() {
    return this.sets;
  }

  public int getRepsPerSet() {
    return repsPerSet;
  }

  public void setRepsPerSet(int repsPerSet) {
    validateRepsPerSet(repsPerSet);
    this.repsPerSet = repsPerSet;
  }

  public void setRestTime(int restTime) {
    validateRestTime(restTime);
    this.restTime = restTime;
  }

  public int getRestTime() {
    return restTime;
  }

  /**
  * Method to view exercise in a simple way.
  *
  * @return returns a string in given format with fields separated by ","
  */
  @Override
  public String toString() {
    return exerciseName + "," + repGoal + "," + weight + "," + sets + "," + restTime;
  }
}