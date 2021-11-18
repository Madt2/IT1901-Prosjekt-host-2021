package beastbook.server;

import beastbook.core.*;
import beastbook.json.BeastBookPersistence;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.HashMap;

/**
 * Service class for server. Contains core methods to execute rest servers requests.
 */
@Service
public class
ServerService {
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
  public void createUser(User user) throws IllegalArgumentException, IOException {
    try {
      persistence.createUser(user);
    } catch (IOException e) {
      //deleteUser(user.getUsername());
      throw e;
    }
  }

  /**
   * Adds IId object to user in server's register.
   *
   * @param obj IIdClass object to add.
   * @param username of user to add workout to.
   * @param workoutId to add exercise to (only use if obj is Exercise object).
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws NullPointerException if obj is instance of Exercise and workoutId is null.
   * @throws IOException if persistence fails to read from or write to file.
   */
  public String addIdObject(IdClasses obj, String username, String workoutId) throws NullPointerException, IllegalStateException, IOException {
    User user = persistence.getUser(username);
    System.out.println("2");
    System.out.println(username);
    Id ids = persistence.getIds(username);
    System.out.println("3");
    obj = ids.giveId(obj);
//    if (obj instanceof Workout) {
//      user.addWorkout(obj.getId());
//    }
    if (obj.getClass() == Exercise.class) {
      if (workoutId == null) {
        throw new IllegalArgumentException("WorkoutId cannot be null");
      }
      Workout workout = persistence.getWorkout(workoutId, username);
      workout.addExercise(obj.getId());
      persistence.saveIdObject(workout, username);
      Exercise exercise = (Exercise) obj;
      exercise.setWorkoutID(workout.getId());
      obj = (IdClasses) exercise;
    }
//    if (obj instanceof History) {
//      user.addHistory(obj.getId());
//    }
    persistence.saveIds(ids, username);
    persistence.saveUser(user);
    persistence.saveIdObject(obj, username);
    return obj.getId();
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
    persistence.saveIdObject((IdClasses) workout, username);
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
    persistence.saveIdObject((IdClasses) exercise, username);
  }

  /**
   * Deletes user in server's register.
   *
   * @param username of user to delete.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read or write to file.
   */
  public void deleteUser(String username) throws IllegalArgumentException, IOException {
    /*
    Id ids = persistence.getIds(username);
    for (String id : ids.) {
      deleteIdObject((IdClasses) persistence.getWorkout(id, username), username);
    }
    for (String id : user.getHistoryIDs()) {
      deleteIdObject((IdClasses) persistence.getHistory(id, username), username);
    }
      persistence.deleteUserDir(username);

     */
  }

  /**
   * Deletes workout in server's register.
   *
   * @param obj IdClass object to delete.
   * @param username of user to delete workout.
   * @throws IllegalArgumentException if user with username does not exist.
   * @throws IOException if persistence fails to read frim or write to file.
   */
  public void deleteIdObject(IdClasses obj, String username) throws IOException {
    Id ids = persistence.getIds(username);
    if (obj instanceof Exercise) {
      Exercise exercise = (Exercise) obj;
      Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
      workout.removeExercise(exercise.getId());
      persistence.saveIdObject((IdClasses) workout, username);
      obj = (IdClasses) exercise;
    }
//    if (obj instanceof Workout) {
//      Workout workout = (Workout) obj;
//      for (String id : workout.getExerciseIDs()) {
//        deleteIdObject((IdClasses) persistence.getExercise(id, username), username);
//      }
//      ids.removeId(obj.getId(), obj.getClass());
//    }
//    if (obj instanceof History) {
//      user.removeHistory(obj.getId());
//    }
    ids.removeId(obj.getId(), obj.getClass());
    persistence.saveIdObject(obj, username);
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
//  public String getName(String id, String username, Class cls) throws IOException {
//    Id ids = persistence.getIds(username);
//    return ids.getName(id, cls);
//  }

  public HashMap<String, String> getMapping (String username, Class cls) throws IOException {
    Id ids = persistence.getIds(username);
    return ids.getMap(cls);
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

  public static void main(String[] args) throws IOException {
    String username = "test";
    ServerService service = new ServerService();
    service.createUser(new User(username, "password"));
    //Workout workout = new Workout("testW");
    //Exercise exercise = new Exercise("testE", 90, 35, 22, 33, 94);
    //service.addIdObject(exercise, username, "OS");
  }
}
