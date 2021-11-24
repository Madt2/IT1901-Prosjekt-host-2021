package beastbook.json.internal;

import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.IdHandler;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IdSerializer extends JsonSerializer<IdHandler> {
  //Todo update javadocs:
  /**
   * Serializes IdHandler from input argument. Serializes IdHandler-object to json format.
   * Format for IdHandler in json: { username: "...", password: "...", workouts: "[...,...]" }.
   *
   * @param idHandler IdHandler to serialize.
   * @param jsonGenerator class that writes JSON-file.
   * @param serializerProvider serializers needed to serialize object.
   * @throws IOException if there is either an underlying I/O problem or
   *     encoding issue at format layer
   */
  @Override
  public void serialize(
          IdHandler idHandler,
          JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeArrayFieldStart("exerciseMap");
    for (String id : idHandler.getMap(Exercise.class).keySet()) {
      jsonGenerator.writeObject(id + ":" + idHandler.getMap(Exercise.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("workoutMap");
    for (String id : idHandler.getMap(Workout.class).keySet()) {
      jsonGenerator.writeObject(id + ":" + idHandler.getMap(Workout.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeArrayFieldStart("historyMap");
    for (String id : idHandler.getMap(History.class).keySet()) {
      jsonGenerator.writeObject(id + ":" + idHandler.getMap(History.class).get(id));
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
