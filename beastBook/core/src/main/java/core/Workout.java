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

    public void saveWorkout() throws FileNotFoundException {
        if (reader == null) {
            reader = new ReadWrite();
        }
        reader.saveWorkout(getName(), getExercises());
    }

    public void loadWorkout(String filename) throws FileNotFoundException {
        if (reader == null) {
            reader = new ReadWrite();
        }
        String data = reader.loadWorkout(filename);
        String[] dataLine = data.split(": ");
        setName(dataLine[0]);
        String[] exerciseData = dataLine[1].split(";");
        for (int i = 0; i<exerciseData.length; i++) {
            String[] temp = exerciseData[i].split(",");
            String name = temp[0];
            int repGoal = Integer.parseInt(temp[1]);
            double weight = Double.parseDouble(temp[2]);
            int sets = Integer.parseInt(temp[3]);
            int restTime = Integer.parseInt(temp[4]);

            Exercise exercise = new Exercise(name, repGoal, weight, sets, restTime);
            addExercise(exercise);
        }
    }

    public static void main(String[] args) {
        Workout workout = new Workout("Test");
  /*      String[] exerciseName = {"Bench Press", "Chest fly", "Bicep Curl"};

            for (int i = 0; i<exerciseName.length; i++) {
            Exercise exercise = new Exercise(exerciseName[i], 30, 50.0, 3, 90);
            workout.addExercise(exercise);
        }*/


   /*     try {
            workout.saveWorkout();
        } catch (Exception e) {
            System.err.println(e);
        }*/
        try {
            workout.loadWorkout("Test");
        } catch (Exception e) {
            System.err.println(e);
        }
        System.out.println(workout.toString());

    }
 
}

