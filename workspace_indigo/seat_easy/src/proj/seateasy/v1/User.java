package proj.seateasy.v1;

import android.os.Bundle;

public class User {
	
	private String FIRSTNAME,LASTNAME,PASS,PHONE,EMAIL,CUSTOMID;
	public int DBid;
	
	
	public User(String firstname){
		this.FIRSTNAME = firstname;
	}
	
	public String getFirstName(){
		return FIRSTNAME;
	}
	
	public String getLastName(){
		return LASTNAME;
	}
	
	public String getPassword(){
		return PASS;
	}
	
	public String getPhone(){
		return PHONE;
	}
	
	public String getEmail(){
		return EMAIL;
	}
	
	public String getCustomId(){
		return CUSTOMID;
	}
	
	public void setFirstName(String name){
		this.FIRSTNAME = name;
	}
	
	public void setLastName(String name){
		this.LASTNAME = name;
	}
	
	public void setPassword(String pass){
		this.PASS = pass;
	}
	
	public void setPhone(String phone){
		this.PHONE = phone;
	}
	
	public void setEmail(String email){
		this.EMAIL = email;
	}
	
	public void setCustomId(String id){
		this.CUSTOMID = id;
	}
}

