package com.example.gps_example;

import java.io.IOException;

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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	
	private TextView lat,lon,textrssi;
	private Button getCoords,startServer,startClient,sendCoords;
	private LocationManager locationManager;
	private String provider;
	private Location location;
	private GPSserver gpsServer;
	private GPSclient gpsClient;
	private double lati,lng;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.registerReceiver(this.myRssiChangeReceiver,
        	       new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.lon);
        textrssi = (TextView) findViewById(R.id.rssi);
        getCoords = (Button) findViewById(R.id.get_coords);
        startServer = (Button) findViewById(R.id.start_server);
        startClient = (Button) findViewById(R.id.start_client);
        sendCoords = (Button) findViewById(R.id.send_coords);
        
        gpsServer = new GPSserver(this.getApplicationContext());
        
        
        gpsClient = new GPSclient(this.getApplicationContext());
        
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       
        
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        
        
         location = locationManager.getLastKnownLocation(provider);
         
         

        // Initialize the location fields
        if (location != null) {
          System.out.println("Provider " + provider + " has been selected.");
          onLocationChanged(location);
        } else {
          lat.setText("Location not available");
          lon.setText("Location not available");
        } 
        
        getCoords.setOnClickListener( new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				location = locationManager.getLastKnownLocation(provider);

		        // Initialize the location fields
		        if (location != null) {
		          //System.out.println("Provider " + provider + " has been selected.");
		          onLocationChanged(location);
		        } else {
		          lat.setText("Location not available");
		          lon.setText("Location not available");
		        }
			}
        	
        });
        
        startServer.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gpsServer.startServer();
			}
        	
        });
        
        startClient.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gpsClient.startClient();
			}
        	
        });
        
        sendCoords.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
	
				sb.append(String.valueOf(lati));
				sb.append(",");
				sb.append(String.valueOf(lng));
				gpsClient.sendServer(sb.toString());
			}
        	
        });
       
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 100, 1, (LocationListener) this);
      IntentFilter rssiFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
      this.registerReceiver(myRssiChangeReceiver, rssiFilter);

      WifiManager wifiMan=(WifiManager) getSystemService(Context.WIFI_SERVICE);
      wifiMan.startScan();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates((LocationListener) this);
      this.unregisterReceiver(myRssiChangeReceiver);
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	try {
			gpsServer.stopSocket();
			gpsClient.stopClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void onLocationChanged(Location location) {
    	
       lati = (double) (location.getLatitude());
       lng = (double) (location.getLongitude());
      System.out.println("Lat: "+lati+" Lon: "+ lng);
      lat.setText(String.valueOf(lati));
      lon.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
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
        int newRssi = wifiMan.getConnectionInfo().getRssi();
     textrssi.setText(String.valueOf(newRssi));
    }};
    
}
