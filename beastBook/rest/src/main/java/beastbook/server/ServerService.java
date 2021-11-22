package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import static beastbook.core.Validation.validateId;

/**
 * Service class for server. Contains core methods to execute rest servers requests.
 */

public class
ServerService {

  User user;
  BeastBookPersistence persistence;

  public ServerService(User user) {
    this.user = user;
    persistence = new BeastBookPersistence(user);
  }

  public void createUser() throws Exceptions.UserAlreadyExistException, Exceptions.ServerException {
    try {
      persistence.createUser();
    } catch (IOException ec) {
      ec.printStackTrace();
      deleteUser();
    }
  }

  public void login() throws Exceptions.UserNotFoundException, Exceptions.PasswordIncorrectException, Exceptions.ServerException {
    try {
      persistence.validateUser();
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  private String addIdObject(IdClasses obj) throws Exceptions.ServerException {
    try {
      Id ids = persistence.getIds();
      obj = ids.giveId(obj);
      persistence.saveIds(ids);
      persistence.saveIdObject(obj);
      return obj.getId();
    } catch (IOException | Exceptions.IdHandlerNotFoundException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void addExercise(Exercise exercise, String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException, Exceptions.IllegalIdException {
    validateId(workoutId, Workout.class);
    try {
      Workout workout = persistence.getWorkout(workoutId);
      if (exercise.getId() != null) {
        throw new Exceptions.ExerciseAlreadyExistsException(exercise.getName());
      }
      Id ids = persistence.getIds();
      Rules.ExerciseRules(exercise, workout, ids);
      exercise = (Exercise) ids.giveId(exercise);
      workout.addExercise(exercise.getId());
      persistence.saveIds(ids);
      persistence.saveIdObject(workout);
      persistence.saveIdObject(exercise);
    } catch (IOException | Exceptions.IllegalIdException | Exceptions.IdHandlerNotFoundException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public String addWorkout(Workout workout) throws Exceptions.ServerException, Exceptions.WorkoutAlreadyExistsException {
    try {
      Rules.WorkoutRules(workout, persistence.getIds());
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      throw new Exceptions.ServerException("");
    }
    if (workout.getId() != null) {
      throw new Exceptions.WorkoutAlreadyExistsException(workout.getName());
    }
    return addIdObject(workout);
  }

  public void addHistory(History history) throws Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException {
    try {
      Rules.HistoryRules(history, persistence.getIds());
    } catch (IOException | Exceptions.IdHandlerNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException("");
    }
    if (history.getId() != null) {
      throw new Exceptions.HistoryAlreadyExistsException(history.getName() + " : " + history.getDate());
    }
    addIdObject(history);
  }

  public void updateWorkout(Workout workout) throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.WorkoutNotFoundException {
    validateId(workout.getId(), Workout.class);
    if (!getMapping(Workout.class).containsKey(workout.getId())) {
      throw new Exceptions.WorkoutNotFoundException(workout.getId());
    }
    try {
      persistence.saveIdObject(workout);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void updateExercise(Exercise exercise) throws Exceptions.ServerException, Exceptions.IllegalIdException, Exceptions.ExerciseNotFoundException {
    validateId(exercise.getId(), Exercise.class);
    if (!getMapping(Exercise.class).containsKey(exercise.getId())) {
      throw new Exceptions.ExerciseNotFoundException(exercise.getId());
    }
    try {
      persistence.saveIdObject(exercise);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void deleteUser() throws Exceptions.ServerException {
    try {
      persistence.deleteUserDir();
    } catch (IOException e) {
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  private void deleteIdObject(String id, Class cls) throws Exceptions.ServerException {
    try {
      Id ids = persistence.getIds();
      ids.removeId(id, cls);
      persistence.saveIds(ids);
      persistence.deleteIdObject(id, cls);
    } catch (IOException | Exceptions.IdHandlerNotFoundException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void deleteExercise(String id) throws Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(id, Exercise.class);
    deleteIdObject(id, Exercise.class);
  }

  public void deleteWorkout(String id) throws Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(id, Workout.class);
    try {
      Workout workout = persistence.getWorkout(id);
      for (String s : workout.getExerciseIDs()) {
        deleteExercise(s);
      }
      deleteIdObject(id, Workout.class);
    } catch (Exceptions.WorkoutNotFoundException e) {
      System.err.println("WARNING: Tried to delete a workout that does not exist for user " + user.getUsername());
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void deleteHistory(String id) throws Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(id, History.class);
    deleteIdObject(id, History.class);
  }

  public Workout getWorkout(String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(workoutId, Workout.class);
    try {
      return persistence.getWorkout(workoutId);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public Exercise getExercise(String exerciseID) throws Exceptions.ServerException, Exceptions.ExerciseNotFoundException, Exceptions.IllegalIdException {
    validateId(exerciseID, Exercise.class);
    try {
      return persistence.getExercise(exerciseID);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public History getHistory(String historyID) throws Exceptions.HistoryNotFoundException, Exceptions.ServerException, Exceptions.IllegalIdException {
    validateId(historyID, History.class);
    try {
      return persistence.getHistory(historyID);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public Map<String, String> getMapping (Class cls) throws Exceptions.ServerException {
    try {
      Id ids = persistence.getIds();
      return ids.getMap(cls);
    } catch (Exceptions.IdHandlerNotFoundException | IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }
}
