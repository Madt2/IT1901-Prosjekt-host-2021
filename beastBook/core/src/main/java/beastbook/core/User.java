package beastbook.core;

import java.util.ArrayList;
import java.util.List;

/**
 * User class for application. Creates a user with username, password and a list of workouts.
 */

public class User {
  private static final int MIN_CHAR_USERNAME = 3;
  private static final int MIN_CHAR_PASSWORD = 3;
  private String username;
  private String password;
  private List<Workout> workouts = new ArrayList<Workout>();
  //private List<String> myHistory = new ArrayList<String>(); for later release

  /**
  * User object for application.
  *
  * @param username username for user.
  * @param password password for user.
  */
  public User(String username, String password) {
    setUserName(username);
    setPassword(password);
  }

  public User() {}

  /**
  * Sets username to input argument. Validation for username length, has to be 3 or more characters.
  *
  * @param username the username to set
  */
  public void setUserName(String username) {
    if (username.length() >= MIN_CHAR_USERNAME) {
      this.username = username;
    } else {
      throw new IllegalArgumentException(
        "Username must be " + MIN_CHAR_USERNAME + " or more characters!"
      );
    }
  }

  public String getUserName() {
    return username;
  }

  /**
  * Sets password to input argument. Validation for password length, has to be 3 or more characters.
  *
  * @param password the password to set
  */
  public void setPassword(String password) {
    if (password.length() >= MIN_CHAR_PASSWORD) {
      this.password = password;
    } else {
      throw new IllegalArgumentException(
        "Password must be " + MIN_CHAR_PASSWORD + " or more characters!"
      );
    }
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
   * This method adds a workout object to users workouts List.
   *
   * @param workout workout to add
   */
  public void addWorkout(Workout workout) {
    for (Workout w : workouts) {
      if (w.getName().equals(workout.getName())) {
        throw new IllegalArgumentException(
          "User already has workout " + workout.getName() 
          + " saved! Workout was not created, please choose another name."
        );
      }
    }
    workouts.add(workout);
  }

  /**
  * Removes workout object from users workouts List.
  *
  * @param workout workout to remove
  */
  public void removeWorkout(Workout workout) {
    if (!workouts.contains(workout)) {
      throw new IllegalArgumentException("User does not have workout " + workout + " saved!");
    }  
    workouts.remove(workout);
  }

  /**
   * Getter to fetch spesific workout.
   *
   * @param workoutName name of workout to fetch.
   * @return workout if it exists, if not it returns null.
   */
  public Workout getWorkout(String workoutName) {
    for (Workout w : workouts) {
      if (w.getName().equals(workoutName)) {
        return w;
      }
    }
    return null;
  }

  /**
   * Getter to fetch workouts List from user.
   *
   * @return copy of workouts List
   */
  public List<Workout> getWorkouts() {
    return new ArrayList<>(workouts);
  }
}
