package beastbook.client;

import beastbook.core.Exceptions;
import beastbook.core.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URISyntaxException;

public class RegisterController {
  ClientService clientService = new ClientService();

  public void registerUser(String username, String password) throws Exceptions.BadPackageException, Exceptions.UserAlreadyExistException, Exceptions.ServerException, URISyntaxException, JsonProcessingException {
    if (username == null || password == null) {
      throw new IllegalArgumentException("Username and password cannot be null!");
    }
    User user = new User(username, password);
    clientService.createUser(user);
  }
}


