import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Graph {
    private int[] rank;
    private int[] representative;
    private ArrayList<GraphEdge> edges;

    public Graph() {
        edges = new ArrayList<GraphEdge>();
    }

    public void addEdge(int source, int destination, int weight) {
        edges.add(new GraphEdge(source, destination, weight));
    }

    public void buildSets() {
        for (int i = 0; i < rank.length; i++) {
            rank[i] = 0;
            representative[i] = i;
        }
    }
    
    private void unionSets(int firstRepresentative, int secondRepresentative) {
        firstRepresentative = findSet(firstRepresentative); //find the sets they belong to.
        secondRepresentative = findSet(secondRepresentative);
        
        if (firstRepresentative == secondRepresentative) {
            return; //they are the same set, no union necessary.
        }

        if (rank[firstRepresentative] < rank[secondRepresentative]) {
            int temp = firstRepresentative; //swap the two representatives.
            firstRepresentative = secondRepresentative;
            secondRepresentative = temp;
        }
        representative[secondRepresentative] = firstRepresentative;
        if (rank[firstRepresentative] == rank[secondRepresentative]) {
            rank[firstRepresentative]++;
        }        
    }

    private int findSet(int vertex) {
        if (representative[vertex] != vertex) {
            representative[vertex]  = findSet(representative[vertex]);
        }
        return representative[vertex];
    }

    public int minimumSpanningTree(int numCities) {
        int cost = 0;
        int visited = 1;
        rank = new int[numCities];
        representative = new int[numCities];
        buildSets(); //initializes the sets
        Collections.sort(edges, new EdgeComparator()); //sorts the edges in the order of 
        
        for (GraphEdge edge : edges) {
            //if they are not in the same subset, if they are not in the same subtree, if they don't create a cycle.
            if (findSet(edge.getSource()) != findSet(edge.getDestination())) { 
                unionSets(edge.getSource(), edge.getDestination());
                cost += edge.getWeight();
                visited++;
            }
        }
        return visited == numCities ? cost*2 : -2;
    }

    public String createLog(PrintStream logOutput) {
        String out = "";
        for (GraphEdge edge : edges) {
            out += edge + "\n";
        }
        return out;
    }
}

class EdgeComparator implements Comparator<GraphEdge> {
    @Override
    public int compare(GraphEdge o1, GraphEdge o2) {
        if (o1.getWeight() == o2.getWeight()) { 
            return 0;
        }
        return o1.getWeight() > o2.getWeight() ? 1 : -1;
    }
}
