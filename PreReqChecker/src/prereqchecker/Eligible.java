package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): ber of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some ber of lines, each with one course ID
 */
public class Eligible {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

	// WRITE YOUR CODE HERE
    StdIn.setFile(args[0]);
    HashMap< String, ArrayList<String> > map = new HashMap< String, ArrayList<String> >();
    String a = StdIn.readLine();
    int numInputsA = Integer.parseInt(a);

    for (int i = 0; i < numInputsA; i++) {
        String id = StdIn.readLine();
        ArrayList<String> empty = new ArrayList<String>();
        map.put(id, empty);
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
    String c = StdIn.readLine();
    int numInputsC = Integer.parseInt(c);
    HashSet<String> taken = new HashSet<String>();

    HashMap<String, Boolean> map2 = new HashMap<String, Boolean>();

    for (int i = 0; i < numInputsC; i++) {
        String course = StdIn.readLine();
        taken.add(course);

        for (String x: map.keySet()) {
            map2.put(  x, false);
        }
        DepthFirstSearch(course, taken , map, map2);
    }

    for (String x: map.keySet()) {
        for (String i: map.get(x)) {
                if (!taken.contains(i) )  break;
                if (taken.contains(i) && map.get(x).indexOf(i) == map.get(x).size() -1 && !taken.contains(x)) StdOut.println(x);
            }
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
