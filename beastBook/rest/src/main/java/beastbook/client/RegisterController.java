package beastbook.client;

import beastbook.core.Exceptions;
import beastbook.core.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;

/**
 * Controller for registering a user in the REST server.
 */
public class RegisterController {
  ClientService clientService = new ClientService();

  /**
   * Sends a request to register the user in the REST Server.
   *
   * @param username of the User to be registered
   * @param password of the User to be registered
   * @throws Exceptions.BadPackageException if an error occurs in parsing json in the REST server.
   * @throws Exceptions.ServerException if an unknown error occurs in the REST server.
   * @throws Exceptions.UserAlreadyExistException if the User already exists in the server.
   * @throws URISyntaxException if an error occurs while parsing URI in ClientService.
   * @throws JsonProcessingException if an error occurs while parsing a Json string.
   */
  public void registerUser(String username, String password)
      throws Exceptions.BadPackageException, Exceptions.UserAlreadyExistException,
      Exceptions.ServerException, URISyntaxException, JsonProcessingException {
    if (username == null || password == null) {
      throw new IllegalArgumentException("Username and password cannot be null!");
    }
    User user = new User(username, password);
    clientService.createUser(user);
  }
}


