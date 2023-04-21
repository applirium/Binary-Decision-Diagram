import java.util.*;

public class Test {

    public static void main(String[] args){
        int limit = 2;
        Test test = new Test();

        String order = test.orderGen(limit);
        String bfunction = test.bfunctionGen(limit);
        BDD robdd = test.create(bfunction,order);

        System.out.println(1);
    }
    public BDD create(String bfunction, String order)
    {
        BDD root = new BDD(bfunction,order);
        HashMap<String,Node> hashTable = new HashMap<>();

        hashTable.put(root.getRoot().getBfuction(),root.getRoot());
        root.setRoot(nodeRecursion(root.getRoot(),order,0,hashTable));
        root.setNumberOfNodes(hashTable.size());
        return root;
    }
    public Node nodeRecursion(Node root,String order,int i,HashMap<String,Node> hashTable){
        Node newNode;
        hashTable.putIfAbsent(root.getBfuction(),root);

        if(root.getLeftchild() == null && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")) && i < order.length())
        {
            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'N')));

            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setLeftchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        if(root.getRightchild() == null && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")) && i < order.length())
        {
            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'P')));

            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setRightchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        return root;
    }

    public BDD createWithBestOrder(String bfunction)
    {
        return null;
    }
    public char use(BDD bdd,String input)
    {
        return 0;
    }
    public String decomposition(String bfunction, char order,char choice) {
        ArrayList<String> functionList = new ArrayList<>(Arrays.asList(bfunction.split("[+]",0)));
        ArrayList<String> finalList = new ArrayList<>(functionList);

        for(String bool: functionList)
        {
            switch (choice) {
                case 'N' -> {
                    if (bool.contains("!" + order))
                    {
                        finalList.set(finalList.indexOf(bool), bool.replace("!" + order, ""));
                        if (bool.replace("!" + order, "").equals(""))
                            return "1";
                    }
                    else if (bool.contains(String.valueOf(order)))
                    {
                        finalList.remove(bool);
                        if (finalList.isEmpty())
                            return "0";
                    }
                }
                case 'P' -> {
                    if (bool.contains("!" + order))
                    {
                        finalList.remove(bool);
                        if (finalList.isEmpty())
                            return "0";
                    }
                    else if (bool.contains(String.valueOf(order)))
                    {
                        finalList.set(finalList.indexOf(bool), bool.replace(String.valueOf(order), ""));
                        if (bool.replace(String.valueOf(order), "").equals(""))
                            return "1";
                    }
                }
            }
        }
        return booleanSimplifing(String.join("+",finalList));
    }
    public String bfunctionGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        ArrayList<Character> list2 = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        //int max = random.nextInt(1,(int)Math.pow(2,limit));
        int max = limit;

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

            int max2 = (int)(limit*random.nextGaussian(0,0.20));
            max2 = max2 < 0 ? -max2 : max2;

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
        return "!A+A";
    }
    public String orderGen(int limit) {
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
        return "AB";
    }
    public String booleanSimplifing(String bfunction){
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
