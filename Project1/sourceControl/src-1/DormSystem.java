import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class DormSystem {
    private TreeMap<Integer, Student> unmatchedStudents = new TreeMap<Integer, Student>();
    private LinkedList<Student> unluckyStudents = new LinkedList<Student>();

    private TreeMap<Integer, House> emptyHouses = new TreeMap<Integer, House>(); 
    private LinkedHashMap<Integer, House> occupiedHouses = new LinkedHashMap<Integer, House>(); 

    public void systemCheck() {
        System.out.println("unmatchedStudents: "+ unmatchedStudents);
        System.out.println("unluckyStudents: "+ unluckyStudents);
        System.out.println("emptyHouses: "+ emptyHouses);
        System.out.println("occupiedHouses: "+ occupiedHouses);
    }

    public void allocate() { //allocate using TreeMap
        Iterator<Integer> emptyIterator = emptyHouses.keySet().iterator();
        while(emptyIterator.hasNext()){
            Integer keyHouse = emptyIterator.next();
            House house = emptyHouses.get(keyHouse);

            Iterator<Integer> unmatchedIterator = unmatchedStudents.keySet().iterator();
            while(unmatchedIterator.hasNext()) {
                Integer keyStudent = unmatchedIterator.next();
                Student student = unmatchedStudents.get(keyStudent);
               
                System.out.println("========================================allocateLoop");
                this.systemCheck();
                System.out.println("house: "+house+" , student "+student);

                if (house.getRating() >= student.getRating()) {

                    emptyIterator.remove();
                    house.addOccupancy(student.getSemester());
                    occupiedHouses.put(keyHouse, house);
                    
                    unmatchedIterator.remove(); 
                    break; 
                }
            }
        }
    }

    public String output() {
        Collections.sort(unluckyStudents);
        String out = "";
        for (Student student: unluckyStudents){
            out += student.toStringName() + "\n";
            System.out.println(student.toString());
        }     
        return out;
    }

    public void clear() {
        clearStudents();
        clearHouses();
    }

    private void clearStudents() {
        Iterator<Integer> unmatchedIterator = unmatchedStudents.keySet().iterator();
        while(unmatchedIterator.hasNext()) {
            Integer key = unmatchedIterator.next();
            Student student = unmatchedStudents.get(key);
            if (student.increment()){ //if student graduated
                unmatchedIterator.remove();
                unluckyStudents.add(student);
            }
        }
    }

    private void clearHouses() {
        Iterator<Integer> occupiedIterator = occupiedHouses.keySet().iterator();
        while(occupiedIterator.hasNext()) {
            Integer key = occupiedIterator.next();
            House house = occupiedHouses.get(key); 
            if (house.increment()) {
                occupiedIterator.remove();
                emptyHouses.put(key, house);
            }
        }
    }

    public boolean resume() {
        return !unmatchedStudents.isEmpty();
    }

    public void addHouse(int id, int occupancy, double rating) {
        House house = new House(id, occupancy, rating);
        if (occupancy == 0) {
            emptyHouses.put(id, house);
        } else {
            occupiedHouses.put(id, house);
        }
    }

    public void addStudent(int id, String name, int semester, double rating){
        Student student = new Student(id, name, semester, rating);
        unmatchedStudents.put(id, student);
    }
}