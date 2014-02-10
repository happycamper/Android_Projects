package jensen.research.pharandroid;



import com.ericsson.interfaces.CommunicatorInterface;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import at.abraxas.amarino.Amarino; //need to import amarino library.jar
import at.abraxas.amarino.AmarinoIntent;
import android.os.Handler;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException; //we never use this, except for error catching



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;



public class pharandroid extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(savedInstanceState != null){
        	Bundle load = savedInstanceState.getBundle(ICICLE_KEY);
        	restorestate(load); //this code is to capture and reload a saved state
        	GoAccel();
        }
        GoAccel();
        
        
    }
    //started making everything public, bad coding but quick and dirty
    public boolean connected = false; //global var used to determine connected state to server
    public String serverIpAddress = ""; //global var to store server IP address
    private EditText serverIp; //inbox to have user input for server ip address
    private TextView displayconnected; //line of text to show current connected servers
    public Socket socket; //standard java socket, need to have networking enabled!
    public PrintWriter out; //output buffer for socket
    private EditText Serverwords; //input for talking to server on test server page
    private String Serverstring = ""; //storage for information to be sent to server
    private TextView senttoserver;
    private TextView homedisplaystatus;
    private TextView pos;
    
    //everything for the goACCEL function
    private TextView accText;
    private TextView pointdisp;
    private SensorManager myManager;
    private List<Sensor> sensors;
    private Sensor accSensor;
    private double disptheta,dispphi;
    private Boolean showdispflag = false;
    private float oldX, oldY, oldZ,cX,cY,cZ,dispx,dispy,dispz,checksum,sendandroidx,sendandroidy = 0f;
    private Handler mHandler = new Handler();
    public float thisX,thisY,thisZ;
    public long mStartTime = 0L;
    private Button accelstart; //start accel button on home screen, public
    private Button accelstop; //stop button on home screen
    private Button calibratebutton; //calibrate button on home screen
    private CheckBox trackposition;
    private boolean accelstarted = false;
    private boolean calibrated = false;
    public float[] sensorarray = new float[3];
    public float[] calibratearray = new float[3];
    public float[] phasediffarray = new float[2];
    private float[] displayPhases = new float[2];
    SystemClock thread;
    
    private static String ICICLE_KEY = "pharandroid";
    private static int SHOW_DESC = 1;
	private static int SHOW_DATA = 2;
	public int op_code;
	public String res[];
	private static final String LOG = "MyApp";
	public String newinfo;
	private String result;
	private Intent newBindIntent;
	public TextView showinfo;
	public String accel = "Accelerometer";
	public int starting,finishing;
	public String[] values;
	public float globalx,globaly,globalz;
	public CommunicatorInterface communicator = null;
	public ComServiceConnection conn;

    
    //This is using amarino to connect to an Arduino BT, put the MAC of the BT
    private static final String DEVICE_ADDRESS =  "00:07:80:99:56:34";
    
    //Arduino receiver, from amarino library, receives data back from BT
    private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
    
    //create options menu happens when user presses menu button, see /res/menu/mainmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//this is standard code from Android Developer website
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    
    //each menu item must have a corresponding xml layout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//TextView display = (TextView) findViewById(R.id.display);
        // Handle item selection
        switch (item.getItemId()) {
        //firstcase is for the main homescreen with all information display
        case R.id.start:
        	setContentView(R.layout.main);
        	//GoAccel() is a continuous running function of sub functions
        	GoAccel();
            return true;
         //server case is to configure a socket
        case R.id.server:
            setContentView(R.layout.serverconfigure);
            ConnectToServer();
            return true;
        //just a test case, if protocol not used, this page is used to send random information
        case R.id.talk:
        	setContentView(R.layout.communicator);
        	ServerTalk();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    //function to load xml and to make a socket connection as client
    public void ConnectToServer(){
    	
    	//setting up the xml on the server configure page
    	serverIp = (EditText) findViewById(R.id.server_ip);
        displayconnected = (TextView) findViewById(R.id.text1);
        Button ServerConnect = (Button) findViewById(R.id.connect_phones);
        Button Disconnect =  (Button) findViewById(R.id.cancel);
        
        //if we are already connected, use global var serverIPaddress to display connection
        //connected is a global variable
        if(connected){
    		displayconnected.setText("Connected to: "+serverIpAddress);
    	}
        
        //create the listener on the disconnect button
        Disconnect.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		//we need to be connected to disconnect
        		if(connected){
        			try{
        				socket.close();
        				displayconnected.setText("Disconnected from :"+serverIpAddress);
        				connected = false;
        			}catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
        		}else{
        			displayconnected.setText("Not Currently Connected, enter IP Address");
        		}
		//		unregisterReceiver(arduinoReceiver);
			}
		});
        
        //button to connect to server
        ServerConnect.setOnClickListener(new View.OnClickListener() {
	        
	        public void onClick(View v) {
	        	//only can connect if we haven't already
	            if (!connected) {
	            	//when the button is pressed, snag the ip from serverIp edittext
	                serverIpAddress = serverIp.getText().toString();
	                //just in case the network is congested, inform user we are connecting
	                displayconnected.setText("Connecting To " + serverIpAddress);
	                if (!serverIpAddress.equals("")) {
	                	//Thread cThread = new Thread(new ClientThread());
	       	              //      cThread.start();
	                	try{
	    			   		InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
			                Log.d("ClientActivity", "C: Connecting...");
			                
	   		                //standard socket connection, random port 4444
			                
	   		                socket =  new Socket(serverAddr, 4444);
	   		                //set global var connected to true
			                connected = true;
			                
			                //confirm to the user that we have connected to serverIpAddress
			                displayconnected.setText("Connected To " + serverIpAddress);
			                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                    .getOutputStream())), true);
			                //assuming user running processing "BEAM", this starts the beam appropriately
                            out.println("180,180,0,-105");
	                	}catch (Exception e) {
	                        Log.e("ClientActivity", "S: Error", e);
	                    }
	                	
	             }
	                
	            }
	        }
});
        
    }
    
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
	 
	 
	/** Depending on the operation code use one of 
	the methods of the Communicator interface */
	public void getOperationCode() throws RemoteException {
		/** Code for showing data on screen */
			checkCallingPermission( "ericsson.msag.Communicator" );
			/** One of the methods of the Communicator interface */
			newinfo = communicator.getLatestCacheItem("Orientation","A100000D9836B4");
			starting = newinfo.indexOf("<swe:values>");
			finishing=newinfo.lastIndexOf("</swe:values>");
			newinfo = newinfo.substring(starting,finishing);
			values = newinfo.split(",");
			globalx = Float.valueOf(values[2]).floatValue();
			globaly = Float.valueOf(values[3]).floatValue();
			globalz = Float.valueOf(values[4]).floatValue();
			/** Unbind service and disconnect */
			unbindService( conn );
		
	}
    //just a test page, probably won't work if protocol is running on server
    public void ServerTalk(){
    	Button Serversend = (Button) findViewById(R.id.send);
    	Serverwords = (EditText) findViewById(R.id.textservertalk);
    	final TextView senttoserverstatus = (TextView) findViewById(R.id.senttoserverstatus);
    	
    	Serversend.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		if(connected){
        			try{
        				Serverstring = Serverwords.getText().toString();
        				out.println(Serverstring);
        				senttoserver.setText(Serverstring);
        			}catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
        		}else{
        			senttoserverstatus.setText("Not Connected");
        			senttoserver.setText("Nothing sent because we're not connected");
        		}
		//		unregisterReceiver(arduinoReceiver);
			}
		});
    	
    }
    
    //main thread, runs information gathering and calculations..
    public void GoAccel(){
    	homedisplaystatus = (TextView) findViewById(R.id.display);
    	pos = (TextView) findViewById(R.id.calibratepos);
		pos.setText("Current Calibrated Position is:\nx:"+cX+";\ny:"+cY+";\nz:"+cZ+";\n");
    	if(accelstarted){
    		//if we have calibrated and started accel button
    		homedisplaystatus.setText("Sensor Reader is Running...");
    		
    		//mhandler is the "timer" of the program we can remove any requests before making a new one
    		mHandler.removeCallbacks(mUpdateTimeTask);
    		mHandler.postDelayed(mUpdateTimeTask,100);
    	}else{
    		homedisplaystatus.setText("Sensor Reader is not Running...");
    	}
    	 myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    		
    	 	//in newer versions of android TYPE_ORIENTATION deprecated
	        sensors = myManager.getSensorList(Sensor.TYPE_ORIENTATION);
	
	        if(sensors.size() > 0)
	        {
	          accSensor = sensors.get(0);
	        }
	        
	        //lets create our buttons
	        accelstart = (Button) findViewById(R.id.startaccel);
	        accelstop = (Button) findViewById(R.id.stopaccel);
	        calibratebutton =  (Button) findViewById(R.id.calibrate);
	        trackposition = (CheckBox) findViewById(R.id.CheckBox01);
	        pointdisp = (TextView) findViewById(R.id.point);
	        
	        //check to see that we've calibrated our position
	        if(calibrated){
	        	
	        	//listen for user to hit the start button to start reading accelerometer
	        accelstart.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					accelstarted = true;
					homedisplaystatus.setText("sensor reader is running...");
					//mStartTime is for the mhandler, by default it should always be 0L
					if (mStartTime == 0L) {
			            mStartTime = System.currentTimeMillis();
			            mHandler.removeCallbacks(mUpdateTimeTask);
			            mHandler.postDelayed(mUpdateTimeTask, 100);
			       }

					
				}
			});
	        }
	        else{
	        	homedisplaystatus.setText("CALIBRATE POSITION FIRST!");
	        }
	        
	        //create listener if user decides to hit stop button
	        accelstop.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//if we press stop, don't update task, stop the accelerometer, set default time, 
					//and start main app
					mHandler.removeCallbacks(mUpdateTimeTask);
					accelstarted = false;
					homedisplaystatus.setText("Sensor Reader is not running...");
					mStartTime = 0L;
					GoAccel();
			       }
			});
	        
	        //button for calibration, it is constantly active in case of recalibration
	        calibratebutton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//oldX,oldY,oldZ are in updateTV, they are current reading of Accelerometer
					//old... are always updated on change of the accelerometer, see updateTV
					connectToMSAG();
					cX = globalx;
					cY = globaly;
					cZ = globalz;
					
					//calibratearray is an array to store the calibrate position in case we leave the program
					calibratearray[0]=cX;
					calibratearray[1]=cY;
					calibratearray[2]=cZ;
					
					//let the user know that we've calibrated, and restart main page
					calibrated = true;
					pos.setText("Current Calibrated Position is:\nx:"+cX+";\ny:"+cY+";\nz:"+cZ+";\n");
					GoAccel();
			       }
			});
	        
    }
    
    //this is the main thread, gets executed by mHandler
    private Runnable mUpdateTimeTask = new Runnable() {
 	   public void run() {
 		   accText = (TextView) findViewById(R.id.accelvalues);
 	       final long start = mStartTime;
 	       long millis = SystemClock.uptimeMillis() - start;
 	       int seconds = (int) (millis / 1000);
 	       int minutes = seconds / 60;
 	       seconds     = seconds % 60;
 	       connectToMSAG();
 	       dispx = globalx - cX;
 	       dispy = globaly - cY;
 	       dispz = globalz - cZ;
 	       displayPhases = phasediff(dispx,dispy,dispz);
 	       sensorarray[0] = dispx;
 	       sensorarray[1] = dispy;
 	       sensorarray[2] = dispz;
 	       //checksum = 255 - (Math.round(dispx)+Math.round(dispy)+Math.round(dispz));
 	       if(connected){
 	    	   out.println(Math.round(dispx)+","+Math.round(dispy)+","+Math.round(dispz)+","+Math.round(Math.toDegrees((double)displayPhases[0]))+","+Math.round(Math.toDegrees((double)displayPhases[1])));
 	       }
 	       send_arduino(200.0,displayPhases); //200.0 is frametype for phasedata
 	       //sendandroidx = Math.round(Math.toDegrees((double)displayPhases[0]));
 	      //sendandroidy = Math.round(Math.toDegrees((double)displayPhases[1]));
 	      ///Amarino.sendDataToArduino(pharandroid.this,"00:07:80:99:56:34", 'B',dispx);
 	      //thread.sleep(100);
 	     //Amarino.sendDataToArduino(pharandroid.this,"00:07:80:99:56:34", 'B',sendandroidx);
 	     //thread.sleep(100);
 	    //Amarino.sendDataToArduino(pharandroid.this,"00:07:80:99:56:34", 'B',sendandroidy);
 	       accText.setText("x: " + dispx + ";\n y:" + Math.round(dispy) + ";\n z: " + Math.round(dispz) +";\n Phase Diff x: "+ Math.round(Math.toDegrees((double)displayPhases[0]))+";\t Theta: "+Math.round(Math.toDegrees(disptheta))+"\n Phase Diff y: "+Math.round(Math.toDegrees((double)displayPhases[1]))+"\t Phi: "+Math.round(Math.toDegrees(dispphi)));
 	       mHandler.postDelayed(this,300); //this is to loop every 300 ms
 	   }
 	};
 	
 	public float[] phasediff(float x2, float y2, float z2){
 		float returnarray[] = new float[2];
 		double w_hat = 0;
 		double PhaseBx,PhaseBy = 0;
 		double a_hat = 0;
 		double phireal = 0;
 		double thetareal = 0;
 		double newpoint[]={0,0,0};
 		double psi = y2*Math.PI/180;
 		double theta = z2*Math.PI/180;
 		double phi = x2*Math.PI/180;
 		double rotmatrix[][] = {{Math.cos(theta)*Math.cos(phi),-1.0*Math.cos(psi)*Math.sin(phi)+Math.sin(psi)*Math.sin(theta)*Math.cos(phi),Math.sin(phi)*Math.sin(psi)+Math.cos(psi)*Math.sin(theta)*Math.cos(phi)},
 				{Math.cos(theta)*Math.sin(phi),Math.cos(psi)*Math.cos(phi)+Math.sin(psi)*Math.sin(theta)*Math.sin(phi),-1.0*Math.sin(psi)*Math.cos(phi)+Math.cos(psi)*Math.sin(theta)*Math.sin(phi)},
 				{-1.0*Math.sin(theta),Math.sin(psi)*Math.cos(theta),Math.cos(psi)*Math.cos(theta)}
 		};
 		
 		double mypoint[] = {0.00001,0.00000001,1.0}; //these are the plane's xyz coordinates
 		int m1rows = rotmatrix.length;
 		if(trackposition.isChecked()){
 			mypoint[0] = 1.0;
 			mypoint[1] = 1.0;
 			mypoint[2] = 1.414;
 		}
 		//int m1cols = rotmatrix[0].length;
 		//int m2rows = mypoint.length;
 			double result = 0;
 		if(trackposition.isChecked()){
 				if(!showdispflag){
 					showpoint();
 					pointdisp.setText("Tracking point x: "+mypoint[0]+" y: "+mypoint[1]+" z: "+mypoint[2]);
 					showdispflag = true;
 				}
 		for(int i=0;i<m1rows;++i){
 			 result = 0;
 			for(int j=0;j<m1rows;++j){
 				result = 1*rotmatrix[i][j]*mypoint[j]+result;
 			}
 			newpoint[i]=result;
 		}
 		}else{
 			if(showdispflag){
 					hidepoint();
 					showdispflag = false;
 					showpoint();
 					pointdisp.setText("You currently have control of beam direction");
 			}
 			for(int i=0;i<m1rows;++i){
 	 			 result = 0;
 	 			for(int j=0;j<m1rows;++j){
 	 				result = -1*rotmatrix[i][j]*mypoint[j]+result;
 	 			}
 	 			newpoint[i]=result;
 	 		}
 		}
 		if(newpoint[1]<0){
 			w_hat = -1*(Math.sqrt(Math.pow(newpoint[0],2)+Math.pow(newpoint[1],2)));
 			phireal = Math.PI + Math.acos(newpoint[0]/w_hat);
 		}
 		if(newpoint[1]>=0){
 			w_hat = Math.sqrt(Math.pow(newpoint[0],2)+Math.pow(newpoint[1],2));
 			phireal = Math.acos(newpoint[0]/w_hat);
 		}
 		a_hat = Math.sqrt(Math.pow(w_hat, 2)+Math.pow(newpoint[2], 2));
 		w_hat = Math.sqrt(Math.pow(newpoint[0],2)+Math.pow(newpoint[1],2));
 		
 		thetareal =  .5*Math.PI - Math.acos(w_hat/a_hat); //pi/2 - angle to get theta
 		
 		
 		
 		//thetareal = 45*Math.PI/180;
 		//phireal = 178*Math.PI/180;
 		
 		disptheta = thetareal;
 		dispphi = phireal;
 		
 		PhaseBx = -1*Math.PI*Math.sin(thetareal)*Math.cos(phireal);
 		PhaseBy = -1*Math.PI*Math.sin(thetareal)*Math.sin(phireal);
 		
 		
 		returnarray[0]= (float) PhaseBx;
 		returnarray[1]= (float)PhaseBy;
 		
 		return returnarray;
 		
 		
 		
 	}
 	
 	public void showpoint(){
 		Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        pointdisp.setVisibility(View.VISIBLE);
        pointdisp.startAnimation(animation);
 	}
 	
 	public void hidepoint(){
 		Animation animation = AnimationUtils.loadAnimation(pharandroid.this,
                android.R.anim.slide_out_right);
        pointdisp.startAnimation(animation);
        pointdisp.setVisibility(View.GONE);
 	}
 	public void updateTV(float x, float y, float z)
    {
     //thisX = x - oldX * 10;
     //thisY = y - oldY * 10;
     //thisZ = z - oldZ * 10;

    // accText.setText("x: " + Math.round(thisX) + ";\n y:" + Math.round(thisY) + ";\n z: " + Math.round(thisZ));	
     oldX = x;
     oldY = y;
     oldZ = z;
    }
 	
 	public void send_arduino(double frametype,float[] values){
 		final double startbyte = 255.0;
 		final int intlength = (int)values.length;
 		final double length = (double)values.length;
 		final int sleeptime = 20;
 		final double frameid = 1.0;
 		final char funcflag = 'B';
 		double checksum = startbyte+length+frametype+frameid;
 		Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,startbyte);
 		thread.sleep(sleeptime);
 		Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,length);
 		thread.sleep(sleeptime);
 		Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,frametype);
 		thread.sleep(sleeptime);
 		Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,frameid);
 		thread.sleep(sleeptime);
 			for(int i=0;i<intlength;++i){
 				sendandroidx = Math.round(Math.toDegrees((double)values[i]));
 					if(sendandroidx < 0){
 				checksum=checksum+256+sendandroidx;
 					}else{
 						checksum+=sendandroidx;
 					}
 				Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,sendandroidx);
 		 		thread.sleep(sleeptime);
 			}
 			checksum = Math.round((double)checksum % 6);
 			Amarino.sendDataToArduino(pharandroid.this,DEVICE_ADDRESS, funcflag,checksum);
 	 		
 		
 	}
 	
 	private final SensorEventListener mySensorListener = new SensorEventListener()
    {
     public void onSensorChanged(SensorEvent event)
     {
          updateTV(event.values[0],
                    event.values[1],
                    event.values[2]);
     }

     public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };
    

    @Override
    protected void onStart()
    {
     super.onStart();
     myManager.registerListener(mySensorListener, accSensor, SensorManager.SENSOR_DELAY_GAME);
     registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		
		// this is how you tell Amarino to connect to a specific BT device from within your own code
		//Amarino.connect(this, DEVICE_ADDRESS);
    }

    @Override
    protected void onStop()
    {    
     
     super.onStop();
     //Amarino.disconnect(this, DEVICE_ADDRESS);
		
		// do never forget to unregister a registered receiver
     mHandler.removeCallbacks(mUpdateTimeTask);
		unregisterReceiver(arduinoReceiver);
		myManager.unregisterListener(mySensorListener);
    }
   public Bundle savestate(){
	   Bundle map = new Bundle();
	   map.putBoolean("accel", accelstarted);
	   map.putBoolean("calibrated",calibrated);
	   map.putFloatArray("calibratedvalues",calibratearray);
	   map.putBoolean("server", connected);
	   map.putString("oldip", serverIpAddress);
	   return map;
   }
   
   public void restorestate(Bundle icicle){
	   accelstarted = icicle.getBoolean("accel");
	   calibrated = icicle.getBoolean("calibrated");
	   sensorarray = icicle.getFloatArray("sensorvalues");
	   serverIpAddress = icicle.getString("oldip");
	   dispx = sensorarray[0];
	   dispy= sensorarray[1];
	   dispz = sensorarray[2];
	   connected = icicle.getBoolean("server");
	   calibratearray = icicle.getFloatArray("calibratedvalues");
	   cX = calibratearray[0];
	   cY = calibratearray[1];
	   cZ = calibratearray[2]; 
	   servereconnect(serverIpAddress);
	   
   }
   
   public void servereconnect(String oldip){
	   try{
	   		InetAddress serverAddr = InetAddress.getByName(oldip);
           Log.d("ClientActivity", "C: Connecting...");
              
           socket =  new Socket(serverAddr, 4444);
           out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                   .getOutputStream())), true);
   	}catch (Exception e) {
           Log.e("ClientActivity", "S: Error", e);
       }
	   
   }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Store the game state
        outState.putBundle(ICICLE_KEY, savestate());
    }
    
    public class ArduinoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;
			
			// the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
			final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
			
			// the type of data which is added to the intent
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
			
			// we only expect String data though, but it is better to check if really string was sent
			// later Amarino will support differnt data types, so far data comes always as string and
			// you have to parse the data to the type you have sent from Arduino, like it is shown below
			if (dataType == AmarinoIntent.STRING_EXTRA){
				data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
				pos.setText(data);
				
				if (data != null){
					//mValueTV.setText(data);
					try {
						// since we know that our string value is an int number we can parse it to an integer
						final int sensorReading = Integer.parseInt(data);
					} 
					catch (NumberFormatException e) { /* oh data was not an integer */ }
				}
			}
		}
	}
    
    
}