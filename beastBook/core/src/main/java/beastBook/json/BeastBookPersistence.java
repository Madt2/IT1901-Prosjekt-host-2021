package beastBook.json;

import beastBook.core.User;
import beastBook.json.internal.BeastBookModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for saving and loading user from file.
 */
public class BeastBookPersistence {
  private ObjectMapper mapper;
  private Path saveFilePath = null;

  public BeastBookPersistence() {
    mapper = new ObjectMapper();
    mapper.registerModule(new BeastBookModule());
  }

  /**
  * Help method for loadUser.
  *
  * @param reader filereader object
  * @return user from file
  * @throws IOException when filepath from reader does not point to existing file.
  */
  private User readUser(Reader reader) throws IOException {
    return mapper.readValue(reader, User.class);
  }

  /**
  * Help method for saveUser.
  *
  * @param user user to write to file
  * @param writer filewriter object
  * @throws IOException when filepath from writer does not point to existing file.
  */
  private void writeUser(User user, Writer writer) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
  }

  /**
  * Sets path to user saveFile, required before calling loadUser and saveUser methods.
  *
  * @param saveFile name of user.
  */
  public void setSaveFilePath(String saveFile) {
    this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
  }

  /**
  * Loads user. Required to run setSaveFilePath before calling.
  *
  * @return User selected in setSaveFilePath
  * @throws IOException when filepath from FileReader does not point to existing file
  * @throws IllegalStateException when saveFilePath is null
  */
  public User loadUser() throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is missing");
    }
    try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      return readUser(reader);
    }
  }

  /**
  * Saves user. Required to run setSaveFilePath before calling.
  *
  * @param user username for user to load.
  * @throws IOException when filepath from FileWriter does not point to existing file.
  * @throws IllegalStateException when saveFilePath is null
  */
  public void saveUser(User user) throws IOException, IllegalStateException {
    if (saveFilePath == null) {
      throw new IllegalStateException("Save file path is missing");
    }
    try (Writer writer = new FileWriter(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
      writeUser(user, writer);
    }
  }
}
