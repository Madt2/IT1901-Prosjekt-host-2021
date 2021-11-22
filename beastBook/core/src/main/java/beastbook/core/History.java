package beastbook.core;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static beastbook.core.Validation.validateId;

/**
 * History class saves a workout with a final workout object and date it was saved.
 */
public class History implements IdClasses {
  private String id;
  private final String name;
  private final String date;
  private final List<Exercise> savedExercises;

  /**
  * Constructor for History object.
  *
  * @param name name of workout to log.
  * @param exercises The Exercises to be saved.
  */
  public History(String name, List<Exercise> exercises) {
    this.name = name;
    this.savedExercises = Collections.unmodifiableList(exercises);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    date = sdf.format(new Date());
  }

  public History(String name, List<Exercise> exercises, String date) {
    this.name = name;
    this.savedExercises = Collections.unmodifiableList(exercises);
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    this.date = date;
  }

  public void setId(String id) throws Exceptions.IllegalIdException {
    validateId(id, this.getClass());
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
  // TODO Correct to not return copy because it is final, right?
  public List<Exercise> getSavedExercises() {
    return savedExercises;
  }

  @Override
  public String toString() {
    return getName() + ", " + getDate() + ": " + getSavedExercises();
  }
}