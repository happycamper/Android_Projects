package com.seateasyapp.SEDesigner;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BlueprintListPage extends ListActivity {
	
	private BlueprintHandler bh;
	private ArrayList<String> FILES;
	private Context mContext;
	private String runtime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blueprintlist);
		FILES = new ArrayList<String>();
		bh = new BlueprintHandler(this);
		mContext = this;
		runtime = "no";
		
		Intent i = getIntent();
		 runtime = i.getStringExtra("runtime");
		
		
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		FILES.clear();
		File[] files = bh.getAllBlueprintNames();
		for(int i =0; i< files.length; ++i){
			if(files[i].getName().toString().contains(".xml")){
			FILES.add(files[i].getName());
			}
		}
		
		if(runtime.contains("yes")){
			BlueprintListAdapterRuntime adapter2 = new BlueprintListAdapterRuntime(this,0,FILES);
			setListAdapter(adapter2);
		}else{
		BlueprintListAdapter adapter = new BlueprintListAdapter(this,0,FILES);
		setListAdapter(adapter);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.blueprint_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.addNew:
			Intent i = new Intent(mContext,SEDesignerActivity.class);
			i.putExtra("filename", "CREATE_NEW_FILE");
			mContext.startActivity(i);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
