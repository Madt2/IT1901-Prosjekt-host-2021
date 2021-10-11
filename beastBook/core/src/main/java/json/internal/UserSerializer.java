package json.internal;

import core.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Workout;

import java.io.IOException;




public class UserSerializer extends JsonSerializer<User>{
    /*
    * format: { username: "...", password: "...", workouts: "[...,...]" }
    */

    private final boolean deep;

    public UserSerializer(boolean deep) {
        this.deep = deep;
    }


    public UserSerializer() {
        this(true);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (user.getUserName() != null) {
            jsonGenerator.writeStringField("username", user.getUserName());
        }
        if (user.getPassword() != null) {
            jsonGenerator.writeStringField("password", user.getPassword());
        }
        if (user instanceof User) {
            jsonGenerator.writeArrayFieldStart("workouts");
            for (Workout item : user.getWorkouts()) {
                jsonGenerator.writeObject(item);
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }
}
