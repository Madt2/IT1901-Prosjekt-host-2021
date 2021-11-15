package beastbook.core;

import beastbook.json.BeastBookPersistence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User class for application. Creates a user with username, password and a list of workouts.
 */
public class User {
  private String username;
  private String password;
  //private BeastBookPersistence persistence = new BeastBookPersistence();
  private List<String> workoutIDs = new ArrayList<>();
  //private List<Workout> workouts = new ArrayList<>();

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
  private void validateUsername(String username) {
    final int MIN_CHAR_USERNAME = 3;
    boolean isLongEnough = username.length() >= MIN_CHAR_USERNAME;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Username must be " + MIN_CHAR_USERNAME + " or more characters!"
      );
    }
  }

  /**
  * Sets username to input argument with validation.
  *
  * @param username the username to set
  */
  public void setUsername(String username) {
    validateUsername(username);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Validation method for Password. Checks for password length, has to be 3 or more characters.
   *
   * @param password password to check.
   */
  private void validatePassword(String password) {
    final int MIN_CHAR_PASSWORD = 3;
    boolean isLongEnough = password.length() >= MIN_CHAR_PASSWORD;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Password must be " + MIN_CHAR_PASSWORD + " or more characters!"
      );
    }
  }

  /**
  * Sets password to input argument with validation.
  *
  * @param password the password to set.
  */
  public void setPassword(String password) {
    validatePassword(password);
    this.password = password;
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
   * This method adds a workout object to users workouts List.
   *
   * @param id workoutID to add to user.
   */
  public void addWorkout(String id) {
    if (hasWorkout(id)) {
      throw new IllegalArgumentException(
              "User already has workout saved! Workout was not created, " +
              "please choose another name."
      );
    }
    workoutIDs.add(id);
  }

  //Todo this will be implemented in json.
  /**
   * This method updates a Workout object by replacing with the new given Workout
   *
   * @param workout the Workout to be replaced
   */
  /*public void updateWorkout(Workout workout) {
    for (int i = 0; i < workouts.size(); i++) {
      if (workouts.get(i).getName().equals(workout.getName())) {
        workouts.set(i, workout);
        return;
      }
    }
    throw new IllegalArgumentException("No workout found to update!");
  }*/

  /**
  * Removes workout object from users workouts List.
  *
   * @throws IllegalArgumentException when workout is not in users list.
  * @param id workoutID to remove from User.
  */
  public void removeWorkout(String id) {
    for (String ID : workoutIDs) {
      if (ID.equals(id)) {
        workoutIDs.remove(id);
        return;
      }
    }
    throw new IllegalArgumentException("User does not have workout saved!");
  }

  /**
   * Getter to fetch specific workout.
   *
   * @param workoutName name of workout to fetch.
   * @return workout if it exists, if not it returns null.
   */
  //Todo might be able to replace workoutName param with workoutID.
  /*public Workout getWorkout(String workoutID) {
    for (String ID : workoutIDs) {
      if (ID.equals(workoutID)) {
        return null;
      }
    }
    return null;
  }*/



  /**
   * Getter to fetch workouts List from user.
   *
   * @return copy of workouts List
   */
  public List<String> getWorkoutIDs() {
    return new ArrayList<>(workoutIDs);
  }

  /**
   * Saves User object to file using persistance.
   *
   * @throws IOException when saveFilePath is wrong.
   */
  /*public void saveUser() throws IOException {
    persistence.setSaveFilePath(getUserName());
    persistence.saveUser(this);
  }*/

  /**
   * Loads User object from file using persistance.
   *
   * @param name name of user
   * @return return User object
   * @throws IOException when saveFilePath is wrong.
   */
  /*public User loadUser(String name) throws IOException {
    persistence.setSaveFilePath(name);
    return persistence.loadUser();
  }*/
}
