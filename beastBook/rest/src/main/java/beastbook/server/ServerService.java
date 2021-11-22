package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import java.io.IOException;
import java.util.Map;

import static beastbook.core.Validation.validateId;

/**
 * Service class for server. Contains core methods to execute rest server's requests.
 */
public class ServerService {
  User user;
  BeastBookPersistence persistence;

  /**
   * Constructor for ServerService.
   *
   * @param user to get server data for
   */
  public ServerService(User user) {
    this.user = user;
    persistence = new BeastBookPersistence(user);
  }

  /**
   * Creates a new User folder in the server files with all necessary files.
   *
   * @throws Exceptions.UserAlreadyExistException if the User already exits in the server
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server
   */
  public void createUser() throws Exceptions.UserAlreadyExistException, Exceptions.ServerException {
    try {
      persistence.createUser();
    } catch (IOException ec) {
      ec.printStackTrace();
      deleteUser();
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Logs in the User that is registered to the ServerService.
   *
   * @throws Exceptions.UserNotFoundException if no User has been registered to the ServerService
   * @throws Exceptions.PasswordIncorrectException if the password given for the User is incorrect
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server
   */
  public void login() throws Exceptions.UserNotFoundException,
      Exceptions.PasswordIncorrectException,
      Exceptions.ServerException {
    try {
      persistence.validateUser();
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   *
   * @param obj
   * @return
   * @throws Exceptions.ServerException
   */
  private String addIdObject(IdClasses obj) throws Exceptions.ServerException {
    try {
      IdHandler ids = persistence.getIds();
      obj = ids.giveId(obj);
      persistence.saveIds(ids);
      persistence.saveIdObject(obj);
      return obj.getId();
    } catch (IOException | Exceptions.IdHandlerNotFoundException
        | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param exercise
   * @param workoutId
   * @throws Exceptions.WorkoutNotFoundException
   * @throws Exceptions.ServerException
   * @throws Exceptions.ExerciseAlreadyExistsException
   * @throws Exceptions.IllegalIdException
   */
  public void addExercise(Exercise exercise, String workoutId) throws Exceptions.WorkoutNotFoundException,
      Exceptions.ServerException,
      Exceptions.ExerciseAlreadyExistsException,
      Exceptions.IllegalIdException {
    validateId(workoutId, Workout.class);
    try {
      Workout workout = persistence.getWorkout(workoutId);
      if (exercise.getId() != null) {
        throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
      }
      IdHandler ids = persistence.getIds();
      Rules.exerciseRules(exercise, workout, ids);
      exercise = (Exercise) ids.giveId(exercise);
      workout.addExercise(exercise.getId());
      persistence.saveIds(ids);
      persistence.saveIdObject(workout);
      persistence.saveIdObject(exercise);
    } catch (IOException | Exceptions.IllegalIdException
        | Exceptions.IdHandlerNotFoundException
        | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param workout
   * @return
   * @throws Exceptions.ServerException
   * @throws Exceptions.WorkoutAlreadyExistsException
   */
  public String addWorkout(Workout workout) throws Exceptions.ServerException,
      Exceptions.WorkoutAlreadyExistsException {
    try {
      Rules.workoutRules(workout, persistence.getIds());
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      throw new Exceptions.ServerException();
    }
    if (workout.getId() != null) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
    return addIdObject(workout);
  }

  /**
   *
   * @param history
   * @throws Exceptions.ServerException
   * @throws Exceptions.HistoryAlreadyExistsException
   */
  public void addHistory(History history) throws Exceptions.ServerException,
      Exceptions.HistoryAlreadyExistsException {
    try {
      Rules.historyRules(history, persistence.getIds());
    } catch (IOException | Exceptions.IdHandlerNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
    if (history.getId() != null) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName() + " : " + history.getDate());
    }
    addIdObject(history);
  }

  /**
   *
   * @param workout
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   * @throws Exceptions.WorkoutNotFoundException
   */
  public void updateWorkout(Workout workout) throws Exceptions.ServerException,
      Exceptions.IllegalIdException,
      Exceptions.WorkoutNotFoundException {
    validateId(workout.getId(), Workout.class);
    if (!getMapping(Workout.class).containsKey(workout.getId())) {
      throw new Exceptions.WorkoutNotFoundException(workout.getId());
    }
    try {
      persistence.saveIdObject(workout);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param exercise
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   * @throws Exceptions.ExerciseNotFoundException
   */
  public void updateExercise(Exercise exercise) throws Exceptions.ServerException,
      Exceptions.IllegalIdException,
      Exceptions.ExerciseNotFoundException {
    validateId(exercise.getId(), Exercise.class);
    if (!getMapping(Exercise.class).containsKey(exercise.getId())) {
      throw new Exceptions.ExerciseNotFoundException(exercise.getId());
    }
    try {
      persistence.saveIdObject(exercise);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @throws Exceptions.ServerException
   */
  public void deleteUser() throws Exceptions.ServerException {
    try {
      persistence.deleteUserDir();
    } catch (IOException e) {
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param id
   * @param cls
   * @throws Exceptions.ServerException
   */
  private void deleteIdObject(String id, Class<?> cls) throws Exceptions.ServerException, Exceptions.IdNotFoundException {
    try {
      IdHandler ids = persistence.getIds();
      ids.removeId(id, cls);
      persistence.saveIds(ids);
      persistence.deleteIdObject(id, cls);
    } catch (IOException | Exceptions.IdHandlerNotFoundException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param id
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   */
  public void deleteExercise(String id) throws Exceptions.ServerException, Exceptions.IllegalIdException {
    try {
      validateId(id, Exercise.class);
      Exercise exercise = persistence.getExercise(id);
      String workoutId = exercise.getWorkoutId();
      Workout workout = persistence.getWorkout(workoutId);
      workout.removeExercise(id);
      persistence.saveIdObject(workout);
      deleteIdObject(id, Exercise.class);
    } catch (Exceptions.IdNotFoundException | Exceptions.ExerciseNotFoundException e) {
      System.err.println("WARNING: Tried to delete an Exercise that does not exist for user " + user.getUsername());
    } catch (Exceptions.WorkoutNotFoundException e) {
      System.err.println("WARNING: Tried to delete an Exercise for workout that does not exist for user " + user.getUsername());
    } catch (IOException e) {
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param id
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   */
  public void deleteWorkout(String id) throws Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(id, Workout.class);
    try {
      Workout workout = persistence.getWorkout(id);
      for (String s : workout.getExerciseIds()) {
        deleteExercise(s);
      }
      deleteIdObject(id, Workout.class);
    } catch (Exceptions.WorkoutNotFoundException| Exceptions.IdNotFoundException e) {
      System.err.println("WARNING: Tried to delete a Workout that does not exist for user " + user.getUsername());
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param id
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   */
  public void deleteHistory(String id) throws Exceptions.ServerException,
      Exceptions.IllegalIdException {
    try {
      validateId(id, History.class);
      deleteIdObject(id, History.class);
    } catch (Exceptions.IdNotFoundException e) {
      System.err.println("WARNING: Tried to delete a History that does not exist for user " + user.getUsername());
    }
  }

  /**
   *
   * @param workoutId
   * @return
   * @throws Exceptions.WorkoutNotFoundException
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   */
  public Workout getWorkout(String workoutId) throws Exceptions.WorkoutNotFoundException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException {
    validateId(workoutId, Workout.class);
    try {
      return persistence.getWorkout(workoutId);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param exerciseId
   * @return
   * @throws Exceptions.ServerException
   * @throws Exceptions.ExerciseNotFoundException
   * @throws Exceptions.IllegalIdException
   */
  public Exercise getExercise(String exerciseId) throws Exceptions.ServerException,
      Exceptions.ExerciseNotFoundException,
      Exceptions.IllegalIdException {
    validateId(exerciseId, Exercise.class);
    try {
      return persistence.getExercise(exerciseId);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param historyId
   * @return
   * @throws Exceptions.HistoryNotFoundException
   * @throws Exceptions.ServerException
   * @throws Exceptions.IllegalIdException
   */
  public History getHistory(String historyId) throws Exceptions.HistoryNotFoundException,
      Exceptions.ServerException,
      Exceptions.IllegalIdException {
    validateId(historyId, History.class);
    try {
      return persistence.getHistory(historyId);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   *
   * @param cls
   * @return
   * @throws Exceptions.ServerException
   */
  public Map<String, String> getMapping(Class cls) throws Exceptions.ServerException {
    try {
      IdHandler ids = persistence.getIds();
      return ids.getMap(cls);
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }
}
