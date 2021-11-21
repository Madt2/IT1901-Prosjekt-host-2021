package beastbook.json.internal;

import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/**
 * Custom JSON-Deserializer for Exercise, converts JSON-file to Exercise object.
 */
public class ExerciseDeserializer extends JsonDeserializer<Exercise> {

  /**
  * Deserializes Exercise data from JSON-file.
  * Format for Exercise in json:
  * { exerciseName: "...", repGoal: "...", weight: "...", sets: "...", restTime: "..." }.
  *
  * @param parser defines how JSON-file should be parsed
  * @param deserializationContext defines context for deserialization
  * @return Deserialized exercise.
  * @throws IOException for low-level read issues or decoding problems for JsonParser.
  */
  @Override
  public Exercise deserialize(
      JsonParser parser,
      DeserializationContext deserializationContext
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }
  
  /**
   * Converts info from jsonNode to Exercise.
   *
   * @param jsonNode jsonNode to convert.
   * @return deserialized exercise, or null deserialization fails
   */
  Exercise deserialize(JsonNode jsonNode) throws IOException {
    if (jsonNode instanceof ObjectNode objectNode) {
      Exercise exercise;
        JsonNode idNode = objectNode.get("id");
        String id = null;
        if (idNode instanceof TextNode) {
          id = idNode.asText();
        }
        JsonNode workoutIdNode = objectNode.get("workoutID");
        String workoutId = null;
        if (workoutIdNode instanceof TextNode) {
          workoutId = workoutIdNode.asText();
        }
        JsonNode nameNode = objectNode.get("name");
        String name = null;
        if (nameNode instanceof TextNode) {
          name = nameNode.asText();
        }
        JsonNode repGoalNode = objectNode.get("repGoal");
        int repGoal = 0;
        if (repGoalNode instanceof TextNode) {
          repGoal = repGoalNode.asInt();
        }
        JsonNode weightNode = objectNode.get("weight");
        double weight = 0;
        if (weightNode instanceof TextNode) {
          weight = weightNode.asDouble();
        }
        JsonNode setsNode = objectNode.get("sets");
        int sets = 0;
        if (setsNode instanceof TextNode) {
          sets  = setsNode.asInt();
        }
        JsonNode repsPerSetNode = objectNode.get("repsPerSet");
        int repsPerSet = 0;
        if (repsPerSetNode instanceof TextNode) {
          repsPerSet = repsPerSetNode.asInt();
        }
        JsonNode restTimeNode = objectNode.get("restTime");
        int restTime = 0;
        if (restTimeNode instanceof TextNode) {
          restTime = restTimeNode.asInt();
        }
        if (id != null && workoutId != null) {
          try {
            exercise = new Exercise(name, repGoal, weight, sets, repsPerSet, restTime);
            exercise.setId(id);
            exercise.setWorkoutID(workoutId);

            return exercise;
          } catch (Exceptions.IllegalIdException e) {
            throw new IOException("Id not found when loading file, "
              + "something is wrong with writing object to file");
          }
        }
    }
    throw new IOException("something when wrong when loading exercise! ");
  }
}
