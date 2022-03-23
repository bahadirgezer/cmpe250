
public class Student implements Comparable<Student> {
    final int id;
    int semester;
    String name;
    double rating;

    public Student(int id, String name, int semester, double rating) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.rating = rating;

    }

    public boolean increment() {
        if (semester == 1) {
            semester -= 1;
            return true;
        } else {
            semester -= 1;
            return false;
        }
    }

    public int getSemester() {
        return semester;
    }

    public double getRating() {
        return rating;
    }

    public String toStringName() {
        return name;
    }

    @Override
    public String toString() {
        String out = "ID: " + id + ", name: " + name;
        return out;
    }

    @Override
    public int compareTo(Student other) {
        if (other.id > this.id) {
            return -1;
        } else {
            return 1;
        }
    }
}