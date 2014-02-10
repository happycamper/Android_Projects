package com.example.gps_example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.view.View;

public class GPSclient {
	
	private Socket socket;
	private PrintWriter out;
		 
		    private static final int SERVERPORT = 6000;
		    private static final String SERVER_IP = "10.0.0.115";
		    private Context mContext;
		 
		    public GPSclient(Context context){
		    	this.mContext = context;
		        
		    }
		    
		    public void startClient(){
		    	new Thread(new ClientThread()).start();
		    }
		 
		    public void sendServer(String str){
		    	if(socket.isConnected()){
		    		out.println(str);
		    	}
		    }
		 
		    class ClientThread implements Runnable {
		 
		        @Override
		        public void run() {
		 
		            try {
		                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
		 
		                socket = new Socket(serverAddr, SERVERPORT);
		                System.out.println("Conneted Socket");
		                 out = new PrintWriter(new BufferedWriter(
			                    new OutputStreamWriter(socket.getOutputStream())),
			                    true);
		                 out.println("test");
		 
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
