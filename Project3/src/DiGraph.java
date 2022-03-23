import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class DiGraph { //Main graph class for dijkstra
    private HashMap<Integer, DiGraphVertex> vertices; //Node id to vertex object.
    
    public DiGraph() {
        vertices = new HashMap<Integer, DiGraphVertex>();
    }

    public void addEdge(Integer from, Integer to, Integer lenght) {
        DiGraphVertex fromVertex = vertices.get(from);
        fromVertex.addEdge(to, lenght);
    }

    public void addVertex(Integer id) {
        vertices.put(id, new DiGraphVertex(id));
    }

    public int getMecnunTime(int endingCity) {
        return vertices.get(endingCity).getDistance();
    }

    public String getShortestPath(Integer from, Integer to) {
        //public function to reach inner helper functions.
        DiGraphVertex fromVertex = vertices.get(from);
        DiGraphVertex toVertex = vertices.get(to);
        this.shortestPath(fromVertex, toVertex);
        return printPath(toVertex);
    }

    private void shortestPath(DiGraphVertex from, DiGraphVertex to) {
        //TreeSet is faster than PQ because PQ remove is O(n), while TreeSet 
        //has O(logn) for both remove and add. It would have been better if a 
        //FibonacciHeap implementation was given to us.
        TreeSet<DiGraphVertex> unknown = new TreeSet<DiGraphVertex>(vertices.values()); 
        from.setSource();

        while(!unknown.isEmpty()) {
            DiGraphVertex vertex = unknown.pollFirst();

            for (Map.Entry<Integer, Integer> adjacentSet : vertex.getAdjacent().entrySet()) {
                DiGraphVertex adjacent = vertices.get(adjacentSet.getKey());
                Integer distance = adjacentSet.getValue();
                //standard dijkstra implementation

                if (unknown.contains(adjacent)) {
                    //(vertex.getDistance() + distance > 0) is to control integer overflow problem.
                    if (vertex.getDistance() + distance < adjacent.getDistance() && (vertex.getDistance() + distance > 0)) {
                        unknown.remove(adjacent);
                        adjacent.setDistance(vertex.getDistance() + distance);
                        adjacent.setPath(vertex);
                        unknown.add(adjacent);
                    }
                }
            }
        }
    }

    private String printPath(DiGraphVertex end) {
        if (end.getPath() == null) {
            return "-1";
        }
        String out = "c"+end.getId();
        DiGraphVertex nextPath = end.getPath();
        while(nextPath != null) {
            out = "c"+nextPath.getId()+" "+out;
            nextPath = nextPath.getPath();
        }
        return out;
    }
} 