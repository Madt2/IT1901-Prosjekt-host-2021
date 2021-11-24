package beastbook.core;

/**
 * Class used to store property values. Can be changed to change behavior of core objects.
 */
public class Properties {
  public static final int maxStringLength = 50;
  public static final int maxIntLength = 5;
  public static final int maxDoubleLength = 7;

  public static final int MIN_CHAR_USERNAME = 3;
  public static final int MIN_CHAR_PASSWORD = 3;

  public static final int HISTORY_ID_LENGTH = 3;
  public static final int EXERCISE_ID_LENGTH = 2;
  public static final int WORKOUT_ID_LENGTH = 2;
  public static final String LEGAL_CHARS_HISTORY_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  public static final String LEGAL_CHARS_EXERCISE_ID = "abcdefghijklmnopqrstuvwxyz0123456789";
  public static final String LEGAL_CHARS_WORKOUT_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Mock values for testing purposes:
   */
  public static final String notValidHistoryId = "notValid";
  public static final String notValidExerciseId = "notValid";
  public static final String notValidWorkoutId = "notValid";
  public static final User mockUser = new User("mock", "mock");
}
