import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NodeMap {
    /* path to file directory, change if necessary
    * txt files are outside the src directory, but inside project directory */
    private static final String GRAPHPATH = new File("").getAbsolutePath()
            .concat("/graph_input.txt");
    private static final String LISTPATH = new File("").getAbsolutePath()
            .concat("/direct_distance.txt");

    // containers for storing all nodes and their names
    private HashMap<String, Node> map;
    private String[] names;

    // constructors with initial capacity
    public NodeMap(int initialCap) { this.map = new HashMap<>(initialCap); }
    public NodeMap() { this(26); }

    // Nodes are added and retrieved with their name as key
    public void addNode(Node node) { map.put(node.getName(), node); }
    public Node getNode(String name) { return map.get(name); }

    // getter for the array of node names
    public String[] getNames() {
        return names;
    }

    /* reads matrix of Nodes from input file, and generates Nodes in map */
    public void readMatrix() {
        try {
            // input file defined with path to graph_input.txt
            var inputFile = new File(GRAPHPATH);
            var scan = new Scanner(inputFile);

            // top row scanned into an array of letters to be used as names for each Node
            names = scan.nextLine().replaceAll("\\s", "").split("");
            // each name used to create new Node in map
            for (String name : names) {
                addNode(new Node(name));
            }

            // remaining rows scanned to add adjacents to each Node in the map
            while (scan.hasNextLine()) {
                String[] rowScan = scan.nextLine().split(" ");
                addAdjacentNodes(rowScan);  // processes raw scan data for adjacents
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found \n");
            e.printStackTrace();
        }

    }

    /* reads list of direct distances from input file and set dd of each Node */
    public void readList() {
        try {
            // inputFile defined with path to direct_distance.txt
            var inputFile = new File(LISTPATH);
            var scan = new Scanner(inputFile);

            // each scan is a [name, distance] pair
            while (scan.hasNextLine()) {
                String[] rowScan = scan.nextLine().split(" ");
                var name = rowScan[0];
                var currentNode = map.get(name);

                // if name exists as a current node in the map, set its dd value
                if (currentNode != null) {
                    currentNode.setDd(Integer.parseInt(rowScan[1]));
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found \n");
            e.printStackTrace();
        }
    }

    /* adds adjacent Nodes to an existing Node in map
    * First parses out all the integers. Then add them as weights if > 0 */
    private void addAdjacentNodes(String[] rowScan) {
        // temp list for values of each row
        var rowVals = new ArrayList<Integer>(names.length);
        // nodeName is the first letter of each row
        String nodeName = rowScan[0];

        // values in a row only added to rowVals list if it's an integer
        for (String s : rowScan) {
            try {
                rowVals.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // not a number -> letters and spaces ditched
            }
        }

        // integers from rowVals are only used to create adjacent Nodes if > 0
        for (int i = 0; i < rowVals.size(); i++) {
            var weight = rowVals.get(i);
            if (weight > 0) {  // if value is a real weight,
                // then it's added to current node adjacents with nodeName as key
                var currentNode = map.get(names[i]);
                currentNode.addAdjacent(nodeName, weight);
            }
        }
        rowVals.clear(); // clear the row values for next row
    }

    /* FOR DEBUGGING ONLY - shows all Nodes and their properties in the map */
    public void displayNodes () {
        for (Map.Entry<String, Node> nodeEntry : map.entrySet()) {
            var node = nodeEntry.getValue();
            System.out.println("node: " + node.getName() +
                    " | adjacents: " + node.getAdjacents() +
                    " | direct distance: " + node.getDd()
            );
        }
    }

}
