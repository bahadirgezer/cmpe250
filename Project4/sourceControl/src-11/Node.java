public class Node {
    private final int id;
    private int height;
    private int excess;
    private boolean active;

    public Node(int id, int height) {
        this.id = id;
        this.height = height;
        excess = 0;
        active = false;
    }

    public boolean hasExcess() {
        return (excess > 0) ? true : false;
    }

    public int getExcess() {
        return excess;
    }

    public void removeExcess(int excess) {
        this.excess -= excess;
    }

    public void addExcess(int excess) {
        this.excess += excess;
    }

    public void setSource(int numNodes) {
        height = numNodes;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getID() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public void makeActive() {
        active = true;
    }

    public void makeInactive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public String toString() {
        return "Node "+id+" excess: "+excess+" height: "+height;
    }
}
