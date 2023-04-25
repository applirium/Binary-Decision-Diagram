public class BDD {
    private Node root;
    private int numberOfNodes;
    private String order;
    BDD(String bfunction,String order) {
        this.numberOfNodes = 1;
        this.order = order;
        this.root = new Node(bfunction);
    }
    public String use(String input) {
        Node node = this.root;
        int i = 0;

        for(String decision: input.split(""))
        {
            if((!decision.equals("0") && !decision.equals("1")) || input.length() > this.order.length())
                return "-1";
        }

        while(!node.getBfuction().equals("0") && !node.getBfuction().equals("1"))
        {
            if(input.charAt(i) == '0')
            {
                while(!node.getBfuction().contains(String.valueOf(this.order.charAt(i))))
                    i++;

                node = node.getLeftchild();
            }
            else
            {
                while(!node.getBfuction().contains(String.valueOf(this.order.charAt(i))))
                    i++;

                node = node.getRightchild();
            }
            i++;
        }
        return node.getBfuction();
    }
    public int getNumberOfNodes() {
        return numberOfNodes;
    }
    public Node getRoot() {
        return root;
    }
    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
    public void setRoot(Node root) {
        this.root = root;
    }
    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
}