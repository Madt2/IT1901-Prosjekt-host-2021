package beastbook.core;

public class Exceptions {

  //Id exceptions:

  public static class IllegalIdException extends Exception {
    public IllegalIdException(String id, Class cls) {
      super("Id " + id + " not legal for class " + cls.getSimpleName() );
    }
  }

  public static class IdHandlerNotFoundException extends Exception {
    public IdHandlerNotFoundException(String username) {
      super("Could not load IdHandler for: " + username);
    }
  }

  public static class IdAlreadyInUseException extends Exception {
    public IdAlreadyInUseException(Class cls, String id) {
      super("Id: " + id + " is already in use for " + cls.getSimpleName());
    }
  }

  public static class IdNotFoundException extends Exception {
    public IdNotFoundException(Class cls, String id) {
      super("Id: " + id + " not found for " + cls.getSimpleName());
    }
  }

  //Not found exceptions:

  public static class UserNotFoundException extends Exception {
    public UserNotFoundException(String username) {
      super("Could not find user: " + username);
    }
  }

  public static class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String username) {
      super("User with username: " + username + " already exists, please choose another name");
    }
  }

  public static class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException() {
      super("Password incorrect!");
    }
  }

  public static class ExerciseNotFoundException extends Exception {
    public ExerciseNotFoundException(String exerciseId) {
      super("Could not find exercise with id: " + exerciseId);
    }
  }

  public static class ExerciseAlreadyExistsException extends Exception {
    public ExerciseAlreadyExistsException(String exerciseName) {
      super("Exercise with name: " + exerciseName + " Already exists!");
    }
  }

  public static class WorkoutNotFoundException extends Exception {
    public WorkoutNotFoundException(String workoutId) {
      super("Could not find workout with id: " + workoutId);
    }
  }

  public static class WorkoutAlreadyExistsException extends Exception {
    public WorkoutAlreadyExistsException(String workoutName) {
      super("Workout with name: " + workoutName + " Already exists!");
    }
  }

  public static class HistoryNotFoundException extends Exception {
    public HistoryNotFoundException(String historyId) {
      super("Could not find history with id: " + historyId);
    }
  }

  public static class HistoryAlreadyExistsException extends Exception {
    public HistoryAlreadyExistsException(String historyNameDate) {
      super("History with name: " + historyNameDate + " Already exists!");
    }
  }

  public static class ServerException extends Exception {
    public ServerException() {
      super("Something wrong happened on the server, check server terminal for debugging!");
    }
  }

  public static class BadPackageException extends Exception {
    public BadPackageException() {
      super("Json package was incorrectly send or received");
    }
  }
}
