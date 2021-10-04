package core;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;


public class Workout {
    private String name; //Can be final? If new name is used file won't be found. 
    private List<Exercise> exercises = new ArrayList<>();
    private ReadWrite readWrite;



    //Are these javadocs needed??
    
    /**
     * Contructor for workout with name parameter.
     * @param name name of the workout.
     */
    public Workout(String name) {
        setName(name);
    }

    /**
     * Contructor for workout with no set name.
     */
    public Workout() {}

    /**
     * Method for setting name of workout.
     * @param name name of workout.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Workout name getter.
     * @return workout name.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an exorcise to workout.
     * @param exercise exercise object to add to workout.
     */
    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    /**
     * Getter for exercises.
     * @return returns the list of exercises in workout.
     */
    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    /**
     * Method for saving workout to file, name of file is the same as name of workout. Delegates to readWrite.
     * @throws FileNotFoundException If file is not found and a new file can not be created, method will throw FileNotFoundException.
     */
    public void saveWorkout() throws FileNotFoundException {
        if (readWrite == null) {
            readWrite = new ReadWrite();
        }
        readWrite.saveWorkout(getName(), getExercises());
    }

    /**
     * Method for loading workout from file. Receives data from readWrite, splits name from exercises-data, sets name of workout, split exercises-data to list of exercise data, initialize each exercise from exercise datalist.
     * @param filename name of workout to load (name of file is the same as name of workout).
     * @throws FileNotFoundException If file is not found, method will throw FileNotFoundException.
     */
    public void loadWorkout(String filename) throws FileNotFoundException {
        if (readWrite == null) {
            readWrite = new ReadWrite();
        }
        String data = readWrite.loadWorkout(filename);
        String[] dataLine = data.split(": ");
        setName(dataLine[0]);
        if (dataLine.length > 1) {
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
    }

    /**
     * toString for workout. Returns object in more readable format.
     * @return (name of workout): [list of exercises]
     */
    @Override
    public String toString() {
        return getName() + ": " + getExercises();
    }

}

