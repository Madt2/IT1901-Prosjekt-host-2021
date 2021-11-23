package beastbook.client;

import beastbook.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Controller to handle ClientRequests to the REST server.
 */
public class ClientController {
  private ClientService clientService = new ClientService();
  private final User user;
  private boolean isDeleted = false;
  private Map<String, String> exerciseMap;
  private Map<String, String> workoutMap;
  private Map<String, String> historyMap;

  /**
   * Constructor for ClientController.
   *
   * @param username The username for the corresponding User to log in.
   * @param password The password for the corresponding User to log in
   * @throws Exceptions.UserNotFoundException if the User the client is trying to log in does not exist.
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws Exceptions.PasswordIncorrectException if the password given by the client
   *     is not correct for the requested User.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public ClientController(String username, String password) throws Exceptions.UserNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.PasswordIncorrectException,
      JsonProcessingException {
    user = new User(username, password);
    clientService.login(user);
    exerciseMap = clientService.queryExerciseMap(user);
    workoutMap = clientService.queryWorkoutMap(user);
    historyMap = clientService.queryHistoryMap(user);
  }

  public static void main(String[] args) throws Exceptions.BadPackageException, Exceptions.UserAlreadyExistException, Exceptions.ServerException, URISyntaxException, JsonProcessingException, Exceptions.UserNotFoundException, Exceptions.PasswordIncorrectException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException, Exceptions.ExerciseAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryAlreadyExistsException, Exceptions.HistoryNotFoundException {
    User user = new User("Temp", "Temp");
    RegisterController reg = new RegisterController();
    reg.registerUser(user.getUsername(), user.getPassword());
    ClientController clientController = new ClientController(user.getUsername(), user.getPassword());
    Workout workout = new Workout("workout");
    clientController.addWorkout(workout , List.of());
    Map<String,String> wmap = clientController.getWorkoutMap();
    String wkey = "";
    for (String keys : wmap.keySet()) {
      wkey = keys;
    }
    clientController.getWorkout(wkey);
    Exercise exercise = new Exercise("ovelse", 1,1, 1, 1, 1);
    clientController.addExercise(exercise, wkey);
    String ekey = "";
    Map<String,String> emap = clientController.getExerciseMap();
    for (String keys : emap.keySet()) {
      ekey = keys;
    }
    System.out.println(ekey);
    exercise = clientController.getExercise(ekey);
    exercise.setRepsPerSet(10);
    clientController.updateExercise(exercise);
    System.out.println(clientController.getExercise(ekey));
    clientController.addHistory(new History("workout", List.of()));
    String hkey = "";
    Map<String,String> hmap = clientController.getHistoryMap();
    for (String keys : hmap.keySet()) {
      hkey = keys;
    }
    clientController.getHistory(hkey);
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

  public Exercise getExercise(String exerciseId) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.ExerciseNotFoundException,
      Exceptions.IllegalIdException,
      JsonProcessingException {
    deletionCheck();
    return clientService.queryExercise(exerciseId, user);
  }

  public Workout getWorkout(String workoutId) throws Exceptions.WorkoutNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.IllegalIdException,
      JsonProcessingException {
    deletionCheck();
    return clientService.queryWorkout(workoutId, user);
  }

  public History getHistory(String historyId) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.HistoryNotFoundException,
      Exceptions.IllegalIdException,
      JsonProcessingException {
    deletionCheck();
    return clientService.queryHistory(historyId, user);
  }

  /**
   * Sends a request to add the given Exercise object to the Workout belonging to the workoutId.
   *
   * @param exercise The Exercise to be added
   * @param workoutId The corresponding Workout's id
   * @throws Exceptions.WorkoutNotFoundException if the Workout does not exist
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws Exceptions.ExerciseAlreadyExistsException if the Exercise already exists in the REST server
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void addExercise(Exercise exercise, String workoutId) throws Exceptions.WorkoutNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.ExerciseAlreadyExistsException,
      JsonProcessingException, Exceptions.IllegalIdException {
    deletionCheck();
    clientService.addExercise(user, workoutId, exercise);
    workoutMap = clientService.queryWorkoutMap(user);
    exerciseMap = clientService.queryExerciseMap(user);
  }

  /**
   * Sends a request to add the given Workout object to the logged-in User.
   *
   * @param workout the Workout to be added.
   * @param exercises the exercises that belong to that workout.
   * @return true if the workout is saved successfully to the server, false if something fails.
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws Exceptions.WorkoutAlreadyExistsException if the Workout already exists in the REST server
   * @throws Exceptions.WorkoutNotFoundException if an error occurs in saving the workout and then tries to add exercises
   * @throws Exceptions.ExerciseAlreadyExistsException if one of the Exercise-objects already exists in the REST server
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void addWorkout(Workout workout, List<Exercise> exercises) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.WorkoutAlreadyExistsException,
      Exceptions.ExerciseAlreadyExistsException,
      JsonProcessingException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException {
    deletionCheck();
    String workoutId = clientService.addWorkout(workout, user);
    System.out.println("Dette er workoutId den får av html package: " + workoutId);
    for (Exercise e : exercises) {
      addExercise(e, workoutId);
    }
    workoutMap = clientService.queryWorkoutMap(user);
    exerciseMap = clientService.queryExerciseMap(user);
  }

  /**
   * Sends a request to add the given History object to the logged-in User.
   *
   * @param history the History to be added
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws Exceptions.HistoryAlreadyExistsException if the History already exists in the server
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void addHistory(History history) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      Exceptions.HistoryAlreadyExistsException,
      JsonProcessingException {
    deletionCheck();
    clientService.addHistory(history, user);
    historyMap = clientService.queryHistoryMap(user);
  }

  /**
   * Sends a request to overwrite the corresponding Exercise in the REST server with the given Exercise.
   *
   * @param exercise the Exercise to overwrite
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   * @throws Exceptions.IllegalIdException if the Exercise object has an illegal ID
   * @throws Exceptions.ExerciseNotFoundException if the Exercise object does not exist in the REST server
   */
  public void updateExercise(Exercise exercise) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException,
      Exceptions.ExerciseNotFoundException {
    deletionCheck();
    clientService.updateExercise(exercise, user);
  }

