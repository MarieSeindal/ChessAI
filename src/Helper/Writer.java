package Helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Writer {

    private FileWriter fw;
    private BufferedWriter output;
    private String fileName = "./src/Data/Moves.txt";
    private Path filePath = Paths.get(fileName);

    public void initialize() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(Paths.get(fileName));
            }
            fw = new FileWriter(fileName,false);
            output = new BufferedWriter(fw);
            output.write("************* Game Begin *************\n");
            output.close();
        } catch (IOException IOE) {
            System.out.println("FileWriter Error in initialize(): " + IOE);
        }
    }

    public void write(String s) {
        try {
            fw = new FileWriter(fileName,true);
            output = new BufferedWriter(fw);
            output.write(s+"\n");
            output.close();
        } catch (IOException IOE) {
            System.out.println("FileWriter Error in write(): " + IOE);
        }
    }
}
