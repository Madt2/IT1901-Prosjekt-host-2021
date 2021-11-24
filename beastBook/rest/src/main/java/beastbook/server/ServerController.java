package beastbook.server;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for server. Uses methods to send requests to ServerService.
 */
@RestController
public class ServerController {


  private ServerService serverService;
  ObjectMapper mapper;

  /**
   * Constructor for ServerController.
   */
  public ServerController() {
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  private void setService(User user) {
    serverService = new ServerService(user);
  }

  /**
   * Converts object to jsonString.
   *
   * @param object The object to be converted.
   * @return the jsonString that the object was converted to.
   */
  private String objectToJson(Object object) throws Exceptions.ServerException {
    try {
      String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
      return jsonString;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Converts jsonString to object.
   *
   * @param jsonString The jsonString to be converted.
   * @param cls The class of the jsonString to be converted.
   * @return The object that the jsonString was converted to.
   */
  private Object jsonToObject(String jsonString, Class<?> cls) throws Exceptions.BadPackageException {
    try {
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException();
    }
  }

  private ResponseEntity<String> sendNotFound(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<String> sendBadRequest(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<String> sendExpectationFailed(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.EXPECTATION_FAILED);
  }

  private ResponseEntity<String> sendNotAcceptable(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Creates a user and
   * sends a request to ServerService
   *
   * @param userString to be used to create a user
   * @return
   */
  @PostMapping("createUser/{userString}")
  public ResponseEntity<String> createUser(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.createUser();
      return new ResponseEntity<>("", HttpStatus.CREATED);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.UserAlreadyExistException e) {
      return sendNotAcceptable(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @PostMapping("login/{userString}")
  public ResponseEntity<String> login(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.login();
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.UserNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.PasswordIncorrectException e) {
      return sendNotAcceptable(e);
    }
  }

  @PostMapping("addWorkout/{userString}/{workoutString}")
  public ResponseEntity<String> addWorkout(@PathVariable String workoutString, @PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    workoutString = URLDecoder.decode(workoutString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = (Workout) jsonToObject(workoutString, Workout.class);
      String workoutId = serverService.addWorkout(workout);
      return new ResponseEntity<>(workoutId, HttpStatus.CREATED);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.WorkoutAlreadyExistsException e) {
      return sendNotAcceptable(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @PostMapping("addExercise/{userString}/{workoutId}/{exerciseString}")
  public ResponseEntity<String> addExercise(
      @PathVariable String userString, @PathVariable String workoutId, @PathVariable String exerciseString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    exerciseString = URLDecoder.decode(exerciseString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = (Exercise) jsonToObject(exerciseString, Exercise.class);
      serverService.addExercise(exercise, workoutId);
      return new ResponseEntity<>("", HttpStatus.CREATED);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.WorkoutNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.ExerciseAlreadyExistsException | Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @PostMapping("addHistory/{userString}/{historyString}")
  public ResponseEntity<String> addHistory(@PathVariable String userString, @PathVariable String historyString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    historyString = URLDecoder.decode(historyString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      History history = (History) jsonToObject(historyString, History.class);
      serverService.addHistory(history);
      return new ResponseEntity<>("", HttpStatus.CREATED);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.HistoryAlreadyExistsException e) {
      return sendNotAcceptable(e);
    }
  }

  @PostMapping("updateWorkout/{userString}/{workoutString}")
  public ResponseEntity<String> updateWorkout(@PathVariable String workoutString, @PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    workoutString = URLDecoder.decode(workoutString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = (Workout) jsonToObject(workoutString, Workout.class);
      serverService.updateWorkout(workout);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.WorkoutNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @PostMapping("updateExercise/{userString}/{exerciseString}")
  public ResponseEntity<String> updateExercise(@PathVariable String userString, @PathVariable String exerciseString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    exerciseString = URLDecoder.decode(exerciseString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = (Exercise) jsonToObject(exerciseString, Exercise.class);
      serverService.updateExercise(exercise);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    } catch (Exceptions.ExerciseNotFoundException e) {
      return sendNotFound(e);
    }
  }

  @PostMapping("deleteUser/{userString}")
  public ResponseEntity<String> deleteUser(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteUser();
      return new ResponseEntity<String>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @PostMapping("deleteWorkout/{userString}/{workoutId}")
  public ResponseEntity<String> deleteWorkout(@PathVariable String workoutId, @PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteWorkout(workoutId);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @PostMapping("deleteExercise/{userString}/{exerciseId}")
  public ResponseEntity<String> deleteExercise(@PathVariable String userString, @PathVariable String exerciseId) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteExercise(exerciseId);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @PostMapping("deleteHistory/{userString}/{historyId}")
  public ResponseEntity<String> deleteHistory(@PathVariable String userString, @PathVariable String historyId) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteHistory(historyId);
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @GetMapping("getWorkout/{userString}/{workoutId}")
  public ResponseEntity<String> sendWorkout(@PathVariable String userString, @PathVariable String workoutId) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = serverService.getWorkout(workoutId);
      String packageString = objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.WorkoutNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @GetMapping("getExercise/{userString}/{exerciseId}")
  public ResponseEntity<String> sendExercise(@PathVariable String userString, @PathVariable String exerciseId) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = serverService.getExercise(exerciseId);
      String packageString = objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.ExerciseNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @GetMapping("getHistory/{userString}/{historyId}")
  public ResponseEntity<String> sendHistory(@PathVariable String userString, @PathVariable String historyId) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      History history = serverService.getHistory(historyId);
      String packageString = objectToJson(history);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    } catch (Exceptions.HistoryNotFoundException e) {
      return sendNotFound(e);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.IllegalIdException e) {
      return sendNotAcceptable(e);
    }
  }

  @GetMapping("getExerciseMap/{userString}")
  public ResponseEntity<String> sendExerciseMap(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      LinkedHashMap<String, String> map = serverService.getMapping(Exercise.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @GetMapping("getWorkoutMap/{userString}")
  public ResponseEntity<String> sendWorkoutMap(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      LinkedHashMap<String, String> map = serverService.getMapping(Workout.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }

  @GetMapping("getHistoryMap/{userString}")
  public ResponseEntity<String> sendHistoryMap(@PathVariable String userString) {
    userString = URLDecoder.decode(userString, StandardCharsets.UTF_8);
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      LinkedHashMap<String, String> map = serverService.getMapping(History.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return sendBadRequest(e);
    } catch (Exceptions.ServerException e) {
      return sendExpectationFailed(e);
    }
  }
}
