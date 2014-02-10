package proj.seateasy.v1;

import android.os.Bundle;

public class Restaurant {
	
	private String name,mapURL,CAHashTag,lat,lon,rating,promo;
	public int DBid;
	
	
	public Restaurant(String name){
		this.name = name;
		lat = "";
		lon = "";
		rating = "";
		promo = "";
		CAHashTag = "";
		DBid = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getMapURL(){
		return mapURL;
	}
	
	public void setPromo(String promo){
		this.promo = promo;
	}
	
	public void setHashTag(String hash){
		this.CAHashTag = hash;
	}
	
	public void setLong(String lon){
		this.lon = lon;
	}
	
	public void setRating(String rate){
		this.rating = rate;
	}
	
	public void setLat(String lat){
		this.lat = lat;
	}
	
	public String getLat(){
		return lat;
	}
	
	public String getLon(){
		return lon;
	}
	
	public String getHash(){
		return CAHashTag;
	}
	
	public String getRating(){
		return rating;
	}
	
	public String getPromo(){
		return promo;
	}
	
	public int getDBid(){
		return DBid;
	}
	
	public void setDBid(int id){
		this.DBid = id;
	}
}

