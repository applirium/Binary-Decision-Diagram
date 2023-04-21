public class BDD {
    private Node root;
    private int numberOfNodes;
    private int numberOfVariables;

    BDD(String bfunction,String order)
    {
        this.numberOfNodes = 1;
        this.numberOfVariables = order.length();
        this.root = new Node(bfunction);
    }
    public int getNumberOfNodes() {
        return numberOfNodes;
    }
    public int getNumberOfVariables() {
        return numberOfVariables;
    }
    public Node getRoot() {
        return root;
    }
    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }
    public void setRoot(Node root) {
        this.root = root;
    }
}