  /**
   * Sends a request to overwrite the corresponding Workout in the REST server with the given Workout.
   *
   * @param workout the Workout to overwrite
   * @throws Exceptions.WorkoutNotFoundException if the Workout object does not exist in the REST server
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws Exceptions.IllegalIdException if the Workout object has an illegal ID
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void updateWorkout(Workout workout) throws Exceptions.WorkoutNotFoundException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException,
      URISyntaxException,
      JsonProcessingException {
    deletionCheck();
    clientService.updateWorkout(workout, user);
  }

  /**
   * Sends a request to remove the Exercise object with the corresponding ID in the REST server
   *
   * @param exerciseId for the Exercise object in the REST server
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   * @throws Exceptions.IllegalIdException if the Exercise object has an illegal ID
   */
  public void removeExercise(String exerciseId) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    deletionCheck();
    clientService.deleteExercise(exerciseId, user);
    exerciseMap = clientService.queryExerciseMap(user);
    workoutMap = clientService.queryWorkoutMap(user);
  }

  /**
   * Sends a request to remove the Workout object with the corresponding ID in the REST server
   *
   * @param workoutId for the Workout object in the REST server
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   * @throws Exceptions.IllegalIdException if the Workout object has an illegal ID
   */
  public void removeWorkout(String workoutId) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    deletionCheck();
    clientService.deleteWorkout(workoutId, user);
    workoutMap = clientService.queryWorkoutMap(user);
    exerciseMap = clientService.queryExerciseMap(user);
  }

  /**
   * Sends a request to remove the History object with the corresponding ID in the REST server
   *
   * @param historyId
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   * @throws Exceptions.IllegalIdException if the History object has an illegal ID
   */
  public void removeHistory(String historyId) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    deletionCheck();
    clientService.deleteHistory(historyId, user);
    historyMap = clientService.queryHistoryMap(user);
  }

  /**
   * Sends a request to delete the User and all data belong to the User.
   *
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void deleteUser() throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException {
    deletionCheck();
    clientService.deleteUser(user);
    isDeleted = true;
  }
}
