import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

public class Sender {
    private static Scanner scanner = new Scanner(System.in);
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    private static byte[] outData;
    private static byte[] inData;
    private static ArrayList<byte[]> packets;
    private static int port;
    private static InetAddress inetAddress;

    public static void main(String[] args) {
        try{
            socket = new DatagramSocket();

            System.out.print("InetAddress: ");
            inetAddress = InetAddress.getByName(scanner.nextLine());
            System.out.print("Port: ");
            port = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            while (true) {
                System.out.print("File Path: ");
                String path = scanner.nextLine();
                sendFile(path);
            }
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    private static void sendFile(String path) throws IOException{
        // send file name (with extension)
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        outData = fileName.getBytes();
        packet = new DatagramPacket(outData,outData.length,inetAddress,port);
        socket.send(packet);

        // calculate & send total no. of packets to be sent
        File file = new File(path);
        int n = (int) Math.ceil(file.length()/(double)(4*1024));
        outData = String.valueOf(n).getBytes();
        packet = new DatagramPacket(outData,outData.length,inetAddress,port);
        socket.send(packet);

        // break file into packets of 4kb (in sequence)
        System.out.println("Reading... ");
        packets = new ArrayList<>();
        int lastPacketSize = 0;
        FileInputStream fileInputStream = new FileInputStream(file);
        for(int i=0;i<n;i++){
            packets.add(new byte[4*1024]);
            lastPacketSize = fileInputStream.read(packets.get(i));
        }

        System.out.println("Sending...");
        while (true){
            // receive packet sequence no.
            inData = new byte[4*1024];
            packet = new DatagramPacket(inData, inData.length);
            socket.receive(packet);
            int i = Integer.parseInt(new String(inData,0,packet.getLength()));

            if(i == -1) break;  // transfer completed (sequence no. = -1)

            // send requested packet
            if(i==packets.size()-1)     // last packet (size != 4kb)
                packet = new DatagramPacket(packets.get(i), lastPacketSize, inetAddress, port);
            else
                packet = new DatagramPacket(packets.get(i), packets.get(i).length, inetAddress, port);
            socket.send(packet);
        }

        System.out.println("File Sent: "+fileName+" ("+file.length()+" bytes)\n");
    }
}
