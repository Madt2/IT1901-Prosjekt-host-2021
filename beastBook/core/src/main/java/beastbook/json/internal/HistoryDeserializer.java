package beastbook.json.internal;

import beastbook.core.Exceptions;
import beastbook.core.Exercise;
import beastbook.core.History;
import beastbook.core.Workout;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
  History deserialize(JsonNode jsonNode) throws IOException {
    if (jsonNode instanceof ObjectNode objectNode) {
      JsonNode idNode = objectNode.get("id");
      String id = null;
      if (idNode instanceof TextNode) {
        id = idNode.asText();
      }
      JsonNode nameNode = objectNode.get("name");
      String name = "";
      if (nameNode instanceof TextNode) {
        name = nameNode.asText();
      }
      JsonNode dateNode = objectNode.get("date");
      String date = "";
      if (dateNode instanceof TextNode) {
        date = dateNode.asText();
      }
      JsonNode savedExercisesNode = objectNode.get("savedExercises");
      List<Exercise> savedExercises = new ArrayList<>();
      if (savedExercisesNode instanceof ArrayNode) {
        ExerciseDeserializer deserializer = new ExerciseDeserializer();
        for (JsonNode node : savedExercisesNode) {
          savedExercises.add(deserializer.deserialize(node));
        }
      }
      try {
        if (!date.equals("") && !name.equals("") && id != null) {
          History history = new History(name, savedExercises, date);
          history.setId(id);
          return history;
        }
      } catch (Exceptions.IllegalIdException e) {
        throw new IOException("Something wrong with loading id in history!");
      }
    }
    return null;
  }
}