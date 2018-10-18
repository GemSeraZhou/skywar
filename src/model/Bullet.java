package model;


import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Bullet {
	
	/**
	 * Dimensions of the frame of the game. Bullet needs to know when it is out of frame
	 * */
	private static double frameWidth = 800;		// Default 800
	private static double frameHeight = 600;
	
	/**
	 * in pixel / millisec
	 * 
	 * low : 3
	 * medium : 4
	 * fast : 5
	 * */
	private static double speed = 4;		
	
	/**
	 * Acceleration : 0.0098 pixel / millisec * millisec
	 * */
	private final static double gravity = 9.8 / 1000;
	
	/**
	 * Size of the bullet
	 * */
	private final static double radius = 5;
	
	/**
	 * the initial center of the bullet(before shoot)
	 * X and Y are stored as in mathematical graph system
	 * */
	private final double X;
	private final double Y;
	
	/**
	 * the angle of the cannon in rad.
	 * */
	private final double angle;
	
	/**
	 * FX view of the bullet
	 * */
	private Circle view;
	
	// if the bullet is in action
	private boolean active;
	
	public Bullet(double X, double Y, double angle){
		
		active = true;
		
		// careful ..
		this.X = X;
		this.Y = frameHeight - Y;
		this.angle = angle;
		
		// set view of the bullet
		view = new Circle(radius);
		view.setFill(Paint.valueOf("black"));
		view.setCenterX(X);
		view.setCenterY(Y);
	}
	
	public static void setFrameDimentions(double width, double height){
		frameWidth = width;
		frameHeight = height;
	}
	
	public static void setSpeed(Speed s){
		switch(s){
		case HIGH:
			speed = 5;
			break;
		case LOW:
			speed = 3;
			break;
		case MEDIUM:
			speed = 4;
			break;
		}
	}
	
	public Node getView(){
		return view;
	}
	
	public boolean isActive(){
		return this.active;
	}
	
	/**
	 * Animate the shooting of the bullet. the function returns immediately and bullet starts moving 
	 * along the trajectory according to the set angle and speed.
	 * 
	 * Add bullet to the frame before calling this
	 * */
	public void move(){
		
		AnimationTimer timer = new AnimationTimer(){
			
			// starting time of the animation
			private long startAt;
			
			// time since start of animation in milliec
			private long time = -1;
			
			@Override
			public void handle(long sysTime) {
				
				if(time == -1){
					startAt = sysTime;
					time = 0;
				} else {
					time = (sysTime - startAt) / 10000000;
				}
				
				// x and y are calculated in Mathematics graph system. X increasing rightwards , Y increasing up
				double dx = Math.cos(angle) * speed * time;
				double y = Y + dx * Math.tan(angle) - ( 
												( gravity * Math.pow(dx, 2) ) / 
												( 2* Math.pow(speed, 2) * Math.pow(Math.cos(angle), 2) )
											);
				
				double x = X + dx;
				
				double pixelX = x;
				double pixelY = frameHeight - y;
				
				view.setCenterY(pixelY);
				view.setCenterX(pixelX);
						
				// if the bullet has moved outside the frame.. 
				if(pixelX - radius > frameWidth || pixelY - radius > frameHeight
						|| pixelX + radius < 0 || pixelY + radius < 0 ) {
					stop();		// stop moving
					active = false;		
					view.setVisible(false);  // hide your self
				}
			}
        };
        
        timer.start();
	}
}
