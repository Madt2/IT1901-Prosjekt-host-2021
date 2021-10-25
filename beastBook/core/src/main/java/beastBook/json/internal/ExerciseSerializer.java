package beastBook.json.internal;

import beastBook.core.Exercise;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Custom JSON-Serializer for Exercise objects, converts Exercise objects to JSON-file.
 */
public class ExerciseSerializer extends JsonSerializer<Exercise> {
  /*
  * format for Exercise in json: { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }
  */

  /**
  * Serializes Exercise from input argument. Serializes Exercise object to JSON-format.
  * Format for Exercise in json:
  * { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }.
  *
  * @param exercise Exercise to serialize.
  * @param jsonGenerator class that writes JSON-file.
  * @param serializerProvider serializers needed to serialize object.
  * @throws IOException if there is either an underlying I/O problem or
  *     encoding issue at format layer
  */
  @Override
  public void serialize(Exercise exercise, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    if (exercise.getExerciseName() != null) {
      jsonGenerator.writeStringField("exerciseName", exercise.getExerciseName());
    }
    if (exercise.getRepGoal() != 0) {
      jsonGenerator.writeStringField("repGoal", exercise.getRepGoal() + "");
    }
    if (exercise.getRepGoal() != 0) {
      jsonGenerator.writeStringField("weight", exercise.getWeight() + "");
    }
    if (exercise.getRepGoal() != 0) {
      jsonGenerator.writeStringField("sets", exercise.getSets() + "");
    }
    if (exercise.getRepGoal() != 0) {
      jsonGenerator.writeStringField("restTime", exercise.getRestTime() + "");
    }
    jsonGenerator.writeEndObject();
  }
}