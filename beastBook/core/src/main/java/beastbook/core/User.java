package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * User class for application. Creates a user with username, password and a list of workouts.
 */
public class User {
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
   * @param username username to validate.
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
   * @param password password to check.
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
   * Checks if user already has Workout.
   *
   * @param id The WorkoutID to be checked
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

  /**
   * Sets username to input argument with validation.
   *
   * @param username the username to set
   */
  public void setUsername(String username) throws IllegalArgumentException {
    validateUsername(username);
    this.username = username;
  }

  /**
   * Sets password to input argument with validation.
   *
   * @param password the password to set.
   */
  public void setPassword(String password) throws IllegalArgumentException {
    validatePassword(password);
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Getter for password.
   *
   * @return users password
   */
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
   * This method adds a workout object to users workouts List.
   *
   * @param id workoutID to add to user.
   */
  public void addWorkout(String id) throws IllegalArgumentException {
    if (hasWorkout(id)) {
      throw new IllegalArgumentException(
              "User already has workout saved! Workout was not created, " +
              "please choose another name."
      );
    }
    workoutIDs.add(id);
  }

  /**
  * Removes workout object from users workouts List.
  *
   * @throws IllegalArgumentException when workout is not in users list.
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
