package beastbook.restserver;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {

  private User user;

  public String getUserString() {
    user = new User("Emil", "IkkeBraPassword");
    Workout workout = new Workout("Latskap");
    Exercise exercise = new Exercise("Sitte i ro", 1, 1.0, 1, 1, 1);
    workout.addExercise(exercise);
    user.addWorkout(workout);
    return "Username: " + user.getUserName() + "\nPassword: " + user.getPassword() + "\nWorkouts: " + user.getWorkouts();
  }

  public void saveUser() {
    user = new User("Emil", "Password");
    Workout workout = new Workout("Latskap");
    Exercise exercise = new Exercise("Sitte i ro", 1, 1.0, 1, 1, 1);
    workout.addExercise(exercise);
    user.addWorkout(workout);
    try {
      user.saveUser();
    } catch (IOException e) {
      System.err.println("Error");
    }
  }

  public String loadUser(String userName) {
    try {
      user = user.loadUser(userName);
      return "Username: " + user.getUserName() + "\nPassword: " + user.getPassword() + "\nWorkouts: " + user.getWorkouts();
    } catch (IOException e) {
      return "Error";
    }
  }
}
