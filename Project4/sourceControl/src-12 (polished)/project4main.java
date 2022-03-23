import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class project4main { 
    public static void main(String[] args) throws IOException {
        BufferedReader input = null;
        Graph graph = new Graph();
        int gifts = 0;
        
        try {
            input = new BufferedReader(new FileReader(new File(args[0])));
            Pattern transportPattern = Pattern.compile("(\\d+)");
            Pattern bagPattern = Pattern.compile("([a-e]+) (\\d+)");
            Matcher transportMatcher, bagMatcher;
            String currLine;

            //start of transport input taking
            //greenTrain
            currLine = input.readLine();
            int numGreenTrain = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numGreenTrain != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    graph.addGreenTrain(Integer.parseInt(transportMatcher.group(1)));
                }
            }

            //redTrain
            currLine = input.readLine();
            int numRedTrain = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numRedTrain != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    graph.addRedTrain(Integer.parseInt(transportMatcher.group(1)));
                }
            }

            //greenReindeer
            currLine = input.readLine();
            int numGreenReindeer = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numGreenReindeer != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    graph.addGreenReindeer(Integer.parseInt(transportMatcher.group(1)));
                }
            }

            //redReindeer
            currLine = input.readLine();
            int numRedReindeer = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numRedReindeer != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    graph.addRedReindeer(Integer.parseInt(transportMatcher.group(1)));
                }
            }

            //start of bag input taking
            currLine = input.readLine();
            int numBags = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numBags != 0) {
                bagMatcher = bagPattern.matcher(currLine);
                while(bagMatcher.find()) {
                    int numGifts = Integer.parseInt(bagMatcher.group(2));
                    if (graph.addBag(bagMatcher.group(1), numGifts)) {
                        gifts += numGifts;
                    }
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

        //matching
        int maxMatching = graph.maximumBipartiteMatching();
        int notDistributed = gifts - maxMatching;

        File outputFile = new File(args[1]);
        outputFile.createNewFile();
        PrintStream output = new PrintStream(outputFile); 
        output.println(notDistributed);
        output.close();
    }
}
