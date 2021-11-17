package beastbook.server;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServerController {

  private final ServerService serverService;

  @Autowired
  public ServerController(ServerService serverService) {
    this.serverService = serverService;
  }

  @PostMapping("createUser/")
  public void createUser(@RequestBody String jsonString) {
    User user = (User) serverService.jsonToObject(jsonString, User.class);
    try {
      serverService.createUser(user);
    } catch (IllegalStateException e) {
      //return e;
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("addWorkout/{username}")
  public void addWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    try {
      serverService.addIdObject(workout, username, null);
    } catch (IllegalStateException e) {
      //return e;
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("addExercise/{workoutID}/{username}")
  public void addExercise(@RequestBody String jsonString,
                          @RequestBody String workoutID,
                          @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    // Todo MUST FIX!!
    Workout workout = new Workout();
    try {
      serverService.addIdObject(exercise, username, workout);
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("addHistory/{username}")
  public void addHistory(@RequestBody String jsonString, @PathVariable String username) {
    History history = (History) serverService.jsonToObject(jsonString, History.class);
    try {
      serverService.addIdObject(history, username, null);
    } catch (IllegalStateException e) {
      //return e;
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("updateWorkout/{username}")
  public void updateWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    try {
      serverService.updateWorkout(workout, username);
    } catch (IllegalStateException e) {
      //return e;
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("updateExercise/{username}")
  public void updateExercise(@RequestBody String jsonString, @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    try {
      serverService.updateExercise(exercise, username);
    } catch (IllegalStateException e) {
      //return e;
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("deleteUser/")
  public void deleteUser(@RequestBody String jsonString) {
    User user = (User) serverService.jsonToObject(jsonString, User.class);
    try {
      serverService.deleteUser(user.getUsername());
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("deleteWorkout/{username}")
  public void deleteWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    try {
      serverService.deleteIdObject(workout, username);
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("deleteExercise/{username}/")
  public void deleteExercise(@RequestBody String jsonString, @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    try {
      serverService.deleteIdObject(exercise, username);
    } catch (IllegalArgumentException e) {
      //return e;
    }
  }

  @PostMapping("deleteHistory/{username}/{historyID}")
  public void deleteHistory(@RequestBody String jsonString, @PathVariable String username) {
    History history = (History) serverService.jsonToObject(jsonString, History.class);
    try {
      serverService.deleteIdObject(history, username);
    } catch (IllegalArgumentException e) {
      //return e;
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
    }
  }

  @GetMapping("getWorkout/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkout(@PathVariable String username, @PathVariable String workoutID) {
    try {
      Workout workout = serverService.getWorkout(workoutID, username);
      String packageString = serverService.objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @GetMapping("getExercise/{username}/{exerciseID}")
  public ResponseEntity<String> sendExercise(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      Exercise exercise = serverService.getExercise(exerciseID, username);
      String packageString = serverService.objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @GetMapping("getHistory/{username}/{historyID}")
  public ResponseEntity<String> sendHistory(@PathVariable String username, @PathVariable String historyID) {
    try {
      History history = serverService.getHistory(historyID, username);
      String packageString = serverService.objectToJson(history);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @GetMapping("getWorkoutName/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkoutName(@PathVariable String username, @PathVariable String workoutID) {
    try {
      String packageString = serverService.getName(workoutID, username, Workout.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @GetMapping("getExerciseName/{username}/{exerciseID}")
  public ResponseEntity<String> sendExerciseName(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      String packageString = serverService.getName(exerciseID, username, Exercise.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @GetMapping("getHistoryName/{username}/{historyID}")
  public ResponseEntity<String> sendHistoryName(@PathVariable String username, @PathVariable String historyID) {
    try {
      String packageString = serverService.getName(historyID, username, History.class);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
