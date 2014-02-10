package proj.seateasy.v1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RestaurantDBManager {
	
	private SQLiteDatabase database;
	  private RestaurantDB dbHelper;
	  private String[] allColumns = {RestaurantDB.COLUMN_NAME, RestaurantDB.COLUMN_LAT, RestaurantDB.COLUMN_LON,
			  RestaurantDB.COLUMN_HASH, RestaurantDB.COLUMN_RATE, RestaurantDB.COLUMN_PROMO};

	  public RestaurantDBManager(Context context) {
	    dbHelper = new RestaurantDB(context);
	  }

	  public void open(){
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void addRestaurant(Restaurant rest){
		  ContentValues values = new ContentValues();
		  values.put(RestaurantDB.COLUMN_NAME, rest.getName());
		  values.put(RestaurantDB.COLUMN_LAT, rest.getLat());
		  values.put(RestaurantDB.COLUMN_LON, rest.getLon());
		  values.put(RestaurantDB.COLUMN_HASH, rest.getHash());
		  values.put(RestaurantDB.COLUMN_RATE, rest.getRating());
		  values.put(RestaurantDB.COLUMN_PROMO,rest.getPromo());
		  long insertId = database.insert(RestaurantDB.TABLE_RESTAURANTS, null, values);
	  }
	  
	  
	  public Restaurant getRestaurant(String name) {
	    Cursor cursor = database.query(RestaurantDB.TABLE_RESTAURANTS,
	        allColumns, RestaurantDB.COLUMN_NAME + " = '" + name + "'", null,
	        null, null, null);
	    cursor.moveToFirst();
	    Restaurant newRestaurant = cursor2Restaurant(cursor);
	    cursor.close();
	    return newRestaurant;
	  }

	  public void deleteRestaurant(String name) {
	    //String name = rest.getName();
	    //System.out.println("Comment deleted with id: " + id);
	    database.delete(RestaurantDB.TABLE_RESTAURANTS, RestaurantDB.COLUMN_NAME
	        + " = '" + name+"'", null);
	  }
	  
	  public void clearRestaurantTable(){
		  database.delete(RestaurantDB.TABLE_RESTAURANTS, null, null);
	  }

	  public List<Restaurant> getAllRestaurants() {
	    List<Restaurant> restaurantList = new ArrayList<Restaurant>();

	    Cursor cursor = database.query(RestaurantDB.TABLE_RESTAURANTS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Restaurant restaurant = cursor2Restaurant(cursor);
	      restaurantList.add(restaurant);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return restaurantList;
	  }

	  private Restaurant cursor2Restaurant(Cursor cursor) {
	    Restaurant rest = new Restaurant(cursor.getString(0));
	    return rest;
	  }

}
