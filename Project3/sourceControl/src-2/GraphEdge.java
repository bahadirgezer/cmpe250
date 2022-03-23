public class GraphEdge {
    final private int source;
    final private int destination; //undirected
    final private int weight;

    public GraphEdge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public String toString() {
        return "from: "+source+", to: "+destination+", weight: "+weight;
    }
}
