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
import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class ClientService {
  private String ipAddress = "localhost";
  private String baseUrl = "http://" + ipAddress + ":8080/";
  private ObjectMapper mapper = new ObjectMapper();
  private RestTemplate restTemplate = new RestTemplate();

  ClientService() {
    mapper.registerModule(new BeastBookModule());
  }

  private String objectToJson(Object object) throws JsonProcessingException {
    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    System.out.println("Dette er det klienten sender til serveren: " + jsonString);
    return jsonString;
  }

  private Object jsonToObject(String jsonString, Class cls) throws Exceptions.BadPackageException {
    try {
      System.out.println("Dette er det klienten fikk fra serveren: " + jsonString);
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException();
    }
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    baseUrl = "http://" + ipAddress + ":8080/";
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

  private void passwordIncorrectExceptionHandler(String errorMessage)
      throws Exceptions.PasswordIncorrectException {
    if (errorMessage.equals(Exceptions.PasswordIncorrectException.class.getSimpleName())) {
      throw new Exceptions.PasswordIncorrectException();
    }
  }

  private void userNotFoundExceptionHandler(String errorMessage, User user)
      throws Exceptions.UserNotFoundException {
    if (errorMessage.equals(Exceptions.UserNotFoundException.class.getSimpleName())) {
      throw new Exceptions.UserNotFoundException(user.getUsername());
    }
  }

  private void exerciseNotFoundExceptionHandler(String errorMessage, String exerciseId)
      throws Exceptions.ExerciseNotFoundException {
    if (errorMessage.equals(Exceptions.ExerciseNotFoundException.class.getSimpleName())) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }

  private void workoutNotFoundExceptionHandler(String errorMessage, String workoutId)
      throws Exceptions.WorkoutNotFoundException {
    if (errorMessage.equals(Exceptions.WorkoutNotFoundException.class.getSimpleName())) {
      throw new Exceptions.WorkoutNotFoundException(workoutId);
    }
  }

  private void historyNotFoundExceptionHandler(String errorMessage, String historyId)
      throws Exceptions.HistoryNotFoundException {
    if (errorMessage.equals(Exceptions.HistoryNotFoundException.class.getSimpleName())) {
      throw new Exceptions.HistoryNotFoundException(historyId);
    }
  }

  private void userAlreadyExistsExceptionHandler(String errorMessage, User user)
      throws Exceptions.UserAlreadyExistException {
    if (errorMessage.equals(Exceptions.UserAlreadyExistException.class.getSimpleName())) {
      throw new Exceptions.UserAlreadyExistException(user.getUsername());
    }
  }

  private void exerciseAlreadyExistsExceptionHandler(String errorMessage, Exercise exercise)
      throws Exceptions.ExerciseAlreadyExistsException {
    if (errorMessage.equals(Exceptions.ExerciseAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
    }
  }

  private void workoutAlreadyExistsExceptionHandler(String errorMessage, Workout workout)
      throws Exceptions.WorkoutAlreadyExistsException {
    if (errorMessage.equals(Exceptions.WorkoutAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
  }

  private void historyAlreadyExistsExceptionHandler(String errorMessage, History history)
      throws Exceptions.HistoryAlreadyExistsException {
    if (errorMessage.equals(Exceptions.HistoryAlreadyExistsException.class.getSimpleName())) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName() + ";" + history.getDate());
    }
  }

  private void illegalIdExceptionHandler(String errorMessage, String id, Class cls)
      throws Exceptions.IllegalIdException {
    if (errorMessage.equals(Exceptions.IllegalIdException.class.getSimpleName())) {
      throw new Exceptions.IllegalIdException(id, cls);
    }
  }

  public static void main(String[] args) throws Exceptions.BadPackageException, Exceptions.UserAlreadyExistException, Exceptions.ServerException, URISyntaxException, JsonProcessingException, Exceptions.WorkoutAlreadyExistsException, Exceptions.WorkoutNotFoundException, Exceptions.IllegalIdException, Exceptions.ExerciseAlreadyExistsException, Exceptions.ExerciseNotFoundException, Exceptions.HistoryAlreadyExistsException, Exceptions.HistoryNotFoundException {
    ClientService service = new ClientService();
    User user = new User("Temp", "Temp");
    service.createUser(user);
    Workout workout = new Workout("workout");
    service.addWorkout(workout , user);
    Map<String,String> wmap = service.queryWorkoutMap(user);
    String wkey = "";
    for (String keys : wmap.keySet()) {
      wkey = keys;
    }
    service.queryWorkout(wkey, user);
    Exercise exercise = new Exercise("ovelse", 1,1, 1, 1, 1);
    service.addExercise(user, wkey, exercise);
    String ekey = "";
    Map<String,String> emap = service.queryExerciseMap(user);
    for (String keys : emap.keySet()) {
      ekey = keys;
    }
    exercise = service.queryExercise(ekey, user);
    exercise.setRepsPerSet(10);
    service.updateExercise(exercise, user);
    System.out.println(service.queryExercise(ekey, user));
    service.addHistory(new History("workout", List.of()), user);
    String hkey = "";
    Map<String,String> hmap = service.queryHistoryMap(user);
    for (String keys : hmap.keySet()) {
      hkey = keys;
    }
    service.queryHistory(hkey, user);

  }

  private ResponseEntity<String> sendPackage(URI uri) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
    ResponseEntity<String> response = restTemplate.postForEntity(uri, httpEntity, String.class);
    return response;
  }

  private ResponseEntity<String> getPackage(URI uri) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForEntity(uri, String.class);
  }

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
