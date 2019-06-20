import java.io.*;

public class SaveFiles extends Thread{

    String what;
    String where;
    PrintWriter pw;
    /**SaveFiles constructor that takes files, folder path, and PrintWriter as parameters.
     * @param what file that needs o be saved
     * @param where path to folder in where the file is needed to be saved
     * @param pw PrintWriter to communicate with server*/
    public SaveFiles(String what, String where, PrintWriter pw){
        this.what=what;
        this.where=where;
        this.pw=pw;
    }
    /**Saves given file in given path.*/
    public void run(){
        try {
            Discs disc = new Discs();

            System.out.println("Saving " + disc.get_file_name(what) + " file.");//Nazwa jaki plik, może ...
            disc.copy_file(where, what);
            System.out.println("File saved");
            //one file has been saved
            pw.println("Saved");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Saving error");
        }
    }
}