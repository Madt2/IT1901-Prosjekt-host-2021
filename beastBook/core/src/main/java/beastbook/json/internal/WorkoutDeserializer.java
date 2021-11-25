package beastbook.json.internal;

import beastbook.core.Exceptions;
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
  * Format for Workout in json: { id: "...", name: "...", exerciseIds: "[...,...]"}.
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
  Workout deserialize(JsonNode jsonNode) throws IOException {
    if (jsonNode instanceof ObjectNode objectNode) {
      Workout workout = new Workout();
      JsonNode nameNode = objectNode.get("name");
      if (nameNode instanceof TextNode) {
        workout.setName(nameNode.asText());
      }
      JsonNode idNode = objectNode.get("id");
      if (idNode instanceof TextNode) {
        try {
          workout.setId(idNode.asText());
        } catch (Exceptions.IllegalIdException e) {
          throw new IOException("IdHandler not found when loading file, "
              + "something is wrong with writing object to file");
        }
      }
      JsonNode exerciseIdsNode = objectNode.get("exerciseIds");
      if (exerciseIdsNode instanceof ArrayNode) {
        for (JsonNode elementNode : exerciseIdsNode) {
          String id = elementNode.asText();
          if (id != null) {
            try {
              workout.addExercise(id);
            } catch (Exceptions.IllegalIdException | Exceptions.ExerciseAlreadyExistsException e) {
              throw new IOException("IdHandler not found when loading file, "
                  + "something is wrong with writing object to file");
            }
          }
        }
      }
      return workout;
    }
    return null;
  }
}
