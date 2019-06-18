import Classes.Multi_Client;
import Classes.MyMap;
import Classes.Discs;
import Classes.Server;

import java.net.InetAddress;
import java.io.FileInputStream;
import java.util.*; 

import javafx.event.*;
import javafx.concurrent.*;
import javafx.beans.property.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.stage.*; 
import javafx.collections.*;
import javafx.event.*;

import java.lang.reflect.*;


//poprawić refresh na wszystkie dyski
//sprawdzić refresh

public class ServerFx extends Application {
	public MyMap[] filelist;
	public List[] files;
	/*public String msg;*/
	public Label status;

	private void refresh() throws Exception {
	    Thread t = new Thread(() -> {
	        while(true) {
	            try {
	            	Platform.runLater(() -> {
						filelist = new MyMap[5];
						files = new ArrayList[5];
						for(int i=0; i<5; i++){
							try {
								filelist[i] = Discs.get_csv("d" + (i+1));//ioexception
							} catch (Exception e) {}
							files[i] = new ArrayList<String>();
							for(String key : filelist[i].keySet()){
								files[i].addAll(filelist[i].get(key));
							}
						}   
	                });
	                Discs.delay(10);
	            } catch (Exception e) {}
	        }
	    });
	    t.start();
	}
	private void status() throws Exception {
	    Thread t = new Thread(() -> {
	        while(true) {
	            try {
	            	Platform.runLater(() -> {
	            		status.setText("Status: " + Server.status);
	                });
	                Discs.delay(1);
	            } catch (Exception e) {}
	        }
	    });
	    t.start();
	}


   	@Override
   	public void start(Stage stage) throws Exception {
   		refresh();  
   		status(); 		

   		Hyperlink hyperlink = new Hyperlink("NYAN!");
   		hyperlink.setUnderline(false);

   		hyperlink.setBorder(Border.EMPTY);
		hyperlink.setPadding(new Insets(4, 0, 4, 0));
		hyperlink.setLayoutY(340);
		hyperlink.setLayoutX(765);
		hyperlink.setOpacity(0.15);
		

        hyperlink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://www.youtube.com/watch?v=QH2-TGUlwu4");
            }
        }); 
		
		Label name = new Label("SUPER SECRET JAVA SERVER");
        name.setLayoutX(200);
		name.setLayoutY(10);
		name.setTextFill(Color.BLACK);
		name.setFont(Font.font("Miriam Fixed", FontWeight.BOLD, 30));

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


		for(int i=0; i<5; i++){//zmienić na 5
			filelist[i] = disc.get_csv("d" + (i+1));//ioexception
			files[i] = new ArrayList<String>();
			for(String key : filelist[i].keySet()){
				files[i].addAll(filelist[i].get(key));
			}
		}

		int y = 90, height = 250, width = 150;

        ListView<String> d1 = new ListView<String>();
        ObservableList<String> items1 = FXCollections.observableArrayList(files[0]);
		d1.setItems(items1);
		d1.setLayoutX(10);
		d1.setLayoutY(y);
		d1.setPrefHeight(height);
		d1.setPrefWidth(width); 
		//dysk2
		ListView<String> d2 = new ListView<String>();
        ObservableList<String> items2 = FXCollections.observableArrayList(files[1]);
		d2.setItems(items2);
		d2.setLayoutX(170);
		d2.setLayoutY(y);
		d2.setPrefHeight(height);
		d2.setPrefWidth(width); 
		//dysk3
		ListView<String> d3 = new ListView<String>();
        ObservableList<String> items3 = FXCollections.observableArrayList(files[2]);
		d3.setItems(items3);
		d3.setLayoutX(330);
		d3.setLayoutY(y);
		d3.setPrefHeight(height);
		d3.setPrefWidth(width); 
		//dysk4
		ListView<String> d4 = new ListView<String>();
        ObservableList<String> items4 = FXCollections.observableArrayList(files[3]);
		d4.setItems(items4);
		d4.setLayoutX(490);
		d4.setLayoutY(y);
		d4.setPrefHeight(height);
		d4.setPrefWidth(width); 
		//dysk5
		ListView<String> d5 = new ListView<String>();
        ObservableList<String> items5 = FXCollections.observableArrayList(files[4]);
		d5.setItems(items5);
		d5.setLayoutX(650);
		d5.setLayoutY(y);
		d5.setPrefHeight(height);
		d5.setPrefWidth(width); 

		Group root = new Group(name,status,ld1,ld2,ld3,ld4,ld5,d1,d2,d3,d4,d5,hyperlink);
      	//Creating a Scene by passing the group object, height and width   
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

/*Button refresh = new Button("Refresh");
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		    }
		});
		refresh.setLayoutX(400);//355
		refresh.setLayoutY(325);
		refresh.setPrefHeight(30);
		refresh.setPrefWidth(100);*/

//getting lists
		/*TreeItem<String> tree1 = 
        new TreeItem<String>("Disc1");
        tree1.setExpanded(true);*/


		/*Discs disc = new Discs();
		MyMap[] filelist = new MyMap[5];
		List[] files = new ArrayList[5];


		for(int i=0; i<1; i++){//zmienić na 5
			filelist[i] = disc.get_csv("d" + (i+1));//ioexception
			files[i] = new ArrayList<String>();
			for(String key : filelist[i].keySet()){
				files[i].addAll(filelist[i].get(key));
			}
		}*/
		//Set<String>

		//to chyba muszę zrobić tak, że
		//biorę liczbę klientów na dysku
		//tworzę treeitem w zależności od liczby klientów
		//w tym treeitem dokładam klientom pliki
		//a pozniej ta liste daje do treeview

		//jesli nie zadziała to sproboje zrobić jedno wyświetlanie
		//z podzialem na dyski, a nie a klientów

		//TreeItem<String>[] disc1 = new TreeItem<String>("Disc1");
        //tree1.setExpanded(true);


		/*for(int i=0; i<1; i++){//zmienić na 5
			filelist[i] = disc.get_csv("d" + (i+1));//ioexception
			for(String key: filelist[i].keySet()){
				TreeItem<String> clLeaf = new TreeItem<String>(key);
				boolean found = false;
				for(TreeItem<String> filNode : tree1.getChildren()){
					for(ListIterator<String> u = filelist[i].get(key).listIterator(); u.hasNext(); ){
						if(filNode.getValue().contentEquals(u.next())){
							filNode.getChildren().add(clLeaf);
							found=true;
							break;
						}
					}
					break;	
				}
				if(!found){
					for(ListIterator<String> u = filelist[i].get(key).listIterator(); u.hasNext(); ){
						TreeItem<String> filNode = new TreeItem<String>(u.next());
						tree1.getChildren().add(filNode);
	                	filNode.getChildren().add(clLeaf);
					}
				}
			}
		}*/
		
		/*TreeView<String> tree = new TreeView<String>(tree1);
		tree.setLayoutX(10);
		tree.setLayoutY(65);
		tree.setPrefHeight(250);
		tree.setPrefWidth(150); */