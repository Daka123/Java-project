//kopiuje na serwer, updejtuje csv, czyta csv, wyciaga file_name pliku

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Discs extends Thread {
	String disc;
	String client;
	String file_path;
	PrintWriter pw;
	/**Discs constructor that don't need any parameters used with functions that don't need full constructor.*/
	public Discs(){}//to allow using other methods
	/**Discs constructor that takes disc, client, filepath, and PrintWriter as parameters.
	 * @param disc disc on which given operations will be made
	 * @param client client name for who given operations will be made
	 * @param file_path path to file which is needed to be saved on server
	 * @param pw stream which is used to communicate with client, to send him messages*/
	public Discs(String disc, String client, String file_path, PrintWriter pw){
		this.disc=disc;
		this.client=client;
		this.file_path=file_path;
		this.pw=pw;
	}
	/**Delays a thread on given seconds.
	 * @param sleep number on seconds of which thread will be sleeping*/
	public static void delay(int sleep) throws Exception {
		TimeUnit.SECONDS.sleep(sleep);
	}
	/**Saves files on given disc and updates .csv file*/
	public void run(){
    	try {
    		String server_path = null;
    		try {
    			server_path = "Server\\" + this.disc + "\\";

				System.out.println("Saving file ... ");
				copy_file(server_path, file_path);
				//pw.println("File saved on server.");

				update_list(file_path, client, this.disc);
				System.out.println("Updated list of files");
				//one file saved on server
				pw.println("Saved");

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection error");
			}
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server error");
        } 
	}  
	/**Gets file name from full path
	 * @param file_path file path from which file name is needed to be taken
	 * @return just a file name*/
	public String get_file_name(String file_path){
		String separator ="\\";
		int lastIndexOf = file_path.lastIndexOf(separator);
		String file_name = file_path.substring(lastIndexOf + 1);
		return file_name;
	}
	/**Copies file given file in given path.
	 * @param to path to folder in which the file is needed to be saved
	 * @param from file that needs to be saved*/
	public void copy_file(String to, String from){
		try{
			Random rand = new Random();
			int sleep = rand.nextInt(11);
			System.out.println("Waiting "+sleep+" seconds.");
	        TimeUnit.SECONDS.sleep(sleep); //Spowalnniacz xD
			to += get_file_name(from);
			ReadWriteLock lock = new ReentrantReadWriteLock();
			Lock writeLock = lock.writeLock();
			try{
				writeLock.lock();
				Files.copy(Paths.get(from),Paths.get(to),StandardCopyOption.REPLACE_EXISTING);
			} finally {
				writeLock.unlock();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while copying file.");
		}
	}
	/**Updates .csv list which contains lists of client and it's files saved on given disc
	 * @param file file that needs to be added
	 * @param client client to whom file is needed to be added
	 * @param disc disc on which .csv file is needed to be updated*/
	public void update_list(String file, String client, String disc) throws IOException {
		//ReadWriteLock
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock writeLock = lock.writeLock();

		String file_name = new String(get_file_name(file));
		MyMap filelist = get_csv(disc);
		List<String> files_as_list = new ArrayList<String>();

		files_as_list.add(file_name);
		if(filelist.containsKey(client)) {
			files_as_list.addAll(filelist.get(client));
			Collections.sort(files_as_list);
		}
		filelist.put(client,files_as_list);

		Set<String> clients = filelist.keySet();
		
  	    //for(Map.Entry e : filelist.entrySet())//do testów
  	    // 	System.out.println(e.getKey() + " -> " + e.getValue());

		String data = new String("Server\\" + disc + "\\Data.csv");
		
		try{
			writeLock.lock();
			Writer writer = new FileWriter(data);//new StringWriter();

			for(String key : clients){
	        	writer.append(key);
				List<String> files = new ArrayList<String>(filelist.get(key));
				for(ListIterator<String> u = files.listIterator(); u.hasNext(); ){
					writer.append(',').append(u.next());
				}
				writer.append("\r\n");
	        }
	        //System.out.println(writer);
	        writer.close();
		} finally {
			writeLock.unlock();
		}
		
		//System.out.println("Na razie nie updejtuje pliku xd");
	}
	/**Reeds clients and their files from .csv file.
	 * @param disc disc from which files and clients are needed to be taken
	 * @return MyMap whit all clients and their files that have them saved on given disc*/
	public static MyMap get_csv(String disc) throws IOException {
		ReadWriteLock lock = new ReentrantReadWriteLock();
		Lock readLock = lock.readLock();

		String file_name = new String("Server\\" + disc + "\\Data.csv");
		File file = new File(file_name);
		
		if(file.length() == 0) return null;

		Path path = Paths.get(file_name);
		String content;
		try{
			readLock.lock();
			content = new String(Files.readAllBytes(path));
		} finally {
			readLock.unlock();
		}

		String[] clients = content.split("\n");
		String[] client = new String[clients.length];
		int index;
		MyMap filelist = new MyMap();
		for(int i=0; i<clients.length; i++){
			index = clients[i].indexOf(",");
			client[i] = clients[i].substring(0,index);
			String[] files_for_client = clients[i].substring(index+1, clients[i].length()-1).split(",");
			List<String> files_as_list = Arrays.asList(files_for_client);
			Collections.sort(files_as_list);
			filelist.put(client[i], files_as_list);
		}
        return filelist;
	}

}