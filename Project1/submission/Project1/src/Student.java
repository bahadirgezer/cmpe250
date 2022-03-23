public class Student implements Comparable<Student> {
    private final int id;
    private int semester;
    private String name;
    private double rating;

    public Student(int id, String name, int semester, double rating) { //constructor
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.rating = rating;
    }

    public boolean increment() { //returns true if student just graduated
        if (semester == 1) {
            semester -= 1;
            return true;
        } else {
            semester -= 1; //returns false if the student still has time
            return false; 
        }
    }

    public int getSemester() { //start of getter methods
        return semester;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() { //standrd toString method. Got used for debugging.
        String out = "ID: " + id + ", name: " + name;
        return out;
    }

    @Override
    public int compareTo(Student other) { //compareTo method to sort Student objects
        if (other.id > this.id) {
            return -1;
        } else {
            return 1;
        }
    }
}