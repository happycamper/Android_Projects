package com.seateasyapp.designer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class Seat_Easy_Designer extends Activity {
    /** Called when the activity is first created. */
	
	private FragmentManager fm;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        fm = getFragmentManager();
        FragmentTransaction fT = fm.beginTransaction();
        
        Restaurant_Design_View fragment = new Restaurant_Design_View();
        fT.add(R.id.mainView, fragment);
        fT.commit();
    }
}