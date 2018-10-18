package model;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plane {

	/**
	 * view type of the plane
	 * */
	public enum Type {
		STATIC , SYMBOLIC, ANIMATED
	}

	public enum Direction{
		EAST , WEST
	}

	/**
	 * Dimensions of the frame of the game. Plane needs to know when it is out of frame
	 * */
	private static double frameWidth = 800;		// Default 800
	private static double frameHeight = 600;

	private static Type type = Type.STATIC;

	// view of the plane
	private ImageView view;

	/**
	 * speed in pixels / millisec
	 * */
	private int speed;

	/**
	 * height of topline of the plane (from the top of the screen)
	 * */
	private int height;

	/**
	 * direction of the plane in first iteration
	 * */
	private Direction inDir;

	// if the plane is in action
	private boolean active;
	
	public Plane(int speed , int height, Direction inDir){
		
		active = true;
		
		this.speed = speed;
		this.height = height;
		this.inDir = inDir;

		view = new ImageView();

		switch(type){
		case ANIMATED:
			view.setImage(new Image("img/plane_animated.gif"));
			break;
		case STATIC:
			view.setImage(new Image("img/plane_static.png"));
			break;
		case SYMBOLIC:
			view.setImage(new Image("img/plane_symbolic.png"));
			break;

		}

		view.setFitWidth(100);
		view.setFitHeight(50);

		switch(inDir){
		case EAST:
			// park left of the frame at desired height
			view.setX(-view.getFitWidth());
			view.setY(height);
			break;
		case WEST:
			// park right of the frame at desired height
			view.setX(frameWidth);
			view.setY(height);

			view.setScaleX(-1); 		// face the plane towards west
			break;

		}

	}

	public boolean isActive(){
		return this.active;
	}
	
	public void move(){

		AnimationTimer timer = new AnimationTimer(){

			// starting time of the animation
			private long startAt;

			// time since start of animation in milliec
			private long time = -1;
			
			private boolean first = true;

			@Override
			public void handle(long sysTime) {

				if(time == -1){
					startAt = sysTime;
					time = 0;
				} else {
					time = (sysTime - startAt) / 10000000;
				}

				switch(inDir){
				case EAST:
					view.setX(view.getX() + speed);
					break;
				case WEST:
					view.setX(view.getX() - speed);
					break;
				}
				
				
				// do the second cycle as well
				if(view.getX() < 0-view.getFitWidth() || view.getX() > frameWidth){
					
					if(first){
						view.setScaleX(-view.getScaleX());		// switch face
						inDir = inDir == Direction.EAST ? Direction.WEST : Direction.EAST;	// switch direction
						first = false;
					} else {
						stop();
						active = false;
					}
					
				}

			}
		};

		timer.start();

	}

	public Node getView(){
		return view;
	}

	public static void setType(Type type){
		Plane.type = type;
	}

	public static void setFrameDimentions(double width, double height){
		frameWidth = width;
		frameHeight = height;
	}

}

