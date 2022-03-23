import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;



public class project1main { //javac Project1/src/*.java -d Project1/bin -target 17
    public static void main(String[] args) throws FileNotFoundException{
        
        Scanner input = new Scanner(new File(args[0]));
        PrintStream output = new PrintStream(new File(args[1]));

        DormSystem system = new DormSystem(); //DormSystem variable which will process every action, also store every house and student

        while (input.hasNextLine()) {
            Scanner line = new Scanner(input.nextLine());
            String charr = line.next();
            System.out.println(charr);


            if (charr.equals("h")) { //takes in house inputs
                int id = line.nextInt();
                int occupancy = line.nextInt();
                double rating = line.nextDouble();
                system.addHouse(id, occupancy, rating); //important bit
                System.out.println("in h");
                system.systemCheck();

            } else if (charr.equals("s")) { //takes in student inputs
                int id = line.nextInt();
                String name = line.next();
                int semester = line.nextInt();
                double rating = line.nextDouble();
                system.addStudent(id, name, semester, rating); //important bit
                System.out.println("in s");
                system.systemCheck();

            }
        }

        while (system.resume()) { // this is where the magic happens.    
            system.allocate(); //allocates every house to a student
            system.clear(); //increments semester and clears houses and students if it's time
        }

        output.print(system.output()); //creates output and returns it as a String

        output.close(); //cleanliness
        input.close();
    }
}