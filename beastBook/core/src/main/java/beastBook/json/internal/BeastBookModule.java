package beastBook.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import beastBook.core.User;
import beastBook.core.Workout;
import beastBook.core.Exercise;

public class BeastBookModule extends SimpleModule {
  /**
  * Configuration for JSON serialization of BeastBook instances.
  */

  /**
  * Initializes this BeastBookModule with appropriate serializers and deserializers.
  */
  public BeastBookModule() {
    super("BeastBookModule", Version.unknownVersion());
    addSerializer(Exercise.class, new ExerciseSerializer());
    addSerializer(Workout.class, new WorkoutSerializer());
    addSerializer(User.class, new UserSerializer());
    addDeserializer(Exercise.class, new ExerciseDeserializer());
    addDeserializer(Workout.class, new WorkoutDeserializer());
    addDeserializer(User.class, new UserDeserializer());
  }

}
