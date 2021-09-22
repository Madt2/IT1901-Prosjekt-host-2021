package core;

public class Exercise {

    private String exerciseName;
    private int repGoal;
    private double weight;
    private int sets; 
    private int restTime;

    public Exercise(String exerciseName, int repGoal, double weight, int sets, int restTime){
        this.exerciseName = exerciseName;
        this.repGoal = repGoal;
        this.weight = weight;
        this.sets = sets;
        this.restTime = restTime;
    }

    public void setExerciseName(String exerciseName){
        this.exerciseName = exerciseName;
    }

    public String getExerciseName(){
        return this.exerciseName;
    }

    public void setRepGoal(int repGoal){
        this.repGoal = repGoal;
    }

    public int getRepGoal(){
        return this.repGoal;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getWeight(){
        return this.weight;
    }

    public void setSets(int sets){
        this.sets = sets;
    }

    public int getSets(){
        return this.sets;
    }

    public void setRestTime(int restTime) {
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