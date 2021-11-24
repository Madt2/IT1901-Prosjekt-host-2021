package beastbook.json;

import beastbook.core.*;
import beastbook.json.internal.BeastBookModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Class for saving and loading objects to and from file.
 */
public class BeastBookPersistence {
  private ObjectMapper mapper;
  private User user;
  private String userPath;
  private String workoutFolderPath;
  private String exerciseFolderPath;
  private String historyFolderPath;

  /**
   * Constructor for BeastBookPersistence.
   *
   * @param user object to handle reading and writing for.
   * @throws NullPointerException if user is null.
   */
  public BeastBookPersistence(User user) {
    if (user == null) {
      throw new NullPointerException("User cannot be null");
    }
    this.user = user;
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
    userPath = System.getProperty("user.home") + "/" + user.getUsername() + "/";
    workoutFolderPath = userPath + "Workouts";
    exerciseFolderPath = userPath + "Exercises";
    historyFolderPath = userPath + "Histories";
  }

  /**
   * Checks if user exists in server's home directory.
   *
   * @return true if userfolder exists, false otherwise.
   */
  private boolean userExists() {
    File userFolder = new File(userPath);
    return userFolder.isDirectory();
  }

  /**
   * Creates folder at given object's filepath.
   *
   * @param folder File object with filepath to where folder should be created.
   * @throws IOException if it fails to create folder.
   */
  private void createFolder(File folder) throws IOException {
    if (folder.mkdir()) {
      System.out.println("Folder: " + folder.getPath() + " was created.");
    } else {
      throw new IOException("Failed to create folder!");
    }
  }

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
      throw new IOException("Object not properly deserialized!");
    }
  }

  /**
   * Deletes file or folder given. If given file is a folder it deletes every subfile,
   * sub folders and itself.
   *
   * @param file object of file or folder to delete.
   * @throws IOException when file deletion fails.
   * @throws Exceptions.ServerException if nullpointer error happens.
   */
  private void deleteFile(File file) throws IOException, Exceptions.ServerException {
    System.out.println(file.getPath());
    try {
      //This asks if a file or folder does not exist
      if (!file.exists()) {
        System.err.println("WARNING: Tried to delete a file that did not exist");
        //This ask if it is a file
      } else if (file.isFile()) {
        //This deletes a file and prints if it is successful
        if (file.delete()) {
          System.out.println("File: " + file.getName() + " deleted");
          //This throw if file was not deleted
        } else {
          throw new IOException("File: " + file.getName() + " could not be deleted");
        }
        //This asks if it is a directory
      } else if (file.isDirectory()) {
        //This deletes directory if it contains 0 files.
        if (file.list().length == 0) {
          //print successful or throw IOException
          if (file.delete()) {
            System.out.println("File: " + file.getName() + " deleted");
          } else {
            throw new IOException("File: " + file.getName() + " could not be deleted");
          }
          //This is else, which means it has to delete all files or folder it contains.
        } else {
          //for all files or folders run deleteFiles recursive
          for (String s : file.list()) {
            if (new File(file.getPath() + "/" + s) != null) {
              deleteFile(new File(file.getPath() + "/" + s));
            }
          }
          //alfter it has deleted all sub files/folder it deletes itself.
          if (file.delete()) {
            System.out.println("File: " + file.getName() + " deleted");
            //else throw IOException
          } else {
            throw new IOException("File: " + file.getName() + " could not be deleted");
          }
        }
        //This happens if it could not delete anything
      } else {
        throw new IOException("Could not delete file!");
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
      throw new Exceptions.ServerException();
    }
  }

  /**
   * Getter for file at path.
   *
   * @param path to file
   * @return File at path
   * @throws FileNotFoundException if it does not find file.
   */
  private File getFile(String path) throws FileNotFoundException {
    File file = new File(path);
    if (!file.isFile()) {
      throw new FileNotFoundException("File: " + path + " is missing!");
    }
    return file;
  }

  public void createUser() throws IOException, Exceptions.UserAlreadyExistException {
    if (userExists()) {
      throw new Exceptions.UserAlreadyExistException(user.getUsername());
    }
    try {
      createFolder(new File(userPath));
      createFolder(new File(workoutFolderPath));
      createFolder(new File(exerciseFolderPath));
      createFolder(new File(historyFolderPath));
      saveIdHandler(new IdHandler());
      saveUser();
    } catch (IOException | Exceptions.UserNotFoundException e) {
      throw new IOException("Could not create all core classes. Failed at: " + e.getMessage());
    }
  }

  /**
   * Method used for saving object with interface IdClass. Handles saving to file for objects with id.
   *
   * @param object to save.
   * @throws IOException if it fails to save properly.
   * @throws Exceptions.IdNotFoundException if object does not have an id.
   */
  public void saveIdObject(IdClasses object) throws IOException, Exceptions.IdNotFoundException {
    String filepath = null;
    if (object.getId() == null) {
      throw new Exceptions.IdNotFoundException(object.getClass(), object.getId());
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

  /**
   * Saves user given in constructor to file.
   *
   * @throws IOException if it fails to save file correctly.
   * @throws Exceptions.UserNotFoundException if user directory does not exist in server's home directory.
   */
  public void saveUser() throws IOException, Exceptions.UserNotFoundException {
    if (!userExists()) {
      throw new Exceptions.UserNotFoundException(user.getUsername());
    }
    String filepath = userPath + "UserData";
    try {
      writeObjectToFile(user, new File(filepath));
    } catch (IOException e) {
      throw new IOException("Could not save user to file");
    }
    System.out.println("Saved user " + user.getUsername() + " to " + userPath);
  }

  /**
   * Saves idHandler to file.
   *
   * @param idHandler to save to file.
   * @throws IOException if it fails to write to file correctly.
   */
  public void saveIdHandler(IdHandler idHandler) throws IOException {
    String filepath = userPath + "IDs";
    System.out.println("DETTE ER FILEPATH: " + filepath);
    try {
      writeObjectToFile(idHandler, new File(filepath));
    } catch (IOException e) {
      throw new IOException("Could not save IdHandler object");
    }
    System.out.println("Saved IDs to " + userPath);
  }

  /**
   * Deletes file for given object.
   *
   * @param objectId to delete file for.
   * @param cls class to delete object for.
   * @throws NullPointerException if username is null, or if objects id is null.
   * @throws IOException if an IO error happens during file deletion.
   * @throws Exceptions.ServerException if it fails to delete file.
   */
  public void deleteIdObject(String objectId, Class cls) throws NullPointerException,
      IOException,
      Exceptions.ServerException {
    String filepath = "";
    if (objectId == null) {
      throw new NullPointerException(cls + " must have ID (dont set manually, use getID from serverService!)");
    }
    if (cls == Exercise.class) {
      filepath = exerciseFolderPath + "/" + objectId;
    } else if (cls == Workout.class) {
      filepath = workoutFolderPath + "/" + objectId;
    } else if (cls == History.class) {
      filepath = historyFolderPath + "/" + objectId;
    } else {
      throw new IllegalArgumentException("Class must be Workout or History");
    }
    File file = new File(filepath);
    deleteFile(file);
    System.out.println("Deleted file: " + filepath);
  }

  /**
   * Deletes User directory on server's home directory for user given in constructor.
   *
   * @throws IOException if IO error happens during file deletion.
   * @throws Exceptions.ServerException if it fails to delete user directory.
   */
  public void deleteUserDir() throws IOException, Exceptions.ServerException {
    deleteFile(new File(userPath));
  }

  /**
   * Reads user given in constructor from file.
   *
   * @return User object deserialized from file.
   * @throws IOException if it fails to read User object from file.
   */
  private User getUser() throws IOException {
    String filepath = userPath + "UserData";
    return (User) readObjectFromFile(getFile(filepath), User.class);
  }

  /**
   * Validates that User given in constructor has same username and password
   * as User stored in server's directory. Used to validate login.
   *
   * @throws IOException If IO error happens during loading of user object.
   * @throws Exceptions.UserNotFoundException if user directory for given user does not exist.
   * @throws Exceptions.PasswordIncorrectException if password for given user does not match
   * password of stored user.
   */
  public void validateUser() throws IOException,
      Exceptions.UserNotFoundException,
      Exceptions.PasswordIncorrectException {
    if (!userExists()) {
      throw new Exceptions.UserNotFoundException(user.getUsername());
    }
    if (!user.getPassword().equals(getUser().getPassword())) {
      throw new Exceptions.PasswordIncorrectException();
    }
  }

  /**
   * Getter for Workout with given id for User given in constructor.
   *
   * @param workoutId belonging to Workout to get.
   * @return Workout object.
   * @throws IOException if IO error happens during reading of Workout object.
   * @throws Exceptions.WorkoutNotFoundException if no Workout with given id exists in user directory.
   */
  public Workout getWorkout(String workoutId) throws IOException,
      Exceptions.WorkoutNotFoundException {
    String filepath = workoutFolderPath + "/" + workoutId;
    try {
      return (Workout) readObjectFromFile(getFile(filepath), Workout.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.WorkoutNotFoundException(workoutId);
    }
  }

  /**
   * Getter for Exercise with given id for User given in constructor.
   *
   * @param exerciseId belonging to Exercise to get.
   * @return Exercise object.
   * @throws Exceptions.ExerciseNotFoundException if Exercise with given id does not exist in user directory.
   * @throws IOException if IO error happens during reading of Exercise object.
   */
  public Exercise getExercise(String exerciseId) throws Exceptions.ExerciseNotFoundException,
      IOException {
    String filepath = exerciseFolderPath + "/" + exerciseId;
    try {
      return (Exercise) readObjectFromFile(getFile(filepath), Exercise.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }

  /**
   * Getter for History with given id for User given in constructor.
   *
   * @param historyId
   * @return
   * @throws IOException if IO error happens during reading of History object.
   * @throws Exceptions.HistoryNotFoundException if History with given id does not exist in user directory.
   */
  public History getHistory(String historyId) throws IOException,
      Exceptions.HistoryNotFoundException {
    String filepath = historyFolderPath + "/" + historyId;
    try {
      return (History) readObjectFromFile(getFile(filepath), History.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.HistoryNotFoundException(historyId);
    }
  }

  /**
   * Getter for idHandler for user given in constructor.
   *
   * @return idHandler object.
   * @throws IOException if IO error happens during reading of IdHandler object.
   * @throws Exceptions.IdHandlerNotFoundException if IdHandler with given id does not exist in user directory.
   */
  public IdHandler getIdHandler() throws IOException, Exceptions.IdHandlerNotFoundException {
    String filepath = userPath + "IDs";
    try {
      return (IdHandler) readObjectFromFile(getFile(filepath), IdHandler.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.IdHandlerNotFoundException(user.getUsername());
    }
  }
}
