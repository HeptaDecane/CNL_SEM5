import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)) {
            System.out.println("connected: "+socket.getInetAddress()+":"+socket.getPort());

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("\nOptions: /chat, /calculator, /transfer, /exit");
            System.out.print("> ");
            String choice = scanner.nextLine();
            dataOutputStream.writeUTF(choice);
            while(!choice.equalsIgnoreCase("/exit")){
                switch (choice){
                    case "/chat":
                        chat();
                        break;

                    case "/calculator":
                        calculator();
                        break;

                    case "/transfer":
                        fileTransfer();
                        break;

                    default:
                        System.out.println("Invalid Option: "+choice);
                }

                System.out.println("\nOptions: /chat, /calculator, /transfer, /exit");
                System.out.print("> ");
                choice = scanner.nextLine();
                dataOutputStream.writeUTF(choice);
            }

        } catch (Exception e){
            System.out.println("Error: "+e.toString());
        }
    }

    private static void chat() throws Exception{
        System.out.println("\n/chat");

        String reply, message;
        while(true){
            System.out.print("message: ");
            message = scanner.nextLine();
            dataOutputStream.writeUTF(message);
            if(message.equalsIgnoreCase("/exit"))
                break;

            reply = dataInputStream.readUTF();
            System.out.println("reply: "+reply);
            if(reply.equalsIgnoreCase("/exit"))
                break;
        }
    }

    private static void calculator() throws Exception{
        System.out.println("\n/calculator");

        String expression;
        String value;
        while (true){
            System.out.print("Expression: ");
            expression = scanner.nextLine();
            dataOutputStream.writeUTF(expression);
            if(expression.equalsIgnoreCase("/exit"))
                break;
            value = dataInputStream.readUTF();
            System.out.println("value: "+value);
        }
    }

    private static void fileTransfer() throws Exception{
        System.out.println("\n/transfer");

        System.out.print("File Path: ");
        String path = scanner.nextLine();
        dataOutputStream.writeUTF(path);
        if(path.equalsIgnoreCase("/exit"))
            return;

        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        dataOutputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
        System.out.println("File Sent");
    }
}
