import java.util.*;

public class Project {
    private static Scanner userScan = new Scanner(System.in);
    private static String nameInput = "";

    // the current map used for this route with no more than 26 Nodes
    private static NodeMap map = new NodeMap();

    // current tracks the current Node in the map as a String reference
    private static Node current = null;

    // containers for tracking the full sequence, the shortest path, shortest length
    private static ArrayList<String> pathSequence = new ArrayList<>();
    private static Stack<String> pathShortest = new Stack<>();
    private static int pathLength = 0;


    /* MAIN PROGRAM */
    public static void main(String[] args) {

        // Node data is read into map
        map.readMatrix();
        map.readList();

        // user input
        nameInput = readUserInput();
        System.out.println("User selected node " + nameInput + "\n");

        // traverses the map by continuously selecting next Node with algorithm 1
        System.out.println("Algorithm 1\n");
        // Add the selected Node to paths and start the current tracker
        initiateRoute();

        while (!current.getName().equals("Z")) {
            // get a Set of adjacent Nodes relative to current Node;
            Set<String> adjacents = current.getAdjacents().keySet();

            // select the next current node from adjacents based on algorithm 1
            Node selected = selectByDd(adjacents);
            System.out.println(selected + " is traversed");
            // add selected Node name to paths
            pathSequence.add(selected.getName());
            pathShortest.add(selected.getName());

            // if no valid paths remaining, backtrack and re-select a new Node
            if (!hasValidPaths(selected)) {
                backtrack();
                continue;
            };

            // get edge weight from current Node to selected Node and add to pathLength
            pathLength += current.getAdjacents().get(selected.getName());
            // update current node with the selected node
            current = selected;

        }
        // show path results
        System.out.println(getResults("Algorithm 1"));

        resetRoute();  // ------- all trackers cleared for next algorithm -----------
        System.out.println("-------------------------------------");

        // traverses the map by continuously selecting next Node with algorithm 2
        System.out.println("Algorithm 2:\n");
        // Add the selected Node to paths and start the current tracker
        initiateRoute();

        while (!current.getName().equals("Z")) {
            // get a Set of adjacent Nodes relative to current Node;
            Set<String> adjacents = current.getAdjacents().keySet();

            // select the next current node from adjacents based on algorithm 2
            Node selected = selectByWeightAndDd(adjacents);
            System.out.println(selected + " is traversed");
            // add selected Node name to paths
            pathSequence.add(selected.getName());
            pathShortest.add(selected.getName());

            // if no valid paths remaining, backtrack and re-select a new Node
            if (!hasValidPaths(selected)) {
                backtrack();
                continue;
            };

            // get edge weight from current Node to selected Node and add to pathLength
            pathLength += current.getAdjacents().get(selected.getName());
            // update current node with the selected node
            current = selected;

        }
        // show path results
        System.out.println(getResults("Algorithm 2"));

    }
    /* END MAIN PROGRAM */


    /* initiates a route by starting trackers */
    private static void initiateRoute() {
        // Start current as the user selected Node and add it to paths
        current = map.getNode(nameInput);
        pathSequence.add(current.getName());
        pathShortest.add(current.getName());
        System.out.println("Walking from " + current);
    }

    /* ALGORITHM 1
    Input: adjacents is a Set of string of Node names adjacent to current Node
    Output: returns the Node selected from adjacents based on the smallest dd value
    */
    private static Node selectByDd(Set<String> adjacents) {
        // cleanse adjacent list of old nodes already traversed
        for (String oldNode : pathSequence) {
            adjacents.remove(oldNode);
        }

        // selected Node initialized as first Node on the list
        var iter = adjacents.iterator();
        var selected = map.getNode(iter.next());

        // selection made by comparing next Node dd with that of currently selected Node
        while (iter.hasNext()) {
            Node next = map.getNode(iter.next());

            // next node selected only if it has smaller dd
            if (next.getDd() < selected.getDd())
                selected = next;
        }
        return selected;
    }

