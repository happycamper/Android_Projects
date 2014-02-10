package com.seateasyapp.SEDesigner;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class Reservation{
	
	private String firstName,lastName,telephone,timeIn,partyNumber,date,seatedTable;
	private int reservationNumber,waitTime,timeSeated;
	private Boolean isSeated,isCompleted,waitTimeSet;
	public Button waitTimeText;
	public TextView seatedTimeText;
	
	public Reservation(Context context) {
		// TODO Auto-generated constructor stub
		firstName = "";
		lastName = "";
		telephone = "";
		timeIn = "";
		partyNumber = "";
		isSeated = isCompleted = false;
		reservationNumber = 0;
		waitTime = 0;
		seatedTable = "";
		date = "";
		timeSeated = 0;
		waitTimeSet = false;
		
		waitTimeText = new Button(context);
		seatedTimeText = new TextView(context);
	}
	
	public Reservation() {
		// TODO Auto-generated constructor stub
		firstName = "";
		lastName = "";
		telephone = "";
		timeIn = "";
		partyNumber = "";
		date = "";
		isSeated = isCompleted = false;
		reservationNumber = 0;
		waitTime = 0;
		seatedTable = "";
		timeSeated = 0;
		waitTimeSet = false;
	}
	
	public void setFirstName(String name){
		firstName = name;
	}
	
	public void setSeatedTimeText(int time){
		seatedTimeText.setText("Seated Time\n"+ time+"\nminutes");
	}
	
	public void setDate(String Date){
		date = Date;
	}
	
	public void setSeatedTable(String tableNumber){
		seatedTable = tableNumber;
	}
	
	public void setWaitTimeText(int time){
		waitTimeText.setText("Wait Time\n"+String.valueOf(time)+"\nminutes");
	}
	
	public String getSeatedTable(){
		return seatedTable;
	}
	
	public void setReservationNumber(int number){
		reservationNumber = number;
	}
	
	public boolean getIsWaitTimeSet(){
		return waitTimeSet;
	}
	
	public void setWaitTimeBoolean(Boolean bool){
		waitTimeSet = bool;
	}
	
	public void setWaitTime(int wait){
		waitTime = wait;
	}
	
	public int getWaitTime(){
		return waitTime;
	}
	
	public void subtractWaitTime(){
		--waitTime;
	}
	
	public int getReservationNumber(){
		return reservationNumber;
	}
	public void setIsSeated(Boolean seated){
		isSeated = seated;
	}
	
	public void setIsCompleted(Boolean completed){
		isCompleted = completed;
	}
	
	public Boolean getIsSeated(){
		return isSeated;
	}
	
	public void setLastName(String name){
		lastName = name;
	}
	
	public void setTelephone(String number){
		telephone = number;
	}
	
	public void setTimeIn(String time){
		timeIn = time;
	}
	
	public void setPartyNumber(String number){
		partyNumber = number;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getTelephone(){
		return telephone;
	}
	
	public String getPartyNumber(){
		return partyNumber;
	}
	
	public String getTimeIn(){
		return timeIn;
	}
	
	
	public int getTimeSeated(){
		return timeSeated;
	}
	
	public void addTimeSeated(){
		++timeSeated;
	}
	
	public Boolean getIsCompleted(){
		return isCompleted;
	}
	
	




}
