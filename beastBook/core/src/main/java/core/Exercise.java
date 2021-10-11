package core;

public class Exercise {

    private String exerciseName;
    private int repGoal;
    private double weight;
    private int sets;
    private int repsPerSet;
    private int restTime;

    /**
     * Creates an Exercise using given input
     *
     * @param exerciseName Name of the exercise
     * @param repGoal Number of repetitions to be performed
     * @param weight Weight to be used for the exercise
     * @param sets Number of sets to be performed
     * @param restTime How much rest between sets
     */
    public Exercise(String exerciseName, int repGoal, double weight, int sets, int restTime) {
        setExerciseName(exerciseName);
        setRepGoal(repGoal);
        setWeight(weight);
        setSets(sets);
        setRestTime(restTime);
    }

    /**
     * Checks if name is valid, valid is not blank
     *
     * @param name Name of the exercise
     */
    private void validateExerciseName(String name) {
        if (name.length() <= 0) {
            throw new IllegalArgumentException("Exercise name can not be blank!");
        }
    }

    /**
     * Checks if repGoal is valid, valid is more than 0
     *
     * @param repGoal Number of repetitions to be performed
     */
    private void validateRepGoal(int repGoal) {
        if (repGoal <= 0) {
            throw new IllegalArgumentException("RepGoal can not be 0 or less than 0.");
        }
    }

    /**
     * Checks if weight is valid, valid is more than 0
     *
     * @param weight Weight to be used for the exercise
     */
    private void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight can not be 0 or less than 0.");
        }
    }

    /**
     * Checks if sets is valid, valid is more than 0
     *
     * @param sets Number of sets to be performed
     */
    private void validateSets(int sets) {
        if (sets <= 0) {
            throw new IllegalArgumentException("Sets can not be 0 or less than 0.");
        }
    }

    private void validateRepsPerSet(int repsPerSet) {
        if (repsPerSet <= 0) {
            throw new IllegalArgumentException("RepsPerSet can not be 0 or less than 0.");
        }
    }

    /**
     * Checks if restTime is valid, valid is more than 0
     *
     * @param restTime How much rest between sets
     */
    private void validateRestTime(int restTime) {
        if (restTime <= 0) {
            throw new IllegalArgumentException("RestTime can not be 0 or less than 0.");
        }
    }
    public void setExerciseName(String exerciseName){
        validateExerciseName(exerciseName);
        this.exerciseName = exerciseName;
    }

    public String getExerciseName(){
        return this.exerciseName;
    }

    public void setRepGoal(int repGoal){
        validateRepGoal(repGoal);
        this.repGoal = repGoal;
    }

    public int getRepGoal(){
        return this.repGoal;
    }

    public void setWeight(double weight){
        validateWeight(weight);
        this.weight = weight;
    }

    public double getWeight(){
        return this.weight;
    }

    public void setSets(int sets){
        validateSets(sets);
        this.sets = sets;
    }

    public int getSets(){
        return this.sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet) {
        validateRepsPerSet(repsPerSet);
        this.repsPerSet = repsPerSet;
    }

    public void setRestTime(int restTime) {
        validateRestTime(restTime);
        this.restTime = restTime;
    }

    public int getRestTime() {
        return restTime;
    }

    /**
     * Method to view exercise in a simple way
     *
     * @return returns a string in given format with fields separated by ","
     */
    @Override
    public String toString() {
        return exerciseName + "," + repGoal + "," + weight + "," + sets + "," + restTime;
    }
}