    /* ALGORITHM 2
    Input: adjacents is a Set of string of Node names adjacent to current Node
    Output: returns the Node selected from adjacents based on smallest dd value + edge weight
    */
    private static Node selectByWeightAndDd(Set<String> adjacents) {
        // cleanse adjacent list of old nodes already traversed
        for (String oldNode : pathSequence) {
            adjacents.remove(oldNode);
        }

        // selected Node initialized as first Node on the list
        var iter = adjacents.iterator();
        var selected = map.getNode(iter.next());

        // each next node's dd is compared with dd of currently selected node
        while (iter.hasNext()) {
            Node next = map.getNode(iter.next());

            // weights retrieved for the selected Node and next Node relative to current
            var weightSelected = current.getAdjacents().get(selected.getName());
            var weightNext = current.getAdjacents().get(next.getName());

            // next node selected only if it has smaller weight + dd
            if (next.getDd() + weightNext < selected.getDd() + weightSelected)
                selected = next;
        }
        return selected;
    }

    /* checks if there are valid paths remaining from the given Node
    Input: a Node object selected by one of the selection algorithms
    Output: true or false
    */
    private static Boolean hasValidPaths(Node thisNode) {
        var adjacents = thisNode.getAdjacents().keySet();

        // if an adjacent Node is not in pathSequence history, then there is a valid path
        for (String nodeName : adjacents) {
            if (!pathSequence.contains(nodeName))
                return true;
        }
        // otherwise dead end
        return false;
    }

    /* backtracks to previous Node and pops the current one off the shortest path */
    private static void backtrack() {
        // set current to previous Node
        var prevNode = map.getNode(pathSequence.get(pathSequence.size() - 2));
        current = prevNode;
        System.out.println("Path has hit a dead end. Walking back to " + prevNode);

        // remove current from shortest path and add previous to total path
        pathShortest.pop();
        pathSequence.add(prevNode.getName());
    }

    /* reset the route by clearing the map and all trackers */
    private static void resetRoute() {
        // map data is reset
        map = new NodeMap();
        map.readMatrix();
        map.readList();
        // paths data structures reset
        pathSequence = new ArrayList<>();
        pathShortest = new Stack<>();
        pathLength = 0;
    }

    /* returns the path results of an algorithm on the console
    Input: a String of the algorithm name used to obtain the current results
    Output: a String that contains the paths results concatenated by a StringBuilder
    */
    private static String getResults(String algName) {
        var resultString = new StringBuilder(
                "\n___Displaying Results for " + algName + "___\n"
        );

        // every node traversed in path
        resultString.append("Sequence of all nodes: ");
        for (int i = 0; i < pathSequence.size() - 1; i++) {
            resultString.append(pathSequence.get(i)).append(" -> ");
        }
        resultString.append(pathSequence.get(pathSequence.size() - 1)).append("\n\n");

        // the shortest path found by algorithm
        resultString.append("Shortest path: ");
        for (int i = 0; i < pathShortest.size() - 1; i++) {
            resultString.append(pathShortest.get(i)).append(" -> ");
        }
        resultString.append(pathShortest.get(pathShortest.size() - 1)).append("\n\n");

        // total length of shortest path
        resultString.append("Shortest path length: ").append(pathLength).append("\n\n");

        return resultString.toString();
    }

    /* reads Node name from user input and validate against existing Nodes in map
     * the validity of user input depends on what nodes are available in input files */
    private static String readUserInput() {
        String name;  // will be returned after validation

        while (true) {
            System.out.println("Enter a letter denoting the name of the starting Node: ");
            var input = userScan.nextLine();

            // validate input to be only one character
            if (input.length() > 1) {
                System.out.println("You've entered too many characters!");
                continue;
            }
            // validate input to be alphabetic character (convert lowercase for user)
            var inputChar = input.toUpperCase().charAt(0);
            if (inputChar < 65 || inputChar > 90) {
                System.out.println("Please enter a valid letter between A-Z!");
                continue;
            }
            // validate node to be available on the map
            var node = String.valueOf(inputChar);
            if (map.getNode(node) == null) {
                System.out.println("The selected node does not exist on the map!");
                continue;
            }
            // all validation tests passed
            name = node;
            break;
        }
        return name;
    }

    /* FOR DEBUGGING ONLY - show dd of each adjacent Node relative to thisNode */
    private static void showAdjacents(Set<String> adjacents, Node thisNode) {
        System.out.println("current node: " + thisNode + "\nadjacent nodes: ");
        for (String name : adjacents) {
            var dd = map.getNode(name).getDd();
            System.out.println(name + ": dd(" + name + ")" + " = " + dd);
        }
    }

}
