package proj.seateasy.v1;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class CallAheadForm extends DialogFragment {
	public Context mContext;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,time;
	public String ifirstname,ilastname,iphone,itime;
	final EasyCommunicator easy;
	final Messenger mMessenger;
	private Restaurant mRestaurant;
	private TimePicker tpick;
	private int hour,chosenHour;
	private int minute,chosenMinute,year,month,day;
	private Calendar c;
	private DatePicker dpick;
	
	public CallAheadForm(final Context context, Restaurant restaurant){
		this.mContext = context;
		mMessenger = new Messenger(new IncomingHandler());
		easy = new EasyCommunicator(mContext,mMessenger);
		this.mRestaurant = restaurant;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
   

    	easy.doBindService();
    	
    	 LayoutInflater inflater = getActivity().getLayoutInflater();
         View v = inflater.inflate(R.layout.call_ahead, null);
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    firstname = (EditText) v.findViewById(R.id.firstnameEdit);
    	    lastname = (EditText) v.findViewById(R.id.lastnameEdit);
    	    phone = (EditText) v.findViewById(R.id.phoneEdit);
    	    tpick = (TimePicker) v.findViewById(R.id.timePicker1);
    	    dpick = (DatePicker) v.findViewById(R.id.datePicker1);
    	    
    	     c = Calendar.getInstance();
    	     hour = c.get(Calendar.HOUR_OF_DAY);
    		 minute = c.get(Calendar.MINUTE);
    		 year = c.get(Calendar.YEAR);
    			month = c.get(Calendar.MONTH);
    			day = c.get(Calendar.DAY_OF_MONTH);
    		 
    		 tpick.setCurrentHour(hour);
    		 tpick.setCurrentMinute(minute);
    		 
    	    
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
               builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   
                	   //get the edit text form and send to socket to submit to web with Intent...
                	   ifirstname = firstname.getText().toString();
                	   ilastname = lastname.getText().toString();
                	   iphone = phone.getText().toString();
                	   chosenHour = tpick.getCurrentHour();
                	   chosenMinute = tpick.getCurrentMinute();
                	   System.out.println(chosenHour);
                	   String toSend = (easy.DOMAIN+"restaurant="+mRestaurant.getName()+"&first="+ifirstname+"&last="+ilastname+"&phone="+iphone);
                	   easy.getHTTP(toSend);
                       choicetrue = true;
                       easy.doUnbindService();
                       dialog.dismiss();
                   }
               });
        // Create the AlertDialog object and return it
               builder.setCancelable(false);
        return builder.create();
    }
    class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Communication_Handler.MSG_SET_INT_VALUE:
            	//we may not want to connect back to this
                break;
            case Communication_Handler.MSG_SET_STRING_VALUE:
            	//we may not want to connect back to this
            default:
                super.handleMessage(msg);
            }
        }
    }
    
   
 
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
 
		}
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListener 
    = new DatePickerDialog.OnDateSetListener() {

// when dialog box is closed, below method will be called.
public void onDateSet(DatePicker view, int selectedYear,
	int selectedMonth, int selectedDay) {
year = selectedYear;
month = selectedMonth;
day = selectedDay;


}
};
}

