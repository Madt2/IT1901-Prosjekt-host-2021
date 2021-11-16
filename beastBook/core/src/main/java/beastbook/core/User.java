package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * User class for application. Creates a user with username, password and a list of references to workouts.
 */
public class User {
  //Todo enum?
  public static final int MIN_CHAR_USERNAME = 3;
  public static final int MIN_CHAR_PASSWORD = 3;

  private String username;
  private String password;
  private List<String> workoutIDs = new ArrayList<>();

  /**
  * User object for application.
  *
  * @param username username for user.
  * @param password password for user.
  */
  public User(String username, String password) {
    setUsername(username);
    setPassword(password);
  }

  public User() {}

  /**
   * Validation method for setUsername. Checks for username length, has to be 3 or more characters.
   *
   * @param username to validate.
   * @throws IllegalArgumentException when username is to short.
   */
  private void validateUsername(String username) throws IllegalArgumentException {
    boolean isLongEnough = username.length() >= MIN_CHAR_USERNAME;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Username must be " + MIN_CHAR_USERNAME + " or more characters!"
      );
    }
  }

  /**
   * Validation method for Password. Checks for password length, has to be 3 or more characters.
   *
   * @param password to validate.
   */
  private void validatePassword(String password) throws IllegalArgumentException {
    boolean isLongEnough = password.length() >= MIN_CHAR_PASSWORD;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Password must be " + MIN_CHAR_PASSWORD + " or more characters!"
      );
    }
  }

  /**
   * Checks if ID given is valid as workoutID.
   *
   * @param id to be checked.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  private void validateWorkoutID(String id) throws IllegalArgumentException {
    if (id.length() != 2) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
    }
    final String legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (!(legalChars.contains(String.valueOf(id.charAt(0)))) &&
            legalChars.contains(String.valueOf(id.charAt(1)))) {
      throw new IllegalArgumentException("ID does not use correct characters!");
    }
  }

  /**
   * Checks if user already has Workout.
   *
   * @param id The WorkoutID to be checked.
   * @return true if user has workout, false otherwise.
   */
  private boolean hasWorkout(String id) {
    for (String ID : workoutIDs) {
      if (ID.equals(id)) {
        return true;
      }
    }
    return false;
  }

  public void setUsername(String username) throws IllegalArgumentException {
    validateUsername(username);
    this.username = username;
  }

  public void setPassword(String password) throws IllegalArgumentException {
    validatePassword(password);
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Getter to fetch workoutIDs List from user.
   *
   * @return List of IDs
   */
  public List<String> getWorkoutIDs() {
    return new ArrayList<>(workoutIDs);
  }

  /**
   * This method adds a workoutID reference to workoutIDs list.
   *
   * @param id workoutID to add to user.
   * @throws IllegalArgumentException when workoutID is already a reference in workoutIDs list,
   *                                  or if validation fails.
   */
  public void addWorkout(String id) throws IllegalArgumentException {
    if (hasWorkout(id)) {
      throw new IllegalArgumentException(
              "User already has workout saved! Workout was not created, " +
              "please choose another name."
      );
    }
    validateWorkoutID(id);
    workoutIDs.add(id);
  }

  /**
  * Removes workout object from users workouts List.
  *
  * @throws IllegalArgumentException when workout reference is not in user's workoutIDs list.
  * @param id workoutID to remove from User.
  */
  public void removeWorkout(String id) throws IllegalArgumentException {
    for (String ID : workoutIDs) {
      if (ID.equals(id)) {
        workoutIDs.remove(id);
        return;
      }
    }
    throw new IllegalArgumentException("User does not have workout saved!");
  }
}
