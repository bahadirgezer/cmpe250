public class Arc {
    private final int to;
    private int flow;

    public Arc(int to, int capacity) {
        this.to = to;
        this.flow = capacity;
    }

    public void addCapacity(int capacity) {
        this.flow += capacity;
    }

    // public int initializePreflow() {
    //     return flow;
    // }

    public int getTo() {
        return to;
    }

    // public void addFlow(int flow) {
    //     this.flow = flow;
    // }
    
    public void setFlow(int flow) {
        this.flow = flow;
    }

    public void addFlow(int flow) {
        this.flow += flow;
    }
    
    public void removeFlow(int flow) {
        this.flow -= flow;
    }
    public int getFlow() {
        return flow;
    }
}