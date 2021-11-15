package beastbook.core;

/**
 * History class saves a workout with a final workout object and date it was saved.
 */
public class History extends Workout {
  private final String date;
  private final String name;
  private final Workout savedWorkout;

  /**
   * Constructor for History object.
   *
   * @param workout The Workout to be saved.
   * @param date The date it was saved.
   */
  public History(Workout workout, String date) {
    this.name = workout.getName();
    this.date = date;
    this.savedWorkout = workout.copy(workout);
  }

  public String getDate() {
    return date;
  }

  public Workout getSavedWorkout() {
    return savedWorkout;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return getSavedWorkout() + ", " + getDate();
  }
}
