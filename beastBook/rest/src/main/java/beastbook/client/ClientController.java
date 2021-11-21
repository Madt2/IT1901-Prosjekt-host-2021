package beastbook.client;

import beastbook.core.*;

import java.net.URISyntaxException;
import java.util.*;

public class ClientController {
  private ClientService clientService = new ClientService();
  private final User user;
  private boolean isDeleted = false;
  private Map<String, String> exerciseMap;
  private Map<String, String> workoutMap;
  private Map<String, String> historyMap;

  public ClientController(String username, String password) throws Exceptions.UserNotFoundException, Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.PasswordIncorrectException {
    user = new User(username, password);
    clientService.login(user);
    exerciseMap = clientService.queryExerciseMap(user);
    workoutMap = clientService.queryWorkoutMap(user);
    historyMap = clientService.queryHistoryMap(user);
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

  public Exercise getExercise(String exerciseID) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.ExerciseNotFoundException {
    deletionCheck();
    return clientService.queryExercise(exerciseID, user);
  }

  public Workout getWorkout(String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    return clientService.queryWorkout(workoutId, user);
  }

  public History getHistory(String historyId) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.HistoryNotFoundException {
    deletionCheck();
    return clientService.queryHistory(historyId, user);
  }

  public void addExercise(Exercise exercise, String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.ExerciseAlreadyExistsException {
    deletionCheck();
    clientService.addExercise(user, workoutId, exercise);
    workoutMap = clientService.queryWorkoutMap(user);
  }

  public boolean addWorkout(Workout workout, List<Exercise> exercises) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.ExerciseAlreadyExistsException {
    deletionCheck();
    String name = workout.getName();
    if (workoutMap.containsValue(name)) {
      return false;
    }
    String workoutId = clientService.addWorkout(workout, user);
    System.out.println("Dette er workoutId den får av html package: " + workoutId);
    for (Exercise e : exercises) {
      addExercise(e, workoutId);
    }
    workoutMap = clientService.queryWorkoutMap(user);
    exerciseMap = clientService.queryExerciseMap(user);
    return true;

    //TODO catch return false
  }

  public void updateExercise(Exercise exercise) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    clientService.updateExercise(exercise, user);
  }

  public void addHistory(History history) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, Exceptions.HistoryAlreadyExistsException {
    deletionCheck();
    clientService.addHistory(history, user);
    historyMap = clientService.queryHistoryMap(user);
  }

  public void removeExercise(String exerciseId) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    clientService.deleteExercise(exerciseId, user);
    exerciseMap = clientService.queryExerciseMap(user);
    workoutMap = clientService.queryWorkoutMap(user);
  }

  public void removeWorkout(String workoutId) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    clientService.deleteWorkout(workoutId, user);
    workoutMap = clientService.queryWorkoutMap(user);
  }

  public void removeHistory(String historyId) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    clientService.deleteHistory(historyId, user);
    historyMap = clientService.queryHistoryMap(user);
  }

  public void deleteUser() throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    deletionCheck();
    clientService.deleteUser(user);
    isDeleted = true;
  }
}
