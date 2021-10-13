package json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Exercise;

import java.io.IOException;

public class ExerciseSerializer extends JsonSerializer<Exercise>{
    /*
    * format for Exercise in json: { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }
    */

    /**
     * Serializes exercise from input argument. Serializes exercise-object to json format.
     * @param exercise exercise to serialize.
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(Exercise exercise, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        if (exercise.getExerciseName() != null) {
            jsonGenerator.writeStringField("exerciseName", exercise.getExerciseName());
        }
        if (exercise.getRepGoal() != 0) {
            jsonGenerator.writeStringField("repGoal", exercise.getRepGoal()+"");
        }
        if (exercise.getRepGoal() != 0) {
            jsonGenerator.writeStringField("weight", exercise.getWeight()+"");
        }
        if (exercise.getRepGoal() != 0) {
            jsonGenerator.writeStringField("sets", exercise.getSets()+"");
        }
        if (exercise.getRepGoal() != 0) {
            jsonGenerator.writeStringField("restTime", exercise.getRestTime()+"");
        }
        jsonGenerator.writeEndObject();
    }

}