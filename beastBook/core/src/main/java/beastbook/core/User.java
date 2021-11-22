package beastbook.core;

import static beastbook.core.Validation.validatePassword;
import static beastbook.core.Validation.validateUsername;

/**
 * User class for application. Creates a user with username, password and a list of references to workouts.
 */
public class User {
  private final String username;
  private final String password;

  /**
  * User constructor. Validates input.
  *
  * @param username username for user.
  * @param password password for user.
  */
  public User(String username, String password) {
    validateUsername(username);
    this.username = username;
    validatePassword(password);
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}

