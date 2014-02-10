package com.seateasyapp.SEDesigner;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ActionsDialog {

	private Dialog dialog;
	private Context mContext;
	private Button deleteButton,copyButton,cancelButton;
	private Table mView;
	private Boolean isDelete;
	private Boolean isCopy,isDone,isCancel;
	
	
	public ActionsDialog(final Context context, Table table){
		this.mContext = context;
		this.mView = table;
		this.isDelete = false;
		this.isCopy = false;
		this.isDone = false;
		this.isCancel = false;
	}

	
    public void show(){
    	
    	 isDelete = false;
    	 isCopy = false;
    	 isDone = false;
    	 isCancel = false;
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.actionlist);
    	 dialog.setTitle(R.string.actions_dialog_title);

    	 deleteButton = (Button) dialog.findViewById(R.id.actionDelete);
    	 copyButton = (Button) dialog.findViewById(R.id.actionCopy);
    	 cancelButton = (Button) dialog.findViewById(R.id.actionCancel);
    	 
    	 deleteButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isDelete = true;
				isDone = true;
				dialog.dismiss();
			}
    		 
    	 });
    	 
    	 copyButton.setOnClickListener(new OnClickListener(){

 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				isCopy = true;
 				isDone = true;
 				dialog.dismiss();
 			}
     		 
     	 });
    	 
    	 cancelButton.setOnClickListener(new OnClickListener(){

  			@Override
  			public void onClick(View v) {
  				// TODO Auto-generated method stub
  				isCancel = true;
  				isDone = true;
  				dialog.dismiss();
  			}
      		 
      	 });
    	    
    	
    		 
    		 dialog.show();
 
    }
    

    
    public Boolean getIsDelete(){
    	return isDelete;
    }
    
    public Boolean getIsCopy(){
    	return isCopy;
    }
    
    public Table returnTable(){
    	return mView;
    }
    
    public Boolean getIsDone(){
    	return isDone;
    }
    
    public Boolean getIsCancel(){
    	return isCancel;
    }
}
