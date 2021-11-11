package beastbook.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RestApiApplication {
  private UserController userController = new UserController(new UserService());

  public static void main(String[] args) {
    SpringApplication.run(RestApiApplication.class, args);
  }
}
