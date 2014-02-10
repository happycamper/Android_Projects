package com.socketserver.research;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class socketserver extends Activity {
	public TextView txt;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txt = (TextView) findViewById(R.id.hello);
        	
        
        try {
     
                Boolean end = false;
     
                ServerSocket ss = new ServerSocket(12345);
     
                while(!end){
     
                        //Server is waiting for client here, if needed
     
                        Socket s = ss.accept();
     
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
     
                        PrintWriter output = new PrintWriter(s.getOutputStream(),true); //Autoflush
     
                        String st = input.readLine();
                        txt.setText(st);
    
                        Log.d("Tcp Example", "From client: "+st);
    
                        output.println("Good bye and thanks for all the fish :)");
    
                        s.close();
    

    
                }
    
        ss.close();
    
               
    
               
    
        } catch (UnknownHostException e) {
    
                // TODO Auto-generated catch block
    
                e.printStackTrace();
    
        } catch (IOException e) {
    
                // TODO Auto-generated catch block
    
                e.printStackTrace();
    
        }

        	
        	
        	
        	
        	
        	
        
    }
}