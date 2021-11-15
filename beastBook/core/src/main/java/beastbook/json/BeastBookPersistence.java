package beastbook.json;

import beastbook.core.Exercise;
import beastbook.core.Id;
import beastbook.core.User;
import beastbook.core.Workout;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for saving and loading user from file.
 */
public class BeastBookPersistence {
  private ObjectMapper mapper;
  private String userPath;
  private String workoutFolderPath;
  private String exerciseFolderPath;

  public BeastBookPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  /**
   *  Help methods
   */

  private void setUserPath(String username) {
    userPath = System.getProperty("user.home") + "/" + username;
    workoutFolderPath = userPath + "/Workouts";
    exerciseFolderPath = userPath + "/Exercises";
  }

  private boolean userExists(String username) throws IllegalArgumentException {
    setUserPath(username);
    File user = new File(userPath);
    return user.isDirectory();
  }

  private void validateUsername(String username) throws IllegalArgumentException {
    if (!userExists(username)) {
      throw new IllegalArgumentException("User does not exist");
    }
  }

  private void createFolder(String path) throws IllegalArgumentException {
    File folder = new File(path);
    if (folder.mkdir()) {
      System.out.println("Folder: " + path + " was created.");
    } else {
      throw new IllegalArgumentException("Path is incorrect format!");
    }
  }

  //Todo good enough catches? should show better that it could not write to file.
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
  private void deleteFile(File file) {
    if (file.delete()) {
      System.out.println("File: " + file.getName() + " deleted");
    } else {
      System.err.println("Could not delete file " + file.getName());
    }
  }

  private File getFile(String path) throws IllegalArgumentException, IllegalStateException {
    if (path == null) {
      //Todo weird throw?
      throw new IllegalStateException("path is incorrect!");
    }
    File file = new File(path);
    if (!file.isFile()) {
      throw new IllegalArgumentException("File: " + path + " is missing!");
    }
    return file;
  }

  /**
   * Public methods
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
    saveUser(user);
    saveIds(new Id(), user.getUsername());
  }

  public void saveExercise(Exercise exercise, String username) throws IllegalStateException, IllegalArgumentException {
    validateUsername(username);
    if (exercise.getID() == null) {
      throw new IllegalStateException("Exercise must have ID (dont set manually, use getID from serverService!)");
    }
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    writeObjectToFile(exercise, filepath);
    System.out.println("Saved exercise " + exercise.getName() + " to " + exerciseFolderPath);
  }

  public void saveWorkout(Workout workout, String username) throws IllegalArgumentException, IllegalStateException {
    validateUsername(username);
    if (workout.getID() == null) {
      throw new IllegalStateException("Workout must have ID (dont set manually, use getID from restServer!)");
    }
    String filepath = workoutFolderPath + "/" + workout.getID();
    writeObjectToFile(workout, filepath);
    System.out.println("Saved workout " + workout.getName() + " to " + workoutFolderPath);
  }

  //Todo update all saveUser in project, does not do the same as old saveUser method!!!
  public void saveUser(User user) throws IllegalStateException, IllegalArgumentException {
    if (user.getUsername() == null) {
      throw new IllegalStateException("User must have username");
    }
    validateUsername(user.getUsername());
    String filepath = userPath + "/UserData";
    writeObjectToFile(user, filepath);
    System.out.println("Saved user " + user.getUsername() + " to " + userPath);
  }

  public void saveIds(Id id, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    writeObjectToFile(id, filepath);
    System.out.println("Saved IDs to " + userPath);
  }

  public void deleteExercise(Exercise exercise, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    File file = new File(filepath);
    deleteFile(file);
  }

  public void deleteWorkout(Workout workout, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = workoutFolderPath + "/" + workout.getID();
    File file = new File(filepath);
    deleteFile(file);
  }

  public void deleteUser(String username) throws IllegalArgumentException {
    //Todo does not work
  }

  public User getUser(String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/UserData";
    return (User) readObjectFromFile(getFile(filepath), User.class);
  }

  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = workoutFolderPath + "/" + workoutID;
    return (Workout) readObjectFromFile(getFile(filepath), Workout.class);
  }

  public Exercise getExercise(String exerciseID, String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = exerciseFolderPath + "/" + exerciseID;
    return (Exercise) readObjectFromFile(getFile(filepath), Exercise.class);
  }

  //Todo maybe rename?
  public Id getIds(String username) throws IllegalArgumentException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    return (Id) readObjectFromFile(getFile(filepath), Id.class);
  }

  public String objectToJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      System.err.println("Failed to serilize " + object.getClass() + " to string!");
    }
    return null;
  }

  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    try {
        return mapper.readValue(jsonString, cls);
      } catch (JsonProcessingException e) {
        System.err.println("Failed to desierilize json string to object type " + cls);
      }
    return null;
  }
}
