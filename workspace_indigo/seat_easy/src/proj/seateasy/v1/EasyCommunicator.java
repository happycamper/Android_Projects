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


public class EasyCommunicator{
		private String indata;
	 	Messenger mService = null;
	 	public Context mContext;
	    boolean mIsBound;
	    public TextView test;
	    private Messenger mMessenger;
	    private ServiceConnection mConnection;
	    public String DOMAIN;
	    
	    public EasyCommunicator(final Context context, final Messenger messenger){
	    	this.mContext = context;
	    	this.mMessenger = messenger;
	    	this.mContext.startService(new Intent(this.mContext, Communication_Handler.class));
	    	this.DOMAIN = Communication_Handler.DOMAIN;
	    	
	    	  mConnection = new ServiceConnection() {
		        public void onServiceConnected(ComponentName className, IBinder service) {
		            mService = new Messenger(service);
		            try {
		                Message msg = Message.obtain(null, Communication_Handler.MSG_REGISTER_CLIENT);
		                msg.replyTo = mMessenger;
		                mService.send(msg);
		                
		            } catch (RemoteException e) {
		                // In this case the service has crashed before we could even do anything with it
		            	e.printStackTrace();
		            }
		        }

		        public void onServiceDisconnected(ComponentName className) {
		            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
		            mService = null;
		            doUnbindService();
		    		 mContext.stopService(new Intent(mContext, Communication_Handler.class));
		        }
		    };
	    	
	    }
	    
	    public void getHTTP(String url){
	    	Message msg;
	    	try{
	    		Bundle b = new Bundle();
	    		b.putString("fromClient", "http://"+url);
	    		msg = Message.obtain(null, Communication_Handler.HTTP_REQUEST);
	    		msg.setData(b);
	    		msg.replyTo = mMessenger;
	    		mService.send(msg);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    		
	    }
	    
	    public void testConnection(){
	    	Message msg;
	    	try{
	    		Bundle b = new Bundle();
	    		b.putString("fromClient", "http://"+DOMAIN+"test=good");
	    		msg = Message.obtain(null, Communication_Handler.HTTP_REQUEST);
	    		msg.setData(b);
	    		msg.replyTo = mMessenger;
	    		mService.send(msg);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    		
	    }
	    
	    public void restaurantDBRequest(String url){
	    	Message msg;
	    	try{
	    		Bundle b = new Bundle();
	    		b.putString("fromClient", "http://"+url);
	    		msg = Message.obtain(null, Communication_Handler.RESTAURANT_DB_REQUEST);
	    		msg.setData(b);
	    		msg.replyTo = mMessenger;
	    		mService.send(msg);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    		
	    }
	    public void easyStopService(){
	    	mContext.stopService(new Intent(mContext, Communication_Handler.class));
	    }
	    
	    public void easyStartService(){
	    	mContext.startService(new Intent(mContext, Communication_Handler.class));
	    }
	    public void sendIntToService(int intvaluetosend) {
	        if (mIsBound) {
	            if (mService != null) {
	                try {
	                    Message msg = Message.obtain(null, Communication_Handler.MSG_SET_INT_VALUE, intvaluetosend, 0);
	                    msg.replyTo = mMessenger;
	                    mService.send(msg);
	                } catch (RemoteException e) {
	                }
	            }
	        }
	    }
	 
	 public void sendStringToService(String stringToSend) {
	        if (mIsBound) {
	            if (mService != null) {
	                try {
	                	Bundle b = new Bundle();
	                	b.putString("fromClient", stringToSend);
	                	Message msg = Message.obtain(null, Communication_Handler.MSG_SET_STRING_VALUE);
		        		msg.setData(b);
	                    mService.send(msg);
	                } catch (RemoteException e) {
	                	e.printStackTrace();
	                }
	            }
	        }
	    }
	 
	
	
	 public void doBindService() {
	        mContext.bindService(new Intent(mContext, Communication_Handler.class), mConnection, Context.BIND_AUTO_CREATE);
	        mIsBound = true;
	    }
	    public void doUnbindService() {
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
	            mContext.unbindService(mConnection);
	            mIsBound = false;
	        }
	    }
	    
	    
	    
	    

}
