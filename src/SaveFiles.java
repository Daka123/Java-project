import java.io.*;

public class SaveFiles extends Thread{

    String what;
    String where;
    PrintWriter pw;

    public SaveFiles(String what, String where, PrintWriter pw){
        this.what=what;
        this.where=where;
        this.pw=pw;
    }

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