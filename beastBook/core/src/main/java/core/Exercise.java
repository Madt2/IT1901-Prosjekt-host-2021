package core;

public class Exercise {

    private String exerciseName;
    private int repGoal;
    private double weight;
    private int sets; 
    private int restTime;

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

    private void validateRepGoal(int repGoal) {
        if (repGoal <= 0) {
            throw new IllegalArgumentException("RepGoal cant be 0 or less than 0.");
        }
    }

    private void validateWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight cant be 0 or less than 0.");
        }
    }

    private void validateSets(int sets) {
        if (sets <= 0) {
            throw new IllegalArgumentException("Sets cant be 0 or less than 0.");
        }
    }

    private void validateRestTime(int restTime) {
        if (restTime <= 0) {
            throw new IllegalArgumentException("RestTime cant be 0 or less than 0.");
        }
    }

    public void setExerciseName(String exerciseName){
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

    public void setRestTime(int restTime) {
        validateRestTime(restTime);
        this.restTime = restTime;
    }

    public int getRestTime() {
        return restTime;
    }
 
    @Override
    public String toString() {
        return exerciseName + " " + repGoal + " " + weight + " " + sets + " " + restTime;
    }
}