package com.seateasyapp.SEDesigner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class AddReservationDialog{
	
	public Context mContext;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,partyOf;
	public String ifirstname,ilastname,iphone,itime;
	private Calendar c;
	private Button sendButton,cancelButton;
	private Dialog dialog;
	private Resources r;
	private ReservationDBManager db;
	private Spinner spin;
	private ArrayAdapter<String> aa;
	private HashMap<String,Integer> resTypes;
	private ArrayList<String> spinVals;
	private int hour,minute;
	
	private String walkIn,callAhead;
	private int decision;
	private TimePicker tpick;

	
	public AddReservationDialog(final Context context){
		this.mContext = context;
		r = mContext.getResources();
		db = new ReservationDBManager(mContext);
		db.open();
		walkIn = r.getString(R.string.walk_in);
		callAhead = r.getString(R.string.call_ahead);
		resTypes = new HashMap<String,Integer>();
		spinVals = new ArrayList<String>();
	}
	
    public void show(){
    	
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.add_reservation_dialog);
    	 dialog.setTitle(r.getString(R.string.add_reservation));
    	 
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    spin = (Spinner) dialog.findViewById(R.id.reservationTypeSpinner);
    	    sendButton = (Button) dialog.findViewById(R.id.addReservationSubmit);
    	    cancelButton = (Button) dialog.findViewById(R.id.addReservationCancel);
    	    firstname = (EditText) dialog.findViewById(R.id.addReservationFirstname);
    	    lastname = (EditText) dialog.findViewById(R.id.addReservationLastname);
    	    phone = (EditText) dialog.findViewById(R.id.addReservationPhone);
    	    partyOf = (EditText) dialog.findViewById(R.id.addReservationPartyOf);
    	    tpick = (TimePicker) dialog.findViewById(R.id.timePickerReservation);
    	    
    	    c = Calendar.getInstance();
   	     	hour = c.get(Calendar.HOUR_OF_DAY);
   	     	minute = c.get(Calendar.MINUTE);
   		 
   	     	tpick.setCurrentHour(1);
   	     	tpick.setCurrentMinute(1);
   	     	tpick.setIs24HourView(true);
    	    
    	    resTypes.put(r.getString(R.string.walk_in), Reservation.WALK_IN);
    	    resTypes.put(r.getString(R.string.call_ahead), Reservation.CALL_AHEAD);
    	    
    	    spinVals.add(walkIn);
    	    spinVals.add(callAhead);
    	    
    	    
    	    aa = new ArrayAdapter<String>(	mContext, android.R.layout.simple_spinner_item, spinVals);
    	    
    	    spin.setAdapter(aa);
    	    
    	    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    	            Object item = parent.getItemAtPosition(pos);
    	            decision = resTypes.get(item.toString());
    	        }
    	        public void onNothingSelected(AdapterView<?> parent) {
    	        	Object item = parent.getItemAtPosition(0);
    	            decision = resTypes.get(item.toString());
    	        }
    	    });
    	    
    	    
       
    		 sendButton.setOnClickListener(new View.OnClickListener() {
    		    	@Override
    		    	public void onClick(View v) {
    		    		//Compute the amount of wait time
    		    		if(!(firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || partyOf.getText().toString().isEmpty())){
    		    			Reservation temp = new Reservation();
    		    			hour = tpick.getCurrentHour();
    		    			minute = tpick.getCurrentMinute();
    		    			temp.setReservationTime(hour, minute);
    		    			temp.setFirstName(firstname.getText().toString());
    		    			temp.setLastName(lastname.getText().toString());
    		    			temp.setPartyNumber(partyOf.getText().toString());
    		    			temp.setTelephone(phone.getText().toString());
    		    			temp.setTimeIn(db.getTime());
    		    			temp.setReservationNumber(db.getNewReservationNumber());
    		    			temp.setReservationType(decision);
    		    			db.addReservation(temp);
    		    			//ReservationFragment.addReservation(temp);
    		    			ReservationFragment.adapter.add(temp);
    		    			ReservationFragment.adapter.notifyDataSetChanged();
    		    			db.close();
    	                       dialog.dismiss();
    		    		}else{
    		    			Toast.makeText(mContext, "Not all text fields are complete", Toast.LENGTH_LONG);
    		    		}
    		    		
                 	   
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
}

