package json;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.User;
import json.internal.BeastBookModule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BeastBookPersistence {

    private ObjectMapper mapper;

    public BeastBookPersistence() {
        mapper = new ObjectMapper();
        mapper.registerModule(new BeastBookModule());
    }

    public User readUser(Reader reader) throws IOException {
        return mapper.readValue(reader, User.class);
    }

    public void writeUser(User user, Writer writer) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
    }

    private Path saveFilePath = null;

    public void setSaveFilePath(String saveFile) {
        this.saveFilePath = Paths.get(System.getProperty("user.home"), saveFile);
    }

    public User loadUser() throws IOException, IllegalStateException {
        if(saveFilePath == null) {
            throw new IllegalStateException("Save file path is missing");
        }
        try (Reader reader = new FileReader(saveFilePath.toFile(), StandardCharsets.UTF_8)) {
            return readUser(reader);
        }
    }

    public void saveUser(User user) throws IOException, IllegalStateException {
        if(saveFilePath == null) {
            throw new IllegalStateException("Save file path is missing");
        }
        try (Writer writer = new FileWriter(saveFilePath.toFile(),StandardCharsets.UTF_8)) {
            writeUser(user,writer);
        }
    }

}
