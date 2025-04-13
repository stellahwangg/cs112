package prereqchecker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }

	// WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        HashMap<String, ArrayList<String>> map = new HashMap< String, ArrayList<String>>();

        String a = StdIn.readLine();
        //System.out.println(a);
        int aInputs = Integer.parseInt(a);
        for(int i = 0; i < aInputs; i++){
            String courseID = StdIn.readLine();
            ArrayList<String> list = new ArrayList<String>();
            map.put (courseID, list);
        }

        String b = StdIn.readLine();
        //System.out.println(b);
        int bInputs = Integer.parseInt(b);
        for(int i = 0; i < bInputs; i++){
            String course = StdIn.readString();
            String prereq = StdIn.readString();
        //System.out.println(prereq);
         

            map.get(course).add(prereq);
        }

        for(String x: map.keySet()){
            String temp = x;
            StdOut.println(x + " " + map.get(x));
            for(int i = 0; i < map.get(x).size(); i++){
                temp = temp + " " + map.get(x).get(i);
            }

            StdOut.println(temp);
        }
    
    }
}
