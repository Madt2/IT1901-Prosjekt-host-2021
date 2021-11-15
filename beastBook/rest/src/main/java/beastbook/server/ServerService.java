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

  public void createUser(User user) {
    //Todo make createUserDIr throw exception, this should also have throws exception!
    persistence.createUserDir(user.getUsername()); //Todo getUsername might throw null pointer exception
    persistence.saveUser(user);
  }

  public void addWorkout(Workout workout, String username) {
    User user = persistence.getUser(username);
    if (workout.getID() == null) {
      workout.setID(giveID(username, Workout.class));
    }
    user.addWorkout(workout.getID());
    persistence.saveUser(user);
    persistence.saveWorkout(workout, username);
  }

  public void addExercise(Exercise exercise, String workoutID, String username) {
    Workout workout = persistence.getWorkout(workoutID, username);
    if (exercise.getID() == null) {
      exercise.setID(giveID(username, Exercise.class));
    }
    workout.addExercise(exercise.getID());
    exercise.setWorkoutID(workout.getID());
    persistence.saveWorkout(workout, username);
    persistence.saveExercise(exercise, username);
  }

  public void updateWorkout(Workout workout, String username) {
    persistence.saveWorkout(workout, username);
  }

  public void updateExercise(Exercise exercise, String username) {
    persistence.saveExercise(exercise, username);
  }

  public void deleteUser(String username) {
    persistence.deleteUser(username);
  }

  public void removeWorkout(String workoutID, String username) {
    Workout workout = persistence.getWorkout(workoutID, username);
    User user = persistence.getUser(username);
    Id ids = persistence.getIds(username);
    persistence.deleteWorkout(workout, username);
    user.removeWorkout(workout.getID());
    persistence.saveUser(user);
    ids.removeWorkoutID(workout.getID());
    persistence.saveIds(ids, username);
  }

  public void removeExercise(String exerciseID, String username) {
    Exercise exercise = persistence.getExercise(exerciseID, username);
    Workout workout = persistence.getWorkout(exercise.getWorkoutID(), username);
    Id ids = persistence.getIds(username);
    persistence.deleteExercise(exercise, username);
    workout.removeExercise(exercise.getID());
    persistence.saveWorkout(workout, username);
    ids.removeExerciseID(exercise.getID());
    persistence.saveIds(ids, username);
  }

  public User getUser(String username) {
    return persistence.getUser(username);
  }

  public Workout getWorkout(String workoutID, String username) {
    return persistence.getWorkout(workoutID, username);
  }

  public Exercise getExercise(String exerciseID, String username) {
    return persistence.getExercise(exerciseID, username);
  }

  public String objectToJson(Object object) {
    return persistence.objectToJson(object);
  }

  public <T> Object jsonToObject(String jsonString, Class<T> cls) {
    return persistence.jsonToObject(jsonString, cls);
  }

  private <T> String giveID(String username, Class<T> cls) {
    final String legalChars;
    if (cls == Exercise.class) {
      legalChars = "abcdefghijklmnopqrstuvwxyz0123456789";
    } else if (cls == Workout.class) {
      legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    } else {
      throw new IllegalArgumentException("Class must be type Exercise or Workout!");
    }
    Id ids = persistence.getIds(username);
    String id;
    while (true) {
      int index1 = ThreadLocalRandom.current().nextInt(legalChars.length());
      int index2 = ThreadLocalRandom.current().nextInt(legalChars.length());
      id = "" + legalChars.charAt(index1) + legalChars.charAt(index2);
      if (!ids.hasWorkoutID(id)) {
        break;
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
