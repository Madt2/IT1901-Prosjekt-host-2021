package beastbook.client;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientService {
  private String ipAddress = "localhost";
  private String baseURL = "http://" + ipAddress + ":8080/";
  private ObjectMapper mapper;

  private String objectToJson(Object object) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Object jsonToObject(String jsonString, Class cls) throws Exceptions.BadPackageException {
    try {
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException(e.getMessage());
    }
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    baseURL = "http://" + ipAddress + ":8080/";
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  private void serverExceptionHandler(String errorMessage) throws Exceptions.ServerException {
    if (errorMessage.equals(Exceptions.ServerException.class.getSimpleName())) {
      throw new Exceptions.ServerException(errorMessage);
    }
  }

  private void badPackageExceptionHandler(String errorMessage) throws Exceptions.BadPackageException {
    if (errorMessage.equals(Exceptions.BadPackageException.class.getSimpleName())) {
      throw new Exceptions.BadPackageException(errorMessage);
    }
  }

  private void passwordIncorrectException(String errorMessage) throws Exceptions.PasswordIncorrectException {
    if (errorMessage.equals(Exceptions.PasswordIncorrectException.class.getSimpleName())) {
      throw new Exceptions.PasswordIncorrectException();
    }
  }

  private void userNotFoundExceptionHandler(String errorMessage, User user) throws Exceptions.UserNotFoundException {
    if (errorMessage.equals(Exceptions.UserNotFoundException.class.getSimpleName())) {
      throw new Exceptions.UserNotFoundException(user.getUsername());
    }
  }

  private void exerciseNotFoundExceptionHandler(String errorMessage, String exerciseId) throws Exceptions.ExerciseNotFoundException {
    if (errorMessage.equals(Exceptions.ExerciseNotFoundException.class.getSimpleName())) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }

  private void workoutNotFoundExceptionHandler(String errorMessage, String workoutId) throws Exceptions.WorkoutNotFoundException {
    if (errorMessage.equals(Exceptions.WorkoutNotFoundException.class.getSimpleName())) {
      throw new Exceptions.WorkoutNotFoundException(workoutId);
    }
  }

  private void historyNotFoundExceptionHandler(String errorMessage, String historyId) throws Exceptions.HistoryNotFoundException {
    if (errorMessage.equals(Exceptions.HistoryNotFoundException.class.getSimpleName())) {
      throw new Exceptions.HistoryNotFoundException(historyId);
    }
  }

  private void userAlreadyExistsException(String errorMessage, User user) throws Exceptions.UserAlreadyExistException {
    if (errorMessage.equals(Exceptions.UserAlreadyExistException.class.getSimpleName())) {
      throw new Exceptions.UserAlreadyExistException(user.getUsername());
    }
  }

  private void exerciseAlreadyExistsExceptionHandler(String errorMessage, Exercise exercise) throws Exceptions.ExerciseAlreadyExistsException {
    if (errorMessage.equals(Exceptions.ExerciseNotFoundException.class.getSimpleName())) {
      throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
    }
  }

  private void workoutAlreadyExistsExceptionHandler(String errorMessage, Workout workout) throws Exceptions.WorkoutAlreadyExistsException {
    if (errorMessage.equals(Exceptions.WorkoutAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  private void historyAlreadyExistsExceptionHandler(String errorMessage, History history) throws Exceptions.HistoryAlreadyExistsException {
    if (errorMessage.equals(Exceptions.HistoryAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName() + ";" + history.getDate());
    }
  }

  private String sendPackage(URI uri) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> data = restTemplate.postForEntity(uri, httpEntity, String.class);
    if (data.getStatusCode().equals(HttpStatus.OK)) {
      return data.getBody() + "";
    }
    return null;
  }

  public void createUser(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.UserAlreadyExistException {
    String userString = objectToJson(user);
    String url = baseURL + "createUser/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      userAlreadyExistsException(message, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void login(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.PasswordIncorrectException, Exceptions.UserNotFoundException {
    String userString = objectToJson(user);
    String url = baseURL + "login/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      passwordIncorrectException(message);
      userNotFoundExceptionHandler(message, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void addExercise(User user, String workoutId, Exercise exercise) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutNotFoundException {
    String userString = objectToJson(user);
    String url = baseURL + "addExercise/" + userString + "/" + workoutId + "/" + exercise;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      exerciseAlreadyExistsExceptionHandler(message, exercise);
      workoutNotFoundExceptionHandler(message, workoutId);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public String addWorkout(Workout workout, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.WorkoutAlreadyExistsException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseURL + "addWorkout/" + userString + "/" + workoutString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      workoutAlreadyExistsExceptionHandler(message, workout);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  return message;
  }

  public void addHistory(History history, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException {
    String historyString = objectToJson(history);
    String userString = objectToJson(user);
    String url = baseURL + "addHistory/" + userString + "/" + historyString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      historyAlreadyExistsExceptionHandler(message, history);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void updateExercise(Exercise exercise, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String exerciseString = objectToJson(exercise);
    String userString = objectToJson(user);
    String url = baseURL + "updateExercise/" + userString + "/" + exerciseString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);

      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void updateWorkout(Workout workout, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseURL + "updateWorkout/" + userString + "/" + workoutString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteExercise(String exerciseId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteExercise/" + userString + "/" + exerciseId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteWorkout(String workoutId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteWorkout/" + userString + "/" + workoutId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteHistory(String historyId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteHistory/" + userString + "/" + historyId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteUser(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteUser/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public Workout queryWorkout(String workoutID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.WorkoutNotFoundException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "getWorkout/" + userString + "/" + workoutID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      workoutNotFoundExceptionHandler(message, workoutID);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (Workout) jsonToObject(message, Workout.class);
  }

  public Exercise queryExercise(String exerciseID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.ExerciseNotFoundException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "getExercise/" + userString + "/" + exerciseID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      exerciseNotFoundExceptionHandler(message, exerciseID);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (Exercise) jsonToObject(message, Exercise.class);
  }

  public History queryHistory(String historyID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryNotFoundException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "getHistory/" + userString + "/" + historyID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      historyNotFoundExceptionHandler(message, historyID);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (History) jsonToObject(message, History.class);
  }

  public Map<String, String> queryExerciseMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "getExerciseMap/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }

  public Map<String, String> queryWorkoutMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "getWorkoutMap/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }

  public Map<String, String> queryHistoryMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException {
    String userString = objectToJson(user);
    String url = baseURL + "/getHistoryMap/" + userString;
    String message = sendPackage(new URI(url));
    if (message == null) {
      //Exceptionhandling
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }
}
