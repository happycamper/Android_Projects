package proj.seateasy.v1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class Communication_Handler extends Service {
	
	public Socket socket;
	public HttpURLConnection con;
	public PrintWriter out;
	public BufferedReader in, httpin;
	public String indata;
	private static boolean isRunning = false;
	public static boolean isConnected = false;
	static final String ipAddress = "192.168.1.129";
	static final String DOMAIN = (ipAddress+"/seat_easy/index.php?");
	static final int ipPort = 4444;
	static final String WEBSITE = "https://www.seateasyapp.com/";
	static final String NETWORK_WARNING = "network_warning";

    ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    int mValue = 0; // Holds last value set by a client.
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_INT_VALUE = 3;
    static final int MSG_SET_STRING_VALUE = 4;
    static final int HTTP_REQUEST = 5;
    static final int START_PRIVATE_SOCKET = 6;
    
    static final int RESTAURANT_DB_REQUEST = 70;
    static final int RESTAURANT_DB_WRITE_OK = 71;
    static final int RESTAURANT_DB_WRITE_FAIL = 72;
    static final int MSG_DECODE_RESTAURANT = 73;
    
    
    
    final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                mClients.add(msg.replyTo);
                break;
            case MSG_UNREGISTER_CLIENT:
                mClients.remove(msg.replyTo);
                break;
            case MSG_SET_INT_VALUE:
            	if(socket.isConnected()){
            	out.println(msg.replyTo+",180,180,180,-105");
            	new BufferedReadTask().execute();
            	}
                break;
            case MSG_SET_STRING_VALUE:
            	if(socket.isConnected()){
            		String toSend = msg.getData().getString("fromClient");
                	out.println(toSend);
                	new BufferedReadTask().execute();
                	}
            	break;
            	
            case HTTP_REQUEST:
            	System.out.println("HERE");
                String toSend = msg.getData().getString("fromClient");
            	new HTTPBufferedReadTask().execute(toSend);
            	
            	break;
           	
            case RESTAURANT_DB_REQUEST:
            	
            	String urlRequest = msg.getData().getString("fromClient");
            	new XMLParseTask().execute(urlRequest);
            	
            	break;
            	
            case START_PRIVATE_SOCKET:
            	new ServerConnectTask().execute();
            	break;
            	
            default:
                super.handleMessage(msg);
            }
        }
    }
	
	 public class LocalBinder extends Binder {
		 Communication_Handler getService() {
	            return Communication_Handler.this;
	        }
	    }

	    @Override
	    public void onCreate() {
	    	
	    	//new ServerConnectTask().execute();
	    	
	    	//we could put a notification here...
	    }
	    
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.i("LocalService", "Received start id " + startId + ": " + intent);
	        // We want this service to continue running until it is explicitly
	        // stopped, so return sticky.
	        return START_STICKY;
	    }
	    
	   /* @Override
	    public void onDestroy() {
	        // Cancel the persistent notification.
	    	try{
	    	socket.close();
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
	    	
	       
	    }*/

	    @Override
	    public IBinder onBind(Intent intent) {
	        return mMessenger.getBinder();
	    }

	    // This is the object that receives interactions from clients.  See
	    // RemoteService for a more complete example.
	    
	
	    class ServerConnectTask extends AsyncTask<String, Void, Boolean> {

	        protected Boolean doInBackground(String... urls) {
	        	try {
	        		socket = new Socket(ipAddress, ipPort);
	        		} catch (UnknownHostException e1) {
	        		// TODO Auto-generated catch block
	        		e1.printStackTrace();
	        		} catch (IOException e1) {
	        		// TODO Auto-generated catch block
	        		e1.printStackTrace();
	        		}
	        	
	               try {
	            	   out = new PrintWriter(new BufferedWriter(new 
	            	    OutputStreamWriter(socket
	            	   .getOutputStream())), true);
	            	   //out.println("180,180,0,-105");
	            	   
	            	   } catch (IOException e) {
	            	   // TODO Auto-generated catch block
	            	   e.printStackTrace();
	            	   }
	              if(socket.isConnected()){
	            	  return true;
	              }else{
	            	  return false;
	              }
	        }
	        @Override
	        protected void onPostExecute(Boolean result) {
	          if(result){
	        	  new BufferedReadTask().execute();
	        	  //do something here for text notification or managing socket
	          }
	        }
	    }
	    ///End of ASYNC TASK
	   class BufferedReadTask extends AsyncTask<String, Void, String> {
	    	public String line;
	    	public Message msg;
	        protected String doInBackground(String... urls) {
	        	try {
	        		
	        		if(socket.isConnected()){
	        		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        	    while ((line = in.readLine()) != null) {
	        	      System.out.println(line);
	        	      try{
			        		Bundle b = new Bundle();
			        		b.putString("fromServer", line);
			        		msg = Message.obtain(null, MSG_SET_STRING_VALUE);
			        		msg.setData(b);
			        		for(int i = mClients.size()-1; i>=0; i--){
			        		mClients.get(i).send(msg);
			        		}
			        	}catch(Exception e){
			        		e.printStackTrace();
			        	}
	        	    	}
	        		}
	        	   }catch (IOException e) {
	        	    e.printStackTrace();
	        	  } finally {
	        	    if (in != null) {
	        	      try {
	        	        in.close();
	        	      }catch (IOException e) {
	        	        e.printStackTrace();
	        	        }
	        	    }
	        	  }
	        		return line;	
	        }
	        @Override
	        protected void onPostExecute(String result) {
	        	System.out.print("made it");
	        	
	        	
	        }
	        
	       /* @Override
	        protected void onCancelled(){
	        	try{
	        		in.close();
	        		out.close();
	        	socket.close();
	        	}catch(IOException e){
	        		e.printStackTrace();
	        	}
	        }*/
	        
	    }
	   
	   class HTTPBufferedReadTask extends AsyncTask<String, Void, String> {
		   String returnstring;
		   Message msg;
		   List<Restaurant> restaurants = new ArrayList<Restaurant>();
	        protected String doInBackground(String... urls) {
	        	try{
	        		returnstring = downloadUrl(urls[0]);
	        	}catch(IOException e){
	        		e.printStackTrace();
	        	}
	        	
	        	if(returnstring == null){
	        		returnstring = "Nothing Received";
	        	}
	        	return returnstring;
	        	
	        }
	        @Override
	        protected void onPostExecute(String result) {
	        	System.out.println("made it");
	        	try{
	        			Bundle b = new Bundle();
	        			b.putString("fromServer", result);
	        			msg = Message.obtain(null, MSG_SET_STRING_VALUE);
	        			msg.setData(b);
	        			for(int i = mClients.size()-1; i>=0; i--){
	        				mClients.get(i).send(msg);
	        			}
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	
	        	
	        }
	    }
	   
	   class XMLParseTask extends AsyncTask<String, Void, String> {
		   String returnstring;
		   Message msg;
		   List<Restaurant> restaurants = new ArrayList<Restaurant>();
	        protected String doInBackground(String... urls) {
	        	try{
	        		
	        		returnstring = xmlParser(urls[0]);
	        	}catch(IOException e){
	        		e.printStackTrace();
	        	}
	        	
	        	if(returnstring == null){
	        		returnstring = "Nothing Received";
	        	}
	        	return returnstring;
	        	
	        }
	        @Override
	        protected void onPostExecute(String result) {
	        	System.out.print("made it");
	        	try{
	        			Bundle b = new Bundle();
	        			b.putString("fromServer", result);
	        			msg = Message.obtain(null, MSG_DECODE_RESTAURANT);
	        			msg.setData(b);
	        			for(int i = mClients.size()-1; i>=0; i--){
	        				mClients.get(i).send(msg);
	        			}
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	
	        	
	        }
	    }
	   
	   private String downloadUrl(String myurl) throws IOException {
		    InputStream is = null;
		    // Only display the first 500 characters of the retrieved
		    // web page content.
		    int len = 500;
		        
		    try {
		        URL url = new URL(myurl);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setReadTimeout(10000 /* milliseconds */);
		        conn.setConnectTimeout(15000 /* milliseconds */);
		        conn.setRequestMethod("GET");
		        conn.setDoInput(true);
		        // Starts the query
		        conn.connect();
		        int response = conn.getResponseCode();
		       // Log.d(DEBUG_TAG, "The response is: " + response);
		        is = conn.getInputStream();

		        // Convert the InputStream into a string
		        
		         String returnstring = convertStreamToString(is);
		        
		        return returnstring;
		        
		    // Makes sure that the InputStream is closed after the app is
		    // finished using it.
		    } finally {
		        if (is != null) {
		            is.close();
		        } 
		    }
		    
		}
	   
	   private String xmlParser(String myurl) throws IOException {
		    InputStream is = null;
		    String returnstring;
		    List<Restaurant> restaurants;
		    // Only display the first 500 characters of the retrieved
		    // web page content.
		    int len = 500;
		        
		    try {
		        URL url = new URL(myurl);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setReadTimeout(10000 /* milliseconds */);
		        conn.setConnectTimeout(15000 /* milliseconds */);
		        conn.setRequestMethod("GET");
		        conn.setDoInput(true);
		        // Starts the query
		        conn.connect();
		        int response = conn.getResponseCode();
		       // Log.d(DEBUG_TAG, "The response is: " + response);
		        is = conn.getInputStream();
		        
		        // Convert the InputStream into a string
		         SeatEasyXMLParser parser = new SeatEasyXMLParser();
		          restaurants = new ArrayList<Restaurant>();
		         
		         try{
		          restaurants = parser.parse(is);
		         }catch(XmlPullParserException e){
		        	 e.printStackTrace();
		         }
		         RestaurantDBManager manager = new RestaurantDBManager(this.getApplicationContext());
		         manager.open();
		         System.out.println("RESTAURANT_DB_REQUEST");
		         for(int i = restaurants.size()-1; i >=0; i--){
		        	 manager.addRestaurant(restaurants.get(i));
		         }
		         manager.close();
		         //returnstring = formRestaurantString(restaurants.get(0));
		         returnstring = "Added Restaurants";
		        
		        return returnstring;
		        
		    // Makes sure that the InputStream is closed after the app is
		    // finished using it.
		    } finally {
		        if (is != null) {
		            is.close();
		        } 
		    }
		    
		}
	   
	   public static String convertStreamToString(InputStream is) throws IOException {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();
		    String line = null;

		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }

		    is.close();

		    return sb.toString();
		}
	   
	  /* private String formRestaurantString(Restaurant rest){
		   return new StringBuilder("?"+RestaurantDB.COLUMN_NAME+"=").append(rest.getName())
				   .append("&"+RestaurantDB.COLUMN_LAT+"=").append(rest.getLat())
				   .append("&"+RestaurantDB.COLUMN_LON+"=").append(rest.getLon())
				   .append("&"+RestaurantDB.COLUMN_RATE+"=").append(rest.getRating())
				   .append("&"+RestaurantDB.COLUMN_PROMO+"=").append(rest.getPromo())
				   .append("&"+RestaurantDB.COLUMN_HASH+"=").append(rest.getHash()).toString();
	   }*/
}
