package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import java.io.IOException;
import java.util.Map;
import java.util.List;

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

  public void addExercise(Exercise exercise, String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException, Exceptions.ExerciseAlreadyExistsException {
    try {
      Workout workout = persistence.getWorkout(workoutId);
      Rules.ExerciseRules(exercise, workout);
      Id ids = persistence.getIds();
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
    Rules.WorkoutRules(workout);
    return addIdObject(workout);
  }

  public void addHistory(History history) throws Exceptions.ServerException, Exceptions.HistoryAlreadyExistsException {
    Rules.HistoryRules(history);
    addIdObject(history);
  }

  public void updateWorkout(Workout workout) throws Exceptions.ServerException {
    try {
      persistence.saveIdObject(workout);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void updateExercise(Exercise exercise) throws Exceptions.ServerException {
    try {
      persistence.saveIdObject(exercise);
    } catch (IOException | Exceptions.IdNotFoundException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public void deleteUser() throws Exceptions.ServerException {
//    try {
//      Id ids = persistence.getIds();
//      List<String> exerciseIds = (List<String>) ids.getMap(Exercise.class).keySet();
//      List<String> workoutIds = (List<String>) ids.getMap(Workout.class).keySet();
//      List<String> historyIds = (List<String>) ids.getMap(History.class).keySet();
//      for (String id : exerciseIds) {
//        deleteExercise(id);
//      }
//      for (String id : workoutIds) {
//        deleteWorkout(id);
//      }
//      for (String id : historyIds) {
//        deleteHistory(id);
//      }
//      persistence.deleteUserDir();
//    } catch (IOException | Exceptions.IdHandlerNotFoundException e) {
//      e.printStackTrace();
//      throw new Exceptions.ServerException(e.getMessage());
//    }
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

  public void deleteExercise(String id) throws Exceptions.ServerException {
    deleteIdObject(id, Exercise.class);
  }

  public void deleteWorkout(String id) throws Exceptions.ServerException {
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

  public void deleteHistory(String id) throws Exceptions.ServerException {
    deleteIdObject(id, History.class);
  }

  public Workout getWorkout(String workoutId) throws Exceptions.WorkoutNotFoundException, Exceptions.ServerException {
    try {
      return persistence.getWorkout(workoutId);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public Exercise getExercise(String exerciseID) throws Exceptions.ServerException, Exceptions.ExerciseNotFoundException {
    try {
      return persistence.getExercise(exerciseID);
    } catch (IOException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException(e.getMessage());
    }
  }

  public History getHistory(String historyID) throws Exceptions.HistoryNotFoundException, Exceptions.ServerException {
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
