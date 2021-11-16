package beastbook.server;

import beastbook.core.Exercise;
import beastbook.core.Id;
import beastbook.core.User;
import beastbook.core.Workout;
import beastbook.json.BeastBookPersistence;
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
   * Adds workout to user in server's register.
   *
   * @param workout to add.
   * @param username of user to add workout to.
   * @throws IllegalArgumentException from persistence.getUser() if user directory already exists.
   */
  public void addWorkout(Workout workout, String username) throws IllegalArgumentException {
    //Todo maybe try catch?
    User user = persistence.getUser(username);
    if (workout.getID() == null) {
      Id ids = persistence.getIds(username);
      String id = giveID(username, Workout.class);
      workout.setID(giveID(username, Workout.class)); //Should never throw exception
      ids.addWorkoutID(id);
      ids.addWorkoutIDEntry(id, workout.getName());
      persistence.saveIds(ids, username);
    }
    user.addWorkout(workout.getID());
    persistence.saveUser(user);
    persistence.saveWorkout(workout, username);
  }

  /**
   * Adds exercise to user in server's register.
   *
   * @param exercise to add.
   * @param workoutID of workout to add to.
   * @param username of user to add exercise to.
   * @throws IllegalArgumentException from persistence.getWorkout() if user directory already exists.
   */
  public void addExercise(Exercise exercise, String workoutID, String username) throws IllegalArgumentException {
    //Todo maybe try catch?
    Workout workout = persistence.getWorkout(workoutID, username);
    if (exercise.getID() == null) {
      Id ids = persistence.getIds(username);
      String id = giveID(username, Exercise.class);
      exercise.setID(id);
      ids.addExerciseID(id);
      ids.addExerciseIDEntry(id, exercise.getName());
      persistence.saveIds(ids, username);
    }
    workout.addExercise(exercise.getID());
    exercise.setWorkoutID(workout.getID());
    persistence.saveWorkout(workout, username);
    persistence.saveExercise(exercise, username);
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
      persistence.saveWorkout(workout, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an workout without id, " +
              "if this workout was gotten from user in server it is corrupted, " +
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
      persistence.saveExercise(exercise, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an exercise without id, " +
              "if this exercise was gotten from user in server it is corrupted, " +
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
    persistence.deleteUser(username);
  }

  /**
   * Deletes workout in server's register.
   *
   * @param workoutID of workout to delete.
   * @param username of user to delete workout.
   * @throws IllegalArgumentException from persistence.getWorkout() if user directory already exists.
   */
  public void deleteWorkout(String workoutID, String username) throws IllegalArgumentException {
    Workout workout = persistence.getWorkout(workoutID, username);
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    try {
      persistence.deleteWorkout(workout, username);
      user.removeWorkout(workout.getID());
      persistence.saveUser(user);
      String id = workout.getID();
      ids.removeWorkoutID(id);
      ids.removeWorkoutIDEntry(id);
      persistence.saveIds(ids, username);
    } catch (IllegalStateException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    }
  }

  /**
   * Deletes exercise in server's register.
   *
   * @param exerciseID of exercise to delete.
   * @param username of user to delete exercise.
   * @throws IllegalArgumentException from persistence.getExercise() or
   * persistence.getWorkout() if user directory already exists.
   */
  public void deleteExercise(String exerciseID, String username) throws IllegalArgumentException {
    Exercise exercise = persistence.getExercise(exerciseID, username);
    Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
    Id ids = persistence.getIds(username);
    try {
      persistence.deleteExercise(exercise, username);
      String id = exercise.getID();
      workout.removeExercise(id);
      ids.removeExerciseID(id);
      ids.removeExerciseIDEntry(id);
      persistence.saveWorkout(workout, username);
      persistence.saveIds(ids, username);
    } catch (IllegalStateException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    }
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

  /**
   * Getter for workout's name from server's register.
   *
   * @param workoutID to get name off.
   * @param username of user to get workout's name from.
   * @return workout's name
   */
  public String getWorkoutName(String workoutID, String username) {
    Id ids = persistence.getIds(username);
    return ids.getWorkoutIDName(workoutID);
  }

  /**
   * Getter for exercise's name from server's register.
   *
   * @param exerciseID to get name off.
   * @param username of user to get exercise's name from.
   * @return exercise's name
   */
  public String getExerciseName(String exerciseID, String username) {
    Id ids = persistence.getIds(username);
    return ids.getExerciseIDName(exerciseID);
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
   * Converts json string to object
   *
   * @param jsonString json string to deserialize.
   * @param cls
   * @return object deserialized (must be casted to class type when used.), returns null if conversion fails
   */
  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    return persistence.jsonToObject(jsonString, cls);
  }

  /**
   * Validation for IDs.
   *
   * @param id to check.
   * @param cls class type to check ID for.
   * @throws IllegalArgumentException if ID does not contain correct amounts of characters,
   * or if ID does not use valid characters.
   */
  private <T> void validateID(String id, Class<T> cls) throws IllegalArgumentException {
    if (id.length() != 2) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
    }
    final String legalChars;
    if (cls == Exercise.class) {
      legalChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    } else {
      legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }
    if (!(legalChars.contains(String.valueOf(id.charAt(0)))) &&
            legalChars.contains(String.valueOf(id.charAt(1)))) {
      throw new IllegalArgumentException("ID does not use correct characters!");
    }
  }

  /**
   * Generates ID for given class.
   *
   * @param cls to generate ID for.
   * @return id
   * @throws IllegalArgumentException if class type is not valid.
   */
  private <T> String generateID(Class<T> cls) throws IllegalArgumentException {
    final String legalChars;
    if (cls == Exercise.class) {
      legalChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    } else if (cls == Workout.class) {
      legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    } else {
      throw new IllegalArgumentException("Class must be type Exercise or Workout!");
    }
    int index1 = ThreadLocalRandom.current().nextInt(legalChars.length());
    int index2 = ThreadLocalRandom.current().nextInt(legalChars.length());
    String id = "" + legalChars.charAt(index1) + legalChars.charAt(index2);
    validateID(id, cls);
    return id;
  }

  /**
   * Generate ID until it find an available ID.
   *
   * @param username to generate ID for.
   * @param cls class type to generate ID for.
   * @return valid ID to use for class
   * @throws IllegalArgumentException if class type is invalid.
   */
  private <T> String giveID(String username, Class<T> cls) throws IllegalArgumentException {
    Id ids = persistence.getIds(username);
    String id;
    int catchTries = 3;
    while (true) {
      try {
        id = generateID(cls);
        if (!ids.hasWorkoutID(id)) {
          break;
        }
      } catch (IllegalArgumentException e) {
        if (e.getMessage().equals("Class must be type Exercise or Workout!")) {
          throw e;
        } else {
          catchTries--;
          System.err.println(e.getMessage());
          if(catchTries <= 0) {
            throw new Error("Something is wrong with ID generator!");
          }
        }
      }
    }
    return id;
  }
}
