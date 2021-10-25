package beastBook.json.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import beastBook.core.Exercise;
import beastBook.core.Workout;

import java.io.IOException;

public class WorkoutDeserializer extends JsonDeserializer<Workout> {
  /*
  * format for Workout in json: { name: "...", exercises: "[...,...]"}
  */

  private ExerciseDeserializer d = new ExerciseDeserializer();
  
  /**
  * Deserializes Workout data from json file.
  *
  * @param parser
  * @param deserializer
  * @return Deserialized workout.
  * @throws IOException
  * @throws JsonProcessingException
  */
  @Override
  public Workout deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
  * Converts info from jsonNode to Workout.
  *
  * @param jsonNode
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
          Exercise exercise = d.deserialize(elementNode);
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
