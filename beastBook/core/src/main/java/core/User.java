package core;

import java.util.ArrayList;
import java.util.List;

public class User {

    private static final int MIN_CHAR_USERNAME = 3;
    private static final int MIN_CHAR_PASSWORD = 3;

    private final String username;
    private final String password;
    private List<Workout> workouts = new ArrayList<Workout>();
    //private List<String> myHistory = new ArrayList<String>(); for later release

    public User(String username, String password) {
        if (username.length() < MIN_CHAR_USERNAME)
            throw new IllegalArgumentException("Too short username.");
        if (username.length() < MIN_CHAR_PASSWORD)
            throw new IllegalArgumentException("Too short password.");
        this.username = username;
        this.password = password;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public void removeWorkout(Workout workout) {
        if (!workouts.contains(workout))
            throw new IllegalArgumentException("User does not have workout " + workout + " saved." );
        workouts.remove(workout);
    }

    public Workout getWorkout(String workoutName) {
        for (Workout w : workouts) {
            if (w.getName().equals(workoutName))
                return w;
        }
        return null;
    }

    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }
}
