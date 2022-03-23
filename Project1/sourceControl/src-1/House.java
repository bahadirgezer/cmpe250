
public class House implements Comparable<House> {
    final private int id;
    private int occupancy;
    double rating;

    public House(int id, int occupancy, double rating) {
        this.id = id;
        this.occupancy = occupancy;
        this.rating = rating;
    }

    public boolean increment() { // returns true if it just got emptied.
        if (occupancy == 1) {
            occupancy -= 1;
            return true;
        } else {
            occupancy -= 1;
            return false;
        }
    }

    public void addOccupancy(int semesters) {
        occupancy += semesters;
    }

    public int getID() {
        return id;
    }

    public double getRating() {
        return rating;
    }

    public boolean isEmpty() {
        if (occupancy == 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int compareTo(House other) {
        if (other.id > this.id) {
            return -1;
        } else {
            return 1;
        }
    }
}