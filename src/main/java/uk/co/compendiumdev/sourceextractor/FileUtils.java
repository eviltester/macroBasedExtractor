package uk.co.compendiumdev.sourceextractor;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {

    //http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file

    public static String readFile(File file) throws IOException {

        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);       // internally use /n as line ending
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    public static void writeFile(File outputFile, String macroResult) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile));
        fileWriter.write(macroResult);
        fileWriter.flush();
        fileWriter.close();
    }
}
