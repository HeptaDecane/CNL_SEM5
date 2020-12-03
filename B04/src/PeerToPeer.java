import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerToPeer {
    private final static Scanner scanner = new Scanner(System.in);
    private static ExecutorService service = Executors.newFixedThreadPool(2);

    private static DatagramSocket socket;
    private static DatagramPacket inPacket;
    private static DatagramPacket outPacket;
    private static byte[] outData = new byte[1024];
    private static byte[] inData = new byte[1024];
    private static InetAddress inetAddress;
    private static int port;
    private final static List<String> queue = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.print("Port to Bind: ");
            socket = new DatagramSocket(Integer.parseInt(scanner.nextLine()));
            System.out.print("Host to Connect: ");
            inetAddress = InetAddress.getByName(scanner.nextLine());
            System.out.print("Port to Connect: ");
            port = Integer.parseInt(scanner.nextLine());

            System.out.println("\n'/refresh' to read received messages");
            service.execute(PeerToPeer::sendMessage);
            service.execute(PeerToPeer::fillQueue);

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
                    outData = message.getBytes();
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
                String sender = inPacket.getSocketAddress().toString();
                String message = new String(inData,0,inPacket.getLength());
                queue.add(sender+": "+message);
            }
        } catch (Exception e){
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
    }

}
