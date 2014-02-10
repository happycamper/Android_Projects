package com.seateasyapp.SEDesigner;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SaveBlueprintDialog {

	private Dialog dialog;
	private Context mContext;
	private Button saveButton;
	private Table mView;
	private Boolean isDelete;
	private BlueprintHandler printHandler;
	private EditText nametext;
	private final ArrayList<Table> tables;
	
	
	public SaveBlueprintDialog(final Context context, ArrayList<Table> tableList){
		this.mContext = context;
		printHandler = new BlueprintHandler(mContext);
		tables = tableList;
	}

	
    public void show(){
    	
 
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(true);
    	 dialog.setContentView(R.layout.save_blueprint);
    	 dialog.setTitle("Save File");


    	 saveButton = (Button) dialog.findViewById(R.id.blueprintSave);
    	 nametext = (EditText) dialog.findViewById(R.id.blueprintName);
    	 
    	 nametext.setText(BlueprintLayout.ACTIVE_LAYOUT);
    	 
    	 saveButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!nametext.getText().toString().isEmpty()){
					printHandler.saveFile(tables, nametext.getText().toString());
					dialog.dismiss();
					Toast.makeText(mContext, "File Saved", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "Need a valid file name", Toast.LENGTH_LONG).show();
				}
			}
    		 
    	 });
    	 
    	
    		 
    		 dialog.show();
 
    }
   
   
}
