public class House {
    final private int id;
    private int occupancy;
    private double rating;

    public House(int id, int occupancy, double rating) { //constructor
        this.id = id;
        this.occupancy = occupancy;
        this.rating = rating;
    }

    public boolean increment() { // returns true if house just got emptied
        if (occupancy == 1) {
            occupancy -= 1;
            return true;
        } else {
            occupancy -= 1; //returns false if the house will still be occupied
            return false;
        }
    }

    public void addOccupancy(int semesters) { //adds the semesters of the incoming student
        occupancy += semesters;
    }

    public int getID() { //start of getter methods
        return id;
    }

    public double getRating() { 
        return rating;
    }
}