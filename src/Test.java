public class Test {

    public static void main(String[] args) {

        BDD robdd = create("AB+AC+BC","012");
        System.out.println(1);
    }

    public static BDD create(String bfunction, String order)
    {
        BDD bdd = new BDD(bfunction,order);
        return bdd;
    }

    public static BDD createWithBestOrder(String bfunction)
    {
        return null;
    }
    public static char use(BDD bdd,String order)
    {
        return 0;
    }


}
