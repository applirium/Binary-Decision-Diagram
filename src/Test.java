import java.io.*;
import java.util.*;

public class Test {
    private static int limit;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Test test = new Test();
        String function;

        do
        {
            System.out.println("\nIncrement = testing with repeated random order and function with increasing number of variables | best = create best order with comparison testing | same = testing with repeated random order and function | end = end of testing");
            System.out.println("Best = create best order with comparison testing | same = testing with repeated random order and function | end = end of testing");
            System.out.println("Same = testing with repeated random order and function | end = end of testing");
            System.out.println("End = end of testing\n");

            function = scanner.nextLine();
            switch (function) {
                case "increment" -> test.incrementTesting(scanner);
                case "best" -> test.bestOrderTesting(scanner);
                case "same" -> test.sameTesting(scanner);
                case "end" -> {}
                default -> System.out.println("Wrong input");
            }
        }
        while(!function.equals("end"));

    }

    public void sameTesting(Scanner scanner){
        System.out.println("Choose lenght of performing same testing");
        int precission = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose number of variables");
        setLimit(Integer.parseInt(scanner.nextLine()));
        long startTime,endTime;
        double reduction,time;

        try {
            FileWriter fileWriter = new FileWriter("results.txt");
            fileWriter.write("Variables | Unique Nodes | Time\n");
            for(int i = 0; i < precission; i++)
            {
                startTime =  System.nanoTime();
                BDD robdd = create(bfunctionGen(limit),orderGen(limit));
                endTime = System.nanoTime();

                reduction = 100 - (robdd.getNumberOfNodes() / (Math.pow(2,limit)-1));
                time = (double)(endTime-startTime)/1000000000;

                fileWriter.write(limit+" "+robdd.getNumberOfNodes()+" "+time+"\n");
                System.out.println("Duration of 2^"+(limit+1)+"-1 original nodes, BDD reduced into "+robdd.getNumberOfNodes() + " unique nodes with approximately "+ reduction+" % reduction, duration of creating: "+ time + " seconds.");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void incrementTesting(Scanner scanner){
        System.out.println("Choose maximum of varibles");
        int maxVariable = Integer.parseInt(scanner.nextLine());

        long startTime,endTime;
        double reduction,time;
        BDD robdd;

        try {
            FileWriter fileWriter = new FileWriter("results.txt");
            fileWriter.write("Variables | Unique Nodes | Time\n");
            for(int i = 1; i <= maxVariable; i++)
            {
                startTime =  System.nanoTime();
                robdd = create(bfunctionGen(i),orderGen(i));
                endTime = System.nanoTime();

                reduction = 100 - (robdd.getNumberOfNodes() / (Math.pow(2,i)-1));
                time = (double)(endTime-startTime)/1000000000;

                fileWriter.write(i+" "+robdd.getNumberOfNodes()+" "+time+"\n");
                System.out.println("Duration of 2^"+(i+1)+"-1 original nodes, BDD reduced into "+robdd.getNumberOfNodes() + " unique nodes with approximately "+ reduction+" % reduction, duration of creating: "+ time + " seconds.");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void bestOrderTesting(Scanner scanner){
        System.out.println("Choose number of variables");
        setLimit(Integer.parseInt(scanner.nextLine()));

        String bfunction = bfunctionGen(limit);
        BDD optimalizedBDD = createWithBestOrder(bfunction);
        BDD randomBDD = create(bfunction,orderGen(limit));

        double reduction1 = 100 - (optimalizedBDD.getNumberOfNodes() / (Math.pow(2,limit)-1));
        double reduction2 = 100 - (randomBDD.getNumberOfNodes() / (Math.pow(2,limit)-1));

        System.out.println("Optimalized BDD has "+optimalizedBDD.getNumberOfNodes()+ " unique nodes with order "+optimalizedBDD.getOrder() +", "+reduction1+ " % reduction");
        System.out.println("Random BDD has "+randomBDD.getNumberOfNodes() + " unique nodes with order "+randomBDD.getOrder() +", "+reduction2+" % reduction");
        System.out.println((randomBDD.getNumberOfNodes() - optimalizedBDD.getNumberOfNodes()) +" unique node difference " +(reduction1 - reduction2) +" % reduction difference");

    }
    public BDD create(String bfunction, String order) {
        BDD root = new BDD(bfunction,order);
        HashMap<String,Node> hashTable = new HashMap<>();

        hashTable.put(root.getRoot().getBfuction(),root.getRoot());
        root.setRoot(nodeRecursion(root.getRoot(),order,0,hashTable));
        root.setNumberOfNodes(hashTable.size());
        root.setOrder(order);

        return root;
    }
    private Node nodeRecursion(Node root,String order,int i,HashMap<String,Node> hashTable){
        Node newNode;

        if(root.getLeftchild() == null && i < order.length()  && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")))
        {
            while(!root.getBfuction().contains(String.valueOf(order.charAt(i))))
                i++;

            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'N')));
            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setLeftchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        if(root.getRightchild() == null && i < order.length()  && !(root.getBfuction().equals("0") || root.getBfuction().equals("1")))
        {
            while(!root.getBfuction().contains(String.valueOf(order.charAt(i))))
                i++;

            newNode = new Node(booleanSimplifing(decomposition(root.getBfuction(),order.charAt(i),'P')));
            hashTable.putIfAbsent(newNode.getBfuction(),newNode);
            root.setRightchild(nodeRecursion(hashTable.get(newNode.getBfuction()),order,i+1,hashTable));
        }

        return root;
    }
    public BDD createWithBestOrder(String bfunction) {
        BDD minimum = null, iteration;

        for(int i = 0; i < limit; i++)
        {
            iteration = create(bfunction,orderGen(limit));

            if(minimum == null || minimum.getNumberOfNodes() > iteration.getNumberOfNodes())
            {
                minimum = iteration;
            }
        }
        return minimum;
    }
    private String decomposition(String bfunction, char order,char choice) {
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
    private String bfunctionGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        ArrayList<Character> list2 = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        //int max = random.nextInt(1,(int)Math.pow(2,limit));

        for(int i = 0; i < limit; i++)
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

        return booleanSimplifing(builder.toString());
    }
    private String orderGen(int limit) {
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
    }
    private String inputGen(int limit) {
        ArrayList<Character> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < limit; i++)
        {
            if(random.nextBoolean())
                list.add('1');
            else
                list.add('0');
        }

        for (Character ch : list)
            builder.append(ch);

        return builder.toString();
    }
    private String booleanSimplifing(String bfunction){
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
    private static void setLimit(int limit) {
        Test.limit = limit;
    }
}
