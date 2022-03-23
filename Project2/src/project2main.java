import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.PrintStream;
import java.io.FileNotFoundException;

public class project2main { //javac Project1/src/*.java -d Project1/bin -target 17
    public static void main(String[] args) throws FileNotFoundException, IOException{
        //Main object to keep track of the whole ExcelFedSystem
        ExcelFedSystem system = new ExcelFedSystem();
        //Main object to keep track of the statistics
        Statistics statistics = new Statistics(); 
        BufferedReader input = null;


        try {
            input = new BufferedReader(new FileReader(new File(args[0])));
            String currLine;
            //Regular expression patterns for parsing the input.
            Pattern playerP = Pattern.compile("^(\\d+) (\\d+)$");
            Pattern trainingP = Pattern.compile("^t (\\d+) (\\d+\\.\\d+|\\d+) (\\d+\\.\\d+|\\d+)$");
            Pattern massageP = Pattern.compile("^m (\\d+) (\\d+\\.\\d+|\\d+) (\\d+\\.\\d+|\\d+)$");
            Pattern therapistP = Pattern.compile("^(\\d+) (.+)$");
            Matcher playerM, trainingM, massageM, therapistM;
            
            //First line is the number of players in the system.
            currLine = input.readLine();
            int numPlayers = Integer.parseInt(currLine); 
            //Input taking loop for Players.
            for (int i=0; i<numPlayers; i++) {
                currLine = input.readLine();
                playerM = playerP.matcher(currLine);

                if (playerM.matches()) {
                    system.addPlayer(Integer.parseInt(playerM.group(1)), 
                    Integer.parseInt(playerM.group(2)));
                }
            }

            //Line for the number of events in the system.
            currLine = input.readLine();
            int numEvents = Integer.parseInt(currLine);
            system.initialize(numEvents); //First of several system initialisations.
            //Input taking loop for Events.
            for (int i=0; i<numEvents; i++) {
                currLine = input.readLine();
                trainingM =  trainingP.matcher(currLine);
                massageM = massageP.matcher(currLine);

                if (trainingM.matches()) { // if training event
                    system.addEvent(1, Integer.parseInt(trainingM.group(1)),
                    Double.parseDouble(trainingM.group(2)), 
                    Double.parseDouble(trainingM.group(3)));

                } else if (massageM.matches()) { //if massage event
                    system.addEvent(4, Integer.parseInt(massageM.group(1)),
                    Double.parseDouble(massageM.group(2)), 
                    Double.parseDouble(massageM.group(3)));
                    
                } else { //shouldn't enter here.
                    System.out.println("PROBLEM: Event not matched!"); 
                } 
            }
            
            //Input line for therapists.
            currLine = input.readLine();            
            therapistM = therapistP.matcher(currLine);
            if (therapistM.matches()) {
                system.initialize(Integer.parseInt(therapistM.group(1)), 
                therapistM.group(2), numEvents);
            }
            
            //Input line for number of massuers and trainers.
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

        //Main loop for the program
        system.initialize(statistics);
        while (system.loop()) { 
            system.start(); //conducts the program for one cycle.
        }
        
        //Output generation.
        File outputFile = new File(args[1]);
        outputFile.createNewFile();
        PrintStream output = new PrintStream(outputFile); 
        output.print(statistics.output(system));
        output.close();
    }
}