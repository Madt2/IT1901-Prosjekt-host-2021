package beastbook.core;

import static beastbook.core.Properties.*;
import static beastbook.core.Validation.validateId;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * IdHandler class. This class keeps track of what IDs are in use for workout and exercise objects,
 * and contains a map of (exerciseId : exerciseName), (workoutId : workoutName)
 * and history (historyId : name;date).
 */
public class IdHandler {
  static int legalLength;
  static String legalChars;
  private LinkedHashMap<String, String> exerciseMap = new LinkedHashMap<>();
  private LinkedHashMap<String, String> workoutMap = new LinkedHashMap<>();
  private LinkedHashMap<String, String> historyMap = new LinkedHashMap<>();

  public LinkedHashMap<String, String> getMap(Class<?> cls) throws IllegalArgumentException {
    return new LinkedHashMap<>(Collections.synchronizedMap(getEditableMap(cls)));
  }

  /**
   * Help method for getting editable hashmap of given class.
   *
   * @param cls Class to get hashmap for.
   * @return hashmap for given class.
   * @throws IllegalArgumentException if Class is not valid input for this method.
   */
  private LinkedHashMap<String, String> getEditableMap(Class<?> cls) throws IllegalArgumentException {
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

  /**
   * Setter for setting legal characters and lenght of id for given Class.
   *
   * @param cls class to set legals for.
   * @throws IllegalArgumentException if Class input is not valid for this method.
   */
  public static void setLegals(Class<?> cls) throws IllegalArgumentException {
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

  /**
   * Checks of idHandler has if stored for given class.
   *
   * @param id to check if exists.
   * @param cls Class to check for.
   * @return true if idHandler has given id stored, false otherwise.
   * @throws IllegalArgumentException if given Class is not valid for this method.
   */
  private boolean hasId(String id, Class<?> cls) throws IllegalArgumentException {
    return getMap(cls).containsKey(id);
  }

  /**
   * Adds id to hashmap with given class.
   *
   * @param id to add.
   * @param displayData data to connect id to (name).
   * @param cls Class for hashmap to add id to.
   * @throws Exceptions.IdAlreadyInUseException if idHandler already has id stored.
   */
  public void addId(String id, String displayData, Class<?> cls) throws Exceptions.IdAlreadyInUseException {
    if (hasId(id, cls)) {
      throw new Exceptions.IdAlreadyInUseException(cls, id);
    }
    getEditableMap(cls).put(id, displayData);
  }

  /**
   * Removes id from hashmap for given Class.
   *
   * @param id to remove.
   * @param cls Class for hashmap to remove id from.
   * @throws Exceptions.IdNotFoundException if idHandler does not have id stored.
   */
  public void removeId(String id, Class<?> cls) throws Exceptions.IdNotFoundException {
    if (!hasId(id, cls)) {
      throw new Exceptions.IdNotFoundException(cls, id);
    }
    getEditableMap(cls).remove(id);
  }

  /**
   * Id generator for given class. Use legal characters and legal length
   * to generate a valid id for given Class.
   *
   * @param cls the class of the object that needs an Id
   * @return the generated Id
   * @throws IllegalStateException if there are no more available Ids that can be created.
   * @throws StackOverflowError if generator loops more than 1000 times.
   */
  private String generateIdWhileLoop(Class<?> cls) throws IllegalStateException, StackOverflowError {
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
        id = id.concat(String.valueOf(legalChars.charAt(randInt)));
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
      throw new StackOverflowError("IdHandler generator tried to generate id 1000 times, "
          + "might be error with generator, or small ids available left!");
    }
    return id;
  }

  /**
   * Gives id to IdClass object. This adds the given id to hashmap corresponding to object class type,
   * and sets id of given object.
   *
   * @param obj object to give id to.
   * @return object with given id.
   */
  public IdClasses giveId(IdClasses obj) {
    String id;
    id = generateIdWhileLoop(obj.getClass());
    try {
      obj.setId(id);
      if (obj.getClass() == History.class) {
        History history = (History) obj;
        addId(history.getId(), history.getName() + ";" + history.getDate(), History.class);
        obj = history;
      } else {
        addId(obj.getId(), obj.getName(), obj.getClass());
      }
    } catch (Exceptions.IdAlreadyInUseException | Exceptions.IllegalIdException e) {
      throw new IllegalStateException("Failed to give id to object! Error: " + obj.getClass().getSimpleName());
    }
    return obj;
  }
}