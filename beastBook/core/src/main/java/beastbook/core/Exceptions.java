package beastbook.core;


/**
 * This is a collection of exceptions made for this project.
 */
public class Exceptions {

  /**
   * Exception thrown when id given is not valid for selected class.
   */
  public static class IllegalIdException extends Exception {
    public IllegalIdException(String id, Class cls) {
      super("IdHandler " + id + " not legal for class " + cls.getSimpleName());
    }
  }

  /**
   * Exception thrown when idHandler file is missing (deleted or corrupt).
   */
  public static class IdHandlerNotFoundException extends Exception {
    public IdHandlerNotFoundException(String username) {
      super("Could not load IdHandler for: " + username);
    }
  }

  /**
   * Exception thrown when id for given class is already in use.
   */
  public static class IdAlreadyInUseException extends Exception {
    public IdAlreadyInUseException(Class cls, String id) {
      super("IdHandler: " + id + " is already in use for " + cls.getSimpleName());
    }
  }

  /**
   * Exception thrown when id is not found by idHandler for given class.
   */
  public static class IdNotFoundException extends Exception {
    public IdNotFoundException(Class cls, String id) {
      super("IdHandler: " + id + " not found for " + cls.getSimpleName());
    }
  }

  /**
   * Exception thrown when user with given username does not exist.
   */
  public static class UserNotFoundException extends Exception {
    public UserNotFoundException(String username) {
      super("Could not find user: " + username);
    }
  }

  /**
   * Exception thrown when trying to create a user that is already saved.
   */
  public static class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String username) {
      super("User with username: " + username + " already exists, please choose another name");
    }
  }

  /**
   * Exception thrown when password given does not match password saved by selected user.
   */
  public static class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException() {
      super("Password incorrect!");
    }
  }

  /**
   * Exception thrown when no Exercise with given id is stored in server.
   */
  public static class ExerciseNotFoundException extends Exception {
    public ExerciseNotFoundException(String exerciseId) {
      super("Could not find exercise with id: " + exerciseId);
    }
  }

  /**
   * Exception thrown when Exercise with id already exists,
   * or if Exercise with same name in given Workout exists.
   */
  public static class ExerciseAlreadyExistsException extends Exception {
    public ExerciseAlreadyExistsException(String exerciseName) {
      super("Exercise with name: " + exerciseName + " Already exists!");
    }
  }

  /**
   * Exception thrown when no Workout with given id is stored in server.
   */
  public static class WorkoutNotFoundException extends Exception {
    public WorkoutNotFoundException(String workoutId) {
      super("Could not find workout with id: " + workoutId);
    }
  }

  /**
   * Exception thrown when Workout with id already exists,
   * or if Workout with same name in given User exists.
   */
  public static class WorkoutAlreadyExistsException extends Exception {
    public WorkoutAlreadyExistsException(String workoutName) {
      super("Workout with name: " + workoutName + " Already exists!");
    }
  }

  /**
   * Exception thrown when no History with given id is stored in server.
   */
  public static class HistoryNotFoundException extends Exception {
    public HistoryNotFoundException(String historyId) {
      super("Could not find history with id: " + historyId);
    }
  }

  /**
   * Exception thrown when History with id already exists,
   * or if History with same name and date in given User exists.
   */
  public static class HistoryAlreadyExistsException extends Exception {
    public HistoryAlreadyExistsException(String historyNameDate) {
      super("History with name: " + historyNameDate + " Already exists!");
    }
  }

  /**
   * Exception thrown if something wrong happens on server.
   * It is meant for telling user to check server logg for stack traces.
   */
  public static class ServerException extends Exception {
    public ServerException() {
      super("Something wrong happened on the server, check server terminal for debugging!");
    }
  }

  /**
   * Exception thrown if there is something wrong with serialization or deserialization on server,
   * or deserialization on client side.
   */
  public static class BadPackageException extends Exception {
    public BadPackageException() {
      super("Json package was incorrectly send or received");
    }
  }
}
