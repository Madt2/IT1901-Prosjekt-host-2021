package beastbook.client;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * Handles sending POST and GET request to REST server.
 */
public class ClientService {
  private String ipAddress = "localhost";
  private String baseUrl = "http://" + ipAddress + ":8080/";
  private ObjectMapper mapper = new ObjectMapper();
  private RestTemplate restTemplate = new RestTemplate();

  /**
   * Constructor for ClientService.
   */
  ClientService() {
    mapper.registerModule(new BeastBookModule());
  }

  /**
   * Serializes object to json string.
   *
   * @param object to serialize.
   * @return serialized object.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  private String objectToJson(Object object) throws JsonProcessingException {
    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    return jsonString;
  }

  /**
   * Deserialize json strong to object.
   *
   * @param jsonString to deserialize.
   * @param cls Class type to deserialize to.
   * @return deserialized object.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     deserialization.
   */
  private Object jsonToObject(String jsonString, Class<?> cls) throws Exceptions.BadPackageException {
    try {
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException();
    }
  }

  /**
   * Method to set the IP-address to get server data from.
   * Added for possible future implementation.
   *
   * @param ipAddress the IP-adress to get server data from.
   */
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    baseUrl = "http://" + ipAddress + ":8080/";
    mapper.registerModule(new BeastBookModule());
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.ServerException simple name, if true throw
   *     Exceptions.ServerException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @throws Exceptions.ServerException if errorMessage is equal to Exceptions.ServerException simple name.
   */
  private void serverExceptionHandler(String errorMessage) throws Exceptions.ServerException {
    if (errorMessage.equals(Exceptions.ServerException.class.getSimpleName())) {
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.BadPackageException simple name, if true throw
   *     Exceptions.BadPackageException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @throws Exceptions.BadPackageException if errorMessage is equal to
   *     Exceptions.BadPackageException simple name.
   */
  private void badPackageExceptionHandler(String errorMessage) throws Exceptions.BadPackageException {
    if (errorMessage.equals(Exceptions.BadPackageException.class.getSimpleName())) {
      throw new Exceptions.BadPackageException();
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.PasswordIncorrectException simple name, if true throw
   *     Exceptions.PasswordIncorrectException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @throws Exceptions.PasswordIncorrectException if errorMessage is equal to
   *     Exceptions.PasswordIncorrectException simple name.
   */
  private void passwordIncorrectExceptionHandler(String errorMessage)
      throws Exceptions.PasswordIncorrectException {
    if (errorMessage.equals(Exceptions.PasswordIncorrectException.class.getSimpleName())) {
      throw new Exceptions.PasswordIncorrectException();
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.UserNotFoundException simple name, if true throw
   *     Exceptions.UserNotFoundException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param user object that caused exception at server.
   * @throws Exceptions.UserNotFoundException if errorMessage is equal to
   *     Exceptions.UserNotFoundException simple name.
   */
  private void userNotFoundExceptionHandler(String errorMessage, User user)
      throws Exceptions.UserNotFoundException {
    if (errorMessage.equals(Exceptions.UserNotFoundException.class.getSimpleName())) {
      throw new Exceptions.UserNotFoundException(user.getUsername());
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.ExerciseNotFoundException simple name, if true throw
   *     Exceptions.ExerciseNotFoundException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param exerciseId that caused exception at server.
   * @throws Exceptions.ExerciseNotFoundException if errorMessage is equal to
   *     Exceptions.ExerciseNotFoundException simple name.
   */
  private void exerciseNotFoundExceptionHandler(String errorMessage, String exerciseId)
      throws Exceptions.ExerciseNotFoundException {
    if (errorMessage.equals(Exceptions.ExerciseNotFoundException.class.getSimpleName())) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.WorkoutNotFoundException simple name, if true throw
   *     Exceptions.WorkoutNotFoundException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param workoutId that caused exception at server.
   * @throws Exceptions.WorkoutNotFoundException if errorMessage is equal to
   *     Exceptions.WorkoutNotFoundException simple name.
   */
  private void workoutNotFoundExceptionHandler(String errorMessage, String workoutId)
      throws Exceptions.WorkoutNotFoundException {
    if (errorMessage.equals(Exceptions.WorkoutNotFoundException.class.getSimpleName())) {
      throw new Exceptions.WorkoutNotFoundException(workoutId);
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.HistoryNotFoundException simple name, if true throw
   *     Exceptions.HistoryNotFoundException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param historyId that caused exception at server.
   * @throws Exceptions.HistoryNotFoundException if errorMessage is equal to
   *     Exceptions.HistoryNotFoundException simple name.
   */
  private void historyNotFoundExceptionHandler(String errorMessage, String historyId)
      throws Exceptions.HistoryNotFoundException {
    if (errorMessage.equals(Exceptions.HistoryNotFoundException.class.getSimpleName())) {
      throw new Exceptions.HistoryNotFoundException(historyId);
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.UserAlreadyExistException simple name, if true throw
   *     Exceptions.UserAlreadyExistException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param user object that caused exception at server.
   * @throws Exceptions.UserAlreadyExistException if errorMessage is equal to
   *     Exceptions.UserAlreadyExistException simple name.
   */
  private void userAlreadyExistsExceptionHandler(String errorMessage, User user)
      throws Exceptions.UserAlreadyExistException {
    if (errorMessage.equals(Exceptions.UserAlreadyExistException.class.getSimpleName())) {
      throw new Exceptions.UserAlreadyExistException(user.getUsername());
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.ExerciseAlreadyExistsException simple name, if true throw
   *     Exceptions.ExerciseAlreadyExistsException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param exercise object that caused exception at server.
   * @throws Exceptions.ExerciseAlreadyExistsException if errorMessage is equal to
   *     Exceptions.ExerciseAlreadyExistsException simple name.
   */
  private void exerciseAlreadyExistsExceptionHandler(String errorMessage, Exercise exercise)
      throws Exceptions.ExerciseAlreadyExistsException {
    if (errorMessage.equals(Exceptions.ExerciseAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.WorkoutAlreadyExistsException simple name, if true throw
   *     Exceptions.WorkoutAlreadyExistsException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param workout object that caused exception at server.
   * @throws Exceptions.WorkoutAlreadyExistsException if errorMessage is equal to
   *     Exceptions.WorkoutAlreadyExistsException simple name.
   */
  private void workoutAlreadyExistsExceptionHandler(String errorMessage, Workout workout)
      throws Exceptions.WorkoutAlreadyExistsException {
    if (errorMessage.equals(Exceptions.WorkoutAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.HistoryAlreadyExistsException simple name, if true throw
   *     Exceptions.HistoryAlreadyExistsException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param history object that caused exception at server.
   * @throws Exceptions.HistoryAlreadyExistsException if errorMessage is equal to
   *     Exceptions.ServerException simple name.
   */
  private void historyAlreadyExistsExceptionHandler(String errorMessage, History history)
      throws Exceptions.HistoryAlreadyExistsException {
    if (errorMessage.equals(Exceptions.HistoryAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName() + ";" + history.getDate());
    }
  }

  /**
   * Checks if given errorMessage is equal to Exceptions.IllegalIdException simple name, if true throw
   *     Exceptions.IllegalIdException.
   *
   * @param errorMessage received from server containing simple name of exception.
   * @param id that caused exception at server.
   * @param cls Class type of object that caused exception at server.
   * @throws Exceptions.IllegalIdException if errorMessage is equal to Exceptions.ServerException simple name.
   */
  private void illegalIdExceptionHandler(String errorMessage, String id, Class<?> cls)
      throws Exceptions.IllegalIdException {
    if (errorMessage.equals(Exceptions.IllegalIdException.class.getSimpleName())) {
      throw new Exceptions.IllegalIdException(id, cls);
    }
  }

  /**
   * Help method for sending POST request to server.
   *
   * @param uri object to set http address for package.
   * @return server's responseEntity containing status indication accepted, or status indicated not accepted and
   *     exception simple name as string.
   */
  private ResponseEntity<String> sendPackage(URI uri) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
    ResponseEntity<String> response = restTemplate.postForEntity(uri, httpEntity, String.class);
    return response;
  }

  /**
   * Help method for sending GET request to server.
   *
   * @param uri object to set http address for package.
   * @return server's responseEntity containing information requested by client and status indication accepted,
   *     or status indicated not accepted and exception simple name as string.
   */
  private ResponseEntity<String> getPackage(URI uri) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForEntity(uri, String.class);
  }

  /**
   * Request create user on server with given User object.
   *
   * @param user object to register.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws Exceptions.UserAlreadyExistException if there is a user with same username in server.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public void createUser(User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.UserAlreadyExistException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "createUser/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(e.getMessage());
      serverExceptionHandler(e.getMessage());
      userAlreadyExistsExceptionHandler(error, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Request login on server with given User object.
   *
   * @param user object to log in.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws Exceptions.PasswordIncorrectException if User object's password does not match password to user in server.
   * @throws Exceptions.UserNotFoundException if user with User object's username does not exists in server.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public void login(User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.PasswordIncorrectException,
      Exceptions.UserNotFoundException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "login/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      passwordIncorrectExceptionHandler(error);
      userNotFoundExceptionHandler(error, user);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to add an Exercise to the User.
   *
   * @param user the user to add the Exercise to.
   * @param workoutId the workout id of the Exercise to be added.
   * @param exercise the Exercise to be added.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.ExerciseAlreadyExistsException if the Exercise requested is already added.
   * @throws Exceptions.WorkoutNotFoundException if the Workout does not exist on the server.
   */
  public void addExercise(User user, String workoutId, Exercise exercise) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.ExerciseAlreadyExistsException,
      Exceptions.WorkoutNotFoundException,
      JsonProcessingException, Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String exerciseString = objectToJson(exercise);
    String url = baseUrl + "addExercise/"
        + URLEncoder.encode(userString, StandardCharsets.UTF_8)
        + "/" + workoutId + "/" + URLEncoder.encode(exerciseString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      illegalIdExceptionHandler(error, workoutId, Workout.class);
      exerciseAlreadyExistsExceptionHandler(error, exercise);
      workoutNotFoundExceptionHandler(error, workoutId);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to register the given Workout to the User.
   *
   * @param workout the Workout to be added.
   * @param user the User to add the workout to.
   * @return the Id of the Workout that was added to the User.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws Exceptions.WorkoutAlreadyExistsException if the Workout already exists on the server.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public String addWorkout(Workout workout, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.WorkoutAlreadyExistsException,
      JsonProcessingException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseUrl + "addWorkout/"
        + URLEncoder.encode(userString, StandardCharsets.UTF_8)
        + "/" + URLEncoder.encode(workoutString, StandardCharsets.UTF_8);
    try {
      return sendPackage(new URI(url)).getBody();
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      workoutAlreadyExistsExceptionHandler(error, workout);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to add a History to the User.
   *
   * @param history the History to be added.
   * @param user the user to add the History to.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.HistoryAlreadyExistsException if the History requested is already added.
   */
  public void addHistory(History history, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.HistoryAlreadyExistsException,
      JsonProcessingException {
    String historyString = objectToJson(history);
    String userString = objectToJson(user);
    String url = baseUrl + "addHistory/"
        + URLEncoder.encode(userString, StandardCharsets.UTF_8)
        + "/" + URLEncoder.encode(historyString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      historyAlreadyExistsExceptionHandler(error, history);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to update an existing Exercise which is registered on the User.
   *
   * @param exercise the Exercise to be updated.
   * @param user the user to update the Exercise from.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.ExerciseNotFoundException if the Exercise requested is not stored in the server.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public void updateExercise(Exercise exercise, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException,
      Exceptions.IllegalIdException,
      Exceptions.ExerciseNotFoundException {
    String exerciseString = objectToJson(exercise);
    String userString = objectToJson(user);
    String url = baseUrl + "updateExercise/"
        + URLEncoder.encode(userString, StandardCharsets.UTF_8)
        + "/" + URLEncoder.encode(exerciseString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      illegalIdExceptionHandler(error, exercise.getId(), Exercise.class);
      exerciseNotFoundExceptionHandler(exerciseString, exercise.getId());
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to update an existing Workout which is registered on the User.
   *
   * @param workout the Workout to be updated.
   * @param user the user to update the Workout from.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.WorkoutNotFoundException if the Workout is not stored in the server.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public void updateWorkout(Workout workout, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException,
      Exceptions.WorkoutNotFoundException,
      Exceptions.IllegalIdException {
    String workoutString = objectToJson(workout);
    String userString = objectToJson(user);
    String url = baseUrl + "updateWorkout/"
        + URLEncoder.encode(userString, StandardCharsets.UTF_8)
        + "/" + URLEncoder.encode(workoutString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      workoutNotFoundExceptionHandler(error, workout.getId());
      illegalIdExceptionHandler(error, workout.getId(), Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to delete an Exercise with given id on given User.
   *
   * @param exerciseId the id to the Exercise to be deleted.
   * @param user the user to delete the Exercise from.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public void deleteExercise(String exerciseId, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "deleteExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + exerciseId;
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      illegalIdExceptionHandler(error, exerciseId, Exercise.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to delete a Workout with given id on given user.
   *
   * @param workoutId the id to the Workout to be deleted.
   * @param user the user to delete the Workout from.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public void deleteWorkout(String workoutId, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "deleteWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + workoutId;
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      illegalIdExceptionHandler(error, workoutId, Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to delete a History with given id on a given User.
   *
   * @param historyId the id to the History to be deleted.
   * @param user the User to delete the History from.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public void deleteHistory(String historyId, User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "deleteHistory/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + historyId;
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      illegalIdExceptionHandler(error, historyId, History.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to delete a User.
   *
   * @param user the User to be deleted.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public void deleteUser(User user) throws URISyntaxException,
      Exceptions.BadPackageException,
      Exceptions.ServerException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "deleteUser/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      sendPackage(new URI(url));
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for a Workout object specified with id.
   *
   * @param workoutId the id of the Workout object to be fetched.
   * @param user the User to get the Workout from.
   * @return Workout the Workout object.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws Exceptions.WorkoutNotFoundException if no workout with the given id is stored in the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public Workout queryWorkout(String workoutId, User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.WorkoutNotFoundException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "getWorkout/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + workoutId;
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (Workout) jsonToObject(response.getBody(), Workout.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      workoutNotFoundExceptionHandler(error, workoutId);
      illegalIdExceptionHandler(error, workoutId, Workout.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for an Exercise object specified with id.
   *
   * @param exerciseId the id of the Exercise object to be fetched.
   * @param user the User to get the Exercise from.
   * @return Exercise the Exercise object.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws Exceptions.ExerciseNotFoundException if no exercise with the given id is stored in the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public Exercise queryExercise(String exerciseId, User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.ExerciseNotFoundException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "getExercise/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + exerciseId;
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (Exercise) jsonToObject(response.getBody(), Exercise.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      exerciseNotFoundExceptionHandler(error, exerciseId);
      illegalIdExceptionHandler(error, exerciseId, Exercise.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for a History object specified with id.
   *
   * @param historyId the id of the History object to be fetched.
   * @param user the User to get the History from.
   * @return History the history object.
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws Exceptions.HistoryNotFoundException if no history with the given id is stored in the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   * @throws Exceptions.IllegalIdException if id given is not valid for selected class.
   */
  public History queryHistory(String historyId, User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      Exceptions.HistoryNotFoundException,
      URISyntaxException,
      JsonProcessingException,
      Exceptions.IllegalIdException {
    String userString = objectToJson(user);
    String url = baseUrl + "getHistory/" + URLEncoder.encode(userString, StandardCharsets.UTF_8) + "/" + historyId;
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (History) jsonToObject(response.getBody(), History.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      historyNotFoundExceptionHandler(error, historyId);
      illegalIdExceptionHandler(error, historyId, History.class);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for the map of exerciseIds.
   *
   * @param user to get the map from.
   * @return a Map object with the exerciseIds
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public Map<String, String> queryExerciseMap(User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "getExerciseMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (LinkedHashMap<String, String>) jsonToObject(response.getBody(), LinkedHashMap.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for the map of workoutIds.
   *
   * @param user to get the map from.
   * @return a Map object with the workoutIds
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public Map<String, String> queryWorkoutMap(User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "getWorkoutMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (LinkedHashMap<String, String>) jsonToObject(response.getBody(), LinkedHashMap.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }

  /**
   * Sends a request to the server for the map of historyIds.
   *
   * @param user to get the map from.
   * @return a Map object with the historyIds
   * @throws Exceptions.BadPackageException if there is something wrong with
   *     serialization or deserialization.
   * @throws Exceptions.ServerException if something wrong happens on the server.
   * @throws URISyntaxException if a string could not be parsed as a URI reference.
   * @throws JsonProcessingException if a problem occur when processing(parsing, generating)
   *     JSON file that are noe I/O problems.
   */
  public Map<String, String> queryHistoryMap(User user) throws Exceptions.BadPackageException,
      Exceptions.ServerException,
      URISyntaxException,
      JsonProcessingException {
    String userString = objectToJson(user);
    String url = baseUrl + "getHistoryMap/" + URLEncoder.encode(userString, StandardCharsets.UTF_8);
    try {
      ResponseEntity<String> response = getPackage(new URI(url));
      return (LinkedHashMap<String, String>) jsonToObject(response.getBody(), LinkedHashMap.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      String error = e.getMessage().substring(5).replace("\"", "").trim();
      badPackageExceptionHandler(error);
      serverExceptionHandler(error);
      throw new UnknownError("Unknown error has occurred. Please check server and client log for debugging");
    }
  }
}
