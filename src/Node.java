import java.util.HashMap;

public class Node {
    private String name;
    private HashMap<String, Integer> adjacents; // each adjacent Node has an int weight value
    private int dToZ;

    // constructors
    public Node(String name, HashMap<String, Integer> adjacents, int dToZ) {
        this.name = name;
        this.adjacents = adjacents;
        this.dToZ = dToZ;
    }
    public Node(String name) {
        this(name, new HashMap<>(), 0);
    }

    // getters
    public String getName() {
        return name;
    }
    public HashMap<String, Integer> getAdjacents() {
        return adjacents;
    }
    public int getdToZ() {
        return dToZ;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setdToZ(int dToZ) {
        this.dToZ = dToZ;
    }

    public void addAdjacent(String name, int weight) {
        adjacents.put(name, weight);
    }

    @Override
    public String toString() {
        return "Node{" + name + '}';
    }
}
