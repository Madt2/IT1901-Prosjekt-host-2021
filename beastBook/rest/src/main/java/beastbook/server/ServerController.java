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
      return new ResponseEntity<>("success", HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
    return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    //send could not save user, IO error, send package again.
  }

}


  @PostMapping("addWorkout/{username}")
  public ResponseEntity<String> addWorkout(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
      String workoutId = serverService.addIdObject(workout, username, null);
      return new ResponseEntity<>(workoutId, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("addExercise/{workoutId}/{username}")
  public ResponseEntity<String> addExercise(@RequestBody String jsonString,
                          @PathVariable String workoutId,
                          @PathVariable String username) {
    try {
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      return new ResponseEntity<>(serverService.addIdObject((IdClasses) exercise, username, workoutId), HttpStatus.OK);
    } catch (NullPointerException e ) {
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("addHistory/{username}")
  public ResponseEntity<String> addHistory(@RequestBody String jsonString, @PathVariable String username) {
    try {
      History history = (History) serverService.jsonToObject(jsonString, History.class);
      return new ResponseEntity<>(serverService.addIdObject(history, username, null), HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("updateWorkout/{username}")
  public ResponseEntity<String> updateWorkout(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
      serverService.updateWorkout(workout, username);
      return new ResponseEntity<>("Success in updating workout",HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);   
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("updateExercise/{username}")
  public ResponseEntity<String> updateExercise(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      serverService.updateExercise(exercise, username);
      return new ResponseEntity<>("Success in updating Exercise",HttpStatus.OK);
    } catch (IllegalStateException e) {
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }  catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("deleteUser/")
  public ResponseEntity<String> deleteUser(@RequestBody String jsonString) {
    try {
      User user = (User) serverService.jsonToObject(jsonString, User.class);
      serverService.deleteUser(user.getUsername());
      return new ResponseEntity<>("Success in deleting User", HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("deleteWorkout/{username}/{workoutId}")
  public ResponseEntity<String> deleteWorkout(@PathVariable String workoutId, @PathVariable String username) {
    try {
      Workout workout = serverService.getWorkout(workoutId, username);
      serverService.deleteIdObject(workout, username);
      return new ResponseEntity<>("Success in deleting Workout", HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("deleteExercise/{username}/")
  public ResponseEntity<String> deleteExercise(@RequestBody String jsonString, @PathVariable String username) {
    try {
      Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
      serverService.deleteIdObject(exercise, username);
      return new ResponseEntity<>("Success in deleting Exercise", HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch( IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @PostMapping("deleteHistory/{username}/{historyID}")
  public ResponseEntity<String> deleteHistory(@RequestBody String jsonString, @PathVariable String username) {
    try {
      History history = (History) serverService.jsonToObject(jsonString, History.class);
      serverService.deleteIdObject(history, username);
      return new ResponseEntity<>("Success in deleting Workout", HttpStatus.OK);
    }  catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }



  @GetMapping("getUser/{username}")
  public ResponseEntity<String> sendUser(@PathVariable String username) {
    try {
      User user = serverService.getUser(username);
      String packageString = serverService.objectToJson(user);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getWorkout/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkout(@PathVariable String username, @PathVariable String workoutID) {
    try {
      Workout workout = serverService.getWorkout(workoutID, username);
      String packageString = serverService.objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getExercise/{username}/{exerciseID}")
  public ResponseEntity<String> sendExercise(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      Exercise exercise = serverService.getExercise(exerciseID, username);
      String packageString = serverService.objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getHistory/{username}/{historyID}")
  public ResponseEntity<String> sendHistory(@PathVariable String username, @PathVariable String historyID) {
    try {
      History history = serverService.getHistory(historyID, username);
      String packageString = serverService.objectToJson(history);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getExerciseMap/{username}")
  public ResponseEntity<String> sendExerciseMap(@PathVariable String username) {
    try {
      Map<String, String> map = serverService.getMapping(username, Exercise.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getWorkoutMap/{username}")
  public ResponseEntity<String> sendWorkoutMap(@PathVariable String username) {
    try {
      Map<String, String> map = serverService.getMapping(username, Workout.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getHistoryMap/{username}")
  public ResponseEntity<String> sendHistoryMap(@PathVariable String username) {
    try {
      Map<String, String> map = serverService.getMapping(username, History.class);
      String packageString = serverService.objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
  }

  @GetMapping("getPassword/{username}")
  public ResponseEntity<String> sendPassword (@PathVariable String username) {
    try {
      String packageString = serverService.getUser(username).getPassword();
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (JsonProcessingException e) { //Error in json
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) { //Send could not deserialize user send again, bad package?
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.NOT_FOUND);
    }  catch(IOException e) { //send user already exists exception
      return new ResponseEntity<>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
      //send could not save user, IO error, send package again.
    }
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
