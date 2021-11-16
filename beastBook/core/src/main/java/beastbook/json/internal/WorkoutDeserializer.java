package beastbook.json.internal;

import beastbook.core.Workout;
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
  
  /**
  * Deserializes Workout data from json file.
  * Format for Workout in json: { name: "...", exercises: "[...,...]"}.
  *
  * @param parser defines how JSON-file should be parsed
  * @param deserializationContext defines context for deserialization
  * @return deserialized Workout.
  * @throws IOException for low-level read issues or decoding problems for JsonParser.
  */
  @Override
  public Workout deserialize(
        JsonParser parser,
        DeserializationContext deserializationContext
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
  * Converts info from jsonNode to Workout.
  *
  * @param jsonNode jsonNode to convert.
  * @return Deserialized workout or null deserialization fails.
  */
  Workout deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      try {
        Workout workout = new Workout();
        JsonNode nameNode = objectNode.get("name");
        if (nameNode instanceof TextNode) {
          workout.setName(nameNode.asText());
        }
        JsonNode idNode = objectNode.get("id");
        if (idNode instanceof TextNode) {
          workout.setID(idNode.asText());
        }
        JsonNode exerciseIDsNode = objectNode.get("exerciseIDs");
        if (exerciseIDsNode instanceof ArrayNode) {
          for (JsonNode elementNode : exerciseIDsNode) {
            String id = elementNode.asText();
            if (id != null) {
              workout.addExercise(id);
            }
          }
        }
        return workout;
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\nMost likely wrong format in file");
      }
    }
    return null;
  }
}
