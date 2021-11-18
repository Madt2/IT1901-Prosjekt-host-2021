package beastbook.server;

import beastbook.core.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class ServerController {

  private final ServerService serverService;

  @Autowired
  public ServerController(ServerService serverService) {
    this.serverService = serverService;
  }


  @PostMapping("createUser/")
  public ResponseEntity createUser(@RequestBody String jsonString) {
    try {
      User user = (User) serverService.jsonToObject(jsonString, User.class);
      serverService.createUser(user);
    } catch (JsonProcessingException e) {
      //Send could not deserialize user send again, bad package?
    } catch (IllegalArgumentException e) {
      //send user already exists exception
    } catch (IOException e) {
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("addWorkout/{username}")
  public ResponseEntity<String> addWorkout(@RequestBody String jsonString, @PathVariable String username) {
    try {
      System.out.println("user gotten");

      Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
      System.out.println(workout.getName());
      serverService.addIdObject(workout, username, null);
    } catch (JsonProcessingException e) {
      //Send could not deserialize user send again, bad package?
    } catch (IllegalArgumentException e) {
      //No user with username
    } catch (IOException e) {
      //Send could not save workout, IO error, send package again.
    }
    return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
  }

  @PostMapping("addExercise/{workoutId}/{username}")
  public void addExercise(@RequestBody String jsonString,
                          @PathVariable String workoutId,
                          @PathVariable String username) {
    try {
      System.out.println("exercise gotten");
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      serverService.addIdObject((IdClasses) exercise, username, workoutId);
    } catch (NullPointerException e ) {
      //send workoutId cannot be null
    } catch (JsonProcessingException e) {
      //Send could not deserialize user send again, bad package?
    } catch (IllegalArgumentException e) {
      //No user with username
    } catch (IOException e) {
      //Send could not save workout, IO error, send package again
    }
  }

  @PostMapping("addHistory/{username}")
  public void addHistory(@RequestBody String jsonString, @PathVariable String username) {
    try {
      History history = (History) serverService.jsonToObject(jsonString, History.class);
      serverService.addIdObject(history, username, null);
    } catch (IllegalArgumentException e) {
      //No user with username
    } catch (JsonProcessingException e) {
      //Send could not deserialize user send again, bad package?
    } catch (IOException e) {
      //Send could not save workout, IO error, send package again
    }
  }

  @PostMapping("updateWorkout/{username}")
  public void updateWorkout(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
      serverService.updateWorkout(workout, username);
    } catch (IllegalStateException e) {
      //Send workout does not have id, use addWorkout
    } catch (IllegalArgumentException e) {
      //No user with username
    } catch (JsonProcessingException e) {
      //Send could not deserialize user send again, bad package?
    } catch (IOException e) {
      //Send could not save workout, IO error, send package again
    }
  }

  @PostMapping("updateExercise/{username}")
  public void updateExercise(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      serverService.updateExercise(exercise, username);
    } catch (IllegalStateException e) {
      //Send workout does not have id, use addWorkout
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {

    }
  }

  @PostMapping("deleteUser/")
  public void deleteUser(@RequestBody String jsonString) {
    try {
      User user = (User) serverService.jsonToObject(jsonString, User.class);
      serverService.deleteUser(user.getUsername());
    } catch (IllegalArgumentException e) {

    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
  }

  @PostMapping("deleteWorkout/{username}/{workoutId}")
  public void deleteWorkout(@PathVariable String workoutId, @PathVariable String username) {
    try {
      Workout workout = serverService.getWorkout(workoutId, username);
      serverService.deleteIdObject(workout, username);
    } catch (IllegalArgumentException e) {

    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
  }

  @PostMapping("deleteExercise/{username}/")
  public void deleteExercise(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      serverService.deleteIdObject(exercise, username);
    } catch (IllegalArgumentException e) {

    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
  }

  @PostMapping("deleteHistory/{username}/{historyID}")
  public void deleteHistory(@RequestBody String jsonString, @PathVariable String username) {
    try {
      History history = (History) serverService.jsonToObject(jsonString, History.class);
      serverService.deleteIdObject(history, username);
    } catch (IllegalArgumentException e) {
      //return e;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("getUser/{username}")
  public ResponseEntity<String> sendUser(@PathVariable String username) {
    try {
      User user = serverService.getUser(username);
      String packageString = serverService.objectToJson(user);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getWorkout/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkout(@PathVariable String username, @PathVariable String workoutID) {
    try {
      Workout workout = serverService.getWorkout(workoutID, username);
      String packageString = serverService.objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getExercise/{username}/{exerciseID}")
  public ResponseEntity<String> sendExercise(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      Exercise exercise = serverService.getExercise(exerciseID, username);
      String packageString = serverService.objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getHistory/{username}/{historyID}")
  public ResponseEntity<String> sendHistory(@PathVariable String username, @PathVariable String historyID) {
    try {
      History history = serverService.getHistory(historyID, username);
      String packageString = serverService.objectToJson(history);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (JsonProcessingException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getExerciseMap/{username}")
  public ResponseEntity<String> sendExerciseMap(@PathVariable String username) {
    try {
      HashMap<String, String> map = serverService.getMapping(username, Exercise.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getWorkoutMap/{username}")
  public ResponseEntity<String> sendWorkoutMap(@PathVariable String username) {
    try {
      HashMap<String, String> map = serverService.getMapping(username, Workout.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getHistoryMap/{username}")
  public ResponseEntity<String> sendHistoryMap(@PathVariable String username) {
    try {
      HashMap<String, String> map = serverService.getMapping(username, HashMap.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getPassword/{username}")
  public ResponseEntity<String> sendPassword (@PathVariable String username) {
    try {
      String packageString = serverService.getUser(username).getPassword();
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /*@GetMapping("getExerciseName/{username}/{exerciseID}")
  public ResponseEntity<String> sendExerciseName(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      String packageString = serverService.getName(exerciseID, username, Exercise.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @GetMapping("getWorkoutName/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkoutName(@PathVariable String username, @PathVariable String workoutID) {
    try {
      String packageString = serverService.getName(workoutID, username, Workout.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {

    }
    return null;
  }

  @GetMapping("getHistoryName/{username}/{historyID}")
  public ResponseEntity<String> sendHistoryName(@PathVariable String username, @PathVariable String historyID) {
    try {
      String packageString = serverService.getName(historyID, username, History.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }*/
}
