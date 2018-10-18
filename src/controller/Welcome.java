package controller;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Welcome implements Initializable{

	@FXML
	private TextField firstName, lastName;
	
	private WelcomeCallback callback = null;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// duh..
	}
	
	@FXML
	private void enter(MouseEvent event){
		
		if(firstName.getText().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Information Missing");
			alert.setHeaderText("First name can not be empty");
			alert.show();
			return;
		}
		
		if(lastName.getText().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Information Missing");
			alert.setHeaderText("Last name can not be empty");
			alert.show();
			return;
		}
		
	 	if (callback != null) callback.userSelected(firstName.getText(), lastName.getText());
		
	}
	
	public void setCallback(WelcomeCallback callback){
		this.callback = callback;
	}
	
	public interface WelcomeCallback{
		public void userSelected(String firstname, String lastName);
	}
	
}
