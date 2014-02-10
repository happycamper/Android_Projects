package com.seateasyapp.SEDesigner;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReservationDB extends SQLiteOpenHelper {
	  
	  
	  public static final String TABLE_RESERVATIONS = "reservations";
	  public static final String FIRST_NAME = "first_name";
	  public static final String LAST_NAME = "last_name";
	  public static final String RESERVATION_NUMBER = "reservation_number";
	  public static final String TELEPHONE = "telephone";
	  public static final String TIME_IN = "time_in";
	  public static final String PARTY_NUMBER = "party_number";
	  public static final String DATE = "date";
	  public static final String WAIT_TIME = "wait_time";
	  public static final String SEATED_TABLE_NUMBER = "seated_table";
	  public static final String SEATED_TABLE_LAYOUT = "seated_table_layout";
	  public static final String RESERVATION_TIME = "reservation_time";
	  public static final String RESERVATION_TYPE = "reservation_type";
	  
	  public static final String SEATED_RESERVATIONS = "seated_reservations";
	  public static final String COMPLETED_RESERVATIONS = "completed_reservations";
	  public static final String CLEARED_RESERVATIONS = "cleared_reservations";
	  public static final String TIME_OUT = "time_out";
	  

	  private static final String DATABASE_NAME = "se_designer_v7.db";
	  private static final int DATABASE_VERSION = 1;

	  // Restaurant table to store local data for application to read - populated through socket
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_RESERVATIONS + "(" + FIRST_NAME
	      + " text, " + LAST_NAME +" text, " + RESERVATION_NUMBER +" int, " + TELEPHONE + " text," +
	      TIME_IN + " text, "+ PARTY_NUMBER + " text, "+ DATE + " text, "+ WAIT_TIME + " text,"+ RESERVATION_TIME + " text,"+ RESERVATION_TYPE + " int);";
	  
	  private static final String CLEARED_CREATE = "create table "
		      + CLEARED_RESERVATIONS + "(" + FIRST_NAME
		      + " text, " + LAST_NAME +" text, " + RESERVATION_NUMBER +" int, " + TELEPHONE + " text," +
		      TIME_IN + " text, "+ PARTY_NUMBER + " text, "+ DATE + " text, "+ WAIT_TIME + " text,"+ RESERVATION_TIME + " text,"+ RESERVATION_TYPE + " int);";
	  
	  private static final String SEATED_CREATE = "create table "
		      + SEATED_RESERVATIONS + "(" + FIRST_NAME
		      + " text, " + LAST_NAME +" text, " + RESERVATION_NUMBER +" int, " + TELEPHONE + " text," +
		      TIME_IN + " text, "+ PARTY_NUMBER + " text, "+ DATE + " text, "+ WAIT_TIME + " text, "+ SEATED_TABLE_NUMBER + " text,"+ SEATED_TABLE_LAYOUT + " text,"+ RESERVATION_TIME + " text,"+ RESERVATION_TYPE + " int);";
	  
	  private static final String COMPLETED_CREATE = "create table "
		      + COMPLETED_RESERVATIONS + "(" + FIRST_NAME
		      + " text, " + LAST_NAME +" text, " + RESERVATION_NUMBER +" int, " + TELEPHONE + " text," +
		      TIME_IN + " text, "+ PARTY_NUMBER + " text, "+ TIME_OUT + " text, "+ DATE + " text, "+ WAIT_TIME + " text, "+ SEATED_TABLE_NUMBER + " text,"+ SEATED_TABLE_LAYOUT + " text,"+ RESERVATION_TIME + " text,"+ RESERVATION_TYPE + " int);";
	  

	  public ReservationDB(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    	database.execSQL(DATABASE_CREATE);
	    	database.execSQL(SEATED_CREATE);
	    	database.execSQL(COMPLETED_CREATE);
	    	database.execSQL(CLEARED_CREATE);
	   
	   // database.execSQL(DATABASE_CREATE_USER);
	  
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(ReservationDB.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
	    db.execSQL("DROP TABLE IF EXISTS " + SEATED_RESERVATIONS);
	    db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_RESERVATIONS);
	    db.execSQL("DROP TABLE IF EXISTS " + CLEARED_RESERVATIONS);
	    onCreate(db);
	  }


}
