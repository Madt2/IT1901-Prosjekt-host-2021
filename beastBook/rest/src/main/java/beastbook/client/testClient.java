package beastbook.client;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;

public class testClient {
  ClientService clientService = new ClientService();

  public void createUser() {
    User user = new User("testing", "password");
    clientService.createUser(user);
  }

  public void addWorkout(String workoutname, String username) {
    Workout workout = new Workout(workoutname);
    clientService.addWorkout(workout, username);
  }

  public void addExercise(String exercisename, String workoutID, String username) {
    Exercise exercise = new Exercise();
    exercise.setExerciseName(exercisename);
    exercise.setWeight(1);
    exercise.setRestTime(2);
    exercise.setRepGoal(2);
    exercise.setSets(3);
    Workout workout = clientService.queryWorkout(workoutID, username);
    clientService.addExercise(exercise, workout, username);
  }

  public String getUser(String username) {
    User user = clientService.queryUser(username);
    return "Username: " + user.getUsername() + " / Password: " + user.getPassword() + " / IDs: " + user.getWorkoutIDs();
  }

  public String getWorkout(String username, String workoutID) {
    Workout workout = clientService.queryWorkout(workoutID, username);
    return "Name: " + workout.getName() + " / ID:" + workout.getID() + " / exerciseIDs: " + workout.getExerciseIDs();
  }

  public String getExercise(String username, String exerciseID) {
    Exercise exercise = clientService.queryExercise(exerciseID, username);
    return "Name: " + exercise.getExerciseName() + " / ID: " + exercise.getID() + " / workoutID: " + exercise.getWorkoutID();
  }

  public void removeExercise(String username, String exerciseID) {
    Exercise exercise = clientService.queryExercise(exerciseID, username);
    clientService.deleteExercise(exercise, username);
  }

  public void removeWorkout(String username, String workoutID) {
    Workout workout = clientService.queryWorkout(workoutID, username);
    clientService.deleteWorkout( workout, username);
  }

  public static void main(String[] args) {
    testClient testClient = new testClient();

    //testClient.createUser();
    //testClient.addWorkout("testw", "testing");
    //testClient.addExercise("teste", "JF", "testing");
    //System.out.println(testClient.getUser("testing"));
    //System.out.println(testClient.getWorkout("testing", "JF"));
    //System.out.println(testClient.getExercise("Testing", "s0"));
    testClient.removeExercise("testing", "s0");
    //testClient.removeWorkout("testing", "JF");
  }

}
