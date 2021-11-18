package beastbook.json.internal;

import beastbook.core.History;
import beastbook.core.User;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom JSON-Serializer for User, converts User objects to JSON-File.
 */
public class UserSerializer extends JsonSerializer<User> {

  /**
  * Serializes user from input argument. Serializes user-object to json format.
  * Format for User in json: { exerciseIDs: "[...,...]", workoutIDs: "[...,...]",
  *             exerciseNameIDMap: "[...,...]", exerciseNameIDMap: "[...,...]" }.
  *
  * @param user User to serialize.
  * @param jsonGenerator class that writes JSON-file.
  * @param serializerProvider serializers needed to serialize object.
  * @throws IOException if there is either an underlying I/O problem or
  *     encoding issue at format layer
  */
  @Override
  public void serialize(
      User user,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    if (user.getUsername() != null) {
      jsonGenerator.writeStringField("username", user.getUsername());
    }
    if (user.getPassword() != null) {
      jsonGenerator.writeStringField("password", user.getPassword());
    }
//    jsonGenerator.writeArrayFieldStart("workoutIDs");
//    for (String id : user.getWorkoutIDs()) {
//      jsonGenerator.writeObject(id);
//    }
//    jsonGenerator.writeEndArray();
//    jsonGenerator.writeArrayFieldStart("historyIDs");
//    for (String id : user.getHistoryIDs()) {
//      jsonGenerator.writeObject(id);
//    }
//    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
