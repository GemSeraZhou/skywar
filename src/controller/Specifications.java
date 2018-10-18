package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import model.Plane;
import model.Plane.Type;
import model.Speed;

public class Specifications implements Initializable{

	@FXML 
	private ComboBox<String> plane_type, speed;
	
	@FXML 
	private ComboBox<Integer> bulltes_count;
	
	private SpecificationsCallback callback = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		ObservableList<String> plane_type_list = FXCollections.observableArrayList(
		        "Symbolic",
		        "Static",
		        "Animated"
		    );
		
		plane_type.setItems(plane_type_list);
		plane_type.getSelectionModel().select(0);  // default: Symbolic
		
		ObservableList<String> speed_list = FXCollections.observableArrayList(
		        "Low",
		        "Medium",
		        "High"
		    );
		
		speed.setItems(speed_list);
		speed.getSelectionModel().select(1);  // default: Medium
		
		ObservableList<Integer> bulltes_count_list = FXCollections.observableArrayList(
		        1,
		        2,
		        3
		    );
		
		bulltes_count.setItems(bulltes_count_list);
		bulltes_count.getSelectionModel().select(0);  // default: 1
		
	}
	
	@FXML
	private void play(MouseEvent event){
		Plane.Type type;
		
		switch (plane_type.getSelectionModel().getSelectedIndex()){
		default:
			type = Type.SYMBOLIC;
			break;
		case 1:
			type = Type.STATIC;
			break;
		case 2:
			type = Type.ANIMATED;
			break;
		}
		
		Speed speed1;
		
		switch(speed.getSelectionModel().getSelectedIndex()){
		default:
			speed1 = Speed.MEDIUM;
			break;
		case 0:
			speed1 = Speed.LOW;
			break;
		case 2:
			speed1 = Speed.HIGH;
			break;
		}
		
		int bullet_count = bulltes_count.getSelectionModel().getSelectedItem();
		
		if (callback != null) callback.specSelected(type, speed1, bullet_count);
		
	}
	
	public void setCallback(SpecificationsCallback callback){
		this.callback = callback;
	}
	
	public interface SpecificationsCallback{
		public void specSelected(Plane.Type type, Speed speed, int bullet_count);
	}

}
