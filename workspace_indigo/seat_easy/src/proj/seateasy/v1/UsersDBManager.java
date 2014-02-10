package proj.seateasy.v1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsersDBManager {
	
	
	
	private SQLiteDatabase database;
	  private UsersDB dbHelper;
	  private String[] allColumns = {UsersDB.COLUMN_FIRSTNAME, UsersDB.COLUMN_LASTNAME, UsersDB.COLUMN_PASS,
			  UsersDB.COLUMN_PHONE, UsersDB.COLUMN_EMAIL, UsersDB.COLUMN_CUSTOMID};

	  public UsersDBManager(Context context) {
	    dbHelper = new UsersDB(context);
	  }

	  public void open(){
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void addUser(User user){
		  ContentValues values = new ContentValues();
		  values.put(UsersDB.COLUMN_FIRSTNAME, user.getFirstName());
		  values.put(UsersDB.COLUMN_LASTNAME, user.getLastName());
		  values.put(UsersDB.COLUMN_PASS, user.getPassword());
		  values.put(UsersDB.COLUMN_PHONE, user.getPhone());
		  values.put(UsersDB.COLUMN_EMAIL, user.getEmail());
		  values.put(UsersDB.COLUMN_CUSTOMID,user.getCustomId());
		  long insertId = database.insert(UsersDB.TABLE_USERS, null, values);
	  }
	  
	 
	  
	  public User getUser(String name) {
	    Cursor cursor = database.query(UsersDB.TABLE_USERS,
	        allColumns, UsersDB.COLUMN_FIRSTNAME + " = '" + name + "'", null,
	        null, null, null);
	    cursor.moveToFirst();
	    User newUser = cursor2User(cursor);
	    cursor.close();
	    return newUser;
	  }

	  public void createTable(){
		  dbHelper.createTable(database);
	  }
	  
	  public void deleteUser(String name) {
	    //String name = rest.getName();
	    //System.out.println("Comment deleted with id: " + id);
	    database.delete(UsersDB.TABLE_USERS, UsersDB.COLUMN_FIRSTNAME
	        + " = '" + name+"'", null);
	  }
	  
	  public void clearUsersTable(){
		  database.delete(UsersDB.TABLE_USERS, null, null);
	  }

	  public List<User> getAllUsers() {
	    List<User> userList = new ArrayList<User>();

	    Cursor cursor = database.query(UsersDB.TABLE_USERS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      User user = cursor2User(cursor);
	      userList.add(user);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return userList;
	  }

	  private User cursor2User(Cursor cursor) {
	    User user = new User(cursor.getString(0));
	    user.setLastName(cursor.getString(1));
	    user.setPassword(cursor.getString(2));
	    user.setPhone(cursor.getString(3));
	    user.setEmail(cursor.getString(4));
	    user.setCustomId(cursor.getString(5));
	    return user;
	  }

}
