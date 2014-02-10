package proj.seateasy.v1;

import java.io.File;

import android.graphics.Bitmap;

public class Promotion {
	
	private String userName,Date,restaurantName,filePath;
	private Boolean isSaved;
	private Bitmap mBitmap;
	
	
	public Promotion(Restaurant rest, User user){
		this.userName = user.getFirstName();
		this.Date = "";
		this.filePath = "";
		this.isSaved = false;
		this.restaurantName = rest.getName();
	}
	
	public void setBitmap(Bitmap bitmap){
		mBitmap = bitmap;
	}
	
	
	public void setUserName(String name){
		userName = name;
	}
	
	public void setDate(String date){
		Date = date;
	}
	
	public void setSaved(){
		isSaved = true;
	}
	
	public Bitmap getBitmap(){
		return mBitmap;
	}
	
	public String getDate(){
		return Date;
	}
	
	public String getRestaurantName(){
		return restaurantName;
	}
	
	public void setFilePath(File file){
		this.filePath = file.toString();
	}
	
	public String getFilePath(){
		return this.filePath;
	}

}
