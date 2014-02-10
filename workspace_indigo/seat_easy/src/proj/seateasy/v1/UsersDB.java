package proj.seateasy.v1;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDB extends SQLiteOpenHelper {
	  
	  
	  public static final String TABLE_USERS = "users";
	  public static final String COLUMN_FIRSTNAME = "first_name";
	  public static final String COLUMN_LASTNAME = "last_name";
	  public static final String COLUMN_PASS = "password";
	  public static final String COLUMN_PHONE = "phone";
	  public static final String COLUMN_EMAIL = "email";
	  public static final String COLUMN_CUSTOMID = "custom_id";
	  private SQLiteDatabase DB;
	  

	  private static final String DATABASE_NAME = "seat_easy_final.db";
	  private static final int DATABASE_VERSION = 1;

	  // Restaurant table to store local data for application to read - populated through socket
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_USERS + "(" + COLUMN_FIRSTNAME
	      + " text, " + COLUMN_LASTNAME +" text, " + COLUMN_PASS +" text, " + COLUMN_PHONE + " text," +
	      COLUMN_EMAIL + " text, "+ COLUMN_CUSTOMID + " text);";
	  

	  public UsersDB(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	  
	  public void createTable(SQLiteDatabase DB){
		  try{
		  DB.execSQL(DATABASE_CREATE);
		  }catch(SQLiteException e){
			  
		  }
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
		  try{
	    	database.execSQL(DATABASE_CREATE);
		  }catch(SQLiteException e){
			  e.printStackTrace();
		  }
	   // database.execSQL(DATABASE_CREATE_USER);
	  
	  }
	  

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(UsersDB.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
	    onCreate(db);
	  }


}
