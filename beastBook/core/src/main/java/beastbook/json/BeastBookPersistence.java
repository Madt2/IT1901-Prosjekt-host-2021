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

  private boolean userExists() {
    File userFolder = new File(userPath);
    return userFolder.isDirectory();
  }

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
   * Deletes file given.
   *
   * @param file to delete
   * @throws IOException when file deletion fails.
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
   * @throws IOException if file at path does not exist.
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
      saveIds(new IdHandler());
      saveUser();
    } catch (IOException | Exceptions.UserNotFoundException e) {
      throw new IOException("Could not create all core classes. Failed at: " + e.getMessage());
    }
  }

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

  public void saveIds(IdHandler id) throws IOException {
    String filepath = userPath + "IDs";
    System.out.println("DETTE ER FILEPATH: " + filepath);
    try {
      writeObjectToFile(id, new File(filepath));
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
   * @throws IOException if deletion of file fails.
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

  public void deleteUserDir() throws IOException, Exceptions.ServerException {
    deleteFile(new File(userPath));
  }

  private User getUser() throws IOException {
    String filepath = userPath + "UserData";
    return (User) readObjectFromFile(getFile(filepath), User.class);
  }

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

  public Workout getWorkout(String workoutId) throws IOException,
      Exceptions.WorkoutNotFoundException {
    String filepath = workoutFolderPath + "/" + workoutId;
    try {
      return (Workout) readObjectFromFile(getFile(filepath), Workout.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.WorkoutNotFoundException(workoutId);
    }
  }

  public Exercise getExercise(String exerciseId) throws Exceptions.ExerciseNotFoundException,
      IOException {
    String filepath = exerciseFolderPath + "/" + exerciseId;
    try {
      return (Exercise) readObjectFromFile(getFile(filepath), Exercise.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.ExerciseNotFoundException(exerciseId);
    }
  }

  public History getHistory(String historyId) throws IOException,
      Exceptions.HistoryNotFoundException {
    String filepath = historyFolderPath + "/" + historyId;
    try {
      return (History) readObjectFromFile(getFile(filepath), History.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.HistoryNotFoundException(historyId);
    }
  }

  public IdHandler getIds() throws IOException, Exceptions.IdHandlerNotFoundException {
    String filepath = userPath + "IDs";
    try {
      return (IdHandler) readObjectFromFile(getFile(filepath), IdHandler.class);
    } catch (FileNotFoundException e) {
      throw new Exceptions.IdHandlerNotFoundException(user.getUsername());
    }
  }
}
