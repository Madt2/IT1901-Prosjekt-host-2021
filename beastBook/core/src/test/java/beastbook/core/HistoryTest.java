package beastbook.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import beastbook.core.Exceptions.IllegalIdException;

public class HistoryTest {

  private History testHistory;
  private List<Exercise> exerciseList;

  @BeforeEach
  void setup(){
    exerciseList = new ArrayList<>();
    Exercise ex1 = new Exercise("Bench press", 20, 20, 20, 20, 20);
    Exercise ex2 = new Exercise("Squats", 30, 30, 30, 30, 30);
    exerciseList.add(ex1);
    exerciseList.add(ex2);
    testHistory = new History("historyTest", exerciseList);
  }

  @Test
  void correctDateOnHistoryTest(){
    String todaysDate;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    todaysDate = sdf.format(new Date());
    assertEquals(todaysDate, testHistory.getDate());
  }
  @Test
  void addExercisesToHistory(){
    Exercise ex3 = new Exercise ("Deadlift", 50, 50, 50, 50, 50);
    exerciseList.add(ex3);
    testHistory = new History("historyTest", exerciseList);
    assertEquals(3, testHistory.getSavedExercises().size());

    assertEquals(ex3, testHistory.getSavedExercises().get(2));
    assertNotEquals(ex3, testHistory.getSavedExercises().get(0));
  }

  @Test
  void incorrectHistoryIdFailsTest(){
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("Kk3");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("   ");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("QQKK");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("aa.");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("QQQ22");
    });
    assertThrows(IllegalIdException.class, () -> {
      testHistory.setId("aa2");
    });
  }
}
