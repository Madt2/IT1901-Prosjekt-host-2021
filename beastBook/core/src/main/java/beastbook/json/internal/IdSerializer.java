package beastbook.json.internal;

import beastbook.core.Id;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IdSerializer extends JsonSerializer<Id> {

  /**
   * Serializes Id from input argument. Serializes Id-object to json format.
   * Format for Id in json: { username: "...", password: "...", workouts: "[...,...]" }.
   *
   * @param id Id to serialize.
   * @param jsonGenerator class that writes JSON-file.
   * @param serializerProvider serializers needed to serialize object.
   * @throws IOException if there is either an underlying I/O problem or
   *     encoding issue at format layer
   */
  @Override
  public void serialize(
          Id id,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeArrayFieldStart("workoutIDs");
    for (String ID : id.getWorkoutIDs()) {
      jsonGenerator.writeObject(ID);
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("exerciseIDs");
    for (String ID : id.getExerciseIDs()) {
      jsonGenerator.writeObject(ID);
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("exerciseNameIDMap");
    for (String ID : id.getExerciseIDs()) {
      jsonGenerator.writeObject(ID + ":" + id.getExerciseIDName(ID));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("workoutNameIDMap");
    for (String ID : id.getWorkoutIDs()) {
      jsonGenerator.writeObject(ID + ":" + id.getWorkoutIDName(ID));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
