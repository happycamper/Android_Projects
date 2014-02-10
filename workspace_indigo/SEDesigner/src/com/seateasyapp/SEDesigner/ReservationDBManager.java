package com.seateasyapp.SEDesigner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReservationDBManager {
	
	public static final int GET_RESERVATIONS = 1;
	public static final int GET_SEATED_RESERVATIONS = 2;
	public static final int GET_COMPLETED_RESERVATIONS = 3;
	public static final int GET_CLEARED_RESERVATIONS = 4;
	
	private SQLiteDatabase database;
	  private ReservationDB dbHelper;
	  private String[] allColumns = {ReservationDB.FIRST_NAME, ReservationDB.LAST_NAME, ReservationDB.RESERVATION_NUMBER,
			  ReservationDB.TELEPHONE, ReservationDB.TIME_IN, ReservationDB.PARTY_NUMBER, ReservationDB.DATE, ReservationDB.WAIT_TIME,ReservationDB.RESERVATION_TIME,ReservationDB.RESERVATION_TYPE };
	  private String[] allSeatedColumns = {ReservationDB.FIRST_NAME, ReservationDB.LAST_NAME, ReservationDB.RESERVATION_NUMBER,
			  ReservationDB.TELEPHONE, ReservationDB.TIME_IN, ReservationDB.PARTY_NUMBER, ReservationDB.DATE, ReservationDB.WAIT_TIME, ReservationDB.SEATED_TABLE_NUMBER,ReservationDB.SEATED_TABLE_LAYOUT,ReservationDB.RESERVATION_TIME,ReservationDB.RESERVATION_TYPE };

	  public ReservationDBManager(Context context) {
	    dbHelper = new ReservationDB(context);
	  }

	  public void open(){
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void addReservation(Reservation reservation){
		  ContentValues values = new ContentValues();
		  values.put(ReservationDB.FIRST_NAME, reservation.getFirstName());
		  values.put(ReservationDB.LAST_NAME, reservation.getLastName());
		  values.put(ReservationDB.TELEPHONE, reservation.getTelephone());
		  values.put(ReservationDB.PARTY_NUMBER, reservation.getPartyNumber());
		  values.put(ReservationDB.TIME_IN, reservation.getTimeIn());
		  values.put(ReservationDB.DATE, getDate());
		  values.put(ReservationDB.WAIT_TIME, reservation.getWaitTime());
		  values.put(ReservationDB.RESERVATION_NUMBER, reservation.getReservationNumber());
		  values.put(ReservationDB.RESERVATION_TIME, reservation.getReservationTime());
		  values.put(ReservationDB.RESERVATION_TYPE, reservation.getReservationType());
		  long insertId = database.insert(ReservationDB.TABLE_RESERVATIONS, null, values);
	  }
	  
	  public void addClearedReservation(Reservation reservation){
		  ContentValues values = new ContentValues();
		  values.put(ReservationDB.FIRST_NAME, reservation.getFirstName());
		  values.put(ReservationDB.LAST_NAME, reservation.getLastName());
		  values.put(ReservationDB.TELEPHONE, reservation.getTelephone());
		  values.put(ReservationDB.PARTY_NUMBER, reservation.getPartyNumber());
		  values.put(ReservationDB.TIME_IN, reservation.getTimeIn());
		  values.put(ReservationDB.DATE, getDate());
		  values.put(ReservationDB.WAIT_TIME, reservation.getWaitTime());
		  values.put(ReservationDB.RESERVATION_NUMBER, reservation.getReservationNumber());
		  values.put(ReservationDB.RESERVATION_TIME, reservation.getReservationTime());
		  values.put(ReservationDB.RESERVATION_TYPE, reservation.getReservationType());
		  long insertId = database.insert(ReservationDB.CLEARED_RESERVATIONS, null, values);
	  }
	  
	  public void addSeatedReservation(Reservation reservation){
		  ContentValues values = new ContentValues();
		  values.put(ReservationDB.FIRST_NAME, reservation.getFirstName());
		  values.put(ReservationDB.LAST_NAME, reservation.getLastName());
		  values.put(ReservationDB.TELEPHONE, reservation.getTelephone());
		  values.put(ReservationDB.PARTY_NUMBER, reservation.getPartyNumber());
		  values.put(ReservationDB.TIME_IN, reservation.getTimeIn());
		  values.put(ReservationDB.DATE, getDate());
		  values.put(ReservationDB.WAIT_TIME, reservation.getWaitTime());
		  values.put(ReservationDB.SEATED_TABLE_NUMBER, reservation.getSeatedTableNumber());
		  values.put(ReservationDB.SEATED_TABLE_LAYOUT, reservation.getSeatedTableNumber());
		  values.put(ReservationDB.RESERVATION_TIME, reservation.getReservationTime());
		  values.put(ReservationDB.RESERVATION_TYPE, reservation.getReservationType());
		  long insertId = database.insert(ReservationDB.SEATED_RESERVATIONS, null, values);
	  }
	  
	  public void addCompletedReservation(Reservation reservation){
		  ContentValues values = new ContentValues();
		  values.put(ReservationDB.FIRST_NAME, reservation.getFirstName());
		  values.put(ReservationDB.LAST_NAME, reservation.getLastName());
		  values.put(ReservationDB.TELEPHONE, reservation.getTelephone());
		  values.put(ReservationDB.PARTY_NUMBER, reservation.getPartyNumber());
		  values.put(ReservationDB.TIME_IN, reservation.getTimeIn());
		  values.put(ReservationDB.DATE, getDate());
		  values.put(ReservationDB.TIME_OUT, getTime());
		  values.put(ReservationDB.WAIT_TIME, reservation.getWaitTime());
		  values.put(ReservationDB.SEATED_TABLE_NUMBER, reservation.getSeatedTableNumber());
		  values.put(ReservationDB.SEATED_TABLE_LAYOUT, reservation.getSeatedTableNumber());
		  values.put(ReservationDB.RESERVATION_TIME, reservation.getReservationTime());
		  values.put(ReservationDB.RESERVATION_TYPE, reservation.getReservationType());
		  long insertId = database.insert(ReservationDB.COMPLETED_RESERVATIONS, null, values);
	  }
	  
	  public void updateWaitTime(Reservation reservation,int time){
		  System.out.println("Updating Wait Time "+time);
		  ContentValues values = new ContentValues();
		  values.put(ReservationDB.WAIT_TIME, time);
		  database.update(ReservationDB.TABLE_RESERVATIONS, values, ReservationDB.RESERVATION_NUMBER + "= "+reservation.getReservationNumber()+"", null);
	  }
	  
	  
	 
	  
	  public Reservation getReservation(int number) {
	    Cursor cursor = database.query(ReservationDB.TABLE_RESERVATIONS,
	        allColumns, ReservationDB.RESERVATION_NUMBER + " = '" + number + "'", null,
	        null, null, null);
	    cursor.moveToFirst();
	    Reservation newReservation = cursor2Reservation(cursor);
	    cursor.close();
	    return newReservation;
	  }

	  public void deleteReservation(int number) {
	    //String name = rest.getName();
	    //System.out.println("Comment deleted with id: " + id);
	    database.delete(ReservationDB.TABLE_RESERVATIONS, ReservationDB.RESERVATION_NUMBER
	        + " = '" + number+"'", null);
	  }
	  
	  public void deleteClearedReservation(int number) {
		    //String name = rest.getName();
		    //System.out.println("Comment deleted with id: " + id);
		    database.delete(ReservationDB.CLEARED_RESERVATIONS, ReservationDB.RESERVATION_NUMBER
		        + " = '" + number+"'", null);
		  }
	  
	  public void deleteSeatedReservation(int number) {
		    //String name = rest.getName();
		    //System.out.println("Comment deleted with id: " + id);
		    database.delete(ReservationDB.SEATED_RESERVATIONS, ReservationDB.RESERVATION_NUMBER
		        + " = '" + number+"'", null);
		  }
	  
	  public void deleteCompletedReservation(int number) {
		    //String name = rest.getName();
		    //System.out.println("Comment deleted with id: " + id);
		    database.delete(ReservationDB.COMPLETED_RESERVATIONS, ReservationDB.RESERVATION_NUMBER
		        + " = '" + number+"'", null);
		  }
	  
	  public void clearReservationTable(){
		  database.delete(ReservationDB.TABLE_RESERVATIONS, null, null);
	  }
	  
	  public void clearClearedReservationTable(){
		  database.delete(ReservationDB.CLEARED_RESERVATIONS, null, null);
	  }
	  
	  public void clearSeatedReservationTable(){
		  database.delete(ReservationDB.SEATED_RESERVATIONS, null, null);
	  }
	  
	  public void clearCompletedReservationTable(){
		  database.delete(ReservationDB.COMPLETED_RESERVATIONS, null, null);
	  }
	  
	  public int getNumberOfReservations(){
		  Cursor cursor;
		  cursor = database.query(ReservationDB.TABLE_RESERVATIONS,
  		        allColumns, null, null, null, null, null);
		  return cursor.getCount();
	  }
	  
	  public int getNumberOfSeated(){
		  Cursor cursor;
		  int seatedNum = 0;
		  cursor = database.query(ReservationDB.SEATED_RESERVATIONS,
  		        allSeatedColumns, null, null, null, null, null);
		  seatedNum = cursor.getCount();
		  cursor = database.query(ReservationDB.COMPLETED_RESERVATIONS,
  		        allSeatedColumns, null, null, null, null, null);
		  seatedNum += cursor.getCount();
		  System.out.println("Seated "+ seatedNum);
		  return seatedNum;
	  }
	  
	  public int getAverageWaitTime(){
		  Cursor cursor;
		  int count = 0;
		  int total = 0;
		  int average = 0;
		  cursor = database.query(ReservationDB.TABLE_RESERVATIONS,
  		        allColumns, ReservationDB.PARTY_NUMBER + "= '4'", null, null, null, null);
		  cursor.moveToFirst();
  	    while (!cursor.isAfterLast()) {
  	    	++count;
  	    	total += Integer.valueOf(cursor.getString(7));
  	    	cursor.moveToNext();
  	    }
  	    if(count != 0){
  	    	average = total/count;
  	    	 return average;
  	    }else{
  	    	return average;
  	    }
  	   
	  }

	  public ArrayList<Reservation> getAllReservations(int choice) {
		  Cursor cursor;
		  
	    ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
	    
	    	switch(choice){
	    	case GET_RESERVATIONS:
	    		cursor = database.query(ReservationDB.TABLE_RESERVATIONS,
	    		        allColumns, null, null, null, null, null);
	    		cursor.moveToFirst();
	    	    while (!cursor.isAfterLast()) {
	    	      Reservation reservation = cursor2Reservation(cursor);
	    	      reservationList.add(reservation);
	    	      cursor.moveToNext();
	    	    }
	    		break;
	    	case GET_CLEARED_RESERVATIONS:
	    		cursor = database.query(ReservationDB.CLEARED_RESERVATIONS,
	    		        allColumns, null, null, null, null, null);
	    		cursor.moveToFirst();
	    	    while (!cursor.isAfterLast()) {
	    	      Reservation reservation = cursor2Reservation(cursor);
	    	      reservationList.add(reservation);
	    	      cursor.moveToNext();
	    	    }
	    		break;
	    	case GET_SEATED_RESERVATIONS:
	    		cursor = database.query(ReservationDB.SEATED_RESERVATIONS,
	    		        allSeatedColumns, null, null, null, null, null);
	    		cursor.moveToFirst();
	    	    while (!cursor.isAfterLast()) {
	    	      Reservation reservation = cursor2SeatedReservation(cursor);
	    	      reservationList.add(reservation);
	    	      cursor.moveToNext();
	    	    }
	    		break;
	    	case GET_COMPLETED_RESERVATIONS:
	    		cursor = database.query(ReservationDB.COMPLETED_RESERVATIONS,
	    		        allSeatedColumns, null, null, null, null, null);
	    		cursor.moveToFirst();
	    	    while (!cursor.isAfterLast()) {
	    	      Reservation reservation = cursor2SeatedReservation(cursor);
	    	      reservationList.add(reservation);
	    	      cursor.moveToNext();
	    	    }
	    		break;
	    		default:
	    			cursor = database.query(ReservationDB.TABLE_RESERVATIONS,
	    			        allColumns, null, null, null, null, null);
	    			cursor.moveToFirst();
	    		    while (!cursor.isAfterLast()) {
	    		      Reservation reservation = cursor2Reservation(cursor);
	    		      reservationList.add(reservation);
	    		      cursor.moveToNext();
	    		    }
	    			break;
	    	}
	    // Make sure to close the cursor
	    cursor.close();
	    return reservationList;
	  }

	  private Reservation cursor2Reservation(Cursor cursor) {
	    Reservation res = new Reservation();
	    res.setFirstName(cursor.getString(0));
	    res.setLastName(cursor.getString(1));
	    res.setReservationNumber(cursor.getInt(2));
	    res.setTelephone(cursor.getString(3));
	    res.setTimeIn(cursor.getString(4));
	    res.setPartyNumber(cursor.getString(5));
	    res.setDate(cursor.getString(6));
	    res.setWaitTime(cursor.getInt(7));
	    return res;
	  }
	  
	  private Reservation cursor2SeatedReservation(Cursor cursor) {
		    Reservation res = new Reservation();
		    res.setFirstName(cursor.getString(0));
		    res.setLastName(cursor.getString(1));
		    res.setReservationNumber(cursor.getInt(2));
		    res.setTelephone(cursor.getString(3));
		    res.setTimeIn(cursor.getString(4));
		    res.setPartyNumber(cursor.getString(5));
		    res.setDate(cursor.getString(6));
		    res.setWaitTime(cursor.getInt(7));
		    res.setSeatedTable(cursor.getString(9),cursor.getString(8));
		    res.setReservationType(cursor.getInt(11));
		    return res;
		  }
	  
	  public String getDate(){
			Calendar c =  Calendar.getInstance();
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(c.get(c.MONTH)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.DAY_OF_MONTH)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.YEAR)));
			sb.append("_");
			/*sb.append(String.valueOf(c.get(c.HOUR_OF_DAY)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.MINUTE)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.SECOND)));*/
			
			return sb.toString();
		}
	  
	  public String getTime(){
		  Calendar c =  Calendar.getInstance();
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(c.get(c.HOUR_OF_DAY)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.MINUTE)));
			sb.append("_");
			sb.append(String.valueOf(c.get(c.SECOND)));
			
			return sb.toString();
		  
	  }
	  
	  public int getNewReservationNumber(){
		  String temp = getTime();
		 temp = temp.replaceAll("_", "");
		  return Integer.valueOf(temp);
	  }

}
