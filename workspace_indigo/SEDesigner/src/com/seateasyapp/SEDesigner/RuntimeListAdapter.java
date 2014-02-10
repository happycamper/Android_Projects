package com.seateasyapp.SEDesigner;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class RuntimeListAdapter extends ArrayAdapter<String> {
	
		private Context mContext;
		private BlueprintHandler bh;
		private ArrayList<String> filenames;
		private int Counter;
		FloorsFragment.loadFloorXML mCallBack;
		
	public RuntimeListAdapter(Context context, int textViewResourceId, ArrayList<String> strings, FloorsFragment.loadFloorXML callBack) {
		super(context, R.layout.blueprint_list_adapter_runtime, strings);
		// TODO Auto-generated constructor stub
		mContext = context;
		bh = new BlueprintHandler(mContext);
		filenames = strings;
		mCallBack = callBack;
		
	}
	

	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.blueprint_list_adapter_runtime, parent, false);
	    Button text = (Button) rowView.findViewById(R.id.folderName);
	    text.setText(filenames.get(position).toString());
	    final String name = filenames.get(position).toString();
	    
	    
	     String[] names = name.split("[.]");
	     final String copyName = names[0];
		
	    text.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCallBack.onItemSelection(name);			
			}
	    	
	    });
	    
	    return rowView;
	    
	}


}