package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Bullet;
import model.Plane;
import model.Speed;
import model.Plane.Direction;
import model.Plane.Type;

public class Game implements Initializable{

	@FXML
	private Pane frame;
	
	@FXML
	private ImageView cannon;
	
	@FXML 
	private Slider connon_slider;
	
	@FXML
	private Button play_btn, exit_btn;
	
	@FXML
	private Label game_over_lbl,  bullets_left_lbl, score_lbl, score_value_lbl, planes_destroyed_lbl; //, planes_left_lbl;
	
	private int planes_destroyed = 0, bullets_left = 10 , succesfull_shots = 0;
	
	private Plane currentPlane;
	
	/**
	 * multiple threads try to access the current bullets. sync them using planeSync
	 * */
	private final Object bulletSync = new Object();
	
	/**
	 * bullets currently moving in the frame
	 * */
	private ArrayList<Bullet> currentBullets = new ArrayList<Bullet>();
	
	private int bulletsPerShot = 1;
	
	private GameCallback callback = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		connon_slider.valueProperty().addListener( (ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {       	
                	// set the rotation of the cannon
                	cannon.setRotate(90 - new_val.intValue());
        });
			
		game_over_lbl.setVisible(false);
		score_lbl.setVisible(false);
		score_value_lbl.setVisible(false);
		
		unsetGame();
	}
	
	public void setPreferences(Type type, Speed speed, int bullet_count){
		Plane.setType(type);
		Bullet.setSpeed(speed);
		bulletsPerShot = bullet_count;
	}
	
	public void setCallback(GameCallback callback){
		this.callback = callback;
	}
	
	/**
	 * runs each iteration of the game on a different thread
	 * */
	private void exec(){
		
		Platform.runLater(() -> {
			
			if(!currentPlane.isActive()) resetPlane();
			
			synchronized(bulletSync){
				
				if(bullets_left < 0) {
					gameOver();
					return;
				}
			
				for(Bullet bullet : currentBullets){ // see if some bullet hit the plane
					if(bullet.getView().getBoundsInParent().intersects(currentPlane.getView().getBoundsInParent())){
						// aha! shot
						succesfull_shots++;
						this.planes_destroyed++;
						planes_destroyed_lbl.setText(Integer.toString(this.planes_destroyed));
						frame.getChildren().remove(bullet.getView());
						frame.getChildren().remove(currentPlane.getView());
						resetPlane();
		
					}
				}	
			}
			
			// if you are here, game is not over... so try again
			exec();
			
		});
		
	}
	
	
	/**
	 * fly a new plane
	 * */
	private void resetPlane(){
		
		//planes_left_lbl.setText(Integer.toString(planes_left + 1));
		
		// find random direction
		Direction dir = Direction.WEST;
		if(randomInt(0,100) % 2 == 0) dir = Direction.EAST;
		
		this.currentPlane = new Plane(randomInt(2,7) , randomInt(10 , 200), dir);
		
		frame.getChildren().add(this.currentPlane.getView());
		
		this.currentPlane.move();   // fly
		
	}
	
	private void gameOver(){
		
		// calculate and save score. beware of divide by zero
		float score = bullets_left == 10 ? 0 : succesfull_shots * 1000 / (10 - this.bullets_left);
		if(callback != null) callback.saveScore(score);
		score_value_lbl.setText(Integer.toString((int) score));
		
		unsetGame();
		
		Platform.runLater(() ->{
			frame.getChildren().remove(currentPlane.getView());
			for(Bullet bullet : currentBullets) frame.getChildren().remove(bullet.getView());
		});
		
		play_btn.setVisible(true);
		exit_btn.setVisible(true);
		game_over_lbl.setVisible(true);
		score_lbl.setVisible(true);
		score_value_lbl.setVisible(true);
	}
	
	@FXML
	private void exit(MouseEvent event){
		Platform.exit();
	}
	
	@FXML
	private void play(MouseEvent event){
		play_btn.setVisible(false);
		exit_btn.setVisible(false);
		game_over_lbl.setVisible(false);
		
		score_lbl.setVisible(false);
		score_value_lbl.setVisible(false);
		
		resetGame();
		resetPlane();
		exec();
	}
	
	// shoot using space bar
	@FXML
	private void shoot2(KeyEvent event){
		
		if(event.getCode().equals(KeyCode.SPACE)) shoot(null);
		
	}
	
	// shoot using mouse, and/or other wise
	@FXML
	private void shoot(MouseEvent event){
		
		synchronized(bulletSync){
			
			// should not be able to shoot bullet when one is active
			for(Bullet bullet : currentBullets){
				if(bullet.isActive()) return;
			}
			
			bullets_left -= this.bulletsPerShot;
			if(bullets_left < 0) return;
			
			// set text board
			bullets_left_lbl.setText(Integer.toString(bullets_left));
			
			
			currentBullets.clear();
			
			for (int i = 0 ; i < this.bulletsPerShot ; i++){
				
				// find the initial position of the bullet
				int rad = 50 - i * 20;
				
				// each bullet is shot 10 pixels behind of other
				// just random numbers found by trial and error
				double x = 430 + i * 20 + rad - rad * Math.cos(toRad(cannon.getRotate()));
				double y = 403 + rad * Math.sin(toRad(cannon.getRotate()));
				
				Bullet bullet = new Bullet(y, x, toRad(connon_slider.getValue()));
				frame.getChildren().add(bullet.getView());
				bullet.move();
				currentBullets.add(bullet);
			}
		}
		
	}
	
	private void resetGame(){
		this.bullets_left = 10;
		this.planes_destroyed = 0;
		this.connon_slider.setDisable(false);
		//planes_left_lbl.setText("5");
		bullets_left_lbl.setText("10");
		succesfull_shots = 0;
	}
	
	private void unsetGame(){
		this.bullets_left = -1;
		this.planes_destroyed = -1;
		this.connon_slider.setDisable(true);
		planes_destroyed_lbl.setText("0");
		//planes_left_lbl.setText("0");
		bullets_left_lbl.setText("0");
	}
	
	// degree to radian
	private double toRad(double degree){
		return Math.PI / 180 * degree;
	}
	
	// generate a random int between low and high
	private int randomInt(int low, int high){
		Random r = new Random();
		return r.nextInt(high-low) + low;
	}
	
	public interface GameCallback{
		public void saveScore(float score);
	}

}
