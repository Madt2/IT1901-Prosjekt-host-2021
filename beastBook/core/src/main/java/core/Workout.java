package core;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;


public class Workout {
    private String name;
    private List<Exercise> exercises = new ArrayList<>();
    private ReadWrite reader;


    //TODO Dårlig inkapsling her, bør fikses.
    public Workout(String name) {
        this.name = name;
    }

    public Workout() {
        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    @Override
    public String toString() {
        return getName() + ": " + getExercises();
    }
 
}

