package beastbook.client;

import beastbook.core.User;

public class RegistrationController {
  ClientService clientService = new ClientService();

  public void registrerUser(String username, String password) {
    User user = new User(username, password);
    clientService.createUser(user);
    //Throws exceptions if user object does not get validated or creating user dir
    // on server fails and throw exception
  }
}
