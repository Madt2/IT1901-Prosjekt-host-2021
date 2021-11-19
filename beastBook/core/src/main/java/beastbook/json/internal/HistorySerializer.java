package beastbook.json.internal;

import beastbook.core.Exercise;
import beastbook.core.History;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom JSON-Serializer for History, converts History object to JSON-file.
 */
public class HistorySerializer extends JsonSerializer<History> {
  /**
   * Serializes History from input-arguments. Serializes History object to json format.
   * Format for History in json: { date: "...", savedWorkout: "[...,...]"}.
   *
   * @param history History object to serialize.
   * @param jsonGenerator class that writes JSON-file.
   * @param serializerProvider provides serializers needed
   *     to serialize instances of types in object.
   * @throws IOException if there is either an
   *     underlying I/O problem or encoding issue at format layer
   */
  @Override
  public void serialize(
      History history,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    if (history.getName() != null) {
      jsonGenerator.writeStringField("id", history.getId());
    }
    if (history.getName() != null) {
      jsonGenerator.writeStringField("name", history.getName());
    }
    if (history.getDate() != null) {
      jsonGenerator.writeStringField("date", history.getDate());
    }
    if (history.getSavedExercises() != null) {
      jsonGenerator.writeArrayFieldStart("savedExercises");
      for (Exercise e : history.getSavedExercises()) {
        jsonGenerator.writeObject(e);
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndObject();
  }
}

