package beastbook.client;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.Workout;
import java.util.HashMap;
import java.util.List;

public class ClientController {
  private ClientService clientService = new ClientService();
  private final String username;
  private boolean isDeleted = false;
  private HashMap<String, String> exerciseMap;
  private HashMap<String, String> workoutMap;
  private HashMap<String, String> historyMap;

  public ClientController(String username, String password) {
    if (clientService.queryPassword(username) == null) {
      throw new IllegalArgumentException("User does not exist!");
    }
    if (!password.equals(clientService.queryPassword(username))) {
      throw new IllegalArgumentException("Password not correct");
    }
    this.username = username;
    exerciseMap = clientService.queryExerciseMap(username);
    workoutMap = clientService.queryWorkoutMap(username);
    historyMap = clientService.queryHistoryMap(username);
  }

  public void setIpAddress(String ipAddress) {
    clientService.setIpAddress(ipAddress);
  }

  public HashMap<String, String> getExerciseMap() {
    return new HashMap<>(exerciseMap);
  }

  public HashMap<String, String> getWorkoutMap() {
    return new HashMap<>(workoutMap);
  }

  public HashMap<String, String> getHistoryMap() {
    return new HashMap<>(historyMap);
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

  public String addExercise(Exercise exercise, String workoutId) {
    deletionCheck();
    clientService.addExercise(exercise, workoutId, username);
    exerciseMap = clientService.queryExerciseMap(username);
    return "exerciseID";
  }

  public void addWorkout(Workout workout, List<Exercise> exercises) {
    deletionCheck();
    String workoutId = clientService.addWorkout(workout, username);
    for (Exercise e : exercises) {
      clientService.addExercise(e, workoutId, username);
    }
    workoutMap = clientService.queryWorkoutMap(username);
    exerciseMap = clientService.queryExerciseMap(username);
  }

  public String addHistory(History history) {
    deletionCheck();
    clientService.addHistory(history, username);
    historyMap = clientService.queryHistoryMap(username);
    return "historyID";
  }

  public void updateExercise(Exercise exercise) {
    deletionCheck();
    clientService.updateExercise(exercise, username);
  }

  public void updateWorkout(Workout workout) {
    deletionCheck();
    clientService.updateWorkout(workout, username);
  }

  public void removeExercise(String exerciseId) {
    deletionCheck();
    clientService.deleteExercise(exerciseId, username);
    exerciseMap = clientService.queryExerciseMap(username);
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
}
