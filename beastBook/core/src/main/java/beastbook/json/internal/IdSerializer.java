package beastbook.json.internal;

import beastbook.core.Id;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IdSerializer extends JsonSerializer<Id> {
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
    jsonGenerator.writeEndObject();
  }
}
