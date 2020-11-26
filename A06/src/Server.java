import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final Scanner scanner = new Scanner(System.in);
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("listening to port:5000");
            Socket socket = serverSocket.accept();
            System.out.println("connected: "+socket.getInetAddress()+":"+socket.getPort());

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            String choice = dataInputStream.readUTF();
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
                choice = dataInputStream.readUTF();
            }

        } catch (Exception e){
            System.out.println("Error: "+e.toString());
        }
    }

    private static void chat() throws Exception{
        System.out.println("\n/chat");

        String reply, message;
        while(true){
            reply = dataInputStream.readUTF();
            System.out.println("reply: "+reply);
            if(reply.equalsIgnoreCase("/exit"))
                break;

            System.out.print("message: ");
            message = scanner.nextLine();
            dataOutputStream.writeUTF(message);
            if(message.equalsIgnoreCase("/exit"))
                break;
        }
    }

    private static void calculator() throws Exception{
        System.out.println("\n/calculator");

        String expression;
        String value;
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("js");

        while (true){
            expression = dataInputStream.readUTF();
            System.out.println("Expression: "+expression);
            if(expression.equalsIgnoreCase("/exit"))
                break;
            try {
                value = String.valueOf(engine.eval(expression));
            }catch (Exception e){
                value = "Invalid Expression";
            }
            dataOutputStream.writeUTF(value);
            System.out.println("value: "+value);
        }
    }

    private static void fileTransfer() throws Exception{
        System.out.println("\n/transfer");

        String path = dataInputStream.readUTF();
        if(path.equalsIgnoreCase("/exit"))
            return;

        String fileName = path.substring(path.lastIndexOf('/') + 1);
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("media/"+fileName);

        long size = dataInputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
        System.out.println("Received File: "+fileName);
    }
}
