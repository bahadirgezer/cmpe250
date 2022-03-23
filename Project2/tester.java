import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class tester {

    public static void main(String[] args) throws FileNotFoundException, IOException{
        String testFolderName = "testcases";
        String projectDir = System.getProperty("user.dir");

        File compilePath =  new File(projectDir);
        File runPath = new File(projectDir+"\\bin");
        try {
            Process pp = Runtime.getRuntime().exec("javac src\\*.java -d bin --release 16", null, compilePath);
            pp.waitFor();

            int loopp = 1;
            long start = System.currentTimeMillis();
            while(loopp != 1000) {
                String inputPath = String.format(projectDir+"\\"+testFolderName+"\\input_%d.txt", loopp);
                String myOutputPath = String.format(projectDir+"\\"+testFolderName+"\\myoutput_%d.txt", loopp); 
                Process p = Runtime.getRuntime().exec("java project2main "+inputPath+" "+myOutputPath, null, runPath);
                p.waitFor();
                System.out.print(loopp+" ");
                //System.out.print(String.format("input%d.txt myoutput%d.txt ", loopp, loopp));
        
                loopp += 1;
            }
           long end = System.currentTimeMillis();
           System.out.println();
           System.out.println(String.format("Total test time: "+(end-start)+" ms"));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        int looppp = 1;
        Boolean passedAll = true;
        int failed = 0;
        while(looppp != 1000) {
            String myOutputPath = String.format(projectDir+"\\"+testFolderName+"\\myoutput_%d.txt", looppp); 
            String outputPath = String.format(projectDir+"\\"+testFolderName+"\\output_%d.txt", looppp); 
           
            if(!compareByDoubleValues(myOutputPath, outputPath)){
                passedAll = false;
                System.out.println(String.format("test %d failed", looppp));
                failed += 1;
            }
            looppp += 1;
        }
        if (passedAll) {
            System.out.println("passed every test");
        } else {
            System.out.println("total failed: "+ failed);
        }
    }

    static String errorDifference(int val1, int val2) {
        return "MyOutput Integer: "+val1+" ,TestOutputInteger: " + val2;

    }

    static String errorDifference(Double val1, Double val2) {
        return "MyOutput Double: " + val1 + " ,TestOutput Double: " + val2 +"\nDifference: " + Math.abs(val1-val2);
    }

    static boolean compareByDoubleValues(String file1, String file2) throws FileNotFoundException, IOException {
        BufferedReader r1 = new BufferedReader(new FileReader(file1));
        BufferedReader r2 = new BufferedReader(new FileReader(file2));
        String line1, line2;
        Double epsilon = 0.0;

        for (int i = 0; i < 3; i++) {
            line1 = r1.readLine();
            line2 = r2.readLine();
            if (Integer.parseInt(line1) != Integer.parseInt(line2)) {
                r1.close();
                r2.close();
                System.out.println(errorDifference(Integer.parseInt(line1), Integer.parseInt(line2)));
                return false;
            }
        }

        for (int i = 0; i < 7; i++) {
            line1 = r1.readLine();
            line2 = r2.readLine();
            if (!(Math.abs(Double.parseDouble(line1) - Double.parseDouble(line2)) <= epsilon)) {
                r1.close();
                r2.close();
                System.out.println(errorDifference(Double.parseDouble(line1), Double.parseDouble(line2)));
                return false;
            }
        }

        for (int i = 0; i < 2; i++) {
            String[] line1Tokens = r1.readLine().split(" ");
            String[] line2Tokens = r2.readLine().split(" ");
            
            if (Integer.parseInt(line1Tokens[0]) != Integer.parseInt(line2Tokens[0])) {
                r1.close();
                r2.close();
                System.out.println(errorDifference(Integer.parseInt(line1Tokens[0]), Integer.parseInt(line2Tokens[0])));
                return false;
            }
            if (!(Math.abs(Double.parseDouble(line1Tokens[1]) - Double.parseDouble(line2Tokens[1])) <= epsilon)) {
                r1.close();
                r2.close();
                System.out.println(errorDifference(Double.parseDouble(line1Tokens[1]), Double.parseDouble(line2Tokens[1])));
                return false;
            }
            
        }

        for (int i = 0; i < 2; i++) {
            line1 = r1.readLine();
            line2 = r2.readLine();
            if (Integer.parseInt(line1) != Integer.parseInt(line2)) {
                r1.close();
                r2.close();
                System.out.println(errorDifference(Integer.parseInt(line1), Integer.parseInt(line2)));
                return false;
            }
        }

        line1 = r1.readLine();
        line2 = r2.readLine();
        if (!(Math.abs(Double.parseDouble(line1) - Double.parseDouble(line2)) <= epsilon)) {
            r1.close();
            r2.close();
            System.out.println(errorDifference(Double.parseDouble(line1), Double.parseDouble(line2)));
            return false;
        }
        r1.close();
        r2.close();
        return true;
    }
    
    static boolean compareTextFiles ( String file1, String file2) throws FileNotFoundException, IOException{
        BufferedReader r1 = new BufferedReader(new FileReader(file1));
        BufferedReader r2 = new BufferedReader(new FileReader(file2));
        int c1=0, c2=0;
        while(true){
            c1 = r1.read();
            c2 = r2.read();
            if(c1==-1 && c2==-1) {
                r1.close();
                r2.close();
                return true;
            } else if(c1==-1 || c2==-1 || c1!=c2){
                r1.close();
                r2.close();
                return false;
            }
        }
    }  

    public static long filesCompareByLine(Path path1, Path path2) throws IOException {
	    try (BufferedReader bf1 = Files.newBufferedReader(path1);
	         BufferedReader bf2 = Files.newBufferedReader(path2)) {
	        
	        long lineNumber = 1;
	        String line1 = "", line2 = "";
	        while ((line1 = bf1.readLine()) != null) {
	            line2 = bf2.readLine();
	            if (line2 == null || !line1.equals(line2)) {
	                return lineNumber;
	            }
	            lineNumber++;
	        }
	        if (bf2.readLine() == null) {
	            return -1;
	        }
	        else {
	            return lineNumber;
	        }
	    }
	}
}