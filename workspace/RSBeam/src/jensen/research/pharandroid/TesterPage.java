package jensen.research.pharandroid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TesterPage extends Activity implements LocationListener {
	
	private Button trackMe,serverConnect,serverDisconnect,testerSave,testerCalibrate,recCalibrate;
	private TextView serverStatus, rssiStatus,latlon;
	private EditText serverIP,saveFileName;
	private GPSclient gpsClient;
	private Location location;
	private LocationManager locationManager;
	private String provider;
	private double lati, lng;
	private GraphView graphView;
	private Handler mHandler;
	private int newRssi;
	private ImageView indicator;
	private Context mContext;
	private ArrayList<String> latlng,rssiList;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
    setContentView(R.layout.tester_page);
    
    	trackMe = (Button) findViewById(R.id.tester_track);
    	recCalibrate = (Button) findViewById(R.id.tester_calibrate_rec);
    	serverConnect = (Button) findViewById(R.id.tester_server_connect);
    	serverDisconnect = (Button) findViewById(R.id.tester_server_disconnect);
    	serverStatus = (TextView) findViewById(R.id.tester_server_status);
    	rssiStatus = (TextView) findViewById(R.id.tester_RSSI_status);
    	latlon = (TextView) findViewById(R.id.tester_latlon);
    	serverIP = (EditText) findViewById(R.id.tester_server_ip);
    	graphView = (GraphView) findViewById(R.id.graph);
    	indicator = (ImageView) findViewById(R.id.server_indicator);
    	testerSave = (Button) findViewById(R.id.tester_save);
    	testerCalibrate = (Button) findViewById(R.id.tester_calibrate);
    	saveFileName = (EditText) findViewById(R.id.tester_save_name);
    	
    	latlng = new ArrayList<String>();
    	rssiList = new ArrayList<String>();
    	

    	
    	
    	
    	mHandler = new Handler();
    	mHandler.postDelayed(UpdateGraph,1000);
    	mHandler.postDelayed(checkStatus, 1000);
    	
    	graphView.setMaxValue(100);
    	graphView.setSpeed(1.0f);
    	
    	mContext = this.getApplicationContext();
    	
    	gpsClient = new GPSclient(this.getApplicationContext());
    	
locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       
        
Criteria criteria = new Criteria();
provider = locationManager.getBestProvider(criteria, false);


 //location = locationManager.getLastKnownLocation(provider);
locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 2000, 0, this);
 

 
 
         
         

        // Initialize the location fields
        if (location != null) {
          System.out.println("Provider " + provider + " has been selected.");
          Toast.makeText(this, "Provider " +provider+ "selected", Toast.LENGTH_LONG);
          onLocationChanged(location);
        } else {
          latlon.setText("Location not available");
          //lon.setText("Location not available");
        } 
        
        testerSave.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String fileName = saveFileName.getText().toString();
					saveFile(fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
        testerCalibrate.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gpsClient.sendServer("1,"+String.valueOf(lati)+","+String.valueOf(lng));
			}
        	
        });
        
        recCalibrate.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gpsClient.sendServer("3,"+String.valueOf(lati)+","+String.valueOf(lng));
			}
        	
        });
    	
    	
    	trackMe.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gpsClient.sendServer("2,"+String.valueOf(lati)+","+String.valueOf(lng));
			}
    		
    	});
    	
    	serverConnect.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if(!serverIP.getText().toString().contains("")){
					gpsClient.startClient(serverIP.getText().toString());
				//}else{
					//Toast.makeText(getApplicationContext(), "You must enter a valid IP", Toast.LENGTH_LONG);
				//}
			}
    		
    	});
    	
    	serverDisconnect.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(gpsClient.socket.isConnected()){
					try {
						gpsClient.socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    		
    	});
    
    
	}
	
	Runnable UpdateGraph =new Runnable()
	{
	    public void run() 
	    {
	    	graphView.addDataPoint((float)-1.0f*newRssi);
        	mHandler.postDelayed(UpdateGraph, 300);   			
	    }
	};
	
	Runnable checkStatus =new Runnable()
	{
	    public void run() 
	    {
	    	checkConnection();
        	mHandler.postDelayed(checkStatus, 700);   			
	    }
	};
        	
  
	public void checkConnection(){
		if(GPSclient.socket != null){
    		if(GPSclient.socket.isConnected()){
    			indicator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.green_indicator));
    		}else{
    			indicator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.red_indicator));
    		}
    	}else{
    		indicator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.red_indicator));
    	}
		
	}
	@Override
    protected void onResume() {
      super.onResume();
      checkConnection();
      Criteria criteria = new Criteria();
      provider = locationManager.getBestProvider(criteria, false);
      
      
      locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 2000, 0, this);
      IntentFilter rssiFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
      this.registerReceiver(myRssiChangeReceiver, rssiFilter);

      WifiManager wifiMan=(WifiManager) getSystemService(Context.WIFI_SERVICE);
      wifiMan.startScan();
    }
	
	@Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates((LocationListener) this);
      this.unregisterReceiver(myRssiChangeReceiver);
    }
	
	@Override
    public void onLocationChanged(Location location) {
    	Toast.makeText(this, "Location Changed", Toast.LENGTH_LONG);
       lati = (double) (location.getLatitude());
       lng = (double) (location.getLongitude());
      System.out.println("Lat: "+lati+" Lon: "+ lng);
      latlon.setText("Lat: "+String.valueOf(lati)+"\nLon: "+String.valueOf(lng));
      
    }
	
	@Override
	public void onStop(){
		super.onStop();
		try {
			gpsClient.stopClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
    
    private BroadcastReceiver myRssiChangeReceiver
    = new BroadcastReceiver(){

    @Override
    public void onReceive(Context arg0, Intent arg1) {
     // TODO Auto-generated method stub
    	WifiManager wifiMan=(WifiManager) getSystemService(Context.WIFI_SERVICE);
    	
        wifiMan.startScan();
         newRssi = wifiMan.getConnectionInfo().getRssi();
         rssiList.add(String.valueOf(newRssi));
         String templatlng = String.valueOf(lati)+","+String.valueOf(lng);
         latlng.add(templatlng);
        
       
       if(newRssi > -70){
    	   rssiStatus.setTextColor(getResources().getColor(R.color.green));
       }else if(newRssi > -90 && newRssi < -70){
    	   rssiStatus.setTextColor(getResources().getColor(R.color.red));
       }
     rssiStatus.setText("RSSI\n"+String.valueOf(newRssi));
    }};
	
    public void saveFile(String fileName) throws IOException{
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i < latlng.size(); ++i){
    		sb.append(latlng.get(i));
    		sb.append(",");
    		sb.append(rssiList.get(i));
    		sb.append("\n");
    	}
    	
    	File file = new File(Environment.getExternalStorageDirectory()+"/"+fileName+".csv");
    	FileWriter fwriter = new FileWriter(file);
    	BufferedWriter out = new BufferedWriter(fwriter);
    	out.write(sb.toString());
    	out.close();
    	System.out.println("File Saved");
    }
	
}
