package com.seateasyapp.designer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Restaurant_Design_View extends Fragment {
	
	private BlueprintSurfaceView bp;
	private Boolean isRunning;
	private ViewGroup blueprintView;
	
	//public void Restaurant_Design_View(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		isRunning = true;
	}
	
	@Override
	public void onAttach(Activity activity){
		bp = new BlueprintSurfaceView(activity);
		super.onAttach(activity);
		
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.restaurant_blueprint,
		        container, false);
			
		    return bp;
	}

	
	@Override 
	public void onPause(){
		super.onPause();
		bp.onPause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		bp.onStop();
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		bp.onResume();
		
	}
	
	

}
