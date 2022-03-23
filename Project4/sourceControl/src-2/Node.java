import java.util.HashSet;

public class Node implements Comparable<Node> {
    private final int id;
    private int height;
    private int excessFlow;
    private HashSet<Integer> adjacent; 

    public Node(int id) {
        this.id = id;
        height = 0;
        excessFlow = 0;
        adjacent = new HashSet<Integer>();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public int getExcessFlow() {
        return excessFlow;
    }

    public void decreaseExcessFlow(int flow) {
        excessFlow -= flow;
    }

    public void increaseExcessFlow(int flow) {
        excessFlow += flow;
    }

    public int getID() {
        return id;
    }

    @Override
    public int compareTo(Node o) {
        return (o.id > this.id) ? 1 : (o.id < this.id) ? -1 : 0;
    }
}
