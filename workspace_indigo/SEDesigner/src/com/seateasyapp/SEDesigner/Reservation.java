package com.seateasyapp.SEDesigner;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class Reservation{
	
	private String firstName,lastName,telephone,timeIn,partyNumber,date,seatedTableNumber,seatedTableLayout,reservationTime;
	private int reservationNumber,waitTime,timeSeated,reservationType;
	private Boolean isSeated,isCompleted,waitTimeSet;
	public Button waitTimeText;
	public TextView seatedTimeText;
	
	public static final int WALK_IN = 1;
	public static final int CALL_AHEAD = 2;
	public static final int SEAT_EASY_APP = 3;
	
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
		seatedTableNumber = "";
		seatedTableLayout = "";
		date = "";
		reservationTime = "";
		reservationType = 0;
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
		seatedTableNumber = "";
		seatedTableLayout = "";
		timeSeated = 0;
		waitTimeSet = false;
	}
	
	public void setReservationTime(int hour, int minute){
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(hour));
		sb.append(":");
		sb.append(String.valueOf(minute));
		String time = sb.toString();
		reservationTime = time;
	}
	
	public String getReservationTime(){
		return reservationTime;
	}
	
	public void setReservationType(int type){
		reservationType = type;
	}
	
	public int getReservationType(){
		return reservationType;
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
	
	public void setSeatedTable(String tableLayout, String tableNumber){
		seatedTableNumber = tableNumber;
		seatedTableLayout = tableLayout;
	}
	
	public void setWaitTimeText(int time){
		waitTimeText.setText("Wait Time\n"+String.valueOf(time)+"\nminutes");
	}
	
	public String getSeatedTableNumber(){
		return seatedTableNumber;
	}
	
	public String getSeatedTableLayout(){
		return seatedTableLayout;
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
