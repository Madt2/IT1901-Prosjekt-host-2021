package beastbook.json;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * Class for saving and loading objects to and from file.
 */
public class BeastBookPersistence {
  private ObjectMapper mapper;
  private String userPath;
  private String workoutFolderPath;
  private String exerciseFolderPath;
  private String historyFolderPath;

  public BeastBookPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  /**
   * Sets path to user folder which contains all user data.
   *
   * @param username of user to set path to.
   * @throws IllegalArgumentException if username is null.
   */
  private void setUserPath(String username) throws IllegalArgumentException {
    if (username == null) {
      throw new IllegalArgumentException("username cannot be null");
    }
    userPath = System.getProperty("user.home") + "/" + username;
    workoutFolderPath = userPath + "/Workouts";
    exerciseFolderPath = userPath + "/Exercises";
    historyFolderPath = userPath + "/Histories";
  }

  /**
   * Checks if user exists in filesystem. Also sets userPath based on username param.
   *
   * @param username for user to check.
   * @return true if user folder exists, false otherwise.
   * @throws IllegalArgumentException from setUserPath() if user is null.
   */
  private boolean userExists(String username) throws IllegalArgumentException {
    setUserPath(username);
    File user = new File(userPath);
    return user.isDirectory();
  }

  /**
   * Validation to check if user for username exists
   *
   * @param username for user folder to check.
   * @throws IllegalArgumentException from setUserPath() if user is null, or if user does not exist.
   */
  private void validateUsername(String username) throws IllegalArgumentException {
    if (!userExists(username)) {
      throw new IllegalArgumentException("User does not exist");
    }
  }

  /**
   * Creates folder at given path.
   *
   * @param path to where to create folder.
   * @throws IllegalArgumentException if path format is incorrect.
   */
  private void createFolder(String path) throws IllegalArgumentException {
    File folder = new File(path);
    if (folder.mkdir()) {
      System.out.println("Folder: " + path + " was created.");
    } else {
      throw new IllegalArgumentException("Path is incorrect format!");
    }
  }

  //Todo good enough catches? should show better that it could not write to file.

