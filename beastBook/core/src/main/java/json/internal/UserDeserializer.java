package json.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import core.User;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserDeserializer extends JsonDeserializer<User> {

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
            JsonNode workoutListNode = objectNode.get("workouts");
            if (workoutListNode instanceof ArrayNode) {

            }
            return user;
        }
        return null;
    }


}
