package beastbook.core;

import static beastbook.core.Properties.MIN_CHAR_PASSWORD;
import static beastbook.core.Properties.MIN_CHAR_USERNAME;

/**
 * User class for application. Creates a user with username, password and a list of references to workouts.
 */
public class User {
  private final String username;
  private final String password;

  /**
  * User object for application.
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

  /**
   * Validation method for setUsername. Checks for username length, has to be 3 or more characters.
   *
   * @param username to validate.
   * @throws IllegalArgumentException if username is too short.
   */
  private void validateUsername(String username) throws IllegalArgumentException {
    boolean isLongEnough = username.length() >= MIN_CHAR_USERNAME;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Username must be " + MIN_CHAR_USERNAME + " or more characters!"
      );
    }
  }

  /**
   * Validation method for Password. Checks for password length.
   *
   * @param password to validate.
   */
  private void validatePassword(String password) throws IllegalArgumentException {
    boolean isLongEnough = password.length() >= MIN_CHAR_PASSWORD;
    if (!isLongEnough) {
      throw new IllegalArgumentException(
              "Password must be " + MIN_CHAR_PASSWORD + " or more characters!"
      );
    }
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}

