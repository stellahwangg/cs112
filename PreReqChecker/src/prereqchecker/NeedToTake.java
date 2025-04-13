package prereqchecker;

import java.util.*;

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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): ber of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some ber of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        StdIn.setFile(args[0]);
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
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
            String prereq = StdIn.readString();
            map.get(course).add(prereq);
        }

        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);

        String targetCourse = StdIn.readLine();
        String d = StdIn.readLine();
        int numInputsD = Integer.parseInt(d);
        HashSet<String> taken = new HashSet<String>();
        HashMap<String, Boolean> map2 = new HashMap<String, Boolean>();

        for (int i = 0; i < numInputsD; i++) {
            String course = StdIn.readLine();
            taken.add(course);
            for (String x : map.keySet()) {
                map2.put(x, false);
            }
            DepthFirstSearch(course, taken, map, map2);
        }

        HashMap<String, Boolean> map3 = new HashMap<String, Boolean>();

        HashSet<String> allNeed = new HashSet<String>();
        for (int i = 0; i < map.get(targetCourse).size(); i++) {
            String course = map.get(targetCourse).get(i);
            allNeed.add(course);
            for (String x : map.keySet()) {
                map3.put(x, false);
            }
            DepthFirstSearch(course, allNeed, map, map3);
        }

        for (String x : allNeed) {
            if (!taken.contains(x))
                StdOut.println(x);
        }

    }

    public static void DepthFirstSearch(String course, HashSet<String> prereqs, HashMap<String, ArrayList<String>> map,
            HashMap<String, Boolean> map2) {
        if (map.get(course).size() == 0) {
            return;
        }
        for (String i : map.get(course)) {
            if (map2.get(i) == false) {
                prereqs.add(i);
            }

            for (String k : map.keySet()) {
                map2.put(k, false);
            }
            DepthFirstSearch(i, prereqs, map, map2);
        }

        map2.replace(course, true);

    }
}
