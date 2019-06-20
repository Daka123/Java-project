import java.util.*;

public class Share extends Thread {
	String file;
	String from_client;
	String to_client;
		
	public Share(String file, String from_client, String to_client){
		this.from_client=from_client;
		this.to_client=to_client;
		this.file=file;
	}
	
	public void run(){
    	try {
    		find_file(this.file, this.from_client, this.to_client);
		    	
		} catch (Exception e) {}
			
	}  


	public void find_file(String file, String from_client, String to_client) throws Exception {
		MyMap[] filelist = new MyMap[5];
		boolean found = false;
		for(int i=0; i<5 && !found; i++){
			filelist[i] = Discs.get_csv("d" + (i+1));//ioexception
			//for(String key: filelist[i].keySet()){
				if(filelist[i].containsKey(from_client)){
					for(ListIterator<String> f = filelist[i].get(from_client).listIterator(); f.hasNext(); ){
						if(f.next().equals(file)){
							Discs disc = new Discs();//System.out.println("Tak, tu jest ten plik: d" +(i+1));
							disc.update_list(file,to_client,"d"+(i+1));
							found = true;
							break;
						}
						//dontHave.add("Server\\d" + (i+1) + "\\" + u.next());
					}
				}
			//}
		}


	}
}