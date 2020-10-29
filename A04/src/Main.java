public class Main {
    public static void main(String[] args) {
        Subnet subnet = new Subnet("10.5.3.69");
        subnet.divide();
        System.out.println(subnet);
    }
}
