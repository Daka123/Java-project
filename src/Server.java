import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
//Hessian
//Burlap
//XML-RPC

public class Server{
    public ServerSocket serv;
    Socket sock;
    public static String status;
    /**Server construcor that tahes port as an argument.
	 * @param port number of port*/
	public Server(int port) throws IOException {
		this.status = "Running...";
		serv = new ServerSocket(port);
		System.out.println("Server is running...");
	}
	/**For every new client it starts a thread to handle synchronization.*/
	public void run(){
		try{
			while(true){
				sock = serv.accept();
				new Multi_Client(serv,sock/*,status*/).start();
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(serv);
			System.out.println(sock);
		}
		
	}

    public static void main(String[] args) throws IOException {
		int port = 3002;
		new Server(port).run();
    }
}