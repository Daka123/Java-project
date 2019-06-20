import java.util.*;

public class Share extends Thread {
	String file;
	String from_client;
	String to_client;
	/**Share constructor that takes file, and two clients ad parameters.
	 * @param file file that is shared with other client
	 * @param from_client client who shares a file
	 * @param to_client client to who file is shared*/
	public Share(String file, String from_client, String to_client){
		this.from_client=from_client;
		this.to_client=to_client;
		this.file=file;
	}
	/**Starts sharing process.*/
	public void run(){
    	try {
    		find_file(this.file, this.from_client, this.to_client);
		} catch (Exception e) {}
			
	}
	/**Finds given file in discs and saves it to given client.
	 * @param file file that is shared with other client
	 * @param from_client client who shares a file
	 * @param to_client client to who file is shared*/
	public void find_file(String file, String from_client, String to_client) throws Exception {
		MyMap[] filelist = new MyMap[5];
		boolean found = false;
		for(int i=0; i<5 && !found; i++){
			filelist[i] = Discs.get_csv("d" + (i+1));
			if(filelist[i].containsKey(from_client)){
				for(ListIterator<String> f = filelist[i].get(from_client).listIterator(); f.hasNext(); ){
					if(f.next().equals(file)){
						Discs disc = new Discs();
						disc.update_list(file,to_client,"d"+(i+1));
						found = true;
						break;
					}
				}
			}
		}
	}
}