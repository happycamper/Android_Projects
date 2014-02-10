package proj.seateasy.v1;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class CallAheadForm2{
	
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
	private Button sendButton,cancelButton;
	private Dialog dialog;
	private User mUser;
	
	public CallAheadForm2(final Context context, Restaurant restaurant,User user){
		this.mContext = context;
		mMessenger = new Messenger(new IncomingHandler());
		easy = new EasyCommunicator(mContext,mMessenger);
		this.mRestaurant = restaurant;
		this.mUser = user;
	}
	
    public void show(){
    	
    	easy.doBindService();
    	
    	 dialog = new Dialog(mContext);
    	 dialog.setCanceledOnTouchOutside(false);
    	 dialog.setContentView(R.layout.call_ahead);
    	 
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    
    	    firstname = (EditText) dialog.findViewById(R.id.firstnameEdit);
    	    firstname.setText(mUser.getFirstName().toString());
    	    lastname = (EditText) dialog.findViewById(R.id.lastnameEdit);
    	    lastname.setText(mUser.getLastName().toString());
    	    phone = (EditText) dialog.findViewById(R.id.phoneEdit);
    	    phone.setText(mUser.getPhone().toString());
    	    tpick = (TimePicker) dialog.findViewById(R.id.timePicker1);
    	    sendButton = (Button) dialog.findViewById(R.id.sendButton);
    	    cancelButton = (Button) dialog.findViewById(R.id.callAheadCancel);
    	    dialog.setTitle("Call Ahead Form");
    	    dpick = (DatePicker) dialog.findViewById(R.id.datePicker1);
    	    dpick.setCalendarViewShown(false);
    	    
    	    
    	     c = Calendar.getInstance();
    	     hour = c.get(Calendar.HOUR_OF_DAY);
    		 minute = c.get(Calendar.MINUTE);
    		 year = c.get(Calendar.YEAR);
    			month = c.get(Calendar.MONTH);
    			day = c.get(Calendar.DAY_OF_MONTH);
    		 
    		 tpick.setCurrentHour(hour);
    		 tpick.setCurrentMinute(minute);
    		 
    	    
       
    		 sendButton.setOnClickListener(new View.OnClickListener() {
    		    	@Override
    		    	public void onClick(View v) {
    		    		//get the edit text form and send to socket to submit to web with Intent...
                 	   ifirstname = firstname.getText().toString();
                 	   ilastname = lastname.getText().toString();
                 	   iphone = phone.getText().toString();
                 	   chosenHour = tpick.getCurrentHour();
                 	   chosenMinute = tpick.getCurrentMinute();
                 	   day = dpick.getDayOfMonth();
                 	   month = dpick.getMonth();
                 	   year = dpick.getYear();
                 	   System.out.println(chosenHour);
                 	   String toSend = (easy.DOMAIN+"restaurant="+mRestaurant.getName()+"&first="+ifirstname+"&last="+ilastname+"&phone="+iphone);
                 	   easy.getHTTP(toSend);
                       choicetrue = true;
                       easy.doUnbindService();
                       dialog.dismiss();
    		    	}
    		    });
    		 
    		 cancelButton.setOnClickListener(new View.OnClickListener() {
 		    	@Override
 		    	public void onClick(View v) {
 		    		System.out.println("Call Ahead Form Canceled");
 		    		easy.doUnbindService();
 		    		dialog.dismiss();
 		    		
 		    	}
    });   
                	   
         dialog.show();      
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

