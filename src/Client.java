import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.io.*;

public class Client { 
    Socket socket = null;
    String client = null;
    String  local_folder_path = null;
    public static String status;

    public Client(InetAddress address, int port, String client, String local_folder_path) throws Exception {
        this.socket = new Socket(address, port);
        this.client = client;
        this.local_folder_path = local_folder_path;
        this.status = "Getting ready...";
        System.out.println("Welcome " + client + ".");
    }

    public void run(){
        try {
            new ControlClient(socket, local_folder_path, client).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IO Exception");
            System.exit(1);
        }
    }
 
    public static void main(String args[]) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        int port = 3002;
        Socket socket = null;

        Scanner ser = null;
        PrintWriter pw = null;
        String msg = null;

        String client = new String();
        String local_folder_path = new String();


        try{
            client = args[0];
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("You didn't provided client name.");
            System.exit(1);
        }
        try{
            local_folder_path = args[1];
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("You didn't provided local folder path.");
            System.exit(1);
        }

        try{
            
            new Client(address,port,client,local_folder_path).run();
        
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client error");
            System.exit(1);
        }
    }
}