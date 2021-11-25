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
 * Class for receiving http requests from client. It uses ServerService class to execute request.
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

  /**
   * Help method for sending not found responseEntity. This is used for sending not found exceptions to client.
   *
   * @param e Exception object.
   * @return ResponseEntity with httpStatus not found and exception type in response body.
   */
  private ResponseEntity<String> sendNotFound(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
  }

  /**
   * Help method for sending bad request responseEntity. This is used for sending bad request exceptions to client.
   *
   * @param e Exception object.
   * @return ResponseEntity with httpStatus bad request and exception type in response body.
   */
  private ResponseEntity<String> sendBadRequest(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Help method for sending expectation failed responseEntity.
   * This is used for sending expectation failed exceptions to client.
   *
   * @param e Exception object.
   * @return ResponseEntity with httpStatus expectation failed and exception type in response body.
   */
  private ResponseEntity<String> sendExpectationFailed(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.EXPECTATION_FAILED);
  }

  /**
   * Help method for sending not acceptable responseEntity. This is used for sending not acceptable exceptions to
   * client.
   *
   * @param e Exception object.
   * @return ResponseEntity with httpStatus not acceptable and exception type in response body.
   */
  private ResponseEntity<String> sendNotAcceptable(Exception e) {
    return new ResponseEntity<>(e.getClass().getSimpleName(), HttpStatus.NOT_ACCEPTABLE);
  }

  /**
   * Creates a User using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   *
   * @param userString encoded serialized User object in string format sent via http.
   * @return responseEntity with http status created if operation is successful,
   * otherwise a responseEntity with appropriate exception.
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

  /**
   * User login using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Add given Workout using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param workoutString encoded serialized Workout object in string format string sent via http.
   * @param userString encoded serialized User object in string format sent via http.
   * @return responseEntity with http status created if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Add given Exercise to Workout with given workoutId using ServerService's method call at request from client using
   * sent User object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param workoutId id of Workout to add exercise to
   * @param exerciseString encoded serialized Exercise object in string format sent via http.
   * @return responseEntity with http status created if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Add given History using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param historyString encoded serialized History object in string format sent via http.
   * @return responseEntity with http status created if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Updates given Workout using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param workoutString encoded serialized Workout object in string format string sent via http.
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Updates given Exercise using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param exerciseString encoded serialized Exercise object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Deletes a User using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Deletes Workout with given id using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param workoutId id of Workout to delete.
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Deletes Exercise with given id using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param exerciseId id of Exercise to delete.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Deletes History with given id using ServerService's method call at request from client using sent User object.
   * If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param historyId id of History to delete.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends Workout with given id to client using ServerService's method call at request from client using sent User
   * object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param workoutId id of Workout to send.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends Exercise with given id to client using ServerService's method call at request from client using sent User
   * object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param exerciseId id of Exercise to send.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends History with given id to client using ServerService's method call at request from client using sent User
   * object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @param historyId id of History to send.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends hashmap with (exerciseId : name) to client using ServerService's method call at request from client using
   * sent User object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends hashmap with (workoutId : name) to client using ServerService's method call at request from client using
   * sent User object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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

  /**
   * Sends hashmap with (historyId : name;date) to client using ServerService's method call at request from client
   * using sent User object. If exception occurs it will send a response package with appropriate exception.
   * Method decodes encoded pathVariables using UTF_8, and deserializes object.
   *
   * @param userString encoded serialized User object in string format string sent via http.
   * @return responseEntity with http status ok if operation is successful,
   * otherwise a responseEntity with appropriate exception.
   */
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
