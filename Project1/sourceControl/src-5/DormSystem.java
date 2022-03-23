import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.lang.Math;

public class DormSystem { //M.V.C. (Most Valuable Class) of the program.
    private TreeMap<Integer, Student> unmatchedStudents0 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents1 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents2 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents3 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents4 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents5 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents6 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents7 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents8 = new TreeMap<Integer, Student>();
    private TreeMap<Integer, Student> unmatchedStudents9 = new TreeMap<Integer, Student>();
    ArrayList <TreeMap<Integer, Student>> unmatchedStudents = new ArrayList<TreeMap<Integer, Student>>() {{
        add(unmatchedStudents0);
        add(unmatchedStudents1);
        add(unmatchedStudents2);
        add(unmatchedStudents3);
        add(unmatchedStudents4);
        add(unmatchedStudents5);
        add(unmatchedStudents6);
        add(unmatchedStudents7);
        add(unmatchedStudents8);
        add(unmatchedStudents9);

    }};

    private LinkedList<Student> unluckyStudents = new LinkedList<Student>();
    //TreeMap => Fast Traversal, automatic sorting, fast add and remove.
    private TreeMap<Integer, House> emptyHouses = new TreeMap<Integer, House>(); 
    private LinkedHashMap<Integer, House> occupiedHouses = new LinkedHashMap<Integer, House>(); 
    // LinkedHashMap => Fast access, fast add and remove, can iterate over it, order not important.
    // LinkedList => Fast add, sortable, can iterate over it.

    public void addHouse(int id, int occupancy, double rating) { //creates House object
        House house = new House(id, occupancy, rating);
        if (occupancy == 0) {
            emptyHouses.put(id, house); //adds it to the accurate collection based on initial occupancy
        } else {
            occupiedHouses.put(id, house);
        }
    }

    public void addStudent(int id, String name, int semester, double rating) { //creates Student object
        Student student = new Student(id, name, semester, rating);
        if (semester == 0) {
            unluckyStudents.add(student); //adds it to the corresponding collection based on initial semester
        } else {
            if (rating < 1) {
                unmatchedStudents0.put(id, student);
            } else if (rating < 2) {
                unmatchedStudents1.put(id, student); 
            } else if (rating < 3) {
                unmatchedStudents2.put(id, student); 
            } else if (rating < 4) {
                unmatchedStudents3.put(id, student); 
            } else if (rating < 5) {
                unmatchedStudents4.put(id, student); 
            } else if (rating < 6) {
                unmatchedStudents5.put(id, student); 
            } else if (rating < 7) {
                unmatchedStudents6.put(id, student); 
            } else if (rating < 8) {
                unmatchedStudents7.put(id, student); 
            } else if (rating < 9) {
                unmatchedStudents8.put(id, student); 
            } else {
                unmatchedStudents9.put(id, student);
            }
        }
    }

    public boolean resume() { //will run the program until all students graduate
        return !(unmatchedStudents0.isEmpty()&&
        unmatchedStudents1.isEmpty()&&
        unmatchedStudents2.isEmpty()&&
        unmatchedStudents3.isEmpty()&&
        unmatchedStudents4.isEmpty()&&
        unmatchedStudents5.isEmpty()&&
        unmatchedStudents6.isEmpty()&&
        unmatchedStudents7.isEmpty()&&
        unmatchedStudents8.isEmpty()&&
        unmatchedStudents9.isEmpty());
    }

    public void allocate() { //allocates every empty house a compatible student
       
        Iterator<Integer> emptyItr = emptyHouses.keySet().iterator();
        while(emptyItr.hasNext()){ //first loop over empty houses
            Integer keyHouse = emptyItr.next();
            House house = emptyHouses.get(keyHouse);
            Double rating = house.getRating();
            int start;
            if (rating == 10){
                start = 9;
            } else {
                start = (int) Math.floor(rating);
            }

            for(TreeMap<Integer, Student> unmatched :  unmatchedStudents.subList(start, 10)) {
                Iterator<Integer> unmatchedItr = unmatched.keySet().iterator();
                while(unmatchedItr.hasNext()) { //second loop over unmatched students
                    Integer keyStudent = unmatchedItr.next();                        
                    Student student = unmatched.get(keyStudent);
                    
                    if (rating >= student.getRating()) { //if they are compatible 
                        emptyItr.remove(); //house gets removed from emptyHouses
                        house.addOccupancy(student.getSemester()); //gets its occupnacy value updated
                        occupiedHouses.put(keyHouse, house);  //then added to occupied houses                            unmatchedItr.remove();  //mathced student is discarded
                        break; 
                    }
                }
            }
        }
    }

    public void clear() { //overfunctioning
        clearStudents();
        clearHouses();
    }

    private void clearStudents() { //check if any unmatched student graduated
        for (TreeMap<Integer, Student> unmatched : unmatchedStudents) {
            Iterator<Integer> unmatchedIterator = unmatched.keySet().iterator();
            while(unmatchedIterator.hasNext()) { //iterate over unmatched students
                Integer key = unmatchedIterator.next();
                Student student = unmatched.get(key);
    
                if (student.increment()){ //if student has graduated
                    unmatchedIterator.remove(); //gets removed from the waiting list
                    unluckyStudents.add(student); //gets added to unlucky list
                }
            }
        }
    }

    private void clearHouses() { //check if any occupied houses will see its occupant graduate this semester
        Iterator<Integer> occupiedIterator = occupiedHouses.keySet().iterator();
        while(occupiedIterator.hasNext()) { //iterate over occupied houses
            Integer key = occupiedIterator.next();
            House house = occupiedHouses.get(key); 

            if (house.increment()) { //if the occupant graduated
                occupiedIterator.remove(); //remove house from occupied houses list
                emptyHouses.put(key, house); // add house to empty houses list
            }
        }
    }

    public String output() { //creates the whole output
        Collections.sort(unluckyStudents); //sorting the unlucky students by their ID
        String out = "";
        for (Student student: unluckyStudents) {
            out = out.concat(student.getName()+"\n");
        }     
        return out; 
    }
}