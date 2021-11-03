package beastbook.json.internal;

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
  * @param deserializer defines context for deserialization
  * @return Deserialized exercise.
  * @throws IOException for low-level read issues or decoding problems for JsonParser
  */
  @Override
  public Exercise deserialize(
      JsonParser parser,
      DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }
  
  /**
   * Converts info from jsonNode to Exercise.
   *
   * @param jsonNode jsonNode to convert.
   * @return deserialized exercise.
   */
  Exercise deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      Exercise exercise = new Exercise();
      JsonNode nameNode = objectNode.get("exerciseName");
      if (nameNode instanceof TextNode) {
        exercise.setExerciseName(nameNode.asText());
      }
      JsonNode repGoalNode = objectNode.get("repGoal");
      if (repGoalNode instanceof TextNode) {
        exercise.setRepGoal(repGoalNode.asInt());
      }
      JsonNode weightNode = objectNode.get("weight");
      if (weightNode instanceof TextNode) {

        exercise.setWeight(weightNode.asDouble());
      }
      JsonNode setsNode = objectNode.get("sets");
      if (setsNode instanceof TextNode) {
        exercise.setSets(setsNode.asInt());
      }
      JsonNode restTimeNode = objectNode.get("restTime");
      if (restTimeNode instanceof TextNode) {
        exercise.setRestTime(restTimeNode.asInt());
      }
      return exercise;
    }
    return null;
  }
}
