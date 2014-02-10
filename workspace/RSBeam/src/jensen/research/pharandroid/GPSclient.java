package jensen.research.pharandroid;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class GPSclient {
	
	public static Socket socket;
	private PrintWriter out;
		 
		    private static final int SERVERPORT = 6000;
		    private static  String SERVER_IP = "10.0.0.115";
		    private Context mContext;
		    public boolean socketConnected;
		   // private TextView statusText;
		 
		    public GPSclient(Context context){
		    	this.mContext = context;
		    	socketConnected = false;
		    	//statusText = status;
		        
		    }
		    
		    public void startClient(String ip){
		    	SERVER_IP = ip;
		    	System.out.println("Connectin to "+SERVER_IP);
		    	new Thread(new ClientThread()).start();
		    }
		 
		    public void sendServer(String str){
		    	if(socket.isConnected()){
		    		System.out.println("sending "+str);
		    		out.println(str);
		    	}
		    }
		 
		    class ClientThread implements Runnable {
		 
		        @Override
		        public void run() {
		 
		            try {
		                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
		 
		                socket = new Socket(serverAddr, SERVERPORT);
		                //statusText.setText("Server Status: Connected to "+ SERVER_IP);
		                System.out.println("Conneted Socket");
		               // Toast.makeText(mContext, "Connected to Server", Toast.LENGTH_LONG);
		                 out = new PrintWriter(new BufferedWriter(
			                    new OutputStreamWriter(socket.getOutputStream())),
			                    true);
		                // out.println("test");
		 
		            } catch (UnknownHostException e1) {
		                e1.printStackTrace();
		            } catch (IOException e1) {
		                e1.printStackTrace();
		            }
		 
		        }
		 
		    }
		    
		    public void stopClient() throws IOException{
		    	if(socket != null){
		    	socket.close();
		    	}
		    }

}
