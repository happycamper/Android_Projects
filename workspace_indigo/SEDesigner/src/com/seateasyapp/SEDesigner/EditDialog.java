package com.seateasyapp.SEDesigner;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditDialog {

	private Dialog dialog;
	private Context mContext;
	private EditText tableName,tableSeats;
	private Button saveButton;
	private Table mView;
	private Boolean isDone = false;
	
	
	public EditDialog(final Context context, Table table){
		this.mContext = context;
		this.mView = table;
	}

	
    public void show(){
    	
    	
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.edit_table);
    	 dialog.setTitle(R.string.dialog_title);

    	 tableName = (EditText) dialog.findViewById(R.id.editTableNumber);
    	 tableSeats = (EditText) dialog.findViewById(R.id.editTableSeats); 
    	 saveButton = (Button) dialog.findViewById(R.id.editTableSave);
    	    
    	
    		 saveButton.setOnClickListener(new View.OnClickListener() {
    		    	@Override
    		    	public void onClick(View v) {
    		    		if(!tableName.getText().toString().isEmpty() & !tableSeats.getText().toString().isEmpty()){
    		    			if(mView.getTableType() == BlueprintLayout.RESTAURANT_OBJECT){
    		    				mView.setTableText(tableName.getText().toString());
    		    			}else{
    		    			mView.setTableNumber(Integer.parseInt(tableName.getText().toString()));
    		    			mView.setSeatsX(Integer.parseInt(tableSeats.getText().toString()));
    		    			}
        		    		isDone = true;
        		    		dialog.dismiss();
    		    		}else{
    		    			Toast.makeText(mContext, "Please fill out the text fields", Toast.LENGTH_LONG).show();
    		    		}
    		    	}
    		 });
    		 
    		 dialog.show();
 
    }
    
    public Table returnTable(){
    	isDone = false;
    	return mView;
    }
    
    public Boolean getIsDone(){
    	return isDone;
    }
}
