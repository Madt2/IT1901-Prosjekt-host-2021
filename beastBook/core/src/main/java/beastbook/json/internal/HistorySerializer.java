package beastbook.json.internal;

import beastbook.core.History;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class HistorySerializer extends JsonSerializer<History> {
    @Override
    public void serialize(
            History history,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        if (history.getDate() != null) {
            jsonGenerator.writeStringField("date", history.getDate());
        }
        if (history.getSavedWorkout() != null) {
            jsonGenerator.writeObjectField("savedWorkout", history.getSavedWorkout());
        }
        jsonGenerator.writeEndObject();
    }
}

