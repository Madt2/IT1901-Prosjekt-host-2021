package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import json.internal.BeastBookModule;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BeastBookPersistence {
    /**
     * Class for saving and loading user from file.
     */
    private ObjectMapper mapper;
    private Path saveFilePath = null;

    public BeastBookPersistence() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BeastBookModule());
    }

    /**
     * Help method for loadUser
     * @param reader
     * @return
     * @throws IOException
     */
    private User readUser(Reader reader) throws IOException {
        return mapper.readValue(reader, User.class);
    }

    /**
     * Help method for saveUser
     * @param user
     * @param writer
     * @throws IOException
     */
    private void writeUser(User user, Writer writer) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
    }

    /**
     * Sets path to user saveFile, required before calling loadUser and saveUser methods.
     * @param saveFile name of user.
     */
    public void setSaveFilePath(String saveFile) {
        this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
    }

    /**
     * Loads user. Required to run setSaveFilePath before calling.
     * @return User selected in setSaveFilePath
     * @throws IOException
     * @throws IllegalStateException
     */
    public User loadUser() throws IOException, IllegalStateException {
        if(saveFilePath == null) {
            throw new IllegalStateException("Save file path is missing");
        }
        try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
            return readUser(reader);
        }
    }

    /**
     * Saves user. Required to run setSaveFilePath before calling.
     * @param user username for user to load.
     * @throws IOException
     * @throws IllegalStateException
     */
    public void saveUser(User user) throws IOException, IllegalStateException {
        if(saveFilePath == null) {
            throw new IllegalStateException("Save file path is missing");
        }
        try (Writer writer = new FileWriter(saveFilePath.toFile(),StandardCharsets.UTF_8)) {
            writeUser(user,writer);
        }
    }

}
