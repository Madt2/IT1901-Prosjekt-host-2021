package beastbook.server;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import beastbook.json.internal.UserDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@RestController
public class ServerController {

  private ServerService serverService;
  ObjectMapper mapper;

  public ServerController() {
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  private void setService(User user) {
    serverService = new ServerService(user);
  }

  private String objectToJson(Object object) throws Exceptions.ServerException {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  private Object jsonToObject(String jsonString, Class cls) throws Exceptions.BadPackageException {
    try {
      return mapper.readValue(jsonString, cls);
    } catch (JsonProcessingException e) {
      throw new Exceptions.BadPackageException(e.getMessage());
    }
  }

  @PostMapping("createUser/")
  public ResponseEntity createUser(@RequestBody String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.createUser();
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.UserAlreadyExistException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("login/{userString}")
  public ResponseEntity login(@RequestBody String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.login();
      return new ResponseEntity<>("", HttpStatus.OK);
    } catch (Exceptions.UserNotFoundException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    } catch (Exceptions.PasswordIncorrectException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping("addWorkout/{userString}/{workoutString}")
  public ResponseEntity<String> addWorkout(@PathVariable String workoutString, @PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = (Workout) jsonToObject(workoutString, Workout.class);
      String workoutId = serverService.addWorkout(workout);
      return new ResponseEntity<String>(workoutId, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.WorkoutAlreadyExistsException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("addExercise/{userString}/{workoutId}/{exerciseString}")
  public ResponseEntity<String> addExercise(@PathVariable String userString, @PathVariable String workoutId, @PathVariable String exerciseString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = (Exercise) jsonToObject(exerciseString, Exercise.class);
      serverService.addExercise(exercise, workoutId);
      return new ResponseEntity<String>("Succsese", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.WorkoutNotFoundException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
    } catch (Exceptions.ExerciseAlreadyExistsException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("addHistory/{userString}/{historyString}")
  public ResponseEntity<String> addHistory(@PathVariable String userString, @PathVariable String historyString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      History history = (History) jsonToObject(historyString, History.class);
      serverService.addHistory(history);
      return new ResponseEntity<String>("succsess", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    } catch (Exceptions.HistoryAlreadyExistsException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("updateWorkout/{userString}/{workoutString}")
  public ResponseEntity<String> updateWorkout(@PathVariable String workoutString, @PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = (Workout) jsonToObject(workoutString, Workout.class);
      serverService.updateWorkout(workout);
      return new ResponseEntity<>("Success in updating workout", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("updateExercise/{userString}/{exerciseString}")
  public ResponseEntity<String> updateExercise(@PathVariable String userString, @PathVariable String exerciseString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = (Exercise) jsonToObject(exerciseString, Exercise.class);
      serverService.updateExercise(exercise);
      return new ResponseEntity<>("Success in updating Exercise",HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("deleteUser/{userString}")
  public ResponseEntity<String> deleteUser(@PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteUser();
      return new ResponseEntity<>("Success in deleting User", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("deleteWorkout/{userString}/{workoutId}")
  public ResponseEntity<String> deleteWorkout(@PathVariable String workoutId, @PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteWorkout(workoutId);
      return new ResponseEntity<>("Success in deleting Workout", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("deleteExercise/{userString}/{exerciseId}")
  public ResponseEntity<String> deleteExercise(@PathVariable String userString, @PathVariable String exerciseId) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteExercise(exerciseId);
      return new ResponseEntity<>("Success in deleting Exercise", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("deleteHistory/{userString}/{historyId}")
  public ResponseEntity<String> deleteHistory(@PathVariable String userString, @PathVariable String historyId) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      serverService.deleteHistory(historyId);
      return new ResponseEntity<>("Success in deleting Workout", HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("getWorkout/{userString}/{workoutId}")
  public ResponseEntity<String> sendWorkout(@PathVariable String userString, @PathVariable String workoutId) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Workout workout = serverService.getWorkout(workoutId);
      String packageString = objectToJson(workout);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.WorkoutNotFoundException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("getExercise/{userString}/{exerciseId}")
  public ResponseEntity<String> sendExercise(@PathVariable String userString, @PathVariable String exerciseId) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Exercise exercise = serverService.getExercise(exerciseId);
      String packageString = objectToJson(exercise);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    } catch (Exceptions.ExerciseNotFoundException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("getHistory/{userString}/{historyId}")
  public ResponseEntity<String> sendHistory(@PathVariable String userString, @PathVariable String historyId) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      History history = serverService.getHistory(historyId);
      String packageString = objectToJson(history);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    } catch (Exceptions.HistoryNotFoundException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName(), HttpStatus.NOT_FOUND);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("getExerciseMap/{userString}")
  public ResponseEntity<String> sendExerciseMap(@PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Map<String, String> map = serverService.getMapping(Exercise.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("getWorkoutMap/{userString}")
  public ResponseEntity<String> sendWorkoutMap(@PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Map<String, String> map = serverService.getMapping(Workout.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("getHistoryMap/{userString}")
  public ResponseEntity<String> sendHistoryMap(@PathVariable String userString) {
    try {
      User user = (User) jsonToObject(userString, User.class);
      setService(user);
      Map<String, String> map = serverService.getMapping(History.class);
      String packageString = objectToJson(map);
      return new ResponseEntity<>(packageString, HttpStatus.OK);
    } catch (Exceptions.BadPackageException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exceptions.ServerException e) {
      return new ResponseEntity<String>(e.getClass().getSimpleName() + ":" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }
}
