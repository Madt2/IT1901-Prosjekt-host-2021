package beastbook.json.internal;

import beastbook.core.Exercise;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom JSON-Serializer for Exercise objects, converts Exercise objects to JSON-file.
 */
public class ExerciseSerializer extends JsonSerializer<Exercise> {

  /**
  * Serializes Exercise from input argument. Serializes Exercise object to JSON-format.
  * Format for Exercise in json:
  * { id: "...", workoutId: "...", name: "...", repGoal: "...", weight: "...", sets: "..."
   * , repsPerSet: "...", restTime: "..."}.
  *
  * @param exercise Exercise to serialize.
  * @param jsonGenerator class that writes JSON-file.
  * @param serializerProvider serializers needed to serialize object.
  * @throws IOException if there is either an underlying I/O problem or
  *     encoding issue at format layer
  */
  @Override
  public void serialize(
      Exercise exercise,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    jsonGenerator.writeStartObject();
    if (exercise.getId() != null) {
      jsonGenerator.writeStringField("id", exercise.getId());
    }
    if (exercise.getWorkoutId() != null) {
      jsonGenerator.writeStringField("workoutId", exercise.getWorkoutId());
    }
    if (exercise.getName() != null) {
      jsonGenerator.writeStringField("name", exercise.getName());
    }
    if (exercise.getRepGoal() != 0) {
      jsonGenerator.writeStringField("repGoal", exercise.getRepGoal() + "");
    }
    if (exercise.getWeight() != 0) {
      jsonGenerator.writeStringField("weight", exercise.getWeight() + "");
    }
    if (exercise.getSets() != 0) {
      jsonGenerator.writeStringField("sets", exercise.getSets() + "");
    }
    if (exercise.getRepsPerSet() >= 0) {
      jsonGenerator.writeStringField("repsPerSet", exercise.getRepsPerSet() + "");
    }
    if (exercise.getRestTime() != 0) {
      jsonGenerator.writeStringField("restTime", exercise.getRestTime() + "");
    }
    jsonGenerator.writeEndObject();
  }
}