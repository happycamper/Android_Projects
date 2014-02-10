package proj.seateasy.v1;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RestaurantDB extends SQLiteOpenHelper {
	  
	  
	  public static final String TABLE_RESTAURANTS = "restaurants";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "restaurant_name";
	  public static final String COLUMN_LAT = "map_lat";
	  public static final String COLUMN_LON = "map_long";
	  public static final String COLUMN_HASH = "hashtag";
	  public static final String COLUMN_RATE = "rating";
	  public static final String COLUMN_PROMO = "promo";
	  
	  public static final String TABLE_USER = "user_creds";
	  public static final String COLUMN_USER_ID = "_id";
	  public static final String COLUMN_USER_NAME = "first_name";
	  public static final String COLUMN_USER_LAST_NAME = "last_name";
	  public static final String COLUMN_PHONE = "phone_number";
	  

	  private static final String DATABASE_NAME = "seat_easy_final.db";
	  private static final int DATABASE_VERSION = 1;

	  // Restaurant table to store local data for application to read - populated through socket
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_RESTAURANTS + "(" + COLUMN_NAME
	      + " text, " + COLUMN_LAT +" text, " + COLUMN_LON +" text, " + COLUMN_HASH + " text," +
	      COLUMN_RATE + " text, "+ COLUMN_PROMO + " text);";
	  
	  //User credentials on the phone are stored to help with call ahead, promotion, etc...
	  private static final String DATABASE_CREATE_USER = "create table "
		      + TABLE_USER + "(" + COLUMN_USER_ID
		      + " integer primary key autoincrement, " + COLUMN_USER_NAME
		      + " varchar(100), " + COLUMN_USER_LAST_NAME +" varchar(100)," + COLUMN_PHONE +"varchar(20));";

	  public RestaurantDB(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    	database.execSQL(DATABASE_CREATE);
	   
	   // database.execSQL(DATABASE_CREATE_USER);
	  
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(RestaurantDB.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
	    onCreate(db);
	  }


}
