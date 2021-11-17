package beastbook.client;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import beastbook.json.BeastBookPersistence;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientService {
  private BeastBookPersistence beastBookPersistence = new BeastBookPersistence();
  private String ipAddress = "localhost";
  private String baseURL = "http://" + ipAddress + ":8080/";

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    baseURL = "http://" + ipAddress + ":8080/";
  }

  private ResponseEntity<String> sendPackage (Object object, URI uri) throws JsonProcessingException {
    String jsonString = "";
    jsonString = beastBookPersistence.objectToJson(object);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> data = restTemplate.postForEntity(uri, httpEntity, String.class);
    return data;
  }

  public ResponseEntity<String> deleteWorkout(Workout workout, String username) throws JsonProcessingException, URISyntaxException {
    URI uri = new URI(baseURL + "deleteWorkout/" + username);
    return sendPackage(workout, uri);
  }

  public ResponseEntity<String> deleteExercise(Exercise exercise, String username) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "deleteExercise/" + username);
      return sendPackage(exercise, uri);
  }

  public ResponseEntity<String> deleteHistory(History history, String username) throws JsonProcessingException, URISyntaxException {
    URI uri = new URI(baseURL + "deleteHistory/" + username);
    return sendPackage(history, uri);
  }

  public ResponseEntity<String> deleteUser(User user, String username) throws JsonProcessingException, URISyntaxException {
    URI uri = new URI(baseURL + "deleteUser/");
    return sendPackage(user, uri);
  }

  public ResponseEntity<String> createUser (User user) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "createUser/");
      return sendPackage(user, uri);
  }

  public ResponseEntity<String> addWorkout(Workout workout, String username) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "addWorkout/" + username);
      return sendPackage(workout, uri);
  }

  public ResponseEntity<String> addExercise(Exercise exercise, Workout workout, String username) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "addExercise/" + workout.getId() + "/" + username);
      return sendPackage(exercise, uri);
  }

  public ResponseEntity<String> addHistory(History history, String username) throws JsonProcessingException, URISyntaxException {
    URI uri = new URI(baseURL + "addHistory/" + username);
    return sendPackage(history, uri);
  }

  public ResponseEntity<String> updateWorkout(Workout workout, String username) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "updateWorkout/" + username);
      return sendPackage(workout, uri);
  }

  public ResponseEntity<String> updateExercise(Exercise exercise, String username) throws JsonProcessingException, URISyntaxException {
      URI uri = new URI(baseURL + "updateExercise/" + username);
      return sendPackage(exercise, uri);
  }

  //Todo right use of name?
  public User queryUser(String username) throws JsonProcessingException {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getUser/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (User) beastBookPersistence.jsonToObject(jsonString, User.class);
  }

  public Workout queryWorkout(String workoutID, String username) throws JsonProcessingException {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getWorkout/" + username + "/" + workoutID;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (Workout) beastBookPersistence.jsonToObject(jsonString, Workout.class);
  }

  public Exercise queryExercise(String exerciseID, String username) throws JsonProcessingException {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getExercise/" + username + "/" + exerciseID;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (Exercise) beastBookPersistence.jsonToObject(jsonString, Exercise.class);
  }

  public Exercise queryHistory(String historyID, String username) throws JsonProcessingException {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getHistory/" + username + "/" + historyID;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (Exercise) beastBookPersistence.jsonToObject(jsonString, Exercise.class);
  }

  public String queryExerciseName(String exerciseID, String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getExerciseName/" + username + "/" + exerciseID;
    String exerciseName = restTemplate.getForObject(url, String.class);
    return exerciseName;
  }

  public String queryWorkoutName(String workoutID, String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getWorkoutName/" + username + "/" + workoutID;
    String workoutName = restTemplate.getForObject(url, String.class);
    return workoutName;
  }

  public String queryHistoryName(String historyID, String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getHistoryName/" + username + "/" + historyID;
    String workoutName = restTemplate.getForObject(url, String.class);
    return workoutName;
  }

}
