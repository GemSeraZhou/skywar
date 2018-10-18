package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import data.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecordsTable implements Initializable{
	
	@FXML
	private TableView<Record> table;

	@FXML 
	private TableColumn<Record,String> first_name , last_name;
	
	@FXML
	private TableColumn<Record,  Integer> score;
	
	@FXML
	private TableColumn<Record,  String> date;
	
	private ObservableList<Record> data;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// attach class vars to table columns
		first_name.setCellValueFactory( new PropertyValueFactory<Record, String>("firstName"));
		last_name.setCellValueFactory( new PropertyValueFactory<Record, String>("lastName"));
		score.setCellValueFactory( new PropertyValueFactory<Record, Integer>("score"));
		date.setCellValueFactory( new PropertyValueFactory<Record, String>("date"));
	
		data = FXCollections.observableArrayList();
		table.setItems(data);
	}
	
	public void addRecords(ArrayList<Record> records){
		data.addAll(records);
	}

}
