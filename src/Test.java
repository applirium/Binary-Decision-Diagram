import java.util.*;

public class Test {

    public static void main(String[] args) {
        int limit = 5;
        String order = orderGen(limit);
        String bfunction = bfunctionGen(limit);
        BDD robdd = create(bfunction,order);
        String bfun2 = decomposition(bfunction,"A",'N');
        System.out.println(1);
    }

    public static BDD create(String bfunction, String order)
    {
        BDD root = new BDD(bfunction,order);
        root.setRoot(nodeRecursion(root.getRoot(),root,order));
        return root;
    }
    public static Node nodeRecursion(Node root,BDD diagram,String order)
    {
        diagram.setNumberOfNodes(diagram.getNumberOfNodes() + 1);
        if(root.getLeftchild() == null && (!root.getBfuction().equals("0") || !root.getBfuction().equals("1")))
        {

        }
        else
        {

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
        return String.join("+",finalList);
    }
    public static String bfunctionGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        ArrayList<Character> list2 = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        int max = random.nextInt(2,limit+1);

        for(int i = 0; i < max ; i++)
        {
            if(i != 0)
                list.add('+');
            int max2 = random.nextInt(1,limit+1);
            for(int j = 0; j < max2; j++)
            {
                if(random.nextInt(2) == 0)
                    list.add('!');

                list.add((char)(65 + j));
            }
        }

        for (Character ch : list)
            builder.append(ch);

        return booleanSimplifing(builder.toString());
        //return "AB+AC+BC";
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

        return builder.toString();
        //return "ABC";
    }

    public static String booleanSimplifing(String bfunction){
        if(bfunction.contains("1"))
            return "1";

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
