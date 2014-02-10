package com.example.gps_example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class GPSserver {
	
	private Context mContext;
	private ServerSocket serverSocket;
    private Handler updateConversationHandler;
    Thread serverThread = null;
    public static final int SERVERPORT = 6000;
    private Handler mHandler;
	
	public GPSserver(Context context){
		this.mContext = context;
		
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
 
                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                            	System.out.println("Got something");
                                Log.d("ServerActivity", line);
                                System.out.println(line);
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
