package com.example.servercommunicate;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;




public class servercommunicate extends Activity {
		    private EditText serverIp;
		    public TextView text1;
		    private TextView label;
		    private Button connectPhones;
		 
		    private String serverIpAddress = "";
		    private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
		    private boolean connected = false;
		 
		    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
       // registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
        serverIp = (EditText) findViewById(R.id.server_ip);
        text1 = (TextView) findViewById(R.id.text1);
        label = (TextView) findViewById(R.id.label);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        Button cancelbutton =  (Button) findViewById(R.id.cancel);
        
        
        cancelbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Amarino.sendDataToArduino(servercommunicate.this,"00:07:80:99:56:34", 'B',1);
		//		unregisterReceiver(arduinoReceiver);
			}
		});
        connectPhones.setOnClickListener(new View.OnClickListener() {
    		        
    		        public void onClick(View v) {
    		            if (!connected) {
    		            	Amarino.sendDataToArduino(servercommunicate.this, "00:07:80:99:56:34", 'A',3);
    		                serverIpAddress = serverIp.getText().toString();
    		                text1.setText("Connecting To " + serverIpAddress);
    		                if (!serverIpAddress.equals("")) {
    		                	//Thread cThread = new Thread(new ClientThread());
    		       	              //      cThread.start();
    		                	connectSocket();
    		             }
    		                
    		            }
    		        }
        });

					
    		    };
    		   
    		    	
    		   private void connectSocket(){
    			   String fromserver;
    			   	try{
    			   		InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
		                Log.d("ClientActivity", "C: Connecting...");
   		                
   		                Socket socket =  new Socket(serverAddr, 4444);
		                connected = true;
		                while (connected) {
		                    try {
		                        Log.d("ClientActivity", "C: Sending command.");
		                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
		                                    .getOutputStream())), true);
		                            out.println("Are you there?");
		                            connected = false;
		                            Log.d("ClientActivity", "C: Sent.");
		                    } catch (Exception e) {
		                        Log.e("ClientActivity", "S: Error", e);
		                    }
		                }
		                socket.close();
		                Log.d("ClientActivity", "C: Closed.");
		            } catch (Exception e) {
		                Log.e("ClientActivity", "C: Error", e);
		                connected = false;
		            }
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
    						
    						if (data != null){
    							text1.setText(data);
    						}
    					}
    				}
    			}   	
    		   }


    		    /*public class ClientThread implements Runnable {
    		    		 Socket socket;

    		    		        public void run() {
    		    		            try {
    		    		            	text1.setText("I made it here!");
    		    		                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
    		    		                Log.d("ClientActivity", "C: Connecting...");
       		    		                
       		    		                this.socket =  new Socket(serverAddr, 4444);
    		    		                connected = true;
    		    		                while (connected) {
    		    		                    try {
    		    		                        Log.d("ClientActivity", "C: Sending command.");
    		    		                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
    		    		                                    .getOutputStream())), true);
    		    		                            // where you issue the commands
    		    		                            out.println("Are you there?");
    		    		                            Log.d("ClientActivity", "C: Sent.");
    		    		                    } catch (Exception e) {
    		    		                        Log.e("ClientActivity", "S: Error", e);
    		    		                    }
    		    		                }
    		    		                socket.close();
    		    		                Log.d("ClientActivity", "C: Closed.");
    		    		            } catch (Exception e) {
    		    		                Log.e("ClientActivity", "C: Error", e);
    		    		                connected = false;
    		    		            }
    		    		        }
    		    		    }*/
//}