import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class project1main { //javac Project1/src/*.java -d Project1/bin -target 17
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader input = null;
        DormSystem system = new DormSystem(); //DormSystem variable which will process everything, also store every house and student object
        try {
            input = new BufferedReader(new FileReader(new File(args[0])));
            Pattern houseP = Pattern.compile("^h (\\d+) (\\d+) (\\d+\\.\\d+|\\d+)$"); //cryptic regex pattern for house and student
            Pattern studentP = Pattern.compile("^s (\\d+) (.+) (\\d+) (\\d+\\.\\d+|\\d+)$");
            Matcher houseM, studentM;
            String currline;

            while ((currline = input.readLine()) != null){ //input taking while loop
                houseM = houseP.matcher(currline); 
                studentM = studentP.matcher(currline);
               
                if (houseM.matches()) {
                    system.addHouse(Integer.parseInt(houseM.group(1)), //creates house object
                    Integer.parseInt(houseM.group(2)), 
                    Double.parseDouble(houseM.group(3)));

                } else if (studentM.matches()){
                    system.addStudent(Integer.parseInt(studentM.group(1)), //creates student object
                    studentM.group(2), 
                    Integer.parseInt(studentM.group(3)), 
                    Double.parseDouble(studentM.group(4)));
                
                } else {
                    System.out.println("oh no");
                }
            }
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

        while (system.resume()) { // main while loop, actually runs the program   
            system.allocate(); //allocates every available house to every available student
            system.clear(); //increments semester, then clears houses and students if they're done
        }

        PrintStream output = new PrintStream(new File(args[1])); 
        output.print(system.output()); //creates output 
        output.close();
    }
}