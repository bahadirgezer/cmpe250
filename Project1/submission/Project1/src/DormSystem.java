import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class DormSystem { //M.V.C. (Most Valuable Class) of the program.
    private TreeMap<Integer, Student> unmatchedStudents = new TreeMap<Integer, Student>();
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
            unmatchedStudents.put(id, student);
        }
    }

    public boolean resume() { //will run the program until all students graduate
        return !unmatchedStudents.isEmpty();
    }

    public void allocate() { //allocates every empty house a compatible student
       
        Iterator<Integer> emptyItr = emptyHouses.keySet().iterator();
        while(emptyItr.hasNext()){ //first loop over empty houses
            Integer keyHouse = emptyItr.next();
            House house = emptyHouses.get(keyHouse);

            Iterator<Integer> unmatchedItr = unmatchedStudents.keySet().iterator();
            while(unmatchedItr.hasNext()) { //second loop over unmatched students
                Integer keyStudent = unmatchedItr.next();
                Student student = unmatchedStudents.get(keyStudent);
               
                if (house.getRating() >= student.getRating()) { //if they are compatible 

                    emptyItr.remove(); //house gets removed from emptyHouses
                    house.addOccupancy(student.getSemester()); //gets its occupnacy value updated
                    occupiedHouses.put(keyHouse, house);  //then added to occupied houses
                    
                    unmatchedItr.remove();  //mathced student is discarded
                    break; 
                }
            }
        }
    }

    public void clear() { //overfunctioning
        clearStudents();
        clearHouses();
    }

    private void clearStudents() { //check if any unmatched student graduated
        Iterator<Integer> unmatchedIterator = unmatchedStudents.keySet().iterator();
        while(unmatchedIterator.hasNext()) { //iterate over unmatched students
            Integer key = unmatchedIterator.next();
            Student student = unmatchedStudents.get(key);

            if (student.increment()){ //if student has graduated
                unmatchedIterator.remove(); //gets removed from the waiting list
                unluckyStudents.add(student); //gets added to unlucky list
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