import java.util.*;

public class Test {

    public static void main(String[] args) {
        int limit = 3;
        String order = orderGen(limit);
        String bfunction = bfunctionGen(limit);

        BDD robdd = create(bfunction,order);
        System.out.println(1);
    }

    public static BDD create(String bfunction, String order)
    {
        BDD root = new BDD(bfunction,order);
        root.setRoot(nodeRecursion(root.getRoot(),root,order,0));
        return root;
    }
    public static Node nodeRecursion(Node root,BDD diagram,String order,int i)
    {
        Node newNode;
        diagram.setNumberOfNodes(diagram.getNumberOfNodes() + 1);

        if(root.getLeftchild() == null && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")) && i < order.length())
        {
            newNode = new Node(decomposition(root.getBfuction(),order.substring(i,i+1),'N'));
            root.setLeftchild(nodeRecursion(newNode,diagram,order,i+1));
        }

        if(root.getRightchild() == null && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")) && i < order.length())
        {
            newNode = new Node(decomposition(root.getBfuction(),order.substring(i,i+1),'P'));
            root.setRightchild(nodeRecursion(newNode,diagram,order,i+1));
        }
        return root;
    }

    public static BDD createWithBestOrder(String bfunction)
    {
        return null;
    }
    public static char use(BDD bdd,String input)
    {
        return 0;
    }

    public static String decomposition(String bfunction, String order,char choice) {
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(bfunction.split("[+]",0)));
        ArrayList<String> finalList = new ArrayList<>(functionList);

        for(String bool: functionList)
        {
            switch (choice) {
                case 'N' -> {
                    if (bool.contains("!" + order))
                        finalList.set(finalList.indexOf(bool), bool.replace("!" + order, ""));

                    else if (bool.contains(order))
                        finalList.remove(bool);

                    finalList.remove("");

                    if (finalList.isEmpty())
                        return "0";

                }
                case 'P' -> {
                    if (bool.contains("!" + order))
                        finalList.remove(bool);

                    else if (bool.contains(order))
                        finalList.set(finalList.indexOf(bool), bool.replace(order, ""));

                    if (finalList.contains(""))
                        return "1";
                }
            }
        }

        return booleanSimplifing(String.join("+",finalList));
    }
    public static String bfunctionGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        ArrayList<Character> list2 = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        int max = random.nextInt(1,(int)Math.pow(2,limit));

        for(int i = 0; i < max ; i++)
        {
            if(i != 0)
            {
                list.add('+');
                list2.clear();
            }

            for(int j = 0; j < limit; j++)
            {

                list2.add((char)(65 + j));
            }

            int max2 = random.nextInt(0,limit);

            for(int j = 0; j < max2; j++)
            {
                int max3 = random.nextInt(0,limit-j);
                list2.remove(max3);
            }

            for(char character: list2)
            {
                if(random.nextInt(2) == 0)
                    list.add('!');

                list.add(character);
            }

        }

        for (Character ch : list)
            builder.append(ch);

        //return booleanSimplifing(builder.toString());
        return "AB+AC+BC";
    }
    public static String orderGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < limit; i++)
        {
            list.add((char)(65 + i));
        }
        Collections.shuffle(list,new Random());

        for (Character ch : list)
            builder.append(ch);

        //return builder.toString();
        return "ABC";
    }
    public static String booleanSimplifing(String bfunction){
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(bfunction.split("[+]",0)));

        for(int i = 0; i < functionList.size(); i++){
            for(int j = i + 1; j < functionList.size(); j++){
                if(functionList.get(i).equals(functionList.get(j)) || functionList.get(j).contains("0") ){      //A + A || A + 0 -> A
                    functionList.remove(j);
                    break;
                }
            }
        }
        return String.join("+",functionList);
    }
}
