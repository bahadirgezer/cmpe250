import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class project4main { 
    public static void main(String[] args) {
        BufferedReader input = null;
        
        try {
            input = new BufferedReader(new FileReader(new File(args[0])));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                input.close();

            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }

        //operation
        //output
    }
}