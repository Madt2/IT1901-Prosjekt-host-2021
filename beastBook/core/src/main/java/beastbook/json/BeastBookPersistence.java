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

  private void setUserPath(String username) {
    userPath = System.getProperty("user.home") + "/" + username;
    System.out.println(userPath);
    workoutFolderPath = userPath + "/Workouts";
    exerciseFolderPath = userPath + "/Exercises";
  }

  private boolean userExists() {
    if (userPath == null) {
      //Todo state or argument exc?
      throw new IllegalStateException("userPath is incorrect!");
    }
    File user = new File(userPath);
    return user.isDirectory();
  }

  public void createUserDir(String username) {
    setUserPath(username);
    if (userExists()) {
      throw new IllegalStateException("User already exists!");
    }
    File userFolder = new File(userPath);
    if (userFolder.mkdir()) {
      System.out.println("Userfolder for " + username + " created.");
    } else {
      //Todo fix throw
      throw new IllegalStateException("error");
    }
    File userProp = new File(userPath + "/UserData");
    try {
      //userProp.createNewFile();
      objectWriter(new User(), userProp);
    } catch (Exception e) {
      //Todo fix exeption
      System.out.println(e);
    }
    File idList = new File(userPath + "/IDs");
    try {
      //idList.createNewFile();
      objectWriter(new Id(), idList);
    } catch (Exception e) {
      //Todo fix exeption
      System.out.println(e);
    }
    File workoutFolder = new File(workoutFolderPath);
    if (workoutFolder.mkdir()) {
      System.out.println("Workout folder for " + username + " was created");
    } else {
      //Todo fix throw
      throw new IllegalStateException("error");
    }

    File exerciseFolder = new File(exerciseFolderPath);
    if (exerciseFolder.mkdir()) {
      System.out.println("Created user folder for " + username);
      //Todo fix throw
    } else {
      throw new IllegalStateException("Error");
    }
  }

  //Todo create repair method?

  //Todo objectwriter good name?
  private void objectWriter(Object object, File file) {
    try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
      mapper.writeValue(writer, object);
    } catch (JsonMappingException e) {
      //Todo fix exceptions
      e.printStackTrace();
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveWorkout(Workout workout, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    if (workout.getID() == null) {
      throw new IllegalStateException("Workout must have ID (dont set manually, use getID from restServer!)");
    }
    String filepath = workoutFolderPath + "/" + workout.getID();
    File file = new File(filepath);
    objectWriter(workout, file);
    System.out.println("Saved workout " + workout.getName() + " to " + workoutFolderPath);
  }

  public void saveExercise(Exercise exercise, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    if (exercise.getID() == null) {
      throw new IllegalStateException("Exercise must have ID (dont set manually, use getID from restServer!)");
    }
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    File file = new File(filepath);
    objectWriter(exercise, file);
    //Todo change getExerciseName to getName?
    System.out.println("Saved exercise " + exercise.getExerciseName() + " to " + exerciseFolderPath);
  }

  //Todo update all saveUser in project, does not do the same as old saveUser method!!!
  public void saveUser(User user) {
    setUserPath(user.getUsername());
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = userPath + "/UserData";
    File file = new File(filepath);
    objectWriter(user, file);
    System.out.println("Saved user " + user.getUsername() + " to " + userPath);
  }

  public void saveIds(Id id, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = userPath + "/IDs";
    File file = new File(filepath);
    objectWriter(id, file);
    //Todo change getExerciseName to getName?
    System.out.println("Saved IDs to " + userPath);
  }

  public void deleteExercise(Exercise exercise, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = exerciseFolderPath + "/" + exercise.getID();
    File file = new File(filepath);
    if (file.delete()) {
      System.out.println("File: " + filepath + " deleted");
    } else {
      //Todo fix throw
      throw new IllegalStateException("error");
    }
  }

  public void deleteWorkout(Workout workout, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = workoutFolderPath + "/" + workout.getID();
    File file = new File(filepath);
    if (file.delete()) {
      System.out.println("File: " + filepath + " deleted");
    } else {
      //Todo fix throw
      throw new IllegalStateException("error");
    }
  }

  public void deleteUser(String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    File file = new File(userPath);
    if (file.delete()) {
      System.out.println("File: " + username + " deleted");
    } else {
      //Todo fix throw
      throw new IllegalStateException("error");
    }
  }

  private <T> Object objectReader(File file, Class<T> cls) {
    try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
      return mapper.readValue(reader, cls);
    } catch (JsonMappingException e) {
      //Todo fix catch
      e.printStackTrace();
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean fileExist(String path) {
    if (path == null) {
      //Todo state or argument exc?
      throw new IllegalStateException("userPath is incorrect!");
    }
    File user = new File(path);
    return user.isFile();
  }

  private File getFile(String path) {
    String filepath = path;
    if (!fileExist(filepath)) {
      throw new IllegalStateException("File: " + path + " is missing!");
    }
    return new File(filepath);
  }

  public User getUser(String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = userPath + "/UserData";
    return (User) objectReader(getFile(filepath), User.class);
  }

  public Workout getWorkout(String workoutID, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = workoutFolderPath + "/" + workoutID;
    return (Workout) objectReader(getFile(filepath), Workout.class);
  }

  public Exercise getExercise(String exerciseID, String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = exerciseFolderPath + "/" + exerciseID;
    return (Exercise) objectReader(getFile(filepath), Exercise.class);
  }

  //Todo maybe rename?
  public Id getIds(String username) {
    setUserPath(username);
    if (!userExists()) {
      throw new IllegalStateException("user dir does not exist!");
    }
    String filepath = userPath + "/IDs";
    return (Id) objectReader(getFile(filepath), Id.class);
  }

  public String objectToJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    try {
        return mapper.readValue(jsonString, cls);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        // TODO add exception
      }
    return null;
  }

//
//
//
//  /**
//  * Help method for loadUser.
//  *
//  * @param reader filereader object
//  * @return user from file
//  * @throws IOException when filepath from reader does not point to existing file.
//  */
//  private User readUser(Reader reader) throws IOException {
//    return mapper.readValue(reader, User.class);
//  }
//
//  /**
//  * Help method for saveUser.
//  *
//  * @param user user to write to file
//  * @param writer filewriter object
//  * @throws IOException when filepath from writer does not point to existing file.
//  */
//  private void writeUser(User user, Writer writer) throws IOException {
//    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
//  }
//
//  public void writeExercise(Exercise exercise, Writer writer) throws  IOException {
//
//  }
//
//  public void saveExercise(Exercise exercise) {
//    if (saveFilePath == null) {
//      throw new IllegalStateException("Save file path is missing");
//    }
//    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
//      writeExercise(exercise, writer);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public String getJSONString() {
//    try {
//      return Files.readString(saveFilePath);
//    } catch (Exception e) {
//      // TODO add exception
//    }
//    return null;
//  }
//
//  public User parseJSONString(String jsonString) {
//      try {
//        return mapper.readValue(jsonString, User.class);
//      } catch (JsonProcessingException e) {
//        e.printStackTrace();
//        // TODO add exception
//      }
//    return null;
//  }
//
//  public String translator(User user) {
//    try {
//      return mapper.writeValueAsString(user);
//    } catch (JsonProcessingException e) {
//      e.printStackTrace();
//      // todo add exception
//    }
//    return null;
//  }
//
//  /**
//  * Sets path to user saveFile, required before calling loadUser and saveUser methods.
//  *
//  * @param saveFile name of user.
//  */
//  public void setSaveFilePath(String saveFile) {
//    this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
//  }
//
//  /**
//  * Loads user. Required to run setSaveFilePath before calling.
//  *
//  * @return User selected in setSaveFilePath
//  * @throws IOException when filepath from FileReader does not point to existing file
//  * @throws IllegalStateException when saveFilePath is null
//  */
//  public User loadUser() throws IOException, IllegalStateException {
//    if (saveFilePath == null) {
//      throw new IllegalStateException("Save file path is missing");
//    }
//    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
//      return readUser(reader);
//    }
//  }
//
//  /**
//  * Saves user. Required to run setSaveFilePath before calling.
//  *
//  * @param user username for user to load.
//  * @throws IOException when filepath from FileWriter does not point to existing file.
//  * @throws IllegalStateException when saveFilePath is null
//  */
//  public void saveUser(User user) throws IOException, IllegalStateException {
//    if (saveFilePath == null) {
//      throw new IllegalStateException("Save file path is missing");
//    }
//    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
//      writeUser(user, writer);
//    }
//  }
//
}
