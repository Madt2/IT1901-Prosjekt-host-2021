package beastbook.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
  private List<String> historyIDs = new ArrayList<>();
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
   * Method to remove History object from histories list.
   *
   * @param id The id of the history to be removed.
   * @throws IllegalArgumentException if no History found.
   */
  public void removeHistory(String id) throws IllegalArgumentException {
    if (hasHistory(id)) {
      historyIDs.remove(id);
      return;
    }
    throw new IllegalArgumentException("No such history found!");
  }

  /**
   * Help method to get the current date without time.
   *
   * @return current date without timestamp.
   */
  public String getDate() {
    Date d = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    return sdf.format(d);
  }

  /**
   * Checks if user already has Workout.
   *
   * @param id The WorkoutID to be checked.
   * @return true if user has workout, false otherwise.
   */
  private boolean hasWorkout(String id) {
    return workoutIDs.contains(id);
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
              "User already has workout saved! Workout was not created, "
                    + "please choose another name."
      );
    }
    Id.validateID(id, Workout.class);
    workoutIDs.add(id);
  }

  /**
  * Removes workout object from users workouts List.
  *
  * @throws IllegalArgumentException when workout reference is not in user's workoutIDs list.
  * @param id workoutID to remove from User.
  */
  public void removeWorkout(String id) throws IllegalArgumentException {
    if (hasWorkout(id)) {
      workoutIDs.remove(id);
      return;
    }
    throw new IllegalArgumentException("User does not have workout saved!");
  }

  public List<String> getHistoryIDs() {
    return new ArrayList<>(historyIDs);
  }

  public void addHistory(String id) throws IllegalArgumentException {
    if (hasHistory(id)) {
      throw new IllegalArgumentException(
            "User already has workout saved! Workout was not created, " +
                  "please choose another name."
      );
    }
    Id.validateID(id, History.class);
    historyIDs.add(id);
  }

  private boolean hasHistory(String id) {
    return historyIDs.contains(id);
  }

}

