package proj.seateasy.v1;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class LocationServices {
	private LocationManager locationManager;
	private Context mContext;
	private LocationProvider locationProvider;
	public LocationListener listener;
	public Boolean providerEnabled;
	public List<String> providerNames;
	private double LAT,LONG;
	
	public LocationServices(Context context){
		this.mContext = context;
		
		 locationManager =
		        (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
		  locationProvider =
			        locationManager.getProvider(LocationManager.GPS_PROVIDER);
		  this.isEnabled();		
	}
	
	public Boolean isEnabled(){
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if(!gpsEnabled){
			providerEnabled = false;
			return false;
		}else{
			providerEnabled = true;
			providerNames = locationManager.getAllProviders();
			return true;
		}
	}
	
	public List<String> getProviderList(){
		return locationManager.getAllProviders();
	}
	
	public void startServices(){
		  listener = new LocationListener() {

		    @Override
		    public void onLocationChanged(Location location) {
		        // A new location update is received.  Do something useful with it.  In this case,
		        // we're sending the update to a handler which then updates the UI with the new
		        // location.
		                updateResources(location.getLatitude(),location.getLongitude());
		        }
		    
		    @Override
		    public void onProviderEnabled(String provider){
		    	providerEnabled = true;
		    	
		    }
		    
		    @Override
		    public void onProviderDisabled(String provider){
		    	providerEnabled = false;
		    }
		    
		    @Override
		    public void onStatusChanged(String string, int num, Bundle b){
		    	
		    }
		    
		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		        1000,          // 10-second interval.
		        10,             // 10 meters.
		        listener);
	}
	
	private void updateResources(double lat, double lon){
		LAT = lat;
		LONG = lon;
	}
	
	public String getLocation(){
		String locations = "";
		locations = LAT+","+LONG;
		return locations;		
	}

}
