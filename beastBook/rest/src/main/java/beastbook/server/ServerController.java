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
    serverService.createUser(user);
  }

  @PostMapping("addWorkout/{username}")
  public void addWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    serverService.addWorkout(workout, username);
  }

  @PostMapping("addExercise/{workoutID}/{username}")
  public void addExercise(@RequestBody String jsonString,
                          @PathVariable String workoutID,
                          @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    serverService.addExercise(exercise, workoutID, username);
  }

  @PostMapping("updateWorkout/{username}")
  public void updateWorkout(@RequestBody String jsonString, @PathVariable String username) {
    Workout workout = (Workout) serverService.jsonToObject(jsonString, Workout.class);
    serverService.updateWorkout(workout, username);
  }

  @PostMapping("updateExercise/{username}")
  public void updateExercise(@RequestBody String jsonString, @PathVariable String username) {
    Exercise exercise = (Exercise) serverService.jsonToObject(jsonString, Exercise.class);
    serverService.updateExercise(exercise, username);
  }

  @PostMapping("deleteUser/{username}")
  public void deleteUser(@PathVariable String username) {
    serverService.deleteUser(username);
  }

  @PostMapping("deleteWorkout/{username}/{workoutID}")
  public void deleteWorkout(@PathVariable String username, @PathVariable String workoutID) {
    serverService.removeWorkout(workoutID, username);
  }

  @PostMapping("deleteExercise/{username}/{exerciseID}")
  public void deleteExercise(@PathVariable String username, @PathVariable String exerciseID) {
    serverService.removeExercise(exerciseID, username);
  }

  @GetMapping("getUser/{username}")
  public ResponseEntity<String> sendUser(@PathVariable String username) {
    User user = serverService.getUser(username);
    String packageString = serverService.objectToJson(user);
    return new ResponseEntity<>(packageString, HttpStatus.OK);
  }

  @GetMapping("getWorkout/{username}/{workoutID}")
  public ResponseEntity<String> sendWorkout(@PathVariable String username, @PathVariable String workoutID) {
    Workout workout = serverService.getWorkout(workoutID, username);
    String packageString = serverService.objectToJson(workout);
    return new ResponseEntity<>(packageString, HttpStatus.OK);
  }

  @GetMapping("getExercise/{username}/{exerciseID}")
  public ResponseEntity<String> sendExercise(@PathVariable String username, @PathVariable String exerciseID) {
    Exercise exercise = serverService.getExercise(exerciseID, username);
    String packageString = serverService.objectToJson(exercise);
    return new ResponseEntity<>(packageString, HttpStatus.OK);
  }
}
