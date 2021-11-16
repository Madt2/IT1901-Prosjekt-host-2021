package beastbook.server;

import beastbook.core.Exercise;
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

  @PostMapping("createUser/{username}")
  public void createUser(@RequestBody String jsonString, @PathVariable String username) {
    User user = (User) serverService.jsonToObject(jsonString, User.class);
    try {
      serverService.createUser(user);
    } catch (IllegalStateException e) {
      return e;
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("addWorkout/{username}")
  public void addWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    try {
      serverService.addWorkout(workout, username);
    } catch (IllegalStateException e) {
      return e;
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("addExercise/{workoutID}/{username}")
  public void addExercise(@RequestBody String jsonString,
                          @PathVariable String workoutID,
                          @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    try {
      serverService.addExercise(exercise, workoutID, username);
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("updateWorkout/{username}")
  public void updateWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    try {
      serverService.updateWorkout(workout, username);
    } catch (IllegalStateException e) {
      return e;
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("updateExercise/{username}")
  public void updateExercise(@RequestBody String jsonString, @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    try {
      serverService.updateExercise(exercise, username);
    } catch (IllegalStateException e) {
      return e;
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("deleteUser/{username}")
  public void deleteUser(@PathVariable String username) {
    try {
      serverService.deleteUser(username);
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("deleteWorkout/{username}/{workoutID}")
  public void deleteWorkout(@PathVariable String username, @PathVariable String workoutID) {
    try {
      serverService.deleteWorkout(workoutID, username);
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @PostMapping("deleteExercise/{username}/{exerciseID}")
  public void deleteExercise(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      serverService.deleteExercise(exerciseID, username);
    } catch (IllegalArgumentException e) {
      return e;
    }
  }

  @GetMapping("getUser/{username}")
  public ResponseEntity<String> sendUser(@PathVariable String username) {
    try {
      User user = serverService.getUser(username);
      String packageString = serverService.objectToJson(user);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @GetMapping("getWorkout/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkout(@PathVariable String username, @PathVariable String workoutID) {
    try {
      Workout workout = serverService.getWorkout(workoutID, username);
      String packageString = serverService.objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @GetMapping("getExercise/{username}/{exerciseID}")
  public ResponseEntity<String> sendExercise(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      Exercise exercise = serverService.getExercise(exerciseID, username);
      String packageString = serverService.objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @GetMapping("getExerciseName/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkoutName(@PathVariable String username, @PathVariable String workoutID) {
    try {
      String packageString = serverService.getWorkoutName(workoutID, username);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @GetMapping("getExerciseName/{username}/{exerciseID}")
  public ResponseEntity<String> sendExerciseName(@PathVariable String username, @PathVariable String exerciseID) {
    try {
      String packageString = serverService.getExerciseName(exerciseID, username);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return;
    }
  }
}
