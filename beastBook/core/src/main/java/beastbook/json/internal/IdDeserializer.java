package beastbook.json.internal;

import beastbook.core.Id;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class IdDeserializer extends JsonDeserializer<Id> {
  /**
   * Deserializes User data from json file.
   * Format for User in json: { exerciseIDs: "[...,...]", workoutIDs: "[...,...]",
   *            exerciseNameIDMap: "[...,...]", exerciseNameIDMap: "[...,...]" }.
   *
   * @param parser defines how JSON-file should be parsed.
   * @param deserializer defines context for deserialization.
   * @return deserialized Id.
   * @throws IOException for low-level read issues or decoding problems for JsonParser.
   */
  @Override
  public Id deserialize(
          JsonParser parser,
          DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Converts info from jsonNode to Id.
   *
   * @param jsonNode jsonNode to convert.
   * @return Deserialized Id or null deserialization fails.
   */
  Id deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      try {
        Id id = new Id();
        JsonNode exerciseIDsNode = objectNode.get("exerciseIDs");
        if (exerciseIDsNode instanceof ArrayNode) {
          for (JsonNode elementNode : exerciseIDsNode) {
            String ID = elementNode.asText();
            if (ID != null) {
              id.addExerciseID(ID);
            }
          }
        }
        JsonNode workoutIDsNode = objectNode.get("workoutIDs");
        if (workoutIDsNode instanceof ArrayNode) {
          for (JsonNode elementNode : workoutIDsNode) {
            String ID = elementNode.asText();
            if (ID != null) {
              id.addWorkoutID(ID);
            }
          }
        }
        JsonNode exerciseIDMapNode = objectNode.get("exerciseNameIDMap");
        if (exerciseIDMapNode instanceof ArrayNode) {
          for (JsonNode elementNode : exerciseIDMapNode) {
            String ID = elementNode.asText();
            if (ID != null) {
              String[] strings = ID.split(":");
              id.addExerciseIDEntry(strings[0], strings[1]);
            }
          }
        }
        JsonNode workoutIDMapNode = objectNode.get("workoutNameIDMap");
        if (workoutIDsNode instanceof ArrayNode) {
          for (JsonNode elementNode : workoutIDMapNode) {
            String ID = elementNode.asText();
            if (ID != null) {
              String[] strings = ID.split(":");
              id.addWorkoutIDEntry(strings[0], strings[1]);
            }
          }
        }
        return id;
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\nMost likely wrong format in file");
      }
    }
    return null;
  }
}