  /**
   * Object to write to file on filepath.
   *
   * @param object to write.
   * @param filepath path to file
   */
  private void writeObjectToFile(Object object, String filepath) {
    File file = new File(filepath);
    try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
      mapper.writeValue(writer, object);
    } catch (JsonMappingException e) {
      System.err.println("Could not deserialize object: " + object.getClass());
    } catch (JsonGenerationException e) {
      System.err.println("Could not serialize object: " + object.getClass());
    } catch (IOException e) {
      System.err.println("IO error when writing object " + object.getClass() + " to file!");
    }
  }

  /**
   * Read and deserialize object from file.
   *
   * @param file file to read from.
   * @param cls class type for object to read.
   * @return object, or null if read fails.
   */
  private <T> Object readObjectFromFile(File file, Class<T> cls) {
    try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
      return mapper.readValue(reader, cls);
    } catch (JsonMappingException e) {
      System.err.println("Could not deserialize file: " + file.getName());
    } catch (JsonGenerationException e) {
      System.err.println("Could not serialize file: " + file.getName());
    } catch (IOException e) {
      System.err.println("IO error when reading file:  " + file.getName());
    }
    return null;
  }

  //Todo should show better if file could not be deleted

  /**
   * Deletes file given
   *
   * @param file to delete
   */
  private void deleteFile(File file) {
    if (file.delete()) {
      System.out.println("File: " + file.getName() + " deleted");
    } else {
      System.err.println("Could not delete file " + file.getName());
    }
  }

  /**
   * Getter for file at path.
   *
   * @param path to file
   * @return File at path
   * @throws IllegalArgumentException if path is null, or if file at path does not exist.
   */
  private File getFile(String path) throws IllegalArgumentException {
    if (path == null) {
      //Todo weird throw?
      throw new IllegalArgumentException("path is incorrect!");
    }
    File file = new File(path);
    if (!file.isFile()) {
      throw new IllegalArgumentException("File: " + path + " is missing!");
    }
    return file;
  }

  /**
   * Creates user directory with username. Contains Exercises and Workouts folder,
   * and userProp (file where user class is stored) and IDs (file where Id class is stored).
   *
   * @param user to create directory for.
   * @throws IllegalArgumentException if user directory already exists.
   * @throws IllegalStateException if username is null.
   */
  public void createUser(User user) throws IllegalArgumentException, IllegalStateException {
    if (user.getUsername() == null) {
      throw new IllegalStateException("User must have username");
    }
    if(userExists(user.getUsername())) {
      throw new IllegalArgumentException("User already exists, delete " + user.getUsername() + " or use another username!");
    }
    createFolder(userPath);
    createFolder(workoutFolderPath);
    createFolder(exerciseFolderPath);
    createFolder(historyFolderPath);
    saveUser(user);
    saveIds(new Id(), user.getUsername());
  }

  /**
   * Saves exercise object to file.
   *
   * @param exercise to save.
   * @param username of user to save exercise to.
   * @throws IllegalStateException if exercise does not have ID.
   * @throws IllegalArgumentException from validateUsername() if userame is null.
   */
  public void saveExercise(Exercise exercise, String username) throws IllegalStateException, IllegalArgumentException {
    validateUsername(username);
    if (exercise.getID() == null) {
      throw new IllegalStateException("Exercise must have ID (dont set manually, use getID from serverService!)");
    }
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    writeObjectToFile(exercise, filepath);
    System.out.println("Saved exercise " + exercise.getName() + " to " + exerciseFolderPath);
  }

  /**
   * Saves workout object to file.
   *
   * @param workout to save.
   * @param username of user to save workout to.
   * @throws IllegalArgumentException if workout does not have ID.
   * @throws IllegalStateException from validateUsername() if username is null.
   */
  public void saveWorkout(Workout workout, String username) throws IllegalArgumentException, IllegalStateException {
    validateUsername(username);
    if (workout.getID() == null) {
      throw new IllegalStateException("Workout must have ID (don't set manually, use getID from restServer!)");
    }
    String filepath = workoutFolderPath + "/" + workout.getID();
    writeObjectToFile(workout, filepath);
    System.out.println("Saved workout " + workout.getName() + " to " + workoutFolderPath);
  }

  public void saveHistory(History history, String username) throws IllegalArgumentException, IllegalStateException {
    validateUsername(username);
    if (history.getID() == null) {
      throw new IllegalStateException("Workout must have ID (don't set manually, use getID from restServer!)");
    }
    String filepath = historyFolderPath + "/" + history.getID();
    writeObjectToFile(history, filepath);
    System.out.println("Saved history" + history.getName() + " to " + historyFolderPath);
  }

  //Todo update all saveUser in project, does not do the same as old saveUser method!!!

  /**
   * Saves user object to file.
   *
   * @param user to save.
   * @throws IllegalStateException from validateUsername() if username is null.
   * @throws IllegalArgumentException if user is null.
   */
  public void saveUser(User user) throws IllegalStateException, IllegalArgumentException {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
    validateUsername(user.getUsername());
    String filepath = userPath + "/UserData";
    writeObjectToFile(user, filepath);
    System.out.println("Saved user " + user.getUsername() + " to " + userPath);
  }

  /**
   * Saves Id object to file.
   *
   * @param id to save.
   * @param username of user to save IDs to.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public void saveIds(Id id, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    writeObjectToFile(id, filepath);
    System.out.println("Saved IDs to " + userPath);
  }

  /**
   * Deletes exercise in user directory.
   *
   * @param exercise to delete
   * @param username of user to remove exercise from.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public void deleteExercise(Exercise exercise, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    File file = new File(filepath);
    deleteFile(file);
  }

  /**
   * Deletes workout in user directory.
   *
   * @param workout to delete
   * @param username of user to remove workout from.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public void deleteWorkout(Workout workout, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = workoutFolderPath + "/" + workout.getID();
    File file = new File(filepath);
    deleteFile(file);
  }

  public void deleteHistory(History history, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = historyFolderPath + "/" + history.getID();
    File file = new File(filepath);
    deleteFile(file);
  }

  /**
   * Deletes user directory.
   *
   * @param username of user to delete.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public void deleteUser(String username) throws IllegalArgumentException {
    //Todo does not work
  }

  /**
   * Getter to get user object.
   *
   * @param username of user to get.
   * @return user object.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public User getUser(String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/UserData";
    return (User) readObjectFromFile(getFile(filepath), User.class);
  }

  /**
   * Getter to get workout object from user.
   *
   * @param workoutID of workout to get.
   * @param username of user to get workout from.
   * @return workout object.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = workoutFolderPath + "/" + workoutID;
    return (Workout) readObjectFromFile(getFile(filepath), Workout.class);
  }

  /**
   * Getter to get exercise object from user.
   *
   * @param exerciseID of exercise to get.
   * @param username of user to get exercise from.
   * @return exercise object.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public Exercise getExercise(String exerciseID, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = exerciseFolderPath + "/" + exerciseID;
    return (Exercise) readObjectFromFile(getFile(filepath), Exercise.class);
  }

  public History getHistory(String historyID, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = historyFolderPath + "/" + historyID;
    return (History) readObjectFromFile(getFile(filepath), History.class);
  }

  //Todo maybe rename?

  /**
   * Getter to get Id object from user.
   *
   * @param username of user to get IDs from.
   * @return Id object.
   * @throws IllegalArgumentException from validateUsername() if username is null.
   */
  public Id getIDs(String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    return (Id) readObjectFromFile(getFile(filepath), Id.class);
  }

  /**
   * Converts object to json string format.
   *
   * @param object to serialize.
   * @return serialized object, returns null if conversion fails.
   */
  public String objectToJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      System.err.println("Failed to serilize " + object.getClass() + " to string!");
    }
    return null;
  }

  /**
   * Converts json string to object
   *
   * @param jsonString json string to deserialize.
   * @param cls class type to deserialize object to.
   * @return object deserialized (must be casted to class type when used.),
   *         returns null if conversion fails
   */
  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    try {
        return mapper.readValue(jsonString, cls);
      } catch (JsonProcessingException e) {
        System.err.println("Failed to desierilize json string to object type " + cls);
      }
    return null;
  }
}
