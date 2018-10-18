package application;

import controller.Specifications;
import controller.Specifications.SpecificationsCallback;
import controller.Welcome.WelcomeCallback;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import model.Plane.Type;
import model.Speed;
import controller.Welcome;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import controller.BattleSky.BattleSkyCallback;
import controller.Game;
import controller.Game.GameCallback;
import controller.RecordsTable;
import controller.BattleSky;
import data.Record;
import data.RecordManager;;


public class Driver extends Application implements SpecificationsCallback, WelcomeCallback, 
GameCallback, BattleSkyCallback{

	private BorderPane root = new BorderPane();

	// holders to pass values from one scene to another
	private String firstName, lastName;

	@Override
	public void start(Stage stage) throws Exception {


		Scene scene = new Scene(root);
		// add some style
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Battle Sky");
		stage.setWidth(800);
		stage.setHeight(650);
		stage.setResizable(false);

		stage.show();

		// compose file menu
		Menu fileMenu = new Menu("File");

		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(actionEvent -> {

			// load the welcome for the game
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/BattleSky.fxml"));	
				Pane pane = loader.load();
				BattleSky controller = loader.getController();
				setBackground(pane, "/img/battlesky.jpg");
				controller.setCallback(this);
				root.setCenter(pane);
			} catch (Exception e) {

			}

		});

		MenuItem exitGame = new MenuItem("Exit");
		exitGame.setOnAction(actionEvent -> Platform.exit());
		
		MenuItem instruc = new MenuItem("Instructions");
		instruc.setOnAction(actionEvent -> {
			try{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/HowTo.fxml"));	
				Pane pane = loader.load();
				setBackground(pane, "/img/battlesky.jpg");
				root.setCenter(pane);
			}catch(Exception io){
				
			}
		});
		
		MenuItem highScore = new MenuItem("High Scores");
		highScore.setOnAction(actionEvent -> {

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/RecordsTable.fxml"));	
				Pane pane = loader.load();
				setBackground(pane, "/img/battlesky.jpg");
				RecordsTable controller = loader.getController();
				controller.addRecords(RecordManager.getRecords());

				root.setCenter(pane);
			} catch (Exception e) {

			}

		});


		MenuItem aboutMenu = new MenuItem("About");
		aboutMenu.setOnAction(actionEvent -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/Credits.fxml"));	
				Pane pane = loader.load();
				setBackground(pane, "/img/battlesky.jpg");
				root.setCenter(pane);
			} catch (Exception e) {

			}
		});

		fileMenu.getItems().addAll(newGame ,highScore , instruc , aboutMenu ,exitGame);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu);

		root.setTop(menuBar);

		newGame.fire(); // start new game
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void specSelected(Type type, Speed speed, int bullet_count) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/Game.fxml"));	
			Pane pane = loader.load();
			Game controller = loader.getController();
			controller.setCallback(this);
			setBackground(pane, "/img/starwars_geonosis_3.jpg");
			controller.setPreferences(type, speed, bullet_count);
			root.setCenter(pane);
		} catch (Exception e) {

		}
	}


	// user is done entering names
	@Override
	public void userSelected(String firstName, String lastName) {

		this.firstName = firstName;
		this.lastName = lastName;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/Specifications.fxml"));	
			Pane pane = loader.load();
			Specifications controller = loader.getController();
			controller.setCallback(this);
			setBackground(pane, "/img/co-op_battle_dl2_01.jpg");
			root.setCenter(pane);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void saveScore(float score) {
		Record record = new Record(this.firstName , this.lastName , (int) score , 
				new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()));

		RecordManager.addRecord(record);  // save record to file

	}

	@Override
	public void enter() {

		// load the welcome for the game
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/model/Welcome.fxml"));	
			Pane pane = loader.load();
			setBackground(pane,"/img/battle-art-war-planes-picture.jpg");
			Welcome controller = loader.getController();
			controller.setCallback(this);
			root.setCenter(pane);
		} catch (Exception e) {

		}
	}
	
	private void setBackground(Pane pane, String url){
		Image img = new Image(url);
		BackgroundImage bgImg = new BackgroundImage(img, 
		    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
		    BackgroundPosition.DEFAULT, 
		    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));

		pane.setBackground(new Background(bgImg));
	}

}
