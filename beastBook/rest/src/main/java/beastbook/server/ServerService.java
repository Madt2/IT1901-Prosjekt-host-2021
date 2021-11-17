package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class for server. Contains core methods to execute rest servers requests.
 */
@Service
public class ServerService {
  BeastBookPersistence persistence = new BeastBookPersistence();

  /**
   * Creates user in server's register.
   *
   * @param user to create.
   * @throws IllegalArgumentException if user directory already exists.
   * @throws NullPointerException if username for user is null.
   * @throws IOException if writing to file fails or if it fails to create folder.
   *                     Deletes all files generated if this process fails.
   */
  public void createUser(User user) throws IllegalArgumentException, IllegalStateException, IOException {
    try {
      persistence.createUser(user);
    } catch (IOException e) {
      deleteUser(user.getUsername());
      throw e;
    }
  }

  /**
   * Adds IId object to user in server's register.
   *
   * @param obj IIdClass object to add.
   * @param username of user to add workout to.
   * @param workout to add exercise to (only use if obj is Exercise object).
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws NullPointerException if obj is instance of Exercise and workout is null.
   * @throws IllegalStateException if workout's id is null.
   * @throws IOException if persistence fails to read from or write to file.
   */
  public void addIdObject(IIdClases obj, String username, Workout workout) throws NullPointerException, IOException, IllegalStateException {
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    obj = ids.giveID(obj);
    if (obj instanceof Workout) {
      user.addWorkout(obj.getId());
    }
    if (obj instanceof Exercise) {
      if (workout == null) {
        throw new NullPointerException("To add an exercise you have to have a workout as argument!");
      }
      if (workout.getId() == null) {
        throw new IllegalStateException("Workout must have ID!");
      }
      workout.addExercise(obj.getId());
      persistence.saveIdObject((IIdClases) workout, username);
      Exercise exercise = (Exercise) obj;
      exercise.setWorkoutID(workout.getId());
      obj = exercise;
    }
    if (obj instanceof History) {
      user.addHistory(obj.getId());
    }
    persistence.saveIds(ids, username);
    persistence.saveUser(user);
    persistence.saveIdObject(obj, username);
  }

  /**
   * Update workout in server's register.
   *
   * @param workout to update.
   * @param username of user to update workout.
   * @throws IllegalStateException if workout does not have ID.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to write to file.
   */
  public void updateWorkout(Workout workout, String username) throws IllegalStateException, IOException, IllegalArgumentException {
    persistence.saveIdObject((IIdClases) workout, username);
  }

  /**
   * Update workout in server's register.
   *
   * @param exercise to update.
   * @param username of user to update exercise.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IllegalStateException if exercise does not have ID.
   * @throws IOException if persistence fails to write to file.
   */
  public void updateExercise(Exercise exercise, String username) throws IllegalArgumentException, IllegalStateException, IOException {
    persistence.saveIdObject(exercise, username);
  }

  /**
   * Deletes user in server's register.
   *
   * @param username of user to delete.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read or write to file.
   */
  public void deleteUser(String username) throws IllegalArgumentException, IOException {
    User user = persistence.getUser(username);
    for (String id : user.getWorkoutIDs()) {
      deleteIdObject((IIdClases) persistence.getWorkout(id, username), username);
    }
    for (String id : user.getHistoryIDs()) {
      deleteIdObject(persistence.getHistory(id, username), username);
    }
      persistence.deleteUserDir(username);
  }

  /**
   * Deletes workout in server's register.
   *
   * @param obj IIdClass object to delete.
   * @param username of user to delete workout.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read frim or write to file.
   */
  public void deleteIdObject(IIdClases obj, String username) throws IOException {
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    if (obj instanceof Exercise) {
      Exercise exercise = (Exercise) obj;
      Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
      workout.removeExercise(exercise.getId());
      persistence.saveIdObject((IIdClases) workout, username);
      obj = exercise;
    }
    if (obj instanceof Workout) {
      Workout workout = (Workout) obj;
      for (String id : workout.getExerciseIDs()) {
        deleteIdObject(persistence.getExercise(id, username), username);
      }
      user.removeWorkout(workout.getId());
    }
    if (obj instanceof History) {
      user.removeHistory(obj.getId());
    }
    persistence.saveIdObject(obj, username);
    persistence.saveUser(user);
    persistence.saveIds(ids, username);
  }

  /**
   * Getter for user from server's register.
   *
   * @param username of user to get.
   * @return user object.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read from file.
   */
  public User getUser(String username) throws IllegalArgumentException, IOException {
    return persistence.getUser(username);
  }

  /**
   * Getter for workout from server's register.
   *
   * @param workoutID of workout to get.
   * @param username of user to get workout from.
   * @return workout object.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read from file.
   */
  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException, IOException {
    return persistence.getWorkout(workoutID, username);
  }

  /**
   * Getter for exercise from server's register.
   *
   * @param exerciseID of exercise to get.
   * @param username of user to get exercise from.
   * @return exercise object.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read from file.
   */
  public Exercise getExercise(String exerciseID, String username) throws IllegalArgumentException, IOException {
    return persistence.getExercise(exerciseID, username);
  }

  /**
   * Getter for history from server's register.
   *
   * @param historyID of history to get.
   * @param username of user to get history from.
   * @return history object.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read from file.
   */
  public History getHistory(String historyID, String username) throws IllegalArgumentException, IOException {
    return persistence.getHistory(historyID, username);
  }

  /**
   * Getter for workout's name from server's register.
   *
   * @param id of object to get name off.
   * @param username of user to get workout's name from.
   * @return object's name
   * @throws IOException if persistence fails to read from file.
   */
  public String getName(String id, String username, Class cls) throws IOException {
    Id ids = persistence.getIds(username);
    return ids.getName(id, cls);
  }

  /**
   * Converts object to json string format.
   *
   * @param object to serialize
   * @return serialized object, returns null if conversion fails.
   * @throws JsonProcessingException if serialization fails.
   */
  public String objectToJson(Object object) throws JsonProcessingException {
    return persistence.objectToJson(object);
  }

  /**
   * Converts json string to object.
   *
   * @param jsonString json string to deserialize.
   * @param cls type of class to be converted.
   * @return object deserialized (must be cast to class type when used), returns null if conversion fails.
   * @throws JsonProcessingException if deserialization fails.
   */
  public Object jsonToObject(String jsonString, Class cls) throws JsonProcessingException {
    return persistence.jsonToObject(jsonString, cls);
  }
}
