package Classes;
//kopiuje na serwer, updejtuje csv, czyta csv, wyciaga file_name pliku
import Classes.MyMap;

import java.net.Socket;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.supercsv.io.*;
import org.supercsv.prefs.*;

public class Discs extends Thread {
	String disc;
	String client;
	String file_path;
	PrintWriter pw;
	ReadWriteLock lock;
		
	public Discs(){}//to allow using other methods
	//public Discs(String disc) { this.disc=disc; }//to csv

	public Discs(String disc, String client, String file_path, PrintWriter pw) throws IOException{
		this.disc=disc;
		this.client=client;
		this.file_path=file_path;
		this.pw=pw;
		//init();
	}
	public static void delay(int sleep) throws Exception {
		TimeUnit.SECONDS.sleep(sleep);
	}
	
	public void run(){
    	try {
    		String server_path = null;
    		try {
    			server_path = new String("Server\\" + this.disc + "\\");

				System.out.println("Saving file ... ");//Nazwa jaki plik, może ...
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

	public String get_file_name(String file_path){
		String separator ="\\";
		int lastIndexOf = file_path.lastIndexOf(separator);
		String file_name = new String(file_path.substring(lastIndexOf + 1));
		return file_name;
	}

	public void copy_file(String to, String from){
		try{
			Random rand = new Random();
			int sleep = rand.nextInt(11);//zmienić na 61 jak już skończę
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
			client[i] = new String(clients[i].substring(0,index));
			String[] files_for_client = clients[i].substring(index+1, clients[i].length()-1).split(",");
			List<String> files_as_list = Arrays.asList(files_for_client);
			Collections.sort(files_as_list);
			filelist.put(client[i], files_as_list);
		}
		//Jak zakomentować fragment kodu - Zaznaczyć fragment i ctrl+shift+/

		//System.out.println(disc);
		// for(Map.Entry e : filelist.entrySet())//do testów
        //     System.out.println(e.getKey() + " -> " + e.getValue());
						
        return filelist;
	}
	

}