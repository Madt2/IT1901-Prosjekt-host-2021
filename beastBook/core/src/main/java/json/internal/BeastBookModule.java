package json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import core.User;
import core.Workout;
import core.Exercise;

public class BeastBookModule extends SimpleModule{
    /**
     * Configuration for JSON serialization of BeastBook instances.
     */
    @SuppressWarnings("serial")
    private static final String NAME = "BeastBookModule";

    /**
     * Initializes this BeastBookModule with appropriate serializers and deserializers.
     * @param deepUserSerializer
     */
    public BeastBookModule(boolean deepUserSerializer) {
        super(NAME, Version.unknownVersion());
        addSerializer(Exercise.class, new ExerciseSerializer());
            addSerializer(Workout.class, new WorkoutSerializer());
            addSerializer(User.class, new UserSerializer(deepUserSerializer));
            addDeserializer(Exercise.class, new ExerciseDeserializer());
            addDeserializer(Workout.class, new WorkoutDeserializer());
            addDeserializer(User.class, new UserDeserializer());
        }

    public BeastBookModule() {
        this(true);
    }
 }
