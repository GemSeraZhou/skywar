package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class BattleSky implements Initializable{

	private BattleSkyCallback callback = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// nah!!!
	}
	
	@FXML
	private void enter(MouseEvent event){
		if(this.callback != null) callback.enter();
	}
	
	public void setCallback(BattleSkyCallback callback){
		this.callback = callback;
	}
	
	public interface BattleSkyCallback{
		public void enter();
	}

}
