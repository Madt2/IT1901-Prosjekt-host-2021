package beastbook.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class UserTest {
  
@Test
void testCreateUser(){
  User user = new User("TestUser", "MyPassword123");
  assertEquals("TestUser", user.getUsername());
  assertEquals("MyPassword123", user.getPassword());

  assertNotEquals("Test User", user.getUsername());
  assertNotEquals("MyPassword12", user.getPassword());
}

@Test
void testConstructor() {
  assertThrows(IllegalArgumentException.class, () -> {
    User user = new User("", "");
  });
  assertThrows(IllegalArgumentException.class, () -> {
    User user = new User("ts", "nts");
  });
  assertThrows(IllegalArgumentException.class, () -> {
    User user = new User("nts", "ts");
  });
}
}
