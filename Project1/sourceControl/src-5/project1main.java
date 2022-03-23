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
        long startTime = System.currentTimeMillis();
        
        BufferedReader input = null;
        DormSystem system = new DormSystem(); //DormSystem variable which will process everything, also store every house and student object
        try {
            input = new BufferedReader(new FileReader(new File(args[0])), 40000000);
            Pattern houseP = Pattern.compile("^h (\\d+) (\\d+) (\\d+\\.\\d+|\\d+)$"); //regex pattern for house and student
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
                    System.out.println("unmatched line: " + currline); //program shouldn't enter here
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

        long firstWhile = System.currentTimeMillis();

        while (system.resume()) { // main while loop, actually runs the program 
            long systemTimeStart = System.currentTimeMillis();  
            
            system.allocate(); //allocates every available house to every available student
            long  allocateTimeEnd = System.currentTimeMillis();
            
            system.clear(); //increments semester, then clears houses and students if they're done
            long clearTimeEnd = System.currentTimeMillis();

            
            System.out.println("allocate(): "+ (allocateTimeEnd - systemTimeStart)+ " ms");
            System.out.println("clear(): "+ (clearTimeEnd- allocateTimeEnd)+ " ms");

        }

        long secondWhile = System.currentTimeMillis();

        PrintStream output = new PrintStream(new File(args[1])); 
        output.print(system.output()); //creates output 
        output.close();

        long endTime = System.currentTimeMillis();
        System.out.println((endTime-startTime)+ " ms");
        System.out.println("Input taking: "+ (firstWhile - startTime)+ " ms");
        System.out.println("Allocating: " + (secondWhile - firstWhile) + " ms");
        System.out.println("Output generating: "+ (endTime - secondWhile) + " ms");
    }
}
