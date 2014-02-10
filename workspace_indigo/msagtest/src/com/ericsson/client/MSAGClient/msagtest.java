package com.ericsson.client.MSAGClient;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.ericsson.interfaces.CommunicatorInterface;

public class msagtest extends Activity {
	
	public TelephonyManager tlp;
	private String result;
	private Intent newBindIntent;
	public TextView showinfo;
	public String accel = "Accelerometer";
	public int starting,finishing;
	public String[] values;
	public float f;
	public CommunicatorInterface communicator = null;
	public ComServiceConnection conn;
	 
	private static int SHOW_DESC = 1;
	private static int SHOW_DATA = 2;
	/** ... more of opcodes as you see fit */
	public int op_code;
	public String res[];
	private static final String LOG = "MyApp";
	public String newinfo;
	 
	/** Check if the MSAG service is running on 
	the background of the operating system */
	private boolean checkMsagAvailability() {
		boolean found = false;
		ActivityManager am = 
		( ActivityManager )this.getSystemService( 
	          Context.ACTIVITY_SERVICE );
		List<RunningServiceInfo> rapi = am.getRunningServices( 100 );
		Iterator<RunningServiceInfo> iterapi = rapi.iterator();
		while( iterapi.hasNext() ) {
			RunningServiceInfo proc = iterapi.next();
			String serviceName = proc.service.flattenToString();
			result += "\n" + serviceName;
			if( serviceName.equals( 
			"ericsson.msag/ericsson.msag.Communicator" ) ) {
				found = true;
				break;
			}
		}
		return found;
	}
	 
	/** Connection point to the MSAG. Binds a new ComServiceConnection */
	private void connectToMSAG() {
		try {
			Log.i( LOG, "Initialize services" );
			conn = new ComServiceConnection();
			newBindIntent = new Intent();
			newBindIntent.setAction( "EXTERNAL_CALL" );
			bindService( newBindIntent, conn, BIND_AUTO_CREATE );
			Log.i( LOG, "Bind to service" );			
		}catch( Exception re ) { 
			Log.i( LOG, re.getMessage() );
		}
	}
	 
	/** Create a ServiceConnection that will bind to the MSAG server stub */
	public class ComServiceConnection implements ServiceConnection {
		public void onServiceConnected( ComponentName name,
	IBinder service ) {
			communicator = 
			CommunicatorInterface.Stub.asInterface( service );
			try {
				/** this operation implements 
	                              remote calls to the MSAG */
				getOperationCode();
			}catch( RemoteException re ) {}
			Log.i( LOG, "CONNECTION: " + name.getClassName() );
		}
		public void onServiceDisconnected( ComponentName name ) {
			communicator = null;
			Log.i( LOG, "Unbound" );
		}
	}
	 
	/** Set the type of operation to be accessed */
	private void setOpcode( int i ) {
		this.op_code = i;
	}
	 
	/** Depending on the operation code use one of 
	the methods of the Communicator interface */
	public void getOperationCode() throws RemoteException {
		/** Code for showing data on screen */
		if( op_code == SHOW_DESC ){
			checkCallingPermission( "ericsson.msag.Communicator" );
			/** One of the methods of the Communicator interface */
			newinfo = communicator.getLatestCacheItem("Orientation","A100000D9836B4");
			starting = newinfo.indexOf("<swe:values>");
			finishing=newinfo.lastIndexOf("</swe:values>");
			newinfo = newinfo.substring(starting,finishing);
			values = newinfo.split(",");
			f = Float.valueOf(values[2]).floatValue();
			showinfo.setText("x: "+f);
			/** Unbind service and disconnect */
			unbindService( conn );
		}else if( op_code == SHOW_DATA ) {
			//checkCallingPermission( "ericsson.msag.Communicator" );
			/** Another method of the Communicator interface */
			res = communicator.getData();
			starting=res[1].indexOf("<swe:values>");
			finishing=res[1].lastIndexOf("</swe:values>");
			//res[1]=res[1].substring(starting,finishing);
			showinfo.setText(res[1].toString());
			values = res[1].toString().split(",");
			//showinfo.setText(values[2].toString());
			/** Unbind service and disconnect */
			unbindService( conn );
		}else {
			res = null;
		}
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        showinfo = (TextView) findViewById(R.id.info);
        showinfo.setText("Made IT");
        
       // Log.i( LOG, "Create MSAG client ..." );
        
    	if( checkMsagAvailability() == false ) {
    		Log.i( LOG, "MSAG service not found..." );
    		showinfo.setText("NOTFOUND");
    	}else {
    		/** The actual access to the MSAG resources.
    		At first we set the specified 
                    operation code that we want to use,
    		Secondly we do the connection,
    		Thirdly manipulate the results from String[] res */
    		setOpcode( SHOW_DESC );
    		connectToMSAG();
    		/** String res[] has the results of the operation
    		... Code for manipulating the result */
    	}
    }
}