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

public class HistoryDeserializer extends JsonDeserializer<History> {

    @Override
    public History deserialize(
            JsonParser parser,
            DeserializationContext deserializationContext
    ) throws IOException, JsonProcessingException {
        TreeNode treeNode = parser.getCodec().readTree(parser);
        return deserialize((JsonNode) treeNode);
    }

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
                History history = new History(savedWorkout, date);
                return history;
            }
        }
        return null;
    }
}
