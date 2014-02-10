package proj.seateasy.v1;

import android.accounts.Account;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//This is a dialog that pops up from the restaurant list page and displays an entry form
//it uses EasyCommunicator to submit information entered into the form to some server

public class UserLoginForm2{
	private Context mContext;
	private Account[] accounts;
	public Boolean choicetrue;
	public EditText firstname,lastname,phone,time,email,pass1,pass2;
	public String ifirstname,ilastname,iphone,itime,iemail,ipass1,ipass2;
	final EasyCommunicator easy;
	final Messenger mMessenger;
	private Dialog dialog;
	private Button send,cancel;
	
	public UserLoginForm2(final Context context){
		this.mContext = context;
		mMessenger = new Messenger(new IncomingHandler());
		easy = new EasyCommunicator(mContext,mMessenger);
	}
	
    
   public void show() {
    	 
    	easy.doBindService();
    	
    	dialog = new Dialog(mContext);
   	 	dialog.setCanceledOnTouchOutside(false);
   	 	dialog.setContentView(R.layout.user_login);
   	    dialog.setTitle("User Login Form");
    	 
    	    firstname = (EditText) dialog.findViewById(R.id.user_login_firstnameEdit);
    	    lastname = (EditText) dialog.findViewById(R.id.user_login_lastnameEdit);
    	    phone = (EditText) dialog.findViewById(R.id.user_login_phoneEdit);
    	    email = (EditText) dialog.findViewById(R.id.user_login_emailEdit);
    	    pass1 = (EditText) dialog.findViewById(R.id.user_login_pass1Edit);
    	    pass2 = (EditText) dialog.findViewById(R.id.user_login_pass2Edit);
    	    send = (Button) dialog.findViewById(R.id.user_login_SendButton);
    	    cancel = (Button) dialog.findViewById(R.id.user_login_Cancel);
    	    
    	    User_Authenticator au = new User_Authenticator(mContext);
    	     accounts = au.getAccounts();
    	    
    	    if(accounts.length !=0){
    	    	email.setText(accounts[0].name);
    	    }
    	    
    	   
    	    	send.setOnClickListener(new View.OnClickListener(){
    	    		@Override
    	    		public void onClick(View v){
    	    	
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
          		           //manager.clearUsersTable();
          		           manager.addUser(user);
          		           manager.close();
          		           easy.doUnbindService();
              		       dialog.dismiss();
          		           Intent result_intent = new Intent(mContext,Main_Page.class);
             			   result_intent.putExtra("result", "email pushed");
             			   result_intent.putExtra("name", user.getFirstName().toString());
             			   result_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             			   mContext.startActivity(result_intent);
                	   }else{
                		   Toast.makeText(mContext, "Passwords do not match", Toast.LENGTH_LONG).show();
                	   }
    	    		}
                	  
    	    	});
    	    	
    	    	cancel.setOnClickListener(new View.OnClickListener(){
    	    		@Override
    	    		public void onClick(View v){
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
}

