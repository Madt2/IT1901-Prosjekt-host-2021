package beastbook.core;

import java.util.Map;

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

  public static final String notValidHistoryId = "notValid";
  public static final String notValidExerciseId = "notValid";
  public static final String notValidWorkoutId = "notValid";
  public static final User mockUser = new User("mock", "mock");

  public static String mockIdGenerator(Class cls, Map map) throws Exceptions.ServerException {
    int legalLength;
    char[] legalChar;
    if (cls == Exercise.class) {
      legalLength = EXERCISE_ID_LENGTH;
      legalChar = LEGAL_CHARS_EXERCISE_ID.toCharArray();
    } else if (cls == Workout.class) {
      legalLength = WORKOUT_ID_LENGTH;
      legalChar = LEGAL_CHARS_WORKOUT_ID.toCharArray();
    } else if (cls == History.class) {
      legalLength = HISTORY_ID_LENGTH;
      legalChar = LEGAL_CHARS_HISTORY_ID.toCharArray();
    } else {
      return null;
    }
    String mockId = "";
    boolean hasId = true;
    int i = 0;
    while (hasId) {
      for (int j = 0; j < legalLength; j++) {
        mockId = mockId + legalChar[i];
      }
      hasId = map.containsKey(mockId);
      i++;
    }
    return mockId;
  }

}
