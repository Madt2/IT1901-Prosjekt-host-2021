package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import java.io.IOException;
import java.util.LinkedHashMap;
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
   * Help method for adding saving given object to file for User given in constructor.
   * This also handles giving of ids to object to save.
   *
   * @param obj to add.
   * @return id that IdHandler gave to object.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   */
  private String addIdObject(IdClasses obj) throws Exceptions.ServerException {
    try {
      IdHandler idHandler = persistence.getIdHandler();
      obj = idHandler.giveId(obj);
      persistence.saveIdHandler(idHandler);
      persistence.saveIdObject(obj);
      return obj.getId();
    } catch (IOException | Exceptions.IdHandlerNotFoundException
        | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Adds Exercise object to User directory.
   *
   * @param exercise object to add.
   * @param workoutId of Workout to add Exercise to.
   * @throws Exceptions.WorkoutNotFoundException if no workout with workoutId was found.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.ExerciseAlreadyExistsException if Exercise with name already exists in Workout,
   * or if Exercise object already has an id.
   * @throws Exceptions.IllegalIdException if Id given for workout is not legal.
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
      IdHandler idHandler = persistence.getIdHandler();
      Rules.exerciseRules(exercise, workout, idHandler);
      exercise = (Exercise) idHandler.giveId(exercise);
      workout.addExercise(exercise.getId());
      exercise.setWorkoutId(workoutId);
      persistence.saveIdHandler(idHandler);
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
   * Adds Workout object to User directory.
   *
   * @param workout object to add.
   * @return
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.WorkoutAlreadyExistsException if Exercise with name already exists in User,
   * or if Workout object already has an id.
   */
  public String addWorkout(Workout workout) throws Exceptions.ServerException,
      Exceptions.WorkoutAlreadyExistsException {
    try {
      Rules.workoutRules(workout, persistence.getIdHandler());
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      throw new Exceptions.ServerException();
    }
    if (workout.getId() != null) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
    return addIdObject(workout);
  }

  /**
   * Adds History object to User directory.
   *
   * @param history object to add.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.HistoryAlreadyExistsException if History with name and date already exists in User,
   * or if History object already has an id.
   */
  public void addHistory(History history) throws Exceptions.ServerException,
      Exceptions.HistoryAlreadyExistsException {
    try {
      Rules.historyRules(history, persistence.getIdHandler());
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
   * Saves updated workout object over existing workout object with same id in server.
   * Should only be name that should be changed.
   *
   * @param workout object to update.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id of Workout is illegal.
   * @throws Exceptions.WorkoutNotFoundException if no Workout with Workout object's id exists in server directory.
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
   * Saves updated Exercise object over existing Exercise object with same id in server.
   *
   * @param exercise object to update
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id of Exercise is illegal.
   * @throws Exceptions.ExerciseNotFoundException if no Exercise with Exercise object's id exists in server directory.
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
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    } catch (Exceptions.IdNotFoundException e) {
      throw new Exceptions.ExerciseNotFoundException(e.getMessage());
    }
  }

  /**
   * Deletes user directory for User object given in constructor.
   *
   * @throws Exceptions.ServerException if error further down in the server occurs.
   */
  public void deleteUser() throws Exceptions.ServerException {
    try {
      persistence.deleteUserDir();
    } catch (IOException e) {
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Help method for deleting id-object in user directory.
   *
   * @param id of object to delete.
   * @param cls Class type of object to delete
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IdNotFoundException if idHandler does not have id given in register.
   */
  private void deleteIdObject(String id, Class<?> cls) throws Exceptions.ServerException, Exceptions.IdNotFoundException {
    try {
      IdHandler idHandler = persistence.getIdHandler();
      idHandler.removeId(id, cls);
      persistence.saveIdHandler(idHandler);
      persistence.deleteIdObject(id, cls);
    } catch (IOException | Exceptions.IdHandlerNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Deletes Exercise object with given id from server.
   *
   * @param id of Exercise object to delete.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id given is invalid as Exercise id.
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
   * Deletes Workout object with given id from server.
   *
   * @param id of Workout to delete,
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id given is invalid as Workout id.
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
   * Deletes History object with given id from server.
   *
   * @param id of History to delete,
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id given is invalid as History id.
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
   * Getter for getting workout object with given id from server.
   *
   * @param workoutId id of Workout to get.
   * @return Workout object.
   * @throws Exceptions.WorkoutNotFoundException if no Workout with given id exist in user directory.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id given is invalid as Workout id.
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
   * Getter for getting Exercise object with given id from server.
   *
   * @param exerciseId id of Exercise to get.
   * @return Exercise object.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.ExerciseNotFoundException if no Exercise with given id exist in user directory.
   * @throws Exceptions.IllegalIdException if id given is invalid as Exercise id.
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
   * Getter for getting History object with given id from server.
   *
   * @param historyId id of History to get.
   * @return History object.
   * @throws Exceptions.HistoryNotFoundException if no History with given id exist in user directory.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   * @throws Exceptions.IllegalIdException if id given is invalid as History id.
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
   * Getter for getting Hashmap from IdHandler for given class.
   *
   * @param cls Class to get map for.
   * @return Hashmap of (id : name) mapping for given Class.
   * @throws Exceptions.ServerException if error further down in the server occurs.
   */
  public LinkedHashMap<String, String> getMapping(Class cls) throws Exceptions.ServerException {
    try {
      IdHandler idHandler = persistence.getIdHandler();
      return idHandler.getMap(cls);
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }
}
