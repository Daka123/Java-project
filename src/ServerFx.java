import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.collections.*;
import javafx.application.*;
import javafx.geometry.*;

public class ServerFx extends Application {
	private List[] files;
	private Label status;
	private ObservableList<String> items1,items2,items3,items4,items5;
	/**Gets files list form given disc.
	 * @param disc disc from which files list needs to be read*/
	private List<String> get_files(int disc){
		final File file = new File("Server\\d" + disc + "\\");
        List<String> list = new ArrayList<>();
        for (final File fileEntry : file.listFiles())
            list.add(fileEntry.getName());
        list.remove("Data.csv");
        Collections.sort(list);
        return list;
	}
	/**Sets list for given disc*/
	private void setLists(int i){
		try{
			if(i == 1) {
				items1.clear();
				items1.addAll(files[0]);
			}
			if(i == 2) {
				items2.clear();
				items2.addAll(files[1]);
			}
			if(i == 3) {
				items3.clear();
				items3.addAll(files[2]);
			}
			if(i == 4) {
				items4.clear();
				items4.addAll(files[3]);
			}
			if(i == 5) {
				items5.clear();
				items5.addAll(files[4]);
			}
		} catch(Exception e) {}
	}
	/**Refreshes files lists for all discs every 5 seconds.*/
	private void refresh(){
	    try {
            Thread t = new Thread(() -> {
                while(true) {
                    try {
                        Platform.runLater(() -> {
                            files = new ArrayList[5];
                            for(int i=0; i<5; i++){
                                try {
                                    files[i] = get_files(i+1);
                                    setLists(i);
                                } catch (Exception e) {}
                            }
                        });
                        Discs.delay(5);
                    } catch (Exception e) {}
                }
            });
            t.start();
	    }catch (Exception e) {}
	}
	/**Refreshes status of synchronization every 1 second.*/
	private void status(){
        try{
            Thread t = new Thread(() -> {
                while(true) {
                    try {
                        Platform.runLater(() ->
                            status.setText("Status: " + Server.status)
                        );
                        Discs.delay(1);
                    } catch (Exception e) {}
                }
            });
            t.start();
        } catch (Exception e) {}
	}
	/**View for server panel.*/
   	@Override
   	public void start(Stage stage){
   		refresh();
   		status();

   		Hyperlink hyperlink = new Hyperlink("NYAN!");
   		hyperlink.setUnderline(false);

   		hyperlink.setBorder(Border.EMPTY);
		hyperlink.setPadding(new Insets(4, 0, 4, 0));
		hyperlink.setLayoutY(340);
		hyperlink.setLayoutX(765);
		hyperlink.setOpacity(0.15);
		

        hyperlink.setOnAction((event) ->
                getHostServices().showDocument("https://www.youtube.com/watch?v=QH2-TGUlwu4")
        );
		
		Label name = new Label("SUPER SECRET JAVA SERVER");
        name.setLayoutX(200);
		name.setLayoutY(10);
		name.setTextFill(Color.BLACK);
		name.setFont(Font.font("MS Reference Sans Serif", FontWeight.BOLD, 30));//"Miriam Fixed"

		status = new Label(" ");
        status.setLayoutX(350);
		status.setLayoutY(50);
		status.setAlignment(Pos.CENTER);
		
		//discs names
		int ly = 75;
		Font font = Font.font("Comic Sans MS", 12);

		Label ld1 = new Label("Disc 1");
		ld1.setLayoutX(65);
		ld1.setLayoutY(ly);
		ld1.setFont(font);
		Label ld2 = new Label("Disc 2");
		ld2.setLayoutX(225);
		ld2.setLayoutY(ly);
		ld2.setFont(font);
		Label ld3 = new Label("Disc 3");
		ld3.setLayoutX(385);
		ld3.setLayoutY(ly);
		ld3.setFont(font);
		Label ld4 = new Label("Disc 4");
		ld4.setLayoutX(545);
		ld4.setLayoutY(ly);
		ld4.setFont(font);
		Label ld5 = new Label("Disc 5");
		ld5.setLayoutX(705);
		ld5.setLayoutY(ly);
		ld5.setFont(font);


		Discs disc = new Discs();
		MyMap[] filelist = new MyMap[5];
		List[] files = new ArrayList[5];

		for(int i=0; i<5; i++){
			try{
			    filelist[i] = disc.get_csv("d" + (i+1));
                files[i] = new ArrayList<String>();
                for(String key : filelist[i].keySet()){
                    files[i].addAll(filelist[i].get(key));
                }
            } catch(Exception e) {}
		}

		int y = 90, height = 250, width = 150;

        ListView<String> d1 = new ListView<>();
        items1 = FXCollections.observableArrayList(files[0]);
		d1.setItems(items1);
		d1.setLayoutX(10);
		d1.setLayoutY(y);
		d1.setPrefHeight(height);
		d1.setPrefWidth(width); 
		//dysk2
		ListView<String> d2 = new ListView<>();
        items2 = FXCollections.observableArrayList(files[1]);
		d2.setItems(items2);
		d2.setLayoutX(170);
		d2.setLayoutY(y);
		d2.setPrefHeight(height);
		d2.setPrefWidth(width); 
		//dysk3
		ListView<String> d3 = new ListView<>();
        items3 = FXCollections.observableArrayList(files[2]);
		d3.setItems(items3);
		d3.setLayoutX(330);
		d3.setLayoutY(y);
		d3.setPrefHeight(height);
		d3.setPrefWidth(width); 
		//dysk4
		ListView<String> d4 = new ListView<>();
        items4 = FXCollections.observableArrayList(files[3]);
		d4.setItems(items4);
		d4.setLayoutX(490);
		d4.setLayoutY(y);
		d4.setPrefHeight(height);
		d4.setPrefWidth(width); 
		//dysk5
		ListView<String> d5 = new ListView<>();
        items5 = FXCollections.observableArrayList(files[4]);
		d5.setItems(items5);
		d5.setLayoutX(650);
		d5.setLayoutY(y);
		d5.setPrefHeight(height);
		d5.setPrefWidth(width);

		ImageView background = new ImageView(new Image("Nobody expects.png",810,370,false,true));
		background.setOpacity(0.05);

		Group root = new Group(background,name,status,ld1,ld2,ld3,ld4,ld5,d1,d2,d3,d4,d5,hyperlink);

      	Scene scene = new Scene(root ,810, 370);
      	stage.setTitle("Server");
      	Color color = Color.web("#a3beed");//#3264dd
      	scene.setFill(color);
      	stage.setScene(scene); 
      	stage.show();
   	}    
   	public static void main(String args[]) throws Exception {   
   		Server server = new Server(3002);
		new Thread(() -> {
			try{
				server.run();
			} catch(Exception e) {}
		}).start();
		launch(args);
   	}         
}