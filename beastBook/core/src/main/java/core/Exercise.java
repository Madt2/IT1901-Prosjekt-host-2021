package core;

public class Exercise {

    private String exerciseName;
    private int repGoal;
    private double weight;
    private int sets; 
    private int restTime;

    /**
     *
     * @param exerciseName
     * @param repGoal
     * @param weight
     * @param sets
     * @param restTime
     */
    public Exercise(String exerciseName, int repGoal, double weight, int sets, int restTime){
        this.exerciseName = exerciseName;
        validateRepGoal(repGoal);
        this.repGoal = repGoal;
        validateWeight(weight);
        this.weight = weight;
        validateSets(sets);
        this.sets = sets;
        validateRestTime(restTime);
        this.restTime = restTime;
    }

    /**
     *
     * @param name
     */
    private void validateExerciseName(String name) {
        if (name.length() <= 0) {
            throw new IllegalArgumentException("Exercise name can not be blank!");
        }
    }

    /**
     *
     * @param repGoal
     */
    private void validateRepGoal(int repGoal) {
        if (repGoal <= 0) {
            throw new IllegalArgumentException("RepGoal can not be 0 or less than 0.");
        }
    }

    /**
     *
     * @param weight
     */
    private void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight can not be 0 or less than 0.");
        }
    }

    /**
     *
     * @param sets
     */
    private void validateSets(int sets) {
        if (sets <= 0) {
            throw new IllegalArgumentException("Sets can not be 0 or less than 0.");
        }
    }

    /**
     *
     * @param restTime
     */
    private void validateRestTime(int restTime) {
        if (restTime <= 0) {
            throw new IllegalArgumentException("RestTime can not be 0 or less than 0.");
        }
    }

    /**
     *
     * @param exerciseName
     */
    public void setExerciseName(String exerciseName){
        validateExerciseName(exerciseName);
        this.exerciseName = exerciseName;
    }

    /**
     *
     * @return
     */
    public String getExerciseName(){
        return this.exerciseName;
    }

    /**
     *
     * @param repGoal
     */
    public void setRepGoal(int repGoal){
        validateRepGoal(repGoal);
        this.repGoal = repGoal;
    }

    /**
     *
     * @return
     */
    public int getRepGoal(){
        return this.repGoal;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(double weight){
        validateWeight(weight);
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public double getWeight(){
        return this.weight;
    }

    /**
     *
     * @param sets
     */
    public void setSets(int sets){
        validateSets(sets);
        this.sets = sets;
    }

    /**
     *
     * @return
     */
    public int getSets(){
        return this.sets;
    }

    /**
     *
     * @param restTime
     */
    public void setRestTime(int restTime) {
        validateRestTime(restTime);
        this.restTime = restTime;
    }

    /**
     *
     * @return
     */
    public int getRestTime() {
        return restTime;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return exerciseName + "," + repGoal + "," + weight + "," + sets + "," + restTime;
    }
}