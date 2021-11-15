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

public class IdDeserializer extends JsonDeserializer<Id> {
  @Override
  public Id deserialize(
          JsonParser parser,
          DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  Id deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      Id id = new Id();
      JsonNode workoutIDsNode = objectNode.get("workoutIDs");
      if (workoutIDsNode instanceof ArrayNode) {
        for (JsonNode elementNode : workoutIDsNode) {
          String ID = elementNode.asText();
          if (ID != null) {
            id.addWorkoutID(ID);
          }
        }
      }
      JsonNode exerciseIDsNode = objectNode.get("exerciseIDs");
      if (exerciseIDsNode instanceof ArrayNode) {
        for (JsonNode elementNode : exerciseIDsNode) {
          String ID = elementNode.asText();
          if (ID != null) {
            id.addExerciseID(ID);
          }
        }
      }
      return id;
    }
    return null;
  }
}
