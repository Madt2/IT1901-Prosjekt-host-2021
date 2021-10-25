package beastBook.json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import beastBook.core.Exercise;
import beastBook.core.Workout;

import java.io.IOException;

public class WorkoutSerializer extends JsonSerializer<Workout> {
  /*
  * format for Workout in json: { name: "...", exercises: "[...,...]"}
  */

  /**
  * Serializes workout from input argument. Serializes workout-object to json format.
  * @param workout workout to serialize.
  * @param jsonGenerator
  * @param serializerProvider
  * @throws IOException
  */
  @Override
  public void serialize(Workout workout, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
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
