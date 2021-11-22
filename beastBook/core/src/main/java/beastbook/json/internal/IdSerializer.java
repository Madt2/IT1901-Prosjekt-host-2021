package beastbook.json.internal;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.Id;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.LinkedHashMap;

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
    jsonGenerator.writeArrayFieldStart("exerciseMap");
    for (String Id : id.getMap(Exercise.class).keySet()) {
      jsonGenerator.writeObject( id + ":" + id.getMap(Exercise.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("workoutMap");
    for (String Id : id.getMap(Workout.class).keySet()) {
      jsonGenerator.writeObject( id + ":" + id.getMap(Workout.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("historyMap");
    for (String Id : id.getMap(History.class).keySet()) {
      jsonGenerator.writeObject( id + ":" + id.getMap(History.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
