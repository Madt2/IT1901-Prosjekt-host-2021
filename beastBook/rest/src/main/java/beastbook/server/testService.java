package beastbook.server;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;

public class testService {

  ServerService serverService = new ServerService();

  /*public void createUser() {
    User user = new User("testuser", "password");
    serverService.createUser(user);
  }

  public void addWorkout(String workoutname, String username) {
    Workout workout = new Workout(workoutname);
    serverService.addWorkout(workout, username);
  }

  public void addExercise(String exercisename, String username) {
    Exercise exercise = new Exercise();
    exercise.setExerciseName(exercisename);
    exercise.setWeight(1);
    exercise.setRestTime(2);
    exercise.setRepGoal(2);
    exercise.setSets(3);
    serverService.addExercise(exercise, "JG", username);
  }

  public String getUser(String username) {
    User user = serverService.getUser(username);
    return "Username: " + user.getUsername() + " / Password: " + user.getPassword() + " / IDs: " + user.getWorkoutIDs();
  }

  public String getWorkout(String username) {
    Workout workout = serverService.getWorkout("JG", username);
    return "Name: " + workout.getName() + " / ID:" + workout.getID() + " / exerciseIDs: " + workout.getExerciseIDs();
  }

  public String getExercise(String username) {
    Exercise exercise = serverService.getExercise("5a", username);
    return "Name: " + exercise.getExerciseName() + " / ID: " + exercise.getID() + " / workoutID: " + exercise.getWorkoutID();
  }

  public void removeExercise(String username) {
    serverService.removeExercise( "5a", username);
  }

  public void removeWorkout(String username) {
    serverService.removeWorkout( "MK", username);
  }

  public void removeUser(String username) {
    serverService.deleteUser(username);
  }


  public static void main(String[] args) {

    testService testService = new testService();

    //testService.createUser();
    //testService.addWorkout("testw", "testuser");
    //testService.addExercise("teste", "testuser");
    //System.out.println(testService.getUser("testuser"));
    //System.out.println(testService.getWorkout("testuser"));
    //System.out.println(testService.getExercise("testuser"));
    //testService.removeExercise("testuser");
    //testService.removeWorkout("testuser");
    //testService.removeUser("testuser");
    System.out.println("Done");
  }*/
}
