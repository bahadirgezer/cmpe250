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
        BufferedReader input = null;
        DiGraph diGraph = new DiGraph();
        Graph graph = new Graph();
        int startingCity=0, endingCity=0, fatherLimit=0, secondPartEndingCity=0;

        try { //Start of input taking.
            input = new BufferedReader(new FileReader(new File(args[0])));
            Pattern initialCitiesPattern = Pattern.compile("^c(\\d+) c(\\d+)$");
            Pattern firstNodePattern = Pattern.compile("c(\\d+) (\\d+)");
            Pattern secondNodePattern = Pattern.compile("d(\\d+) (\\d+)");
            Pattern firstStartPattern = Pattern.compile("^c(\\d+)");
            Pattern secondStartPattern = Pattern.compile("^d(\\d+)");
            String currLine;

            fatherLimit = Integer.parseInt(input.readLine());
            Integer.parseInt(input.readLine());

            currLine = input.readLine();
            Matcher initialCitiesMatcher = initialCitiesPattern.matcher(currLine);

            if (initialCitiesMatcher.matches()) {
                startingCity = Integer.parseInt(initialCitiesMatcher.group(1));
                endingCity = Integer.parseInt(initialCitiesMatcher.group(2));
            }

            while ((currLine = input.readLine()) != null){
                Matcher firstNodeMatcher = firstNodePattern.matcher(currLine);
                Matcher secondNodeMatcher = secondNodePattern.matcher(currLine);
                Matcher firstStartMatcher = firstStartPattern.matcher(currLine);
                Matcher secondStartMatcher = secondStartPattern.matcher(currLine);

                //This part is modified to work when Leyla isn't in the last c city.
                if (firstStartMatcher.find()) { //c cities
                    int cityId = Integer.parseInt(firstStartMatcher.group(1));
                    diGraph.addVertex(cityId);
                    while (firstNodeMatcher.find()) {
                        diGraph.addEdge(cityId,
                        Integer.parseInt(firstNodeMatcher.group(1)), 
                        Integer.parseInt(firstNodeMatcher.group(2)));
                    }
                    if (cityId == endingCity) {
                        while (secondNodeMatcher.find()) {
                            graph.addEdge(0,
                            Integer.parseInt(secondNodeMatcher.group(1)), 
                            Integer.parseInt(secondNodeMatcher.group(2)));
                        }
                    }
                } else if (secondStartMatcher.find()) { //d cities
                    secondPartEndingCity++;
                    int cityId = Integer.parseInt(secondStartMatcher.group(1));
                    while (secondNodeMatcher.find()) {
                        graph.addEdge(cityId,
                        Integer.parseInt(secondNodeMatcher.group(1)), 
                        Integer.parseInt(secondNodeMatcher.group(2)));
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
                secondLine = Integer.toString(graph.minimumSpanningTree(secondPartEndingCity+1));
            }
        }
        output.print(firstLine+"\n"+secondLine);
        output.close();
    }
}