package json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Exercise;
import core.User;
import core.Workout;

import java.io.IOException;

public class WorkoutSerializer extends JsonSerializer<Workout> {
    /*
     * format: { name: "...", exercises: "[...,...]"}
     */


    @Override
    public void serialize(Workout workout, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (workout.getName() != null) {
            jsonGenerator.writeStringField("username", workout.getName());
        }
        if (workout instanceof Workout) {
            jsonGenerator.writeArrayFieldStart("exercises");
            for (Exercise item : workout.getExercises()) {
                jsonGenerator.writeObject(item);
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }
}
