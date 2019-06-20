import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class ControlClient extends Thread{
    Socket socket;
    PrintWriter pw;
    String local_folder_path;
    String client;
    /**ClientControl constructor that takes socket, his local folder path and his name.
     * @param socket socket took from Client class
     * @param local_folder_path path to client local folder with files that is synchronized with server
     * @param client client name, which with he is associated on server*/
    public ControlClient(Socket socket, String local_folder_path, String client) throws IOException{
        this.socket=socket;
        this.pw=new PrintWriter(socket.getOutputStream(), true);
        this.local_folder_path=local_folder_path;
        this.client=client;
    }
    /**Gets files list from client local folder.
     * @param path - path to folder from which files are taken.
     * @param client - client name
     * @return MyMap with key being client name and value being list of files*/
    static public MyMap get_file_list(final String path, String client) {
        final File file = new File(path);
        MyMap filelist = new MyMap();
        List<String> list = new ArrayList<>();
        for (final File fileEntry : file.listFiles())
            list.add(fileEntry.getName());
        Collections.sort(list);
        filelist.put(client, list);
        return filelist;
    }
    /**Sends client files to server.
     * @param map map of client files, with key being client name and value being list of client files
     * @param stream stream in which map needs to be send*/
    static public void send_files_map (MyMap map, OutputStream stream) throws Exception {
        TimeUnit.SECONDS.sleep(5);
        ObjectOutputStream mapOutStream = new ObjectOutputStream(stream);
        mapOutStream.writeObject(map);
    }
    /**Returns list of files, that needs to be saved in client folder, from server.*/
    public List<String> get_server_files_list () throws Exception{
        ObjectInputStream listInStream = new ObjectInputStream(socket.getInputStream());//exception
        List<String> list = (ArrayList<String>) listInStream.readObject();//ioexception, classnotfoundexception
        //mapInStream.close();//exception
        return list;
    }
    /**For every file that client don't have it starts a new thread to download it.
     * @param dontHave files that client don't have, and needs to download them*/
    public void get_files(List<String> dontHave){
        for(ListIterator<String> u = dontHave.listIterator(); u.hasNext(); ){
            new SaveFiles(u.next(), local_folder_path + "\\", this.pw).start();
        }
    }
    /**Synchronizes files between client and server.*/
    public void run(){
        System.out.println("Connected with server.");
        Client.status = "Connected with server";
        MyMap files = new MyMap();
        try{
            try{
                Scanner scan = new Scanner(socket.getInputStream());
                String msg = new String();
                this.pw.println(client);
                this.pw.println(local_folder_path);

                while(true) {
                    //Client is ready
                    files = get_file_list(local_folder_path, client);

                    this.pw.println("Ready");
                    //Server is ready
                    msg = scan.nextLine();
                    if(!msg.equals("Ready")){
                        System.out.println("Error while connecting to server");
                    }
                    System.out.println("Synchronizing...");
                    Client.status = "Saving files on server";
                    
                    send_files_map(files, socket.getOutputStream());
    
                    //Server got files map
                    msg = scan.nextLine();
                    if(!msg.equals("Got")){
                        System.out.println("Error while synchronizing");
                    }
                    //how many files will be saved on server
                    msg = scan.nextLine();
                    try{
                        Integer.valueOf(msg);
                    }catch(NumberFormatException  e){
                        System.out.println("Error while saving files");
                    }

                    int how_many = Integer.valueOf(msg);
                    if(how_many == 0) System.out.println("Nothing to be saved");
                    else{
                        for(int i=0; i<how_many; i++){
                            msg = scan.nextLine();
                            //one file saved on server
                            if(!msg.equals("Saved")){
                                System.out.println("Error while copying files");
                            } 
                        }
                        System.out.println("Files saved on server.");
                        Client.status = "Files saved on server";
                    }

                    //Client knows that all files has been saved
                    pw.println("Have");

                    List<String> server_files = get_server_files_list();
                    //Client got files list
                    pw.println("Got");
                    Client.status = "Downloading files...";
                    
                    //how many files will be saved
                    if(server_files.isEmpty()) pw.println("0");
                    else {
                        pw.println(server_files.size());
                        get_files(server_files);
                    }
                    
                    //Server knows that all files has been saved
                    msg = scan.nextLine();
                    if(!msg.equals("OK")){
                        System.out.println("Error while copying file");
                    }

                    Client.status = "Done";
                    //EVERYTHING IS DONE
                    pw.println("Done");
    
                    System.out.println("Synchronized");

                    TimeUnit.SECONDS.sleep(10);
                }
                // Scanner ser = new Scanner(socket.getInputStream());
                // new GetMessage(ser, pw).start(); 

            } catch (Exception e) {
                //e.printStackTrace();
                System.err.println("Lost connection with server");//Socket write error
                System.exit(1);
            }            
        } finally {
            try {
                pw.close();
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
