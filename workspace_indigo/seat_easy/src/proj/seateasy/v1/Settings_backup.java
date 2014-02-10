package proj.seateasy.v1;

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Settings_backup extends Activity {
	 	Messenger mService = null;
	    boolean mIsBound;
	    public TextView test;
	    final Messenger mMessenger = new Messenger(new IncomingHandler());
	    
	 
	    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings);
	        Button send = (Button) findViewById(R.id.servicebutton);
	        Button disconnect = (Button) findViewById(R.id.dservicebutton);
	        
	        ActionBar main_bar = getActionBar();
			main_bar.show();
			
			
			startService(new Intent(Settings_backup.this, Communication_Handler.class));
   		 	doBindService();
			
   		 send.setOnClickListener(new View.OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		 sendMessageToService(1);
		    	}
		    });
			
			disconnect.setOnClickListener(new View.OnClickListener() {
		    	@Override
		    	public void onClick(View v) {
		    		 
		    		 doUnbindService();
		    		 stopService(new Intent(Settings_backup.this, Communication_Handler.class));
		    	}
		    });
	 	}
	 
	 class IncomingHandler extends Handler { // Handler of incoming messages from clients.
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case Communication_Handler.MSG_SET_INT_VALUE:
	            	test.setText("Made it");
	                break;
	            case Communication_Handler.MSG_SET_STRING_VALUE:
	            	test.setText(msg.getData().getString("fromServer"));
	            default:
	                super.handleMessage(msg);
	            }
	        }
	    }
	 
	 
	 private void sendMessageToService(int intvaluetosend) {
	        if (mIsBound) {
	            if (mService != null) {
	                try {
	                	test = (TextView) findViewById(R.id.test2);
	                    Message msg = Message.obtain(null, Communication_Handler.MSG_SET_INT_VALUE, intvaluetosend, 0);
	                    msg.replyTo = mMessenger;
	                    mService.send(msg);
	                } catch (RemoteException e) {
	                }
	            }
	        }
	    }
	
	//Override for the ActionBar
		 @Override
		 public boolean onCreateOptionsMenu(Menu menu) {
		     MenuInflater inflater = getMenuInflater();
		     inflater.inflate(R.layout.main_menu, menu);
		     return true;
		 }
		 
		 private ServiceConnection mConnection = new ServiceConnection() {
		        public void onServiceConnected(ComponentName className, IBinder service) {
		            mService = new Messenger(service);
		            try {
		                Message msg = Message.obtain(null, Communication_Handler.MSG_REGISTER_CLIENT);
		                msg.replyTo = mMessenger;
		                mService.send(msg);
		            } catch (RemoteException e) {
		                // In this case the service has crashed before we could even do anything with it
		            }
		        }

		        public void onServiceDisconnected(ComponentName className) {
		            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
		            mService = null;
		        }
		    };
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	         case R.id.menu_Home:
	             Intent home_intent = new Intent(this, Main_Page.class);
	             startActivity(home_intent);
	             return true;
	             
	         case R.id.menu_Settings:
	        	 Intent settings_intent = new Intent(this, Settings_backup.class);
	             startActivity(settings_intent);
	             return true;
	             
	         case R.id.menu_Share:
	        	 Intent share_intent = new Intent(this, Settings_backup.class);
	             startActivity(share_intent);
	             return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }
	
	 void doBindService() {
	        bindService(new Intent(this, Communication_Handler.class), mConnection, Context.BIND_AUTO_CREATE);
	        mIsBound = true;
	    }
	    void doUnbindService() {
	        if (mIsBound) {
	            // If we have received the service, and hence registered with it, then now is the time to unregister.
	            if (mService != null) {
	                try {
	                    Message msg = Message.obtain(null, Communication_Handler.MSG_UNREGISTER_CLIENT);
	                    msg.replyTo = mMessenger;
	                    mService.send(msg);
	                } catch (RemoteException e) {
	                    // There is nothing special we need to do if the service has crashed.
	                }
	            }
	            // Detach our existing connection.
	            unbindService(mConnection);
	            mIsBound = false;
	        }
	    }
	    
	    
	    
	    

}
