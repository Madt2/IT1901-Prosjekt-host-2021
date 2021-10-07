package json.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.User;
import core.Workout;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<User> {

    /*
     * format: { username: "...", password: "...", workouts: "[...,...]" }
     */

    @Override
    public User deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

    User deserialize(JsonNode jsonNode) {
        if (jsonNode instanceof ObjectNode objectNode) {
            User user = new User();
            JsonNode usernameNode = objectNode.get("username");
            if (usernameNode instanceof TextNode) {
                user.setUserName(usernameNode.asText());
            }
            JsonNode passwordNode = objectNode.get("password");
            if (passwordNode instanceof TextNode) {
                user.setPassword(passwordNode.asText());
            }
            JsonNode workoutsNode = objectNode.get("workouts");
            if (workoutsNode instanceof ArrayNode) {
                for (JsonNode elementNode : ((ArrayNode) workoutsNode)) {
                    Workout workout = WorkoutDeserializer.deserialize(elementNode);
                    if (workout != null) {
                        user.addWorkout(workout);
                    }
                }
            }
            return user;
        }
        return null;
    }


}
