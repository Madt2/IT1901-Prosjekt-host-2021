package beastbook.json.internal;

import beastbook.core.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

/**
 * Custom JSON-Serializer for IdHandler objects, converts IdHandler objects to JSON-file.
 */
public class IdHandlerDeserializer extends JsonDeserializer<IdHandler> {
  /**
   * Deserializes User data from json file.
   * Format for User in json: { exerciseIdMap: "[(...:...),(...:...)]",
   * workoutIdMap: "[(...:...),(...:...)]",
   * historyIdMap: "[(...:...),(...:...)]" }.
   *
   * @param parser defines how JSON-file should be parsed.
   * @param deserializer defines context for deserialization.
   * @return deserialized IdHandler.
   * @throws IOException for low-level read issues or decoding problems for JsonParser.
   */
  @Override
  public IdHandler deserialize(
          JsonParser parser,
          DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Converts info from jsonNode to IdHandler.
   *
   * @param jsonNode jsonNode to convert.
   * @return Deserialized IdHandler or null deserialization fails.
   */
  IdHandler deserialize(JsonNode jsonNode) throws IOException {
    if (jsonNode instanceof ObjectNode objectNode) {
      IdHandler idHandler = new IdHandler();
      JsonNode exerciseMapNode = objectNode.get("exerciseMap");
      if (exerciseMapNode instanceof ArrayNode) {
        for (JsonNode elementNode : exerciseMapNode) {
          String id = elementNode.asText();
          if (id != null) {
            String[] strings = id.split(":");
            try {
              idHandler.addId(strings[0], strings[1], Exercise.class);
            } catch (Exceptions.IdAlreadyInUseException e) {
              throw new IOException("IdHandler not found when loading file, "
                  + "something is wrong with writing object to file");
            }
          }
        }
      }
      JsonNode workoutMapNode = objectNode.get("workoutMap");
      if (workoutMapNode instanceof ArrayNode) {
        for (JsonNode elementNode : workoutMapNode) {
          String id = elementNode.asText();
          if (id != null) {
            String[] strings = id.split(":");
            try {
              idHandler.addId(strings[0], strings[1], Workout.class);
            } catch (Exceptions.IdAlreadyInUseException e) {
              throw new IOException("IdHandler not found when loading file, "
                  + "something is wrong with writing object to file");
            }
          }
        }
      }
      JsonNode historyMapNode = objectNode.get("historyMap");
      if (historyMapNode instanceof ArrayNode) {
        for (JsonNode elementNode : historyMapNode) {
          String id = elementNode.asText();
          if (id != null) {
            String[] strings = id.split(":");
            try {
              idHandler.addId(strings[0], strings[1], History.class);
            } catch (Exceptions.IdAlreadyInUseException e) {
              throw new IOException("IdHandler not found when loading file, "
                  + "something is wrong with writing object to file");
            }
          }
        }
      }
      return idHandler;
    }
    throw new IOException("Something went wrong with loading IdHandler!");
  }
}
