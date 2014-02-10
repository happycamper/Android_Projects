package proj.seateasy.v1;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class UserLoginForm extends DialogFragment {
	public Context mContext;
	private Account[] accounts;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,time,email,pass1,pass2;
	public String ifirstname,ilastname,iphone,itime,iemail,ipass1,ipass2;
	final EasyCommunicator easy;
	final Messenger mMessenger;
	
	public UserLoginForm(final Context context){
		this.mContext = context;
		mMessenger = new Messenger(new IncomingHandler());
		easy = new EasyCommunicator(mContext,mMessenger);
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	 
    	easy.doBindService();
    	
    	 LayoutInflater inflater = getActivity().getLayoutInflater();
         View v = inflater.inflate(R.layout.user_login, null);
    	    // Inflate and set the layout for the dialog
    	    // Pass null as the parent view because its going in the dialog layout
    	    firstname = (EditText) v.findViewById(R.id.user_login_firstnameEdit);
    	    lastname = (EditText) v.findViewById(R.id.user_login_lastnameEdit);
    	    phone = (EditText) v.findViewById(R.id.user_login_phoneEdit);
    	    email = (EditText) v.findViewById(R.id.user_login_emailEdit);
    	    pass1 = (EditText) v.findViewById(R.id.user_login_pass1Edit);
    	    pass2 = (EditText) v.findViewById(R.id.user_login_pass2Edit);
    	    
    	    User_Authenticator au = new User_Authenticator(mContext);
    	     accounts = au.getAccounts();
    	    
    	    if(accounts.length !=0){
    	    	email.setText(accounts[0].name);
    	    }
    	    
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
               builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   easy.doBindService();
                	   ipass1 = pass1.getText().toString();
                	   ipass2 = pass2.getText().toString();
                	   
                	   if(ipass1.contentEquals(ipass2)){
                		   User user = new User(firstname.getText().toString());
                		   user.setPassword(ipass1);
                		   user.setLastName(lastname.getText().toString());
                		   user.setPhone(phone.getText().toString());
                		   user.setEmail(email.getText().toString());
                		   user.setCustomId("1");
                		   
                		   UsersDBManager manager = new UsersDBManager(mContext);
                		   
          		           manager.open();
          		           manager.createTable();
          		           manager.addUser(user);
          		           manager.close();
          		           
                		   easy.doUnbindService();
                		   dialog.dismiss();
                		   Intent result_intent = new Intent(mContext,Main_Page.class);
               			   result_intent.putExtra("result", "email pushed");
                	   }else{
                		   Toast.makeText(mContext, "Passwords do not match", Toast.LENGTH_LONG).show();
                	   }
                	   
                   }
               });
        // Create the AlertDialog object and return it
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
}

