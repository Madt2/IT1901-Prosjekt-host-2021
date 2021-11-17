package beastbook.json;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.core.JsonProcessingException;
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
   * @throws NullPointerException if username is null.
   */
  private void setUserPath(String username) {
    if (username == null) {
      throw new NullPointerException("Username cannot be null!");
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
   * @throws NullPointerException if username is null.
   */
  private boolean userExists(String username) throws NullPointerException {
    setUserPath(username);
    File user = new File(userPath);
    return user.isDirectory();
  }

  /**
   * Validation to check if user for username exists
   *
   * @param username for user folder to check.
   * @throws IllegalArgumentException if user does not exist.
   * @throws NullPointerException if username is null.
   */
  private void validateUsername(String username) throws IllegalArgumentException {
    if (!userExists(username)) {
      throw new IllegalArgumentException("User does not exist");
    }
  }

  /**
   * Creates folder at given path.
   *
   * @param folder path to where folder will be created.
   * @throws IOException if creation of folder fails.
   * @throws IllegalArgumentException if folder already exists.
   */
  private void createFolder(File folder) throws IOException, IllegalArgumentException {
    if (folder.isDirectory()) {
      throw new IllegalArgumentException(folder.getPath() + " is already a directory!");
    }
    if (folder.mkdir()) {
      System.out.println("Folder: " + folder.getPath() + " was created.");
    } else {
      throw new IOException("Failed to create folder!");
    }
  }

  //Todo good enough catches? should show better that it could not write to file.

  /**
   * Object to write to file on filepath.
   *
   * @param object to write.
   * @param file path to file
   * @throws IOException if it fails to write to file.
   */
  private void writeObjectToFile(Object object, File file) throws IOException {
    Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
    try {
      mapper.writeValue(writer, object);
      writer.flush();
      writer.close();
    } catch (IOException e) {
      writer.close();
      throw e;
    }

  }

  /**
   * Read and deserialize object from file.
   *
   * @param file file to read from.
   * @param cls class type for object to read.
   * @return object deserialized.
   * @throws IOException if read fails.
   */
  private Object readObjectFromFile(File file, Class cls) throws IOException {
    Reader reader = new FileReader(file, StandardCharsets.UTF_8);
    try {
      Object obj = mapper.readValue(reader, cls);
      reader.close();
      return obj;
    } catch (IOException e) {
      reader.close();
      throw e;
    }
  }

  /**
   * Deletes file given
   *
   * @param file to delete
   * @throws IOException when file deletion fails.
   */
  private void deleteFile(File file) throws IOException {
    if (!file.exists()) {
      System.err.println("WARNING: Tried to delete a file that did not exist");
    } else if (file.delete()) {
      System.out.println("File: " + file.getName() + " deleted");
    } else {
      throw new IOException("Could not delete file!");
    }
  }

  /**
   * Getter for file at path.
   *
   * @param path to file
   * @return File at path
   * @throws IOException if file at path does not exist.
   */
  private File getFile(String path) throws IOException {
    File file = new File(path);
    if (!file.isFile()) {
      throw new IOException("File: " + path + " is missing!");
    }
    return file;
  }

  /**
   * Creates user directory with username. Contains Exercises and Workouts folder,
   * and userProp (file where user class is stored) and IDs (file where Id class is stored).
   *
   * @param user to create directory for.
   * @throws IllegalArgumentException if user directory already exists.
   * @throws NullPointerException if username for user is null.
   * @throws IOException if writing to file fails or if it fails to create folder.
   */
  public void createUser(User user) throws IllegalArgumentException, NullPointerException, IOException {
    if (user.getUsername() == null) {
      throw new NullPointerException("User must have username");
    }
    if(userExists(user.getUsername())) {
      throw new IllegalArgumentException("User already exists, delete " + user.getUsername() + " or use another username!");
    }
    try {
      createFolder(new File(userPath));
      createFolder(new File(workoutFolderPath));
      createFolder(new File(exerciseFolderPath));
      createFolder(new File(historyFolderPath));
      saveUser(user);
      saveIds(new Id(), user.getUsername());
    } catch (IOException e) {
      throw new IOException("Could not create all core classes. Failed at: " + e.getMessage());
    }

  }

  /**
   * Saves IdClasses object to file.
   *
   * @param object to save.
   * @param username of user to save exercise to.
   * @throws IllegalStateException if object does not have ID.
   * @throws NullPointerException if userame is null.
   * @throws IOException if writing to file fails.
   */
  public void saveIdObject(IdClasses object, String username) throws IOException {
    validateUsername(username);
    String filepath = "";
    if (object.getId() == null) {
      throw new NullPointerException(object.getClass() + " must have ID (dont set manually, use getID from serverService!)");
    }
    if (object instanceof Exercise) {
      filepath = exerciseFolderPath + "/" + object.getId();
    }
    if (object instanceof Workout) {
      filepath = workoutFolderPath + "/" + object.getId();
    }
    if (object instanceof History) {
      filepath = historyFolderPath + "/" + object.getId();
    }
    writeObjectToFile(object, new File(filepath));
    System.out.println("Saved " + object.getName() + " " + object.getName() + " to " + filepath);
  }

  //Todo update all saveUser in project, does not do the same as old saveUser method!!!

  /**
   * Saves user object to file.
   *
   * @param user to save.
   * @throws IllegalStateException from validateUsername() if username is null.
   * @throws NullPointerException if user is null.
   * @throws IOException if writing to file fails.
   */
  public void saveUser(User user) throws NullPointerException, IllegalArgumentException, IOException {
    if (user == null) {
      throw new NullPointerException("User cannot be null");
    }
    validateUsername(user.getUsername());
    String filepath = userPath + "/UserData";
    try {
      writeObjectToFile(user, new File(filepath));
    } catch (IOException e) {
      throw new IOException("Could not save user to file");
    }
    System.out.println("Saved user " + user.getUsername() + " to " + userPath);
  }

  /**
   * Saves Id object to file.
   *
   * @param id to save.
   * @param username of user to save IDs to.
   * @throws NullPointerException if username is null.
   * @throws IOException if writing to file fails.
   */
  public void saveIds(Id id, String username) throws NullPointerException, IOException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    try {
      writeObjectToFile(id, new File(filepath));
    } catch (IOException e) {
      throw new IOException("Could not save Id object");
    }
    System.out.println("Saved IDs to " + userPath);
  }

  /**
   * Deletes file for given object.
   *
   * @param object to delete file for.
   * @param username for user.
   * @throws NullPointerException if username is null, or if objects id is null.
   * @throws IOException if deletion of file fails.
   */
  public void deleteIdObject(IdClasses object, String username) throws NullPointerException, IOException {
    validateUsername(username);
    String filepath = "";
    if (object.getId() == null) {
      throw new NullPointerException(object.getClass() + " must have ID (dont set manually, use getID from serverService!)");
    }
    if (object instanceof Exercise) {
      filepath = exerciseFolderPath + "/" + object.getId();
    }
    if (object instanceof Workout) {
      filepath = workoutFolderPath + "/" + object.getId();
    }
    if (object instanceof History) {
      filepath = historyFolderPath + "/" + object.getId();
    }
    File file = new File(filepath);
    deleteFile(file);
    System.out.println("Deleted file: " + filepath);
  }

  /**
   * Deletes user directory.
   *
   * @param username of user to delete.
   * @throws NullPointerException if username is null.
   * @throws IOException if deletion of file fails.
   */
  public void deleteUserDir(String username) throws NullPointerException, IOException {
    setUserPath(username);
    File file = new File(workoutFolderPath);
    try {
      deleteFile(file);
      file = new File(exerciseFolderPath);
      deleteFile(file);
      file = new File(historyFolderPath);
      deleteFile(file);
      file = new File(userPath + "/UserData");
      deleteFile(file);
      file = new File(userPath + "/IDs");
      deleteFile(file);
    } catch (IOException e) {
      throw new IOException("Could not delete base userdata files!");
    }

  }

  /**
   * Getter to get user object.
   *
   * @param username of user to get.
   * @return user object.
   * @throws NullPointerException if username is null.
   * @throws IOException if reading from file fails.
   * @throws IllegalArgumentException if user does not exist.
   */
  public User getUser(String username) throws IllegalArgumentException, NullPointerException, IOException {
    validateUsername(username);
    String filepath = userPath + "/UserData";
    try {
      return (User) readObjectFromFile(getFile(filepath), User.class);
    } catch (IOException e) {
      throw new IOException("Could not read user");
    }
  }

  /**
   * Getter to get workout object from user.
   *
   * @param workoutID of workout to get.
   * @param username of user to get workout from.
   * @return workout object.
   * @throws NullPointerException if username is null.
   * @throws IOException if reading from file fails.
   */
  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException, IOException {
    validateUsername(username);
    String filepath = workoutFolderPath + "/" + workoutID;
    try {
      return (Workout) readObjectFromFile(getFile(filepath), Workout.class);
    } catch (IOException e) {
      throw new IOException("Could not read workout");
    }
  }

  /**
   * Getter to get exercise object from user.
   *
   * @param exerciseID of exercise to get.
   * @param username of user to get exercise from.
   * @return exercise object.
   * @throws IllegalArgumentException if username is null.
   * @throws IOException if reading from file fails.
   */
  public Exercise getExercise(String exerciseID, String username) throws NullPointerException, IOException {
    validateUsername(username);
    String filepath = exerciseFolderPath + "/" + exerciseID;
    try {
      return (Exercise) readObjectFromFile(getFile(filepath), Exercise.class);
    } catch (IOException e) {
      throw new IOException("Could not read exercise");
    }
  }

  /**
   * Getter to get History object from user.
   *
   * @param historyID of history to get.
   * @param username of user to get exercise from.
   * @return history object.
   * @throws NullPointerException if username is null.
   * @throws IOException if reading from file fails.
   */
  public History getHistory(String historyID, String username) throws NullPointerException, IOException {
    validateUsername(username);
    String filepath = historyFolderPath + "/" + historyID;
    try {
      return (History) readObjectFromFile(getFile(filepath), History.class);
    } catch (IOException e) {
      throw new IOException("could not read history");
    }
  }

  /**
   * Getter to get Id object from user.
   *
   * @param username of user to get IDs from.
   * @return Id object.
   * @throws NullPointerException if username is null.
   * @throws IOException if reading from file fails.
   */
  public Id getIds(String username) throws IllegalArgumentException, IOException {
    validateUsername(username);
    String filepath = userPath + "/IDs";
    try {
      return (Id) readObjectFromFile(getFile(filepath), Id.class);
    } catch (IOException e) {
      throw new IOException("could not read IDs");
    }
  }

  /**
   * Converts object to json string format.
   *
   * @param object to serialize.
   * @return serialized object.
   * @throws JsonProcessingException if serialization fails.
   */
  public String objectToJson(Object object) throws JsonProcessingException {
      return mapper.writeValueAsString(object);
  }

  /**
   * Converts json string to object
   *
   * @param jsonString json string to deserialize.
   * @param cls class type to deserialize object to.
   * @return object deserialized
   * @throws JsonProcessingException if deserialization fails.
   */
  public Object jsonToObject(String jsonString, Class cls) throws JsonProcessingException {
    return mapper.readValue(jsonString, cls);
  }
}
