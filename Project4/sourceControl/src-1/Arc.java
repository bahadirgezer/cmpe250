public class Arc {
    private int capacity;
    private int flow;
    private int reverseFlow;
    private int from;
    private int to;

    public Arc(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public void addCapacity(int extraCapacity) {
        capacity += extraCapacity;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getDeficit() {
        return (capacity - flow);
    }

    public void adjustFlow(int newFlow) {
        flow += newFlow;
        reverseFlow -= reverseFlow;
    }
}