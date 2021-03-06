package jensen.research.pharandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GPSserver {
	
	private Context mContext;
	private ServerSocket serverSocket;
    private Handler updateConversationHandler;
    Thread serverThread = null;
    public static final int SERVERPORT = 6000;
    private Handler mHandler;
    private LocationManager locationManager;
    private String provider;
    private Location location,recLocation,calLocation,newLocation;
    private double lati,lng;
    public static double calLat,calLon,calZ;
	
	public GPSserver(Context context){
		this.mContext = context;
		calLat = 0.0;
		calLon = 0.0;
		calZ = 0.0;
		recLocation = new Location("dummy1");
		calLocation = new Location("dummy2");
		newLocation = new Location("dummy3");
		
	}
	
	
	public void startServer(){
		mHandler = new Handler();
		this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
        
	}
	
	public class ServerThread implements Runnable {
		 
        public void run() {
            try {
                    serverSocket = new ServerSocket(SERVERPORT);
                    System.out.println("Started Server");
                    while (true) {
                        // listen for incoming clients
                        Socket client = serverSocket.accept();
                        System.out.println("Accepted Client");
                        //Toast.makeText(mContext, "Received Client", Toast.LENGTH_LONG);
 
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                            	System.out.println("Got something");
                                Log.d("ServerActivity", line);
                                System.out.println(line);
                                String[] splitter = line.split(",");
                                if(splitter[0].contains("1")){
                                	System.out.println("got cal");
                                	//calLat = 10000*(pharandroid.GLOBAL_LAT - Double.valueOf(splitter[1]));
                                	//calLon = 10000*(pharandroid.GLOBAL_LON - Double.valueOf(splitter[2]));
                                	
                                	calLat = Double.valueOf(splitter[1]);
                                	calLon = Double.valueOf(splitter[2]);
                                	//calZ = Math.sqrt(calLat*calLat+calLon*calLon);
                                	calLocation.setLatitude(calLat);
                                	calLocation.setLongitude(calLon);
                                	calZ = recLocation.distanceTo(calLocation);
                                	
                                }else if(splitter[0].contains("3")){
                                	pharandroid.GLOBAL_LAT = Double.valueOf(splitter[1]);
                                	pharandroid.GLOBAL_LON = Double.valueOf(splitter[2]);
                                	recLocation.setLatitude(pharandroid.GLOBAL_LAT);
                                	recLocation.setLongitude(pharandroid.GLOBAL_LON);
                                	
                                }else if(splitter[0].contains("2")){
                                
                                	System.out.println("got new point");
                               // double temp1 = 10000*(pharandroid.GLOBAL_LAT - Double.valueOf(splitter[1]));
                                //double temp2 = 10000*(pharandroid.GLOBAL_LON - Double.valueOf(splitter[2]));
                                
                                double temp1 = Double.valueOf(splitter[1]);
                                double temp2 = Double.valueOf(splitter[2]);
                                newLocation.setLatitude(temp1);
                                newLocation.setLongitude(temp2);
                                
                               // double tempz = Math.sqrt(temp1*temp1+temp2*temp2);
                                double tempz = recLocation.distanceTo(newLocation);
                                
                               // double newpointdifflat = temp1-calLat;
                                //double newpointdifflon = temp2-calLon;
                               // double thirdside = Math.sqrt(newpointdifflat*newpointdifflat+newpointdifflon*newpointdifflon);
                                double thirdside = calLocation.distanceTo(newLocation);
                                pharandroid.thetaC = Math.toDegrees(Math.acos((tempz*tempz+calZ*calZ-thirdside*thirdside)/(2*calZ*tempz)));
                                
                                double setY = (calLocation.getLatitude()/calLocation.getLongitude())*newLocation.getLatitude();
                                if(newLocation.getLatitude() >= calLocation.getLatitude()){
                                if((temp1 >= calLat) && (temp2 <= setY) ){
                                	pharandroid.phiC = 0.0;
                                }else if((temp1 >= calLat) && (temp2 >= setY) ){
                                pharandroid.phiC = 180.0;
                                }
                                }else{
                                	if((temp1 >= calLat) && (temp2 >= setY) ){
                                    	pharandroid.phiC = 0.0;
                                    }else if((temp1 >= calLat) && (temp2 <= setY) ){
                                    pharandroid.phiC = 180.0;
                                    }
                                }
                               // pharandroid.mypoint[0] = 0;
                                //pharandroid.mypoint[1] = temp1;
                                //pharandroid.mypoint[2] = tempz;
                                
                                
                                System.out.println("calZ "+calZ+" tempz "+tempz+"diff "+thirdside);
                                System.out.println("Theta "+pharandroid.thetaC);
                                }
                                //pharandroid.thetaC = 180 - Math.toDegrees(Math.asin((pharandroid.GLOBAL_LON-Double.valueOf(splitter[2]))/(pharandroid.GLOBAL_LAT-Double.valueOf(splitter[1]))));
                                //pharandroid.phiC = 180;
                                
                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
			    
			    public void stopSocket() throws IOException{
			    	if(serverSocket != null){
			    	serverSocket.close();
			    	}
			    }


}
