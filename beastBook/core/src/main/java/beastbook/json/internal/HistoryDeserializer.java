package beastbook.json.internal;

import beastbook.core.History;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;


/**
 * Custom JSON-Deserializer for History, converts JSON-file to History object.
 */
public class HistoryDeserializer extends JsonDeserializer<History> {

  /**
   * Deserializes History data from json file.
   * Format for History in json: { date: "...", savedWorkout: "[...,...]"}.
   *
   * @param parser defines how JSON-file should be parsed
   * @param deserializationContext defines context for deserialization
   * @return deserialized History.
   * @throws IOException for low-level read issues or decoding problems for JsonParser
   */
  @Override
  public History deserialize(
      JsonParser parser,
      DeserializationContext deserializationContext
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
   * Converts info from jsonNode to History.
   *
   * @param jsonNode jsonNode to convert.
   * @return Deserialized History or null if there is an error.
   */
  History deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      JsonNode dateNode = objectNode.get("date");
      String date = "";
      Workout savedWorkout = new Workout();
      if (dateNode instanceof TextNode) {
        date = dateNode.asText();
      }
      JsonNode savedWorkoutNode = objectNode.get("savedWorkout");
      if (savedWorkoutNode instanceof ObjectNode) {
        WorkoutDeserializer deserializer = new WorkoutDeserializer();
        savedWorkout = deserializer.deserialize(savedWorkoutNode);
      }
      if (!date.equals("") && !(savedWorkout == null)) {
        return new History(savedWorkout, date);
      }
    }
    return null;
  }
}
