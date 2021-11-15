package beastbook.client;

import beastbook.core.Exercise;
import beastbook.core.User;
import beastbook.core.Workout;
import beastbook.json.BeastBookPersistence;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientService {
  private BeastBookPersistence beastBookPersistence = new BeastBookPersistence();
  private final String baseURL = "http://localhost:8080/";

  private ResponseEntity<String> sendPackage (Object object, URI uri) {
    String jsonString = beastBookPersistence.objectToJson(object);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> data = restTemplate.postForEntity(uri, httpEntity, String.class);
    return data;
  }

  private void sendMessage (String message, String username) {
    /*HttpHeaders headers = new HttpHeaders();
    try {
      URI uri = new URI(baseURL + message + "/" + username);
      HttpEntity<String> httpEntity = new HttpEntity<>(headers);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> data = restTemplate.postForEntity(uri, httpEntity, String.class);
      return data;
    } catch (URISyntaxException e) {
      //Todo fix catch
      e.printStackTrace();
    }
    return null;*/
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + message + "/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
  }

  public void deleteWorkout (Workout workout, String  username) {
    sendMessage("deleteWorkout/" + workout.getID(), username);
  }

  public void deleteExercise (Exercise exercise, String username) {
    sendMessage("deleteExercise/" + exercise.getID(), username);
  }

  public void deleteUser (String username) {
    sendMessage("deleteUser", username);
  }

  public ResponseEntity<String> createUser (User user) {
    try {
      URI uri = new URI(baseURL + "createUser/" + user.getUsername());
      return sendPackage(user, uri);
    } catch (URISyntaxException e) {
      //Todo fix try catch
      e.printStackTrace();
    }
    //Todo this will not work, find another solution!
    return null;
  }

  public ResponseEntity<String> addWorkout(Workout workout, String username) {
    try {
      URI uri = new URI(baseURL + "addWorkout/" + username);
      return sendPackage(workout, uri);
    } catch (URISyntaxException e) {
      //Todo fix try catch
      e.printStackTrace();
    }
    //Todo this will not work, find another solution!
    return null;
  }

  public ResponseEntity<String> addExercise(Exercise exercise, Workout workout, String username) {
    try {
      URI uri = new URI(baseURL + "addExercise/" + workout.getID() + "/" + username);
      return sendPackage(exercise, uri);
    } catch (URISyntaxException e) {
      //Todo fix try catch
      e.printStackTrace();
    }
    //Todo this will not work, find another solution!
    return null;
  }

  public ResponseEntity<String> updateWorkout(Workout workout, String username) {
    try {
      URI uri = new URI(baseURL + "updateWorkout/" + username);
      return sendPackage(workout, uri);
    } catch (URISyntaxException e) {
      //Todo fix try catch
      e.printStackTrace();
    }
    //Todo this will not work, find another solution!
    return null;
  }

  public ResponseEntity<String> updateExercise(Exercise exercise, String username) {
    try {
      URI uri = new URI(baseURL + "updateExercise/" + username);
      return sendPackage(exercise, uri);
    } catch (URISyntaxException e) {
      //Todo fix try catch
      e.printStackTrace();
    }
    //Todo this will not work, find another solution!
    return null;
  }

  //Todo right use of name?
  public User queryUser(String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getUser/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (User) beastBookPersistence.jsonToObject(jsonString, User.class);
  }

  public Workout queryWorkout(String workoutID, String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getWorkout/" + username + "/" + workoutID;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (Workout) beastBookPersistence.jsonToObject(jsonString, Workout.class);
  }

  public Exercise queryExercise(String exerciseID, String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getExercise/" + username + "/" + exerciseID;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (Exercise) beastBookPersistence.jsonToObject(jsonString, Exercise.class);
  }

}
