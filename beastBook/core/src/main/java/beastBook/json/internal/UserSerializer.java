package beastBook.json.internal;

import beastBook.core.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import beastBook.core.Workout;
import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {
  /*
  * format for User in json: { username: "...", password: "...", workouts: "[...,...]" }
  */

  /**
  * Serializes user from input argument. Serializes user-object to json format.
  * @param user user to serialize.
  * @param jsonGenerator
  * @param serializerProvider
  * @throws IOException
  */
  @Override
  public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    if (user.getUserName() != null) {
      jsonGenerator.writeStringField("username", user.getUserName());
    }
    if (user.getPassword() != null) {
      jsonGenerator.writeStringField("password", user.getPassword());
    }
    jsonGenerator.writeArrayFieldStart("workouts");
    for (Workout item : user.getWorkouts()) {
      jsonGenerator.writeObject(item);
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
