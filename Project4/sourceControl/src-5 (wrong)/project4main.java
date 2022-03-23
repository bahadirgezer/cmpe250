import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class project4main { 
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        BufferedReader input = null;
        Graph graph = new Graph();
        int gifts = 0;
        
        try {
            //input = new BufferedReader(new FileReader(new File("C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project4\\tests\\inputs\\input2.txt")));
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
            int greenTrainCapacity = 0;
            if (numGreenTrain != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    int capacity = Integer.parseInt(transportMatcher.group(1));
                    if (capacity == 0) {
                        numGreenTrain -= 1;
                    }
                    greenTrainCapacity += capacity;
                }
                graph.addGreenTrain(greenTrainCapacity, numGreenTrain);
            }

            //redTrain
            currLine = input.readLine();
            int numRedTrain = Integer.parseInt(currLine);
            currLine = input.readLine();
            int redTrainCapacity = 0;
            if (numRedTrain != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    int capacity = Integer.parseInt(transportMatcher.group(1));
                    if (capacity == 0) {
                        numRedTrain -= 1;
                    }
                    redTrainCapacity += capacity;
                }
                graph.addRedTrain(redTrainCapacity, numRedTrain);
            }

            //greenReindeer
            currLine = input.readLine();
            int numGreenReindeer = Integer.parseInt(currLine);
            currLine = input.readLine();
            int greenReindeerCapacity = 0;
            if (numGreenReindeer != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    int capacity = Integer.parseInt(transportMatcher.group(1));
                    if (capacity == 0) {
                        numGreenReindeer -= 1;
                    }
                    greenReindeerCapacity += capacity;
                }
                graph.addGreenReindeer(greenReindeerCapacity, numGreenReindeer);
            }

            //redReindeer
            currLine = input.readLine();
            int numRedReindeer = Integer.parseInt(currLine);
            currLine = input.readLine();
            int redReindeerCapacity = 0;
            if (numRedReindeer != 0) {
                transportMatcher = transportPattern.matcher(currLine);
                while(transportMatcher.find()) {
                    int capacity = Integer.parseInt(transportMatcher.group(1));
                    if (capacity == 0) {
                        numRedReindeer -= 1;
                    }
                    redReindeerCapacity += capacity;
                }
                graph.addRedReindeer(redReindeerCapacity, numRedReindeer);
            }

            //start of bag input taking
            currLine = input.readLine();
            int numBags = Integer.parseInt(currLine);
            currLine = input.readLine();
            if (numBags != 0) {
                bagMatcher = bagPattern.matcher(currLine);
                while(bagMatcher.find()) {
                    int numGifts = Integer.parseInt(bagMatcher.group(2));
                    graph.addBag(bagMatcher.group(1), numGifts);
                    gifts += numGifts;
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

        long inputEnd = System.currentTimeMillis();
        System.out.println("Input: "+ (inputEnd-start)+ " ms");

        int maxMatching = graph.maximumBipartiteFlow();
        int notDistributed = gifts - maxMatching;

        //File outputFile = new File("C:\\Users\\bahad\\Desktop\\bounDers\\3.Donem\\Cmpe250\\projects\\Project4\\tests\\outputs\\myoutput2.txt");
        File outputFile = new File(args[1]);
        outputFile.createNewFile();
        PrintStream output = new PrintStream(outputFile); 
        output.println(notDistributed);
        output.close();
        
        long end = System.currentTimeMillis();

        System.out.println("Output: "+(end-inputEnd) + " ms");
    }
}

// & 'C:\Program Files\Java\jdk-17.0.1\bin\java.exe' '-agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=localhost:59095' '--enable-preview' '-XX:+ShowCodeDetailsInExceptionMessages' '-cp' 'C:\Users\bahad\AppData\Roaming\Code\User\workspaceStorage\bdbf76bbbd5e8dd77a00818358d65723\redhat.java\jdt_ws\Project4_7598599e\bin' 'project4main C:\Users\bahad\Desktop\bounDers\3.Donem\Cmpe250\projects\Project4\tests\inputs\input1.txt C:\Users\bahad\Desktop\bounDers\3.Donem\Cmpe250\projects\Project4\tests\outputs\myoutput1.txt'