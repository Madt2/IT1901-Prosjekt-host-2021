package beastbook.json.internal;

import beastbook.core.Exercise;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom JSON-Serializer for Workout, converts Workout object to JSON-file.
 */
public class WorkoutSerializer extends JsonSerializer<Workout> {
  /**
  * Serializes Workout from input-arguments. Serializes Workout-object to json format.
  * Format for Workout in json: { name: "...", exercises: "[...,...]"}.
  *
  * @param workout Workout to serialize.
  * @param jsonGenerator class that writes JSON-file.
  * @param serializerProvider provides serializers needed
  *     to serialize instances of types in object.
  * @throws IOException if there is either an
  *     underlying I/O problem or encoding issue at format layer
  */
  @Override
  public void serialize(
      Workout workout,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    if (workout.getName() != null) {
      jsonGenerator.writeStringField("name", workout.getName());
    }
    jsonGenerator.writeArrayFieldStart("exercises");
    for (Exercise item : workout.getExercises()) {
      jsonGenerator.writeObject(item);
    }
    jsonGenerator.writeEndArray();
    jsonGenerator.writeEndObject();
  }
}
