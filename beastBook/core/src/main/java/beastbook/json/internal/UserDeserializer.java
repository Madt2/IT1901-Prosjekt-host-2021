package beastbook.json.internal;

import beastbook.core.User;
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
 * Custom JSON-Deserializer for User, converts JSON-file to User object.
 */
public class UserDeserializer extends JsonDeserializer<User> {
  
  /**
  * Deserializes User data from json file.
  * Format for User in json: { username: "...", password: "...", workouts: "[...,...]" }.
  *
  * @param parser defines how JSON-file should be parsed
  * @param deserializer defines context for deserialization
  * @return deserialized User.
  * @throws IOException for low-level read issues or decoding problems for JsonParser
  */
  @Override
  public User deserialize(
        JsonParser parser,
        DeserializationContext deserializer
  ) throws IOException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  /**
  * Converts info from jsonNode to User.
  *
  * @param jsonNode jsonNode to convert.
  * @return Deserialized user.
  */
  User deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode objectNode) {
      try {
        User user = new User();
        JsonNode usernameNode = objectNode.get("username");
        if (usernameNode instanceof TextNode) {
          user.setUsername(usernameNode.asText());
        }
        JsonNode passwordNode = objectNode.get("password");
        if (passwordNode instanceof TextNode) {
          user.setPassword(passwordNode.asText());
        }
        JsonNode workoutIDsNode = objectNode.get("workoutIDs");
        if (workoutIDsNode instanceof ArrayNode) {
          for (JsonNode elementNode : workoutIDsNode) {
            String id = elementNode.asText();
            if (id != null) {
              user.addWorkout(id);
            }
          }
        }
        return user;
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage() + "\nMost likely wrong format in file");
      }
    }
    return null;
  }
}
