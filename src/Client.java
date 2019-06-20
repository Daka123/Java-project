import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.io.*;

public class Client { 
    Socket socket = null;
    String client = null;
    String  local_folder_path = null;
    public static String status;
    /**Client class constructor, that takes address, port, client name, and path to local folder.
     * @param address address
     * @param port port number
     * @param client client name, which with he is associated on server
     * @param local_folder_path path to client local folder with files that is synchronized with server*/
    public Client(InetAddress address, int port, String client, String local_folder_path) throws Exception {
        this.socket = new Socket(address, port);
        this.client = client;
        this.local_folder_path = local_folder_path;
        this.status = "Getting ready...";
        System.out.println("Welcome " + client + ".");
    }
    /**Starts new thread that control synchronization with server.*/
    public void run(){
        try {
            new ControlClient(socket, local_folder_path, client).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IO Exception");
            System.exit(1);
        }
    }
    /**Asks for client name and local folder path, then runs new client thread.*/
    public static void main(String args[]) throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        int port = 3002;

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