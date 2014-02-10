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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BlueprintListAdapter extends ArrayAdapter<String> {
	
		private Context mContext;
		private BlueprintHandler bh;
		private ArrayList<String> filenames;
		private int Counter;
		public Boolean mIsChecked;
		
	public BlueprintListAdapter(Context context, int textViewResourceId, ArrayList<String> strings) {
		super(context, R.layout.blueprint_list_adapter, strings);
		// TODO Auto-generated constructor stub
		mContext = context;
		bh = new BlueprintHandler(mContext);
		filenames = strings;
		mIsChecked = false;
		
	}
	

	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.blueprint_list_adapter, parent, false);
	    Button text = (Button) rowView.findViewById(R.id.folderName);
	    CheckBox check = (CheckBox) rowView.findViewById(R.id.deleteCheckbox);
	    text.setText(filenames.get(position).toString());
	    String name = filenames.get(position).toString();
	    
	     String[] names = name.split("[.]");
	     final String copyName = names[0];
	     final String copyXMLName = name;
	     
	     check.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mIsChecked = isChecked;
				if(mIsChecked){
					BlueprintListPage.toDelete.add(copyName);
				}else{
					for(int i = 0; i<BlueprintListPage.toDelete.size();++i){
						String temp = BlueprintListPage.toDelete.get(i).toString();
						if(temp.matches(copyName)){
							BlueprintListPage.toDelete.remove(i);
						}
					}
				}
			}
	    	 
	     });
		
	    text.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent i = new Intent(mContext,SEDesignerActivity.class);
				i.putExtra("filename", copyName);
				mContext.startActivity(i);				
			}
	    	
	    });
	    
	    return rowView;
	    
	}


}
