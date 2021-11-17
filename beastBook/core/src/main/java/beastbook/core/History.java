package beastbook.core;

import java.util.List;

/**
 * History class saves a workout with a final workout object and date it was saved.
 */
public class History extends Workout implements IIdClases {
  private final String date;
  private final String name;
  private String id;
  private final List<Exercise> savedExercises;

  /**
   * Constructor for History object.
   *
   * @param workout The Workout to be saved.
   * @param date The date it was saved.
   */
  public History(String name, List<Exercise> workout, String date) {
    this.name = name;
    this.date = date;
    this.savedExercises = workout;
  }

  public void setId(String id) {
    Id.validateID(id, this.getClass());
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

  public List<Exercise> getSavedExercises() {
    return savedExercises;
  }

  @Override
  public String toString() {
    return getName() + ", " + getDate() + ": " + getSavedExercises();
  }
}