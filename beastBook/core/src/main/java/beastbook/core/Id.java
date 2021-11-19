package beastbook.core;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Id class. This class keeps track of what IDs are in use for workout and exercise objects,
 * and contains a map of (exerciseID : exerciseName) and (workoutID : workoutName).
 */
public class Id {
  public static final int HISTORY_ID_LENGTH = 3;
  public static final int EXERCISE_ID_LENGTH = 2;
  public static final int WORKOUT_ID_LENGTH = 2;
  public static final String LEGAL_CHARS_HISTORY_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  public static final String LEGAL_CHARS_EXERCISE_ID = "abcdefghijklmnopqrstuvwxyz0123456789";
  public static final String LEGAL_CHARS_WORKOUT_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private Map<String, String> exerciseMap = new LinkedHashMap<>();
  private Map<String, String> workoutMap = new LinkedHashMap<>();
  private Map<String, String> historyMap = new LinkedHashMap<>();

  public Map<String, String> getMap(Class cls) throws IllegalArgumentException {
    Map<String, String> map;
    if (cls == Exercise.class) {
      map = Collections.synchronizedMap(exerciseMap);
    } else if (cls == Workout.class) {
      map = Collections.synchronizedMap(workoutMap);
    } else if (cls == History.class) {
      map = Collections.synchronizedMap(historyMap);
    } else {
      throw new IllegalArgumentException("Class must be type Exercise, Workout or History!");
    }
    return new LinkedHashMap<>(map);
  }

  private static String setLegalChars(Class cls) throws IllegalArgumentException {
    final String legalChars;
    if (cls == Exercise.class) {
      legalChars = LEGAL_CHARS_EXERCISE_ID;
    } else if (cls == Workout.class) {
      legalChars = LEGAL_CHARS_WORKOUT_ID;
    } else if (cls == History.class) {
      legalChars = LEGAL_CHARS_HISTORY_ID;
    } else {
      throw new IllegalArgumentException("Class must be type Exercise, Workout or History!");
    }
    return legalChars;
  }

  private static int setLegalLength(Class cls) throws IllegalArgumentException {
    final int legalLength;
    if (cls == Exercise.class) {
      legalLength = EXERCISE_ID_LENGTH;
    } else if (cls == Workout.class) {
      legalLength = WORKOUT_ID_LENGTH;
    } else if (cls == History.class) {
      legalLength = HISTORY_ID_LENGTH;
    } else {
      throw new IllegalArgumentException("Class must be type Exercise, Workout or History!");
    }
    return legalLength;
  }

  private boolean hasId(String id, Class cls) throws IllegalArgumentException {
    List<String> ids = getIds(cls);
    return ids.contains(id);
  }

  public List<String> getIds(Class cls) throws IllegalArgumentException {
    return new ArrayList<>(getMap(cls).keySet());
  }

  public String getName(String id, Class cls) throws IllegalArgumentException {
    return getMap(cls).get(id);
  }

  public void addId(String id, String displayData, Class cls) throws IllegalArgumentException {
    if (hasId(id, cls)) {
      throw new IllegalArgumentException(cls.getName() + " already have ID " + id + " stored");
    }
    if (cls == Exercise.class) {
      exerciseMap.put(id, displayData);
    } else if (cls == Workout.class) {
      workoutMap.put(id, displayData);
    } else if (cls == History.class) {
      historyMap.put(id, displayData);
    }
  }

  /**
   * Removes id from IDs in use.
   *
   * @param id to remove from .
   * @param cls type of class to remove ID for.
   * @throws IllegalArgumentException if ID is not stored in file.
   */
  public void removeId(String id, Class cls) throws IllegalArgumentException {
    Map<String, String> map = getMap(cls);
    if (!hasId(id, cls)) {
      throw new IllegalArgumentException(cls.getName() + " does not have ID " + id + " stored in file.");
    }
    map.remove(id);
  }

  /**
   * Checks if ID given is valid for class.
   *
   * @param id to be checked.
   * @param cls class to validate id for.
   * @throws IllegalArgumentException when amount of characters in id is wrong,
   *                                  or if id consists of wrong characters.
   */
  public static void validateID(String id, Class cls) throws IllegalArgumentException {
    final String legalChars = setLegalChars(cls);
    final int legalLength = setLegalLength(cls);
    if (id.length() != legalLength) {
      throw new IllegalArgumentException("ID does not contain right amount of characters!");
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
    throw new IllegalArgumentException("ID does not use correct characters!");
  }

  /**
   * Generates id in random fashion for given class.
   *
   * @param cls to generate ID for.
   * @return valid id for class
   * @throws IllegalArgumentException if class type is not valid.
   * @throws IllegalStateException if no more ids ar available for object type.
   */
  private String generateIdWhileLoop(Class cls, int possibilities) throws IllegalArgumentException, IllegalStateException {
    final String legalChars = setLegalChars(cls);
    final int legalLength = setLegalLength(cls);
    if (getIds(cls).size() == possibilities) {
      throw new IllegalStateException("Not any more free ids for " + cls.getName()
              + " class, please increase id limit for " + cls.getName()
              + ", or delete some " + cls.getName() + "s though client!");
    }
    String id = "";
    while (true) {
      for (int i = 0; i < legalLength; i++) {
        int randInt = ThreadLocalRandom.current().nextInt(legalChars.length());
        id += legalChars.charAt(randInt);
      }
      if (!hasId(id, cls)) {
        break;
      }
    }
    validateID(id, cls);
    return id;
  }

/*  *//**
   * Generates ID with bruteforce checking for available ids given class.
   *
   * @param cls to generate ID for.
   * @return valid id for class
   * @throws IllegalArgumentException if class type is not valid.
   * @throws IllegalStateException if no more ids ar available for object type.
   *//*
  private String generateIdForLoop(Class cls, int possibilities) throws IllegalArgumentException, IllegalStateException {
    final String legalChars = setLegalChars(cls);
    final int legalLength = setLegalLength(cls);
    if (getIds(cls).size() == possibilities) {
      throw new IllegalStateException("Not any more free ids for " + cls.getName()
              + " class, please increase id limit for " + cls.getName()
              + ", or delete some " + cls.getName() + "s though client!");
    }
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < legalLength; i++) {
      indexes.add(legalChars.length());
    }
    return "does not work!";
  }*/

  /**
   * Gives id to IdObject.
   *
   * @param obj object to give id.
   * @return IdClass with set id
   * @throws IllegalArgumentException if object already has an id.
   * @throws IllegalStateException If no more ids are available.
   */
  public IdClasses giveId(IdClasses obj) throws IllegalArgumentException, IllegalStateException {
    Class cls = obj.getClass();
    String id = null;
    final String legalChars = setLegalChars(cls);
    final int legalLength = setLegalLength(cls);
    int possibilities = (int) Math.pow(legalChars.length(), legalLength);
 //   if (getIds(cls).size() < possibilities / 2) {
      id = generateIdWhileLoop(cls, possibilities);
//    }
    //TODO FIX this
    /*else {
      id = generateIdForLoop(cls, possibilities);
    }*/
    obj.setId(id);
    if (obj.getClass() == History.class) {
      History history = (History) obj;
      addId(history.getId(), history.getName() + "§" + history.getDate(), history.getClass());
      obj = history;
    } else {
      addId(obj.getId(), obj.getName() ,obj.getClass());
    }
    return obj;
  }
}