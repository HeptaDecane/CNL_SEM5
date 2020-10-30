import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static byte[] outData;
    private static byte[] inData;
    private static InetAddress inetAddress;
    private static int port;

    public static void main(String[] args) {
        try{
            socket = new DatagramSocket(5000);
            System.out.println("Receiving at Port:5000\n");

            while (true) {
                receiveFile();
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private static void receiveFile() throws Exception{
        // receive file name (with extension)
        inData = new byte[4*1024];
        packet = new DatagramPacket(inData,inData.length);
        socket.receive(packet);
        String fileName = new String(inData, 0, packet.getLength());
        System.out.println("Incoming File: "+fileName);

        // get sender's address & port
        inetAddress = packet.getAddress();
        port = packet.getPort();
        System.out.println("Sender: "+inetAddress+":"+port);

        // receive total no. of packets to be sent
        inData = new byte[4*1024];
        packet = new DatagramPacket(inData, inData.length);
        socket.receive(packet);
        int n = Integer.parseInt(new String(inData,0, packet.getLength()));
        System.out.println("Packets: "+n);

        // receive packets
        FileOutputStream fileOutputStream = new FileOutputStream("media/"+fileName);
        System.out.println("Receiving... ");
        for(int i=0;i<n;i++){
            // send required packet sequence no.
            outData = String.valueOf(i).getBytes();
            packet = new DatagramPacket(outData,outData.length,inetAddress,port);
            socket.send(packet);

            // receive requested packet
            inData = new byte[4*1024];
            packet = new DatagramPacket(inData, inData.length);
            socket.receive(packet);
            fileOutputStream.write(inData,0, packet.getLength());
        }

        fileOutputStream.close();
        // send completion ack
        outData = String.valueOf(-1).getBytes();
        packet = new DatagramPacket(outData,outData.length,inetAddress,port);
        socket.send(packet);

        System.out.println("File Received: "+fileName+"\n");
    }
}
