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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientService {
  private BeastBookPersistence beastBookPersistence = new BeastBookPersistence();
  private String ipAddress = "localhost";
  private String baseURL = "http://" + ipAddress + ":8080/";

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    baseURL = "http://" + ipAddress + ":8080/";
  }

  private ResponseEntity<String> sendPackage (Object object, URI uri) throws JsonProcessingException {
    try {
      String jsonString = beastBookPersistence.objectToJson(object);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> httpEntity = new HttpEntity<>(jsonString, headers);
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<String> data = restTemplate.postForEntity(uri, httpEntity, String.class);
      return data;
    } catch (Exception e) {
      throw  e;
    }
  }

  private void exceptionHandler(String exception) {
    String[] exceptionPack = exception.split(":");
    String exceptionType = exceptionPack[0];
    String exceptionMessage = exceptionPack[0];

    if (exception.equals("IOException")) {

    } else if (exception.equals("IllegalArgumentException")) {

    } else if (exception.equals("IllegalStateException")) {

    }
  }


  public ResponseEntity<String> deleteWorkout(String workoutId, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "deleteWorkout/" + username + "/" + workoutId);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage("deleteWorkout", uri);
    } catch (JsonProcessingException e) {

      return null;
    }
  }

  public ResponseEntity<String> deleteExercise(String exerciseId, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "deleteExercise/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage("deleteExercise", uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResponseEntity<String> deleteHistory(String historyId, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "deleteHistory/" + username + "/" + historyId);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage("deleteHistory", uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> deleteUser(String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "deleteUser/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage("deleteUser", uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> createUser (User user) throws URISyntaxException, JsonProcessingException{
//    try {
      URI uri = new URI(baseURL + "createUser/");
      return sendPackage(user, uri);
/*    } catch (URISyntaxException e) {
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;*/
  }

  public ResponseEntity<String> addWorkout(Workout workout, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "addWorkout/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage(workout, uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> addExercise(Exercise exercise, String workoutId, String username) {
    URI uri = null;
    try {

      uri = new URI(baseURL + "addExercise/" + workoutId + "/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {

      return sendPackage(exercise, uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> addHistory(History history, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "addHistory/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage(history, uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> updateWorkout(Workout workout, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "updateWorkout/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage(workout, uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<String> updateExercise(Exercise exercise, String username) {
    URI uri = null;
    try {
      uri = new URI(baseURL + "updateExercise/" + username);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    try {
      return sendPackage(exercise, uri);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  //Todo right use of name?
  /*public User queryUser(String username)  {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getUser/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (User) beastBookPersistence.jsonToObject(jsonString, User.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }*/

  public User queryUser(String username)  throws JsonProcessingException {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getUser/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    return (User) beastBookPersistence.jsonToObject(jsonString, User.class);
  }

  public Workout queryWorkout(String workoutID, String username)  {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getWorkout/" + username + "/" + workoutID;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (Workout) beastBookPersistence.jsonToObject(jsonString, Workout.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Exercise queryExercise(String exerciseID, String username)  {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getExercise/" + username + "/" + exerciseID;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (Exercise) beastBookPersistence.jsonToObject(jsonString, Exercise.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public History queryHistory(String historyID, String username)  {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getHistory/" + username + "/" + historyID;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (History) beastBookPersistence.jsonToObject(jsonString, History.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Map<String, String> queryExerciseMap(String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getExerciseMap/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (Map<String, String>) beastBookPersistence.jsonToObject(jsonString, LinkedHashMap.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Map<String, String> queryWorkoutMap(String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getWorkoutMap/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (Map<String, String>) beastBookPersistence.jsonToObject(jsonString, LinkedHashMap.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Map<String, String> queryHistoryMap(String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "/getHistoryMap/" + username;
    String jsonString = restTemplate.getForObject(url, String.class);
    try {
      return (Map<String, String>) beastBookPersistence.jsonToObject(jsonString, LinkedHashMap.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String queryPassword(String username) {
    final RestTemplate restTemplate = new RestTemplateBuilder().build();
    String url = baseURL + "getPassword/" + username;
    String password = restTemplate.getForObject(url, String.class);
    return password;
  }


//  public String queryExerciseName(String exerciseID, String username) {
//    final RestTemplate restTemplate = new RestTemplateBuilder().build();
//    String url = baseURL + "/getExerciseName/" + username + "/" + exerciseID;
//    String exerciseName = restTemplate.getForObject(url, String.class);
//    return exerciseName;
//  }
//
//  public String queryWorkoutName(String workoutID, String username) {
//    final RestTemplate restTemplate = new RestTemplateBuilder().build();
//    String url = baseURL + "/getWorkoutName/" + username + "/" + workoutID;
//    String workoutName = restTemplate.getForObject(url, String.class);
//    return workoutName;
//  }
//
//  public String queryHistoryName(String historyID, String username) {
//    final RestTemplate restTemplate = new RestTemplateBuilder().build();
//    String url = baseURL + "/getHistoryName/" + username + "/" + historyID;
//    String workoutName = restTemplate.getForObject(url, String.class);
//    return workoutName;
//  }
}
