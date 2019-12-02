import java.util.HashMap;

public class Node {
    private String name;
    // each adjacent Node has an int weight value
    private HashMap<String, Integer> adjacents;
    // distance from this Node to Node Z
    private int dd;

    // constructors
    public Node(String name, HashMap<String, Integer> adjacents, int dd) {
        this.name = name;
        this.adjacents = adjacents;
        this.dd = dd;
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
    public int getDd() {
        return dd;
    }

    // setters
    public void setDd(int dd) {
        this.dd = dd;
    }
    public void addAdjacent(String name, int weight) {
        adjacents.put(name, weight);
    }

    @Override
    public String toString() {
        return "Node{" + name + '}';
    }

}
