package core;

import java.util.ArrayList;
import java.util.List;

public class User {
  private static final int MIN_CHAR_USERNAME = 3;
  private static final int MIN_CHAR_PASSWORD = 3;
  private String username;
  private String password;
  private List<Workout> workouts = new ArrayList<Workout>();
  //private List<String> myHistory = new ArrayList<String>(); for later release

  /**
  * User object for application.
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
  * @param username
  */
  public void setUserName(String username) {
    if (username.length() >= MIN_CHAR_USERNAME) {
      this.username = username;
    } else {
      throw new IllegalArgumentException("Username must be more than 2 characters");
    }
  }

  public String getUserName() {
    return username;
  }

  /**
  * Sets password to input argument. Validation for password length, has to be 3 or more characters.
  * @param password
  */
  public void setPassword(String password) {
    if (password.length() >= MIN_CHAR_PASSWORD) {
      this.password = password;
    } else {
      throw new IllegalArgumentException("Password must be more than 2 characters");
    }
  }
  
  public String getPassword() {
    return password;
  }

  public void addWorkout(Workout workout) {
    workouts.add(workout);
  }

  /**
  * Removes workout object from input argument.
  * @param workout
  */
  public void removeWorkout(Workout workout) {
    if (!workouts.contains(workout)) {
      throw new IllegalArgumentException("User does not have workout " + workout + " saved.");
    }  
    workouts.remove(workout);
  }

  public Workout getWorkout(String workoutName) {
    for (Workout w : workouts) {
      if (w.getName().equals(workoutName)) {
        return w;
      }
    }
    return null;
  }

  public List<Workout> getWorkouts() {
    return new ArrayList<>(workouts);
  }
}
