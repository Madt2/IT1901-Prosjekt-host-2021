package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class ReadWrite {

    String userDir = System.getProperty("user.dir") + "/sampleData"; //Why is this outside the constructor? Also maybe switch to user.home

    public ReadWrite () {
        new File(userDir).mkdirs();
    }

    /**
     * Saves a workout object to a .txt file with the name of the workout as the filename
     * Separates exercises with a \n for easier viewing
     *
     * @param workoutName Name of the workout to be saved
     * @param exercises List of the exercises referenced to the workout to be saved
     * @throws FileNotFoundException If file is not found and a new file can not be created, method will throw FileNotFoundException
     */
    public void saveWorkout(String workoutName, List<Exercise> exercises) throws FileNotFoundException {
        String data = "";
        for (int i = 0; i < exercises.size(); i++) {
            String exerciseData = exercises.get(i).toString() + "\n";
            data = data + exerciseData;
        }
        PrintWriter writer = new PrintWriter(userDir + "/" + workoutName);
        writer.println(data);
        writer.flush();
        writer.close();
    }

    /**
     * Loads a workout from a file with the given filename
     * Adds separators for easier use of data in class that fetches data.
     *
     * @param filename The name of the file that is to be read
     * @return Returns a string where the name of the workout is first, seperated from exercises with ": ". The exercises are seperated by ";"
     * @throws FileNotFoundException If file is not found, method will throw FileNotFoundException
     */
    public String loadWorkout(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(userDir + "/" + filename));

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
