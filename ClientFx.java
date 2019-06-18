import Classes.Client;
import Classes.ControlClient;
import Classes.Multi_Client;
import Classes.MyMap;
import Classes.Discs;

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
import javafx.animation.*;

//edytować slowpoki xD

public class ClientFx extends Application {
	public ObservableList<String> items;
	public List<String> client_files, other;
	public String user, path;
	public Scene scene;
	public Group root;
	public Label status;
	public Client i;
	public ImageView imageView;
	public boolean slow;

	public List<String> get_files(){
		MyMap filelist = ControlClient.get_file_list(path,user);
    	List<String> files = new ArrayList<String>();
    	files.addAll(filelist.get(user));
    	return files;
	}

	private void refresh() throws Exception{
	    Thread t = new Thread(() -> {
	        while(true) {
	            try {
	                Platform.runLater(() -> {
	                	try {
	                		other = Multi_Client.get_clients_list(user);
	                	} catch (Exception e) {}
	                	client_files = get_files();
				    	items.clear();
				    	if(client_files != null && client_files.size() > 0)
				    		items.addAll(client_files);
				    		   
	                });
	                Discs.delay(10);
	            } catch (Exception e) {}
	        }
	    });
	    t.start();
	}
	private void status() throws Exception{
	    Thread t = new Thread(() -> {
	        while(true) {
	            try {
	                Platform.runLater(() -> {
	                	status.setText(i.status);
	                	if(status.getText().equals("Done")) {
	                		imageView.setImage(new Image("Blank.png"));
	                		slow = false;
	                	}
	                	else {
	                		if(!slow){
	                			imageView.setImage(new Image("Slowpoke.gif"));
	                			slow = true;
	                		}
	                	}	    		   
	                });
	                Discs.delay(5);
	            } catch (Exception e) {}
	        }
	    });
	    t.start();
	}

	public void Client_Panel(Stage stage) throws Exception {
		refresh();
		status();

   		Hyperlink hyperlink = new Hyperlink("YES?");
   		hyperlink.setUnderline(false);

   		hyperlink.setBorder(Border.EMPTY);
		hyperlink.setPadding(new Insets(4, 0, 4, 0));
		hyperlink.setLayoutY(290);
		hyperlink.setLayoutX(520);
		hyperlink.setOpacity(0.15);
		

        hyperlink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getHostServices().showDocument("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            }
        }); 

        //public Client(InetAddress address, int port, String client, String local_folder_path)
        i = new Client(InetAddress.getLocalHost(), 3002, user, path);
        i.run();

    	/*MyMap filelist = ControlClient.get_file_list("k1","k1");
    	List<String> files = new ArrayList<String>();
    	files.addAll(filelist.get("k1"));
    	*///System.out.println(files);

    	other = Multi_Client.get_clients_list(user);
    	//System.out.println(other);

    	//wyglądu listy nie ruszamy xD
    	client_files = get_files();
        ListView<String> list = new ListView<String>();
        items = FXCollections.observableArrayList(client_files);//ObservableList<String> 
		list.setItems(items);
		list.setLayoutX(10);
		list.setLayoutY(10);
		list.setPrefHeight(300);
		list.setPrefWidth(400);

    	//"Blank.png" - nic, dosłownie xD
    	slow = false;
		imageView = new ImageView(new Image("Blank.png")); 
		imageView.setX(430); 
		imageView.setY(50); 
		//tego fragmentu też xd
		imageView.setFitHeight(65); 
		imageView.setFitWidth(65);
		//imageView.setPreserveRatio(true); 

		Label status_text = new Label("Status: ");
		status_text.setLayoutX(430);
		status_text.setLayoutY(10);
		status = new Label(" "/*this.msg*/);
		/*new Thread(() -> {
        while (true) {
            try {
                Platform.runLater(() -> {
                  status.setText(i.status);
                });
                Thread.sleep(300);
            }
            catch (Exception e) {}
        }
		}).start();
*/

        status.setLayoutX(430);
		status.setLayoutY(25);
		
        Label label = new Label("Other Clients: ");
        label.setLayoutX(430);
		label.setLayoutY(180);//130

        ObservableList<String> options = 
		    FXCollections.observableArrayList(other);
		ComboBox clients = new ComboBox(options);
		clients.setLayoutX(430);
		clients.setLayoutY(200);//150
		clients.setPrefHeight(30);
		clients.setPrefWidth(100);

		Label error = new Label("");
        error.setLayoutX(430);
		error.setLayoutY(150);//105

