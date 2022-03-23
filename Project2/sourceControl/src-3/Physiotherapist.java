public class Physiotherapist implements Comparable<Physiotherapist> {
    private final int ID;
    private final double duration;


    public Physiotherapist(int ID, double duration) {
        this.ID = ID;
        this.duration = duration;

    }

    public double getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Physiotherapist other) { //compareTo method to sort Student objects
        return other.ID > this.ID ? 1 : -1;
    }
}
