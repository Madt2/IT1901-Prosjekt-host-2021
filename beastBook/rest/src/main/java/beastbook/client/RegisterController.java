package beastbook.client;

import beastbook.core.User;

public class RegisterController {
  ClientService clientService = new ClientService();

  public String registerUser(String username, String password) {
    try {
      User user = new User(username, password);
      clientService.createUser(user).getBody();
      return "Success";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "Failure";
    }

    //Throws exceptions if user object does not get validated or creating user dir
    // on server fails and throw exception
  }

//  public static void main(String[] args) {
//    RegisterController reg = new RegisterController();
//    reg.registerUser("test", "test");
//  }
}
