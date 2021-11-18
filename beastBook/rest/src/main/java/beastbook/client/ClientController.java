package beastbook.client;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;

import java.util.*;

public class ClientController {
  private ClientService clientService = new ClientService();
  private final String username;
  private boolean isDeleted = false;
  private Map<String, String> exerciseMap;
  private Map<String, String> workoutMap;
  private Map<String, String> historyMap;

  public ClientController(String username, String password) {
    if (!password.equals(clientService.queryPassword(username))) {
      throw new IllegalArgumentException("Password not correct");
    }
    this.username = username;
    System.out.println("dette er før map query");
    System.out.println(exerciseMap = clientService.queryExerciseMap(username));
    System.out.println(workoutMap = clientService.queryWorkoutMap(username));
    System.out.println(historyMap = clientService.queryHistoryMap(username));
    System.out.println("kom ut her");
  }

  public void setIpAddress(String ipAddress) {
    clientService.setIpAddress(ipAddress);
  }

  public Map<String, String> getExerciseMap() {
    return exerciseMap;
  }

  public Map<String, String> getWorkoutMap() {
    return workoutMap;
  }

  public Map<String, String> getHistoryMap() {
    return historyMap;
  }

  private void deletionCheck() {
    if (isDeleted) {
      throw new IllegalStateException("This user is deleted, you can no longer use this controller!");
    }
  }

  public Exercise getExercise(String exerciseID) {
    deletionCheck();
    return clientService.queryExercise(exerciseID, username);
  }

  public Workout getWorkout(String workoutId) {
    deletionCheck();
    return clientService.queryWorkout(workoutId, username);
  }

  public History getHistory(String historyId) {
    deletionCheck();
    return clientService.queryHistory(historyId, username);
  }

  public void addExercise(Exercise exercise, String workoutId) {
    deletionCheck();
    clientService.addExercise(exercise, workoutId, username);
    workoutMap = clientService.queryWorkoutMap(username);
  }

  public void addWorkout(Workout workout, List<Exercise> exercises) {
    deletionCheck();
    String name = workout.getName();
    if (workoutMap.containsValue(name)) {
      return false;
    }
    String workoutId = clientService.addWorkout(workout, username).getBody();
    System.out.println("Dette er workoutId den får av html package: " + workoutId);
    for (Exercise e : exercises) {
      addExercise(e, workoutId);
    }
    workoutMap = clientService.queryWorkoutMap(username);
    exerciseMap = clientService.queryExerciseMap(username);
  }

  public void updateExercise(Exercise exercise) {
    deletionCheck();
    clientService.updateExercise(exercise, username);
  }

  public void addHistory(History history) {
    deletionCheck();
    clientService.addHistory(history, username);
    historyMap = clientService.queryHistoryMap(username);
  }

  public void removeExercise(Exercise exercise, Workout workout) {
    deletionCheck();
    clientService.deleteExercise(exercise.getId(), username);
    exerciseMap = clientService.queryExerciseMap(username);
    workoutMap = clientService.queryWorkoutMap(username);
  }

  public void removeWorkout(String workoutId) {
    deletionCheck();
    clientService.deleteWorkout(workoutId, username);
    workoutMap = clientService.queryWorkoutMap(username);
  }

  public void removeHistory(String historyId) {
    deletionCheck();
    clientService.deleteHistory(historyId, username);
    historyMap = clientService.queryHistoryMap(username);
  }

  public void deleteUser() {
    deletionCheck();
    clientService.deleteUser(username);
    isDeleted = true;
  }

  public static void main(String[] args) {
    ClientController controller = new ClientController("test", "test");

    Workout workout = new Workout("test");
    Exercise exercise = new Exercise("Chestpress", 90, 35, 22, 33, 94);
    List<Exercise> exercises = new ArrayList<>();
   // String s = controller.addWorkout(workout, exercises);
    //controller.addExercise(exercise, "UC");
  }

}
