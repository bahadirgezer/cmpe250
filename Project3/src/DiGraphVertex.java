import java.util.HashMap;
import java.lang.Math;

public class DiGraphVertex implements Comparable<DiGraphVertex> {
    private Integer distance;
    private DiGraphVertex path;
    private HashMap<Integer, Integer> adjacent; //Node ID and lenght to it
    private int id;

    public DiGraphVertex(int id) {
        this.id = id;
        distance = Integer.MAX_VALUE;
        path = null;
        adjacent = new HashMap<Integer, Integer>();
    }

    public void addEdge(Integer adjID, Integer lenght) {
        if (adjacent.containsKey(adjID)) {
            //updates the entry if the second matching is shorter.
            lenght = Math.min(lenght, adjacent.get(adjID));
        }
        adjacent.put(adjID, lenght);
    }

    //getter and setter methods.
    public void setSource() {
        distance = 0;
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
            } else { //TreeSet implementation requires return 0; in the compare method
                return 0;
            }
        }
    }
}
