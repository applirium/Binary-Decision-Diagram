import java.io.PrintStream;

public class BinaryTreePrinter {

    private Node tree;

    public BinaryTreePrinter(Node tree) {
        this.tree = tree;
    }

    private String traversePreOrder(Node root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getBfuction());

        String pointerRight = "└──";
        String pointerLeft = (root.getRightchild() != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.getLeftchild(), root.getRightchild() != null);
        traverseNodes(sb, "", pointerRight, root.getRightchild(), false);

        return sb.toString();
    }

    private void traverseNodes(StringBuilder sb, String padding, String pointer, Node node,
                               boolean hasRightSibling) {

        if (node != null) {

            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getBfuction());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.getRightchild() != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeftchild(), node.getRightchild() != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.getRightchild(), false);

        }

    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(tree));
    }



}