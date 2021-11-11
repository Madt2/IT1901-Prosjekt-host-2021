package beastbook.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History extends Workout{
    private final String date;
    private final String name;
    private final Workout savedWorkout;

    public History(Workout workout, String date) {
        this.name = workout.getName();
        this.date = date;
        this.savedWorkout = workout.copy(workout);
    }

    public String getDate() {
        return date;
    }

    public Workout getSavedWorkout() {
        return savedWorkout;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getSavedWorkout() + ", " + getDate();
    }
}
