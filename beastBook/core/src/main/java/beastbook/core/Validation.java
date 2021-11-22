package beastbook.core;

import static beastbook.core.Id.*;
import static beastbook.core.Properties.*;
import static beastbook.core.Properties.maxStringLength;

public class Validation {

  /**
   * Validation method for setUsername. Checks for username length, has to be 3 or more characters.
   *
   * @param username to validate.
   * @throws IllegalArgumentException if username is too short.
   */
  public static void validateUsername(String username) throws IllegalArgumentException {
    boolean isLongEnough = username.length() >= MIN_CHAR_USERNAME;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
        "Username must be " + MIN_CHAR_USERNAME + " or more characters!"
      );
    }
  }

  /**
   * Validation method for Password. Checks for password length.
   *
   * @param password to validate.
   */
  public static void validatePassword(String password) throws IllegalArgumentException {
    boolean isLongEnough = password.length() >= MIN_CHAR_PASSWORD;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
        "Password must be " + MIN_CHAR_PASSWORD + " or more characters!"
      );
    }
  }

  /**
   * Checks if name is valid, valid is not blank.
   *
   * @param name Name of the exercise
   */
  public static void validateExerciseName(String name) throws IllegalArgumentException {
    boolean isTooLong = name.length() >= maxStringLength;
    if (isTooLong) {
      throw new IllegalArgumentException(
        "Exercise Name can not be longer than " + maxStringLength + " characters!"
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
  public static void validateRepGoal(int repGoal) throws IllegalArgumentException {
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
  public static void validateWeight(double weight) throws IllegalArgumentException {
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
  public static void validateSets(int sets) throws IllegalArgumentException {
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

  /**
   * Checks if repsPerSets is valid, valid is more than 0.
   *
   * @param repsPerSet value to check.
   * @throws IllegalArgumentException when validation fails.
   */
  public static void validateRepsPerSet(int repsPerSet) throws IllegalArgumentException {
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
  public static void validateRestTime(int restTime) throws IllegalArgumentException {
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

  public static void validateId(String id, Class cls) throws Exceptions.IllegalIdException {
    setLegals(cls);
    if (id.length() != legalLength) {
      throw new Exceptions.IllegalIdException(id, cls);
    }
    for (int i = 0; i < legalChars.length(); i++) {
      for (int j = 0; j < id.length(); j++) {
        if (id.charAt(j) == legalChars.charAt(i)) {
          id = id.replace(id.substring(j, j + 1), "");
        }
        if (id.length() == 0) {
          return;
        }
      }
    }
    throw new Exceptions.IllegalIdException(id, cls);
  }
}
