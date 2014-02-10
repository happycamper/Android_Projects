package com.seateasyapp.SEDesigner;


import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FloorsFragment extends ListFragment {
	
	private ArrayList<String> FILES;
	private BlueprintHandler bh;
	private Context mContext;
	loadFloorXML mCallBack;

	
	public interface loadFloorXML {
		public void onItemSelection(String xmlname);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
		try {
			mCallBack = (loadFloorXML) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
		
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.runtimelist,
		        container, false);
		
		
		bh = new BlueprintHandler(mContext);
		
		FILES = new ArrayList<String>();
		FILES.clear();
		File[] files = bh.getAllBlueprintNames();
		for(int i =0; i< files.length; ++i){
			if(files[i].getName().toString().contains(".xml")){
			FILES.add(files[i].getName());
			}
		}
		
		RuntimeListAdapter adapter = new RuntimeListAdapter(mContext,0,FILES,mCallBack);
		setListAdapter(adapter);
		
			view.setX(0.0f);
			view.setY(540.0f);
			
		    return view;
	}
	
	public void updateList(){
		FILES = new ArrayList<String>();
		FILES.clear();
		File[] files = bh.getAllBlueprintNames();
		for(int i =0; i< files.length; ++i){
			if(files[i].getName().toString().contains(".xml")){
				String[] names = files[i].getName().toString().split("[.]");
			FILES.add(names[0]);
			}
		}
		
		RuntimeListAdapter adapter = new RuntimeListAdapter(mContext,0,FILES,mCallBack);
		setListAdapter(adapter);
		
	}

}
