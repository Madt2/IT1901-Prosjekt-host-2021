package beastbook.client;

import beastbook.core.Exceptions;
import beastbook.core.User;

import java.net.URISyntaxException;

public class RegisterController {
  ClientService clientService = new ClientService();

  public void registerUser(String username, String password) throws Exceptions.BadPackageException, Exceptions.UserAlreadyExistException, Exceptions.ServerException, URISyntaxException {
    User user = new User(username, password);
    clientService.createUser(user);
  }
}


//    try {
//      User user = new User(username, password);
//      clientService.createUser(user).getBody();
//      return "Success";
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//      return "Failure";
//    }

    //Throws exceptions if user object does not get validated or creating user dir
    // on server fails and throw exception
//  }

//  public static void main(String[] args) {
//    RegisterController reg = new RegisterController();
//    reg.registerUser("test", "test");
//  }

