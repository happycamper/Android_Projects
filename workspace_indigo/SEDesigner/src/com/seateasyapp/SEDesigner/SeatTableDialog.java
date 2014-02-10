package com.seateasyapp.SEDesigner;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class SeatTableDialog{
	
	public Context mContext;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,time;
	public String ifirstname,ilastname,iphone,itime;
	private Spinner spin;
	private int hour,chosenHour;
	private int minute,chosenMinute,year,month,day;
	private Calendar c;
	private Button sendButton,cancelButton;
	private Dialog dialog;
	private final Reservation mReservation;
	private Resources r;
	private ReservationDBManager db;
	private ArrayAdapter<String> aa;
	private ArrayList<String> tableNums;
	private int listPosition;
	private String decision;
	
	public SeatTableDialog(final Context context, Reservation reservation, int position){
		this.mContext = context;
		this.mReservation = reservation;
		r = mContext.getResources();
		db = new ReservationDBManager(mContext);
		db.open();
		listPosition = position;
	}
	
    public void show(){
    	
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.seat_table_dialog);
    	 
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    spin = (Spinner) dialog.findViewById(R.id.tableListSpinner);
    	    sendButton = (Button) dialog.findViewById(R.id.seatTimeSave);
    	    cancelButton = (Button) dialog.findViewById(R.id.seatTimeCancel);
    	    dialog.setTitle(r.getString(R.string.send_party_to_table));
    	    
    	    tableNums = new ArrayList<String>();
    	    for(int i=0; i<RealTimeLayout.TABLE_COUNT-1; ++i){
    	    	Table temp = RealTimeLayout.activeTables.get(i);
    	    	if(temp.getTableNumber() != 0)
    	    	tableNums.add(String.valueOf(temp.getTableNumber()));
    	    }
    	    
    	    aa = new ArrayAdapter<String>(	mContext, android.R.layout.simple_spinner_item, tableNums);
    	    
    	    spin.setAdapter(aa);
    	    
    	    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	            Object item = parent.getItemAtPosition(pos);
    	            decision = item.toString();
    	        }
    	        public void onNothingSelected(AdapterView<?> parent) {
    	        	Object item = parent.getItemAtPosition(0);
    	            decision = item.toString();
    	        }
    	    });
    	    

    	    
    		 
    	    
       
    		 sendButton.setOnClickListener(new View.OnClickListener() {
    		    	@Override
    		    	public void onClick(View v) {
    		    		//Compute the amount of wait time
    		    		mReservation.setIsSeated(true);
    		    		ReservationFragment.adapter.remove(mReservation);
    		    		ReservationFragment.adapter.notifyDataSetChanged();
    		    		db.deleteReservation(mReservation.getReservationNumber());
    		    		mReservation.setSeatedTable(RealTimeLayout.ACTIVE_LAYOUT,decision);
    		    		db.addSeatedReservation(mReservation);
    		    		SeatedFragment.adapter.add(mReservation);
    		    		SeatedFragment.adapter.notifyDataSetChanged();
    		    		//SeatedFragment.addSeatedReservation(mReservation);
    		    		
    		    		
    		    		for(int i = 0; i<RealTimeLayout.activeTables.size();++i){
    		    			Table temp = RealTimeLayout.activeTables.get(i);
    		    			System.out.println("Table Number"+ temp.getTableNumber()+ "decision "+ decision);
    		    			if(temp.getTableNumber() == Integer.valueOf(decision)){
    		    				RealTimeLayout.seatedTables.add(temp);
    		    				temp.setisTaken(true);
    		    			}
    		    		}
    		    		
    		    		
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