        Button share = new Button("Share");
		share.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		    	boolean check = clients.getSelectionModel().isEmpty();
		    	boolean checkList = list.getSelectionModel().isEmpty();
		    	if(check){
		    		error.setText("Choose client");//client name\n and folder path//You didn't provide\n any parameters.
		    	}
		    	else if(checkList){
		    		error.setText("Choose file");
		    	}
		    	else{
		    		error.setText("");
		    		status.setText("Sharing file");
		    		try{ Discs.delay(5); } catch(Exception exc) {}
		    		slow = true;
		    		imageView.setImage(new Image("Slowpoke.gif"));
		    		//i tu uruchania updatecsv-nic więcej nie trzeba;
		    	}
		    }
		 });
		share.setLayoutX(430);
		share.setLayoutY(250);//200
		share.setPrefHeight(30);
		share.setPrefWidth(100);

		/*Button refresh = new Button("Refresh");
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		    }
		 });//
		refresh.setOnAction(e -> refresh());
		refresh.setLayoutX(430);
		refresh.setLayoutY(250);
		refresh.setPrefHeight(30);
		refresh.setPrefWidth(100);*/

		/*ImageView imageView = new ImageView(image); 
		imageView.setX(430); 
		imageView.setY(30); 
		//tego fragmentu też xd
		imageView.setFitHeight(65); 
		imageView.setFitWidth(65);
		//imageView.setPreserveRatio(true);*/

		root = new Group(list,imageView,status_text,status,label,error,clients,share,hyperlink);
		scene = new Scene(root ,550, 320);
		Color color = Color.web("#a3beed");//#3264dd
		stage.setTitle("Client " + user);
      	scene.setFill(color);
      	stage.setScene(scene); 
      	stage.show();      	
	}

   	@Override     
   	public void start(Stage stage) throws Exception {
   		//Loging in
   		GridPane grid = new GridPane();
   		Text t_name = new Text("Username:");
		TextField name = new TextField();
		name.setPromptText("Enter your username here");
		name.setPrefColumnCount(10);

		Text t_folder_path = new Text("Folder path:");
		TextField folder_path = new TextField();
		folder_path.setPromptText("Enter path to your folder");

		Label comment = new Label(" ");

		HBox buttons = new HBox();
		buttons.setSpacing(10);

		Button submit = new Button("Submit");
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        if(name.getText().isEmpty() && folder_path.getText().isEmpty()) {
		            comment.setText("You didn't provide any parameters.");//client name\n and folder path
		        }
		        else if(folder_path.getText().isEmpty()) {
		            comment.setText("You didn't provide folder path.");
		        }
		        else if(name.getText().isEmpty()) {
		            comment.setText("You didn't provide username.");
		        }
		        else {
		        	user = name.getText();
		        	path =  folder_path.getText();
		        	try{
		        		Client_Panel(stage);
		        	} catch(Exception exc){
		        		exc.printStackTrace();
		        		System.out.println("Error while lodaing client pannel");
		        	}
		        	//comment.setText("OK");
		        }
		    }
		 });

		Button clear = new Button("Clear All");
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        name.clear();
		        folder_path.clear();
		    }
		 });
		
		buttons.getChildren().addAll(clear, submit);
		//buttons.setAlignment(Pos.CENTER);
		//GridPane.setConstraints(submit, 2, 0);
		//grid.getChildren().add(submit);

      	//Creating a Group object  
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		//texts
		grid.add(t_name,0,0);
		grid.add(t_folder_path,0,1);
		//text fields
		grid.add(name,1,0);
		grid.add(folder_path,1,1);
		//buttons
		grid.add(buttons,1,2,2,1);
		//grid.add(clear,1,3);
		//grid.add(submit,1,4);
		//label.setAlignment(Pos.CENTER);
		grid.add(comment,0,2,2,10);

		/*Scene */scene = new Scene(grid ,350, 300); 
      	stage.setTitle("Log in");
      	Color color = Color.web("#a3beed");//#3264dd
      	scene.setFill(color);
      	stage.setScene(scene); 
      	stage.show();
		


   		//actual Client panel
 
      	//Scene scene = new Scene(root ,550, 320); 
      	/*stage.setTitle("Client");
      	Color color = Color.web("#a3beed");//#3264dd
      	scene.setFill(color);
      	stage.setScene(scene); 
      	stage.show();*/
   	}    
   	public static void main(String args[]){   
   		launch(args);
   	}         
} 

/*//Setting the title to Stage. 
      	
		Text t_name = new Text("Name:");
		TextField name = new TextField();
		name.setPromptText("Enter your name here");
		//name.setPrefColumnCount(10);
		//name.setMinWidth(100);
		//name.getText();
		//GridPane.setConstraints(name, 1, 0);
		//grid.getChildren().add(name);

		Text t_folder_path = new Text("Folder path:");
		TextField folder_path = new TextField();
		folder_path.setPromptText("Enter path to your folder");*/
		/*GridPane.setConstraints(folder_path, 1, 1);
		grid.getChildren().add(folder_path);*/
		/*
    	TextField nametext = new TextField();
        HBox hb = new HBox();
		hb.getChildren().addAll(label1, textField);
		hb.setSpacing(10);*/
		/*Label label = new Label(" ");

		HBox buttons = new HBox();
		buttons.setSpacing(10);

		Button submit = new Button("Submit");
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        if(name.getText().isEmpty() && folder_path.getText().isEmpty()) {
		            label.setText("You didn't provide any parameters.");//client name\n and folder path
		        }
		        else if((folder_path.getText() != null && !folder_path.getText().isEmpty())) {
		            label.setText("sTest");
		        }
		    }
		 });

		Button clear = new Button("Clear All");
		
		buttons.getChildren().addAll(clear, submit);
		//buttons.setAlignment(Pos.CENTER);
		//GridPane.setConstraints(submit, 2, 0);
		//grid.getChildren().add(submit);

      	//Creating a Group object  
      	GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		//texts
		grid.add(t_name,0,0);
		grid.add(t_folder_path,0,1);
		//text fields
		grid.add(name,1,0);
		grid.add(folder_path,1,1);
		//buttons
		grid.add(buttons,1,2,2,1);
		//grid.add(clear,1,3);
		//grid.add(submit,1,4);
		//label.setAlignment(Pos.CENTER);
		grid.add(label,0,2,2,10);*/

		/*
		//Defining the Clear button
		Button clear = new Button("Clear");
		GridPane.setConstraints(clear, 2, 1);
		grid.getChildren().add(clear);*/

		/*FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(hyperlink);
		*/