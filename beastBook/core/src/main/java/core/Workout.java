package core;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;


public class Workout {
    private String name;
    private List<Exercise> exercises = new ArrayList<>();
    private ReadWrite reader;


    /**
     *
     * @param name
     */
    public Workout(String name) {
        setName(name);
    }

    /**
     *
     */
    public Workout() {}

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param exercise
     */
    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    /**
     *
     * @return
     */
    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public void saveWorkout() throws FileNotFoundException {
        if (reader == null) {
            reader = new ReadWrite();
        }
        reader.saveWorkout(getName(), getExercises());
    }

    /**
     *
     * @param filename
     * @throws FileNotFoundException
     */
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


    @Override
    public String toString() {
        return getName() + ": " + getExercises();
    }

}

