import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class project3main {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        long start = System.currentTimeMillis();
       
        BufferedReader input = null;
        DiGraph diGraph = new DiGraph();
        Graph graph = new Graph();
        int startingCity=0, endingCity=0, cityCount=0, fatherLimit=0;
        

        try {
            input = new BufferedReader(new FileReader(new File(args[0])));
            Pattern initialCitiesPattern = Pattern.compile("^c(\\d+) c(\\d+)$");
            Pattern firstNodePattern = Pattern.compile("c(\\d+) (\\d+)");
            Pattern secondNodePattern = Pattern.compile("d(\\d+) (\\d+)");
            String currLine;

            fatherLimit = Integer.parseInt(input.readLine());
            cityCount = Integer.parseInt(input.readLine());

            currLine = input.readLine();
            Matcher initialCitiesMatcher = initialCitiesPattern.matcher(currLine);

            if (initialCitiesMatcher.matches()) {
                startingCity = Integer.parseInt(initialCitiesMatcher.group(1));
                endingCity = Integer.parseInt(initialCitiesMatcher.group(2));
            }

            for (int cityId = 1; cityId < endingCity; cityId++) {
                currLine = input.readLine();
                Matcher firstNodeMatcher = firstNodePattern.matcher(currLine);

                diGraph.addVertex(cityId);
                while (firstNodeMatcher.find()) {
                    diGraph.addEdge(cityId,
                    Integer.parseInt(firstNodeMatcher.group(1)), 
                    Integer.parseInt(firstNodeMatcher.group(2)));
                }
            }
            diGraph.addVertex(endingCity);





    

            
            // int size = cityCount - endingCity + 1;
            // for (int cityId = -1; ++cityId < size;)

            for (int cityId = 0; cityId < cityCount - endingCity + 1; cityId++) {
                currLine = input.readLine();
                Matcher secondNodeMatcher = secondNodePattern.matcher(currLine);

                while(secondNodeMatcher.find()) {
                    graph.addEdge(cityId, 
                    Integer.parseInt(secondNodeMatcher.group(1)), 
                    Integer.parseInt(secondNodeMatcher.group(2)));
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

        File outputFile = new File(args[1]);
        outputFile.createNewFile();
        PrintStream output = new PrintStream(outputFile); 
        String firstLine = diGraph.getShortestPath(startingCity, endingCity);
        String secondLine;
        int mecnunTime = diGraph.getMecnunTime(endingCity);
        if (firstLine.equals("-1")) {
            secondLine = "-1";
        } else {
            if (mecnunTime>fatherLimit) {
                secondLine = "-1";
            } else {
                secondLine = Integer.toString(graph.minimumSpanningTree(cityCount-endingCity+1));
            }
        }
        output.print(firstLine+"\n"+secondLine);
        output.close();

        // File outputLogFile = new File(args[2]);
        // outputLogFile.createNewFile();
        // PrintStream outputLog = new PrintStream(outputLogFile);
        // outputLog.print(graph.createLog(outputLog));
        // outputLog.close();

        long end = System.currentTimeMillis();
        System.out.println((end-start)+" ms");
    }
}