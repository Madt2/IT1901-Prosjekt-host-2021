package beastbook.server;

import beastbook.core.Exercise;
import beastbook.core.Id;
import beastbook.core.User;
import beastbook.core.Workout;
import beastbook.json.BeastBookPersistence;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ServerService {
  BeastBookPersistence persistence = new BeastBookPersistence();

  public void createUser(User user) throws IllegalArgumentException, IllegalStateException {
    persistence.createUser(user);
  }

  public void addWorkout(Workout workout, String username) throws IllegalArgumentException {
    //Todo maybe try catch?
    User user = persistence.getUser(username);
    if (workout.getID() == null) {
      workout.setID(giveID(username, Workout.class)); //Should never throw exception
    }
    user.addWorkout(workout.getID());
    persistence.saveUser(user);
    persistence.saveWorkout(workout, username);
  }

  public void addExercise(Exercise exercise, String workoutID, String username) throws IllegalArgumentException {
    //Todo maybe try catch?
    Workout workout = persistence.getWorkout(workoutID, username);
    if (exercise.getID() == null) {
      exercise.setID(giveID(username, Exercise.class)); //Should never throw exception
    }
    workout.addExercise(exercise.getID());
    exercise.setWorkoutID(workout.getID());
    persistence.saveWorkout(workout, username);
    persistence.saveExercise(exercise, username);
  }

  public void updateWorkout(Workout workout, String username) throws IllegalArgumentException, IllegalStateException {
    try {
      persistence.saveWorkout(workout, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an workout without id, " +
              "if this workout was gotten from user in server it is corrupted, " +
              "if it is a new workout use addWorkout method");
    }
  }

  public void updateExercise(Exercise exercise, String username) throws IllegalArgumentException, IllegalStateException {
    try {
      persistence.saveExercise(exercise, username);
    } catch (IllegalStateException e) {
      throw new IllegalStateException("Cannot update an exercise without id, " +
              "if this exercise was gotten from user in server it is corrupted, " +
              "if it is a new exercise use addExercise method");
    }
  }

  public void deleteUser(String username) throws IllegalArgumentException {
    persistence.deleteUser(username);
  }

  public void deleteWorkout(String workoutID, String username) throws IllegalArgumentException {
    Workout workout = persistence.getWorkout(workoutID, username);
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    try {
      persistence.deleteWorkout(workout, username);
      user.removeWorkout(workout.getID());
      persistence.saveUser(user);
      ids.removeWorkoutID(workout.getID());
      persistence.saveIds(ids, username);
    } catch (IllegalStateException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      //This should not happen since classes is gotten directly gotten from server.
      System.err.println(e.getMessage());
    }
  }

  public void deleteExercise(String exerciseID, String username) throws IllegalArgumentException {
    Exercise exercise = persistence.getExercise(exerciseID, username);
    Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
    Id ids = persistence.getIds(username);
    try {
      persistence.deleteExercise(exercise, username);
      workout.removeExercise(exercise.getID());
      ids.removeExerciseID(exercise.getID());
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

  public User getUser(String username) throws IllegalArgumentException {
    return persistence.getUser(username);
  }

  public Workout getWorkout(String workoutID, String username) throws IllegalArgumentException {
    return persistence.getWorkout(workoutID, username);
  }

  public Exercise getExercise(String exerciseID, String username) throws IllegalArgumentException {
    return persistence.getExercise(exerciseID, username);
  }

  public String objectToJson(Object object) {
    return persistence.objectToJson(object);
  }

  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    return persistence.jsonToObject(jsonString, cls);
  }

  private <T>  void validateID(String id, Class<T> cls) throws IllegalArgumentException {
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
    if (cls == Exercise.class) {
      ids.addExerciseID(id);
    } else if (cls == Workout.class) {
      ids.addWorkoutID(id);
    }
    persistence.saveIds(ids, username);
    return id;
  }
}
