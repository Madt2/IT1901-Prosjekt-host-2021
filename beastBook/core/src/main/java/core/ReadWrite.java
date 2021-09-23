package core;

public class ReadWrite {

    public void saveWorkout(String workoutName, List<Exercise> exercises) throws FileNotFoundException { //Saves a workout object to a .txt file with the name of the workout as the filename
        String data = "";
        for (int i = 0; i < exercises.size(); i++) {
            String exerciseData = exercises.get(i).toString() + "\n";
            data = data + exerciseData;
        }
        System.out.println(data);
        PrintWriter writer = new PrintWriter(workoutName);
        writer.println(data);
        writer.flush();
        writer.close();
    }

    public String loadWorkout(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        String data = filename + ": ";
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (scanner.hasNextLine()) {
                data = data + line + ";";
            } else {
                data = data + line;
            }
        }
        scanner.close();
        return data;
    }

}
