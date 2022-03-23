import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class DiGraph {
    private HashMap<Integer, DiGraphVertex> vertices;
    
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

    public String getShortestPath(Integer from, Integer to) {
        DiGraphVertex fromVertex = vertices.get(from);
        this.shortestPath(fromVertex);
        DiGraphVertex toVertex = vertices.get(to);
        return printPath(toVertex);
    }

    private void shortestPath(DiGraphVertex from){
        from.setSource();
        TreeSet<DiGraphVertex> unknown = new TreeSet<DiGraphVertex>(vertices.values()); 

        while(!unknown.isEmpty()) {
            DiGraphVertex vertex = unknown.pollFirst();
            vertex.makeKnown();
            
            for (Map.Entry<Integer, Integer> adjacentSet : vertex.getAdjacent().entrySet()) {
                DiGraphVertex adjacent = vertices.get(adjacentSet.getKey());
                Integer distance = adjacentSet.getValue();
                
                if (unknown.contains(adjacent)) {
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

    public int getMecnunTime(int endingCity) {
        return vertices.get(endingCity).getDistance();
    }
} 