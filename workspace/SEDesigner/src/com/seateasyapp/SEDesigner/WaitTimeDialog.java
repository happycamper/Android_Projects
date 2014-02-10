package com.seateasyapp.SEDesigner;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class WaitTimeDialog{
	
	public Context mContext;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,time;
	public String ifirstname,ilastname,iphone,itime;
	private TimePicker tpick;
	private int hour,chosenHour;
	private int minute,chosenMinute,year,month,day;
	private Calendar c;
	private DatePicker dpick;
	private Button sendButton,cancelButton;
	private Dialog dialog;
	private final Reservation mReservation;
	private Resources r;
	private ReservationDBManager db;
	
	public WaitTimeDialog(final Context context, Reservation reservation){
		this.mContext = context;
		this.mReservation = reservation;
		r = mContext.getResources();
		db = new ReservationDBManager(mContext);
		db.open();
	}
	
    public void show(){
    	
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.call_ahead);
    	 
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    tpick = (TimePicker) dialog.findViewById(R.id.timePicker1);
    	    sendButton = (Button) dialog.findViewById(R.id.waitTimeSave);
    	    cancelButton = (Button) dialog.findViewById(R.id.waitTimeCancel);
    	    dialog.setTitle(r.getString(R.string.setWaitTime));

    	    
    	    
    	     c = Calendar.getInstance();
    	     hour = c.get(Calendar.HOUR_OF_DAY);
    		 minute = c.get(Calendar.MINUTE);
    		 
    		 tpick.setCurrentHour(1);
    		 tpick.setCurrentMinute(1);
    		 tpick.setIs24HourView(true);
    		 
    	    
       
    		 sendButton.setOnClickListener(new View.OnClickListener() {
    		    	@Override
    		    	public void onClick(View v) {
    		    		//Compute the amount of wait time
                 	   chosenHour = tpick.getCurrentHour();
                 	   chosenMinute = tpick.getCurrentMinute();
                 	   int hours2minutes = chosenHour*60;
                 	   chosenMinute += hours2minutes;
                 	   
                 	   
                 	   db.updateWaitTime(mReservation, chosenMinute);
                 	   
                 	   
                 	   mReservation.setWaitTime(chosenMinute);
                 	   mReservation.setWaitTimeText(chosenMinute);
                 	   mReservation.setWaitTimeBoolean(true);
                 	   db.close();
                       dialog.dismiss();
    		    	}
    		    });
    		 
    		 cancelButton.setOnClickListener(new View.OnClickListener() {
 		    	@Override
 		    	public void onClick(View v) {
 		    		dialog.dismiss();
 		    		db.close();
 		    		
 		    	}
    });   
                	   
         dialog.show();      
    }
 
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
 
		}
	};
}

