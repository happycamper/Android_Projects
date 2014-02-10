package com.seateasyapp.SEDesigner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EntryPage extends Activity{
	
	private Button runtime,designer,promotion,stats;
	private Context mContext;
	private Intent i,j;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_layout);
		
		mContext = this;
		runtime = (Button) findViewById(R.id.mainRuntime);
		
		designer = (Button) findViewById(R.id.designer);
		stats = (Button) findViewById(R.id.statistics);
		promotion = (Button) findViewById(R.id.promotionDesigner);
		
		 i = new Intent(this, BlueprintListPage.class);
		 i.putExtra("runtime", "no");
		 j = new Intent(this, BlueprintListPage.class);
		j.putExtra("runtime", "yes");
		
		runtime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mContext.startActivity(j);
			}
			
		});
		
		designer.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.startActivity(i);
			}
			
		});
		
	}

}
