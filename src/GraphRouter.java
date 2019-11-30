import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class GraphRouter {
    private static Scanner userScan = new Scanner(System.in);

    // the current map used for this route with no more than 26 Nodes
    private static NodeMap map = new NodeMap(26);

    // current tracks the current Node in the map as a String reference
    private static Node current = null;

    // containers for tracking the full sequence, the shortest path, shortest length
    private static ArrayList<String> pathSequence = new ArrayList<>();
    private static Stack<String> pathShortest = new Stack<>();
    private static int pathLength = 0;

    public static void main(String[] args) {

        // Node data is read into map
        map.readMatrix();
        map.readList();

        // user input
        String nameInput = readUserInput();
        System.out.println("User selected node " + nameInput);

        current = map.getNode(nameInput);
        pathSequence.add(current.getName());
        pathShortest.add(current.getName());

        while (!current.getName().equals("Z")) {
            // get a Set of adjacent Nodes to current Node;
            var adjacents = current.getAdjacents().keySet();

            // DEBUGGING ONLY - show dd of each adjacent Node
            System.out.println("current node: " + current);
            for (String name : adjacents) {
                var dd = map.getNode(name).getDd();
                System.out.println(name + ": dd(" + name + ")" + " = " + dd);
            }

            // select the next current node from adjacents based on algorithm 1
            var selected = selectByDd(adjacents);
            pathSequence.add(selected.getName());
            // get edge weight from current node to next selected and add to pathLength
            pathLength += current.getAdjacents().get(selected.getName());
            // update current node with the selected node
            current = selected;
            System.out.println(selected + " is selected\n");
        }

        // show path results
        System.out.println("\nPath taken: ");
        System.out.println(pathSequence.toString());
        System.out.println("Total length: " + pathLength);


    }

    /* returns the Node selection based on the shortest path of algorithm 1
    * only direct distance (dd) is considered */
    private static Node selectByDd(Set<String> adjacents) {

        // selected Node will be returned as the next Node in shortest path
        var iter = adjacents.iterator();
        var selected = map.getNode(iter.next());

        // each next node's dd is compared with dd of currently selected node
        while (iter.hasNext()) {
            Node next = map.getNode(iter.next());
            if (next.getDd() < selected.getDd())
                selected = next;
        }
        return selected;
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

    }
