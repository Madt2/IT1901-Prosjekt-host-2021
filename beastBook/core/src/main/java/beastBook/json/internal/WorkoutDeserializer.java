package beastBook.json.internal;

import beastBook.core.Exercise;
import beastBook.core.Workout;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/**
 * Custom JSON-Deserializer for Workout, converts JSON-file to Workout object.
 */
public class WorkoutDeserializer extends JsonDeserializer<Workout> {

  private ExerciseDeserializer deserializer = new ExerciseDeserializer();
  
  /**
  * Deserializes Workout data from json file.
  * Format for Workout in json: { name: "...", exercises: "[...,...]"}.
  *
  * @param parser defines how JSON-file should be parsed
  * @param deserializer defines context for deserialization
  * @return deserialized Workout.
  * @throws IOException for low-level read issues or decoding problems for JsonParser
  */
  @Override
  public Workout deserialize(
        JsonParser parser,
        DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
  * Converts info from jsonNode to Workout.
  *
  * @param jsonNode jsonNode to convert.
  * @return Deserialized workout.
  */
  Workout deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      Workout workout = new Workout();
      JsonNode nameNode = objectNode.get("name");
      if (nameNode instanceof TextNode) {
        workout.setName(nameNode.asText());
      }
      JsonNode exercisesNode = objectNode.get("exercises");
      if (exercisesNode instanceof ArrayNode) {
        for (JsonNode elementNode : exercisesNode) {
          Exercise exercise = deserializer.deserialize(elementNode);
          if (exercise != null) {
            workout.addExercise(exercise);
          }
        }
      }
      return workout;
    }
    return null;
  }
}
