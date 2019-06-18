package Classes;

import Classes.Multi_Client;

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
		
	public Server(int port) throws IOException {
		this.status = "Running...";
		serv = new ServerSocket(port);
		System.out.println("Server is running...");
	}

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