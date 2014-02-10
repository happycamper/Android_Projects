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


public class Settings extends Activity {
		//communicate out of ports, http(s)
		private EasyCommunicator easy; 
		public String tmp;
		public Context context;
	 	Messenger mService = null;
	    boolean mIsBound;
	    private TextView test;
	    final Messenger mMessenger = new Messenger(new IncomingHandler());
	    private Button send,disconnect;
	    private ActionBar main_bar;
	    public static final String PREF_LOCATION = "pref_location";

	    
	 
	    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.settings);
	        
	        test = (TextView) findViewById(R.id.test2);
	         send = (Button) findViewById(R.id.servicebutton);
	         disconnect = (Button) findViewById(R.id.dservicebutton);
	        
	        easy = new EasyCommunicator(this,mMessenger);
	         main_bar = getActionBar();
	 	}
	 
	 class IncomingHandler extends Handler { // Handler of incoming messages from clients.
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case Communication_Handler.MSG_SET_INT_VALUE:
	            	test.setText("Made it");
	                break;
	            case Communication_Handler.MSG_SET_STRING_VALUE:
	            	 tmp = msg.getData().getString("fromServer").toString();
	            	try{
	            		test.setText(tmp);
	            	}catch(NullPointerException e){
	            		e.printStackTrace();
	            	}
	            	
	            	
	            default:
	                super.handleMessage(msg);
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

	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	         case R.id.menu_Home:
	             Intent home_intent = new Intent(this, Main_Page.class);
	             startActivity(home_intent);
	             return true;
	             
	         case R.id.menu_Settings:
	        	 Intent settings_intent = new Intent(this, Settings.class);
	             startActivity(settings_intent);
	             return true;
	             
	         case R.id.menu_Share:
	        	 Intent share_intent = new Intent(this, Settings.class);
	             startActivity(share_intent);
	             return true;
	         default:
	             return super.onOptionsItemSelected(item);
	     }
	 }
	
	@Override
	protected void onStart(){
		super.onStart();
		easy.doBindService();
		main_bar.show();
		tmp = "test";
		
		
		 send.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		 easy.restaurantDBRequest("192.168.1.129/seat_easy/index.php?ALL_RESTAURANTS=true");
	    	}
	    });
		
		disconnect.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		 
	    		 easy.doUnbindService();
	    	}
	    });
		
	}

	@Override
	protected void onStop(){
		super.onStop();
		easy.doUnbindService();
		
	}

}