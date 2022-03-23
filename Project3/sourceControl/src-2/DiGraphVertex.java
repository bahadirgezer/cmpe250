import java.util.HashMap;
import java.lang.Math;

public class DiGraphVertex implements Comparable<DiGraphVertex> {
    private Integer distance;
    private DiGraphVertex path;
    private boolean known; 
    private HashMap<Integer, Integer> adjacent; //Node ID and lenght to it
    private int id;

    public DiGraphVertex(int id) {
        this.id = id;
        distance = Integer.MAX_VALUE;
        path = null;
        known = false;
        adjacent = new HashMap<Integer, Integer>();
    }

    public void addEdge(Integer adjID, Integer lenght) {
        if (adjacent.containsKey(adjID)) {    
            Math.min(lenght, adjacent.get(adjID));
        }
        adjacent.put(adjID, lenght);
    }

    public void setSource() {
        distance = 0;
    }

    @Override
    public int compareTo(DiGraphVertex o) {
        if (o.distance > this.distance) {
            return -1;
        } else if (o.distance < this.distance) {
            return 1;
        } else {
            if (o.id > this.id) {
                return -1;
            } else if (o.id < this.id) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void makeKnown() {
        known = true;
    }
    
    public HashMap<Integer, Integer> getAdjacent() {
        return adjacent;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setPath(DiGraphVertex path) {
        this.path = path;
    }

    public DiGraphVertex getPath() {
        return path;
    }

    public Integer getId() {
        return id;
    }

    public String toString() {
        return "Vertex ID: "+id+", distance: "+distance;
    }
}
