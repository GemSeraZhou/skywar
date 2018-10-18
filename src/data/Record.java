package data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * single record of a game result
 * */
public class Record {

	/**
	 * first name of the scorer
	 * */
	private final SimpleStringProperty firstName = new SimpleStringProperty();
	
	/**
	 * last name of the scorer
	 * */
	private final SimpleStringProperty lastName = new SimpleStringProperty();
	
	/**
	 * value of the score
	 * */
	private final SimpleIntegerProperty score = new SimpleIntegerProperty();
	
	/**
	 * well, the date
	 * **/
	private final SimpleStringProperty date = new SimpleStringProperty();
	
	
	public Record(String firstName , String lastName, Integer score, String date){
		this.firstName.set(firstName);
		this.lastName.set(lastName);
		this.score.set(score);
		this.date.set(date);
	}


	public String getFirstName() {
		return firstName.get();
	}


	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}


	public String getLastName() {
		return lastName.get();
	}


	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}


	public Integer getScore() {
		return score.get();
	}


	public void setScore(Integer score) {
		this.score.set(score);
	}


	public String getDate() {
		return date.get();
	}


	public void setDate(String date) {
		this.date.set(date);
	}
	
}
