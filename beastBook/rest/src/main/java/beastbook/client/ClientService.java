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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientService {
  private String ipAddress = "localhost";
  private String baseURL = "http://" + ipAddress + ":8080/";
  private ObjectMapper mapper;

  private String objectToJson(Object object) throws JsonProcessingException {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }

  private Object jsonToObject(String jsonString, Class cls) throws Exceptions.BadPackageException {
    try {
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException();
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
      throw new Exceptions.ServerException();
    }
  }

  private void badPackageExceptionHandler(String errorMessage) throws Exceptions.BadPackageException {
    if (errorMessage.equals(Exceptions.BadPackageException.class.getSimpleName())) {
      throw new Exceptions.BadPackageException();
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

  private void illegalIdException(String errorMessage, String id, Class cls) throws Exceptions.IllegalIdException {
    if (errorMessage.equals(Exceptions.HistoryAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.IllegalIdException(id, cls);
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

  public void createUser(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.UserAlreadyExistException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "createUser/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      userAlreadyExistsException(message, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void login(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.PasswordIncorrectException, Exceptions.UserNotFoundException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "login/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      passwordIncorrectException(message);
      userNotFoundExceptionHandler(message, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void addExercise(User user, String workoutId, Exercise exercise) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.WorkoutNotFoundException, JsonProcessingException {
    String userString = objectToJson(user);
    String exerciseString = objectToJson(exercise);
    String url = baseURL + "addExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + workoutId + "/" + URLEncoder.encode(exerciseString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      exerciseAlreadyExistsExceptionHandler(message, exercise);
      workoutNotFoundExceptionHandler(message, workoutId);
      exerciseAlreadyExistsExceptionHandler(message, exercise);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public String addWorkout(Workout workout, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.WorkoutAlreadyExistsException, JsonProcessingException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseURL + "addWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + URLEncoder.encode(workoutString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      workoutAlreadyExistsExceptionHandler(message, workout);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  return message;
  }

  public void addHistory(History history, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException, JsonProcessingException {
    String historyString = objectToJson(history);
    String userString = objectToJson(user);
    String url = baseURL + "addHistory/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + URLEncoder.encode(historyString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      historyAlreadyExistsExceptionHandler(message, history);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void updateExercise(Exercise exercise, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException, Exceptions.IllegalIdException, Exceptions.ExerciseNotFoundException {
    String exerciseString = objectToJson(exercise);
    String userString = objectToJson(user);
    String url = baseURL + "updateExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + URLEncoder.encode(exerciseString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      illegalIdException(message, exercise.getId(), Exercise.class);
      exerciseNotFoundExceptionHandler(exerciseString, exercise.getId());
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void updateWorkout(Workout workout, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseURL + "updateWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + URLEncoder.encode(workoutString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      workoutNotFoundExceptionHandler(message, workout.getId());
      illegalIdException(message, workout.getId(), Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteExercise(String exerciseId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + exerciseId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      illegalIdException(message, exerciseId, Exercise.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteWorkout(String workoutId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + workoutId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      illegalIdException(message, workoutId, Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteHistory(String historyId, User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteHistory/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + historyId;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      illegalIdException(message, historyId, History.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public void deleteUser(User user) throws URISyntaxException, Exceptions.BadPackageException, Exceptions.ServerException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "deleteUser/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  public Workout queryWorkout(String workoutID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.WorkoutNotFoundException, URISyntaxException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "getWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + workoutID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      workoutNotFoundExceptionHandler(message, workoutID);
      illegalIdException(message, workoutID, Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (Workout) jsonToObject(message, Workout.class);
  }

  public Exercise queryExercise(String exerciseID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.ExerciseNotFoundException, URISyntaxException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "getExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + exerciseID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      exerciseNotFoundExceptionHandler(message, exerciseID);
      illegalIdException(message, exerciseID, Exercise.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (Exercise) jsonToObject(message, Exercise.class);
  }

  public History queryHistory(String historyID, User user) throws Exceptions.BadPackageException, Exceptions.ServerException, Exceptions.HistoryNotFoundException, URISyntaxException, JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseURL + "getHistory/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + historyID;
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      historyNotFoundExceptionHandler(message, historyID);
      illegalIdException(message, historyID, History.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (History) jsonToObject(message, History.class);
  }

  public Map<String, String> queryExerciseMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "getExerciseMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }

  public Map<String, String> queryWorkoutMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "getWorkoutMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }

  public Map<String, String> queryHistoryMap(User user) throws Exceptions.BadPackageException, Exceptions.ServerException, URISyntaxException, JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseURL + "/getHistoryMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    String message = sendPackage(new URI(url));
    if (message == null) {
      badPackageExceptionHandler(message);
      serverExceptionHandler(message);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
    return (LinkedHashMap<String, String>) jsonToObject(message, LinkedHashMap.class);
  }
}
