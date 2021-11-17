package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import com.fasterxml.jackson.databind.introspect.ConcreteBeanPropertyBase;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

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
   * @throws IllegalArgumentException from persistence.createUser() if user directory already exists.
   * @throws IllegalStateException from persistence.createUser() if username is null.
   */
  public void createUser(User user) throws IllegalArgumentException, IllegalStateException {
    persistence.createUser(user);
  }

  /**
   * Adds IId object to user in server's register.
   *
   * @param obj IIdClass object to add.
   * @param username of user to add workout to.
   * @param workout to add exercise to (only use if obj is Exercise object).
   * @throws IllegalArgumentException from persistence.getUser() if user directory already exists. For Exercise
   */
  public void addIdObject(IIdClases obj, String username, @Nullable Workout workout) {
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    obj = ids.giveID(obj);
    if (obj instanceof Workout) {
      user.addWorkout(obj.getId());
    }
    if (obj instanceof Exercise) {
      if (workout == null) {
        throw new IllegalArgumentException("To add an exercise you have to have a workout as argument!");
      }
      if (workout.getId() == null) {
        throw new IllegalArgumentException("Workout must have ID!");
      }
      workout.addExercise(obj.getId());
      persistence.saveIdObject(workout, username);
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
   * @throws IllegalArgumentException from persistence.saveWorkout() if user directory already exists.
   * @throws IllegalStateException if workout does not have ID.
   */
  public void updateWorkout(Workout workout, String username) throws IllegalArgumentException, IllegalStateException {
    try {
      persistence.saveIdObject(workout, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an workout without id, " +
              "if this workout was fetched from user in server it is corrupted, " +
              "if it is a new workout use persistence.addWorkout method");
    }
  }

  /**
   * Update workout in server's register.
   *
   * @param exercise to update.
   * @param username of user to update exercise.
   * @throws IllegalArgumentException from saveExercise() if user directory already exists.
   * @throws IllegalStateException if exercise does not have ID.
   */
  public void updateExercise(Exercise exercise, String username) throws IllegalArgumentException, IllegalStateException {
    try {
      persistence.saveIdObject(exercise, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an exercise without id, " +
              "if this exercise was fetched from user in server it is corrupted, " +
              "if it is a new exercise use addExercise method");
    }
  }

  /**
   * Deletes user in server's register.
   *
   * @param username of user to delete.
   * @throws IllegalArgumentException from deleteUser() if user directory already exists.
   */
  public void deleteUser(String username) throws IllegalArgumentException {
    User user = persistence.getUser(username);
    for (String id : user.getWorkoutIDs()) {
      deleteIdObject(persistence.getWorkout(id, username), username);
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
   * @throws IllegalArgumentException from persistence.getWorkout() if user directory already exists.
   */
  public void deleteIdObject(IIdClases obj, String username) {
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    if (obj instanceof Exercise) {
      Exercise exercise = (Exercise) obj;
      Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
      workout.removeExercise(exercise.getId());
      persistence.saveIdObject(workout, username);
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
   * @return user.
   * @throws IllegalArgumentException from persistence.getUser() if user directory already exists.
   */
  public User getUser(String username) throws IllegalArgumentException {
    return persistence.getUser(username);
  }

  /**
   * Getter for workout from server's register.
   *
   * @param workoutID of workout to get.
   * @param username of user to get workout from.
   * @return workout
   * @throws IllegalArgumentException from persistence.getWorkout() if user directory already exists.
   */
  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException {
    return persistence.getWorkout(workoutID, username);
  }

  /**
   * Getter for exercise from server's register.
   *
   * @param exerciseID of exercise to get.
   * @param username of user to get exercise from.
   * @return exercise
   * @throws IllegalArgumentException from persistence.getExercise() if user directory already exists.
   */
  public Exercise getExercise(String exerciseID, String username) throws IllegalArgumentException {
    return persistence.getExercise(exerciseID, username);
  }

  public History getHistory(String historyID, String username) throws IllegalArgumentException {
    return persistence.getHistory(historyID, username);
  }

  /**
   * Getter for workout's name from server's register.
   *
   * @param id of object to get name off.
   * @param username of user to get workout's name from.
   * @return object's name
   */
  public String getName(String id, String username, Class cls) {
    Id ids = persistence.getIds(username);
    return ids.getName(id, cls);
  }

  /**
   * Converts object to json string format.
   *
   * @param object to serialize
   * @return serialized object, returns null if conversion fails.
   */
  public String objectToJson(Object object) {
    return persistence.objectToJson(object);
  }

  /**
   * Converts json string to object.
   *
   * @param jsonString json string to deserialize.
   * @param cls type of class to be converted.
   * @return object deserialized (must be cast to class type when used), returns null if conversion fails
   */
  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    return persistence.jsonToObject(jsonString, cls);
  }
}
