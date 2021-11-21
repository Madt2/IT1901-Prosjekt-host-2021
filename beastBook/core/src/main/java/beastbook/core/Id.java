package beastbook.core;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static beastbook.core.Properties.*;


/**
 * Id class. This class keeps track of what IDs are in use for workout and exercise objects,
 * and contains a map of (exerciseID : exerciseName) and (workoutID : workoutName).
 */
public class Id {
  private static int legalLength;
  private static String legalChars;
  private Map<String, String> exerciseMap = new LinkedHashMap<>();
  private Map<String, String> workoutMap = new LinkedHashMap<>();
  private Map<String, String> historyMap = new LinkedHashMap<>();

  public Map<String, String> getMap(Class cls) throws IllegalArgumentException {
    return new LinkedHashMap<>(Collections.synchronizedMap(getEditableMap(cls)));
  }

  private Map<String, String> getEditableMap(Class cls) throws IllegalArgumentException {
    Map<String, String> map;
    if (cls == Exercise.class) {
      return exerciseMap;
    } else if (cls == Workout.class) {
      return workoutMap;
    } else if (cls == History.class) {
      return historyMap;
    } else {
      throw new IllegalArgumentException("Class must be type Exercise, Workout or History!");
    }
  }

  public static void setLegals(Class cls) throws IllegalArgumentException {
    if (cls == Exercise.class) {
      legalChars = LEGAL_CHARS_EXERCISE_ID;
      legalLength = EXERCISE_ID_LENGTH;
    } else if (cls == Workout.class) {
      legalChars = LEGAL_CHARS_WORKOUT_ID;
      legalLength = WORKOUT_ID_LENGTH;
    } else if (cls == History.class) {
      legalChars = LEGAL_CHARS_HISTORY_ID;
      legalLength = HISTORY_ID_LENGTH;
    } else {
      throw new IllegalArgumentException("Class must be type Exercise, Workout or History!");
    }
  }

  private boolean hasId(String id, Class cls) {
    return getMap(cls).containsKey(id);
  }

  public void addId(String id, String displayData, Class cls) throws Exceptions.IdAlreadyInUseException {
    if (hasId(id, cls)) {
      throw new Exceptions.IdAlreadyInUseException(cls, id);
    }
    getEditableMap(cls).put(id, displayData);
  }

  public void removeId(String id, Class cls) throws Exceptions.IdNotFoundException {
    if (!hasId(id, cls)) {
      throw new Exceptions.IdNotFoundException(cls, id);
    }
    getEditableMap(cls).remove(id);
  }

  public static void validateId(String id, Class cls) throws Exceptions.IllegalIdException {
    setLegals(cls);
    if (id.length() != legalLength) {
      throw new Exceptions.IllegalIdException("ID does not contain right amount of characters!");
    }
    for (int i = 0; i < legalChars.length(); i++) {
      for (int j = 0; j < id.length(); j++) {
        if (id.charAt(j) == legalChars.charAt(i)) {
          id = id.replace(id.substring(j, j + 1), "");
        }
        if (id.length() == 0) {
          return;
        }
      }
    }
    throw new Exceptions.IllegalIdException("ID does not use correct characters!");
  }

  private String generateIdWhileLoop(Class cls) throws IllegalStateException, StackOverflowError {
    setLegals(cls);
    int possibilities = (int) Math.pow(legalChars.length(), legalLength);
    if (getMap(cls).size() == possibilities) {
      throw new IllegalStateException("Not any more free ids for " + cls.getName()
              + " class, please increase id limit for " + cls.getName()
              + ", or delete some " + cls.getName() + "s though client!");
    }
    String id = null;
    int genTries = 1000;
    while (genTries > 0) {
      id = "";
      for (int i = 0; i < legalLength; i++) {
        int randInt = ThreadLocalRandom.current().nextInt(legalChars.length());
        id += legalChars.charAt(randInt);
      }
      try {
        validateId(id, cls);
      } catch (Exceptions.IllegalIdException e) {
        throw new IllegalStateException("Something wrong with id generator in IdHandler! Error: " + e);
      }
      if (!hasId(id, cls)) {
        break;
      }
      genTries--;
    }
    if (genTries == 0) {
      throw new StackOverflowError("Id generator tried to generate id 1000 times, " +
              "might be error with generator, or small ids available left!");
    }
    return id;
  }

  public IdClasses giveId(IdClasses obj) {
    String id = null;
    id = generateIdWhileLoop(obj.getClass());
    try {
      obj.setId(id);
      if (obj.getClass() == History.class) {
        History history = (History) obj;
        addId(history.getId(), history.getName() + ";" + history.getDate(), History.class);
        obj = history;
      } else {
        addId(obj.getId(), obj.getName() ,obj.getClass());
      }
    } catch (Exceptions.IdAlreadyInUseException | Exceptions.IllegalIdException e) {
      throw new IllegalStateException("Failed to give id to object! Error: " + obj.getClass().getSimpleName());
    }
    return obj;
  }
}