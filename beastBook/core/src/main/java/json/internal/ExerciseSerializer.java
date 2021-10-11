package json.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.Exercise;
import core.Workout;

import java.io.IOException;

public class ExerciseSerializer extends JsonSerializer<Exercise>{
    /*
    * format: { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }
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