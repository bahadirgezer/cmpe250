import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class project1main { //javac Project1/src/*.java -d Project1/bin -target 17
    public static void main(String[] args) throws FileNotFoundException{
    
        Scanner input = new Scanner(new File(args[0]));
        PrintStream output = new PrintStream(new File(args[1]));

        DormSystem system = new DormSystem(); //DormSystem variable which will process every action, also store every house and student object

        while (input.hasNextLine()) { // while loop for taking inputs
            Scanner line = new Scanner(input.nextLine());
            String charr = line.next();

            if (charr.equals("h")) { //takes in house inputs
                int id = line.nextInt();
                int occupancy = line.nextInt();
                double rating = line.nextDouble();
                system.addHouse(id, occupancy, rating); //creates house object
           
            } else if (charr.equals("s")) { //takes in student inputs
                int id = line.nextInt();
                String name = line.next();
                int semester = line.nextInt();
                double rating = line.nextDouble();
                system.addStudent(id, name, semester, rating); //creates student object

            }
        }

        while (system.resume()) { // main while loop, actually runs the program   
            system.allocate(); //allocates every available house to every available student
            system.clear(); //increments semester after which it clears houses and students if they're done
        }

        output.print(system.output()); //creates output and returns it as a String to the Printstream object

        output.close(); //good practice
        input.close();
    }
}