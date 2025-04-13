package prereqchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): ber of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): ber of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
	// WRITE YOUR CODE HERE
    StdIn.setFile(args[0]);


    HashMap< String, ArrayList<String> > map = new HashMap< String, ArrayList<String> >();
    String a = StdIn.readLine();
    int numInputsA = Integer.parseInt(a);

    for (int i = 0; i < numInputsA; i++) {
        String courseID = StdIn.readLine();
        ArrayList<String> empty = new ArrayList<String>();
        map.put(courseID, empty);
    }


    String b = StdIn.readLine();
    int numInputsB = Integer.parseInt(b);
    
    for (int i = 0; i < numInputsB; i++) {
        String course = StdIn.readString();
        String preReq = StdIn.readString();

        map.get(course).add(preReq);

    }

    StdIn.setFile(args[1]);
    StdOut.setFile(args[2]);

    String course1 = StdIn.readLine();
    String course2 = StdIn.readLine();


    HashSet<String> allPreReqs = new HashSet<String>();

    HashMap<String, Boolean> map2 = new HashMap<String, Boolean>();
    for (String x: map.keySet()) {
        map2.put(x, false);
    }
    DepthFirstSearch(course2, allPreReqs, map, map2);

    if (allPreReqs.contains(course1)) {
        StdOut.println("NO");
    }
    else {
        StdOut.println("YES");
    }
    
    }

    public static void DepthFirstSearch(String course, HashSet<String> prereqs, HashMap<String, ArrayList<String> > map, HashMap<String, Boolean> map2){
        if(map.get(course).size() == 0){
            return;
        }
        for(String i : map.get(course)){
            if(map2.get(i) == false){
                prereqs.add(i);
            }

            for(String k: map.keySet()){
                map2.put(k, false);
            }
            DepthFirstSearch(i, prereqs, map, map2);
        }

        map2.replace(course, true);

    }
}
