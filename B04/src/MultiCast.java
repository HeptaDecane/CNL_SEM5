import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiCast {
    private final static Scanner scanner = new Scanner(System.in);
    private static final ExecutorService service = Executors.newFixedThreadPool(2);
    private final static List<String> queue = new ArrayList<>();
    private final static Random random = new Random();

    private static MulticastSocket socket;
    private static DatagramPacket inPacket;
    private static DatagramPacket outPacket;
    private static byte[] outData = new byte[1024];
    private static byte[] inData = new byte[1024];
    private static InetAddress inetAddress;
    private static int port;
    private static String userId;


    public static void main(String[] args) {
        try {
            System.out.print("Host: ");
            inetAddress = InetAddress.getByName(scanner.nextLine());
            System.out.print("Port: ");
            port = Integer.parseInt(scanner.nextLine());
            socket = new MulticastSocket(port);
            socket.joinGroup(inetAddress);

            userId = "u"+String.format("%08d",random.nextInt(99999999));
            System.out.println("\n'/refresh' to read received messages");
            System.out.println("User ID: "+userId);
            service.execute(MultiCast::sendMessage);
            service.execute(MultiCast::fillQueue);

        } catch (Exception e){
            System.out.println(e.toString());

        }
    }

    private static void sendMessage(){
        try {
            while (true){
                System.out.print("> ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("/refresh")){
                    if(queue.isEmpty())
                        System.out.println("Queue Empty");
                    queue.forEach(System.out::println);
                    queue.clear();
                } else {
                    outData = (userId+": "+message).getBytes();
                    outPacket = new DatagramPacket(outData,outData.length,inetAddress,port);
                    socket.send(outPacket);
                }
            }
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
    }

    private static void fillQueue(){
        try {
            while (true){
                inPacket = new DatagramPacket(inData,inData.length);
                socket.receive(inPacket);
                String message = new String(inData,0,inPacket.getLength());
                String sender = message.split(":")[0];
                if(!sender.equals(userId))
                    queue.add(message);
            }
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
    }

}