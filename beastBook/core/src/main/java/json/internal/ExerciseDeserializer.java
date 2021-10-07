package json.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import core.Exercise;

import java.io.IOException;

public class ExerciseDeserializer extends JsonDeserializer<Exercise> {

    /*
     * format: { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }
     */

    @Override
    public Exercise deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    Exercise deserialize(JsonNode jsonNode) {
        if (jsonNode instanceof ObjectNode objectNode) {
            Exercise exercise = new Exercise();
            JsonNode nameNode = objectNode.get("exerciseName");
            if (nameNode instanceof TextNode) {
                exercise.setExerciseName(nameNode.asText());
            }
            JsonNode repGoalNode = objectNode.get("repGoal");
            if (repGoalNode instanceof IntNode) {
                exercise.setRepGoal(repGoalNode.asInt());
            }
            JsonNode weightNode = objectNode.get("weight");
            if (weightNode instanceof DoubleNode) {
                exercise.setWeight(weightNode.asDouble());
            }
            JsonNode setsNode = objectNode.get("sets");
            if (setsNode instanceof IntNode) {
                exercise.setSets(setsNode.asInt());
            }
            JsonNode restTimeNode = objectNode.get("restTime");
            if (restTimeNode instanceof IntNode) {
                exercise.setRestTime(setsNode.asInt());
            }
            return exercise;
        }
        return null;
    }
}
