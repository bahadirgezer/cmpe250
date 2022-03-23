import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.PrintStream;
import java.io.FileNotFoundException;



public class project2main { //javac Project1/src/*.java -d Project1/bin -target 17
    public static void main(String[] args) throws FileNotFoundException {      
        ExcelFedSystem system = new ExcelFedSystem();
        Statistics stats = new Statistics();
        BufferedReader input = null;

        
        try {
            input = new BufferedReader(new FileReader(new File(args[0])));
            String currLine;
            Pattern playerP = Pattern.compile("^(\\d+) (\\d+)$"); //regex pattern for house and student
            Pattern trainingP = Pattern.compile("^t (\\d+) (\\d+\\.\\d+|\\d+) (\\d+\\.\\d+|\\d+)$");
            Pattern massageP = Pattern.compile("^m (\\d+) (\\d+\\.\\d+|\\d+) (\\d+\\.\\d+|\\d+)$");
            Pattern therapistP = Pattern.compile("^(\\d+) (\\d+)$");
            Matcher playerM, trainingM, massageM, therapistM;
            
            currLine = input.readLine();
            int numPlayers = Integer.parseInt(currLine);

            for (int i=0; i<numPlayers; i++) {
                currLine = input.readLine();
                playerM = playerP.matcher(currLine);

                if (playerM.matches()) {
                    system.addPlayer(Integer.parseInt(playerM.group(1)), 
                    Integer.parseInt(playerM.group(2)));
                }
            }

            currLine = input.readLine();
            int numEvents = Integer.parseInt(currLine);
            system.initialize(numEvents);

            for (int i=0; i<numEvents; i++) {
                currLine = input.readLine();
                trainingM =  trainingP.matcher(currLine);
                massageM = massageP.matcher(currLine);

                if (trainingM.matches()) {
                    system.addEvent(1, Integer.parseInt(trainingM.group(1)),
                    Double.parseDouble(trainingM.group(2)), 
                    Double.parseDouble(trainingM.group(3)));

                } else if (massageM.matches()) {
                    system.addEvent(4, Integer.parseInt(massageM.group(1)),
                    Double.parseDouble(massageM.group(2)), 
                    Double.parseDouble(massageM.group(3)));
                    
                } else {
                    System.out.println("PROBLEM: Event not matched!");
                }
            }
            
            currLine = input.readLine();            
            therapistM = therapistP.matcher(currLine);
            if (therapistM.matches()) {
                system.initialize(Integer.parseInt(therapistM.group(1)), 
                therapistM.group(2), numEvents);
    
            }
            
            currLine = input.readLine();
            playerM = playerP.matcher(currLine);
            if (playerM.matches()) {
                system.initialize(Integer.parseInt(playerM.group(1)), 
                Integer.parseInt(playerM.group(2)), 
                numEvents);
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


        system.initialize(stats);
        while (system.loop()) {
            system.consoleDebuggerBefore();
            system.start();
            system.consoleDebuggerAfter();

        }
    
    
        PrintStream output = new PrintStream(new File(args[1])); 
        output.print(stats.output()); //creates output 
        output.close();
    
    
        
    }
}