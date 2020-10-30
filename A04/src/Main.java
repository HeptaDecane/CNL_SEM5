import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter IPv4: ");
        String ipv4 = scanner.nextLine();
        if(Subnet.isValidIpv4(ipv4)){
            Subnet subnet = new Subnet(ipv4);
            System.out.println("IPv4 Class: "+subnet.getIpv4Class());
            System.out.println("Default Subnet Mask: ");
            printIpv4(subnet.getSubnetMask());
            System.out.print("Enter No. of address per subnet: ");
            subnet.setAddresses(scanner.nextInt());
            if(subnet.divide()){
                System.out.println("Subnet Mask: ");
                printIpv4(subnet.getSubnetMask());
                System.out.println("Network Address: ");
                printIpv4(subnet.getNetworkAddress());
                System.out.println("Direct Broad Cast: ");
                printIpv4(subnet.getDirectBroadCastAddress());
                System.out.print("no. of subnets: "+subnet.getSubnets()+"\n");
                System.out.print("no. of hosts per subnet: "+subnet.getHosts());
            } else {
                System.out.println("Address Limit Exceeded");
            }
        } else {
            System.out.println("Invalid IPv4");
        }
    }

    public static String binaryOctet(int n){
        String string = Integer.toBinaryString(n);
        if(string.length()<8){
            String suffix = "";
            for(int i=0; i<8-string.length(); i++)
                suffix += "0";
            string = suffix + string;
        }
        return string;
    }

    public static void printIpv4(int[] array){
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]);
            if(i!=array.length-1)
            System.out.print(".");
        }
        System.out.println();

        for(int i=0;i<array.length;i++){
            System.out.print(binaryOctet(array[i]));
            if(i!=array.length-1)
                System.out.print(".");
        }
        System.out.println("\n");
    }
}
