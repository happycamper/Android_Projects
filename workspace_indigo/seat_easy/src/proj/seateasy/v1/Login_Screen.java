package proj.seateasy.v1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import proj.seateasy.v1.CallAheadForm.IncomingHandler;

import android.accounts.Account;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;





public class Login_Screen extends Activity {
	public final String ipAddress = "192.168.1.129";
	public final int ipPort = 4444;
	public TextView tester;
	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;
	private Boolean networkOK;
	private FragmentManager manager;
	public Class<?> mClass;
	public Context mContext;
	public ImageButton email_logon,facebook_logon;
	public Intent newIntent;
	public LocationServices gps;
	private Messenger mMessenger;
	private EasyCommunicator easy;
	private FragmentManager fragManager;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = getIntent();
        tester = (TextView) findViewById(R.id.logintest);
        facebook_logon = (ImageButton) findViewById(R.id.ImageButton01);
	    email_logon = (ImageButton) findViewById(R.id.ImageButton02);
        manager = getFragmentManager();
        mClass = getClass();
        mContext = this.getBaseContext();
        networkOK = true;
        User_Authenticator au = new User_Authenticator(mContext);
	    Account[] accounts = au.getAccounts();
        String totest = "";
        
        fragManager = getFragmentManager();
        mMessenger = new Messenger(new IncomingHandler());
        easy = new EasyCommunicator(mContext,mMessenger);
        easy.doBindService();
        
        
        
        
        tester.setText(totest);
        
        gps = new LocationServices(mContext);
        gps.startServices();
        List<String> providers = gps.getProviderList();

        //We will want to check the network settings first thing
       // new ServerConnectTask().execute();
        
        facebook_logon.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			
    		}
    	});
    
    	email_logon.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    				UserLoginForm2 userForm = new UserLoginForm2(Login_Screen.this);
    				userForm.show();
    		}
    	});
        
    	
    
	}
	
	class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Communication_Handler.MSG_SET_INT_VALUE:
            	//we may not want to connect back to this
                break;
            case Communication_Handler.MSG_SET_STRING_VALUE:
            	String data = msg.getData().getString("fromServer");
            	if(data.contentEquals("good")){
            		networkOK = true;
            		tester.setText(data);
            	}else{
            		networkOK = false;
            	}
            default:
                super.handleMessage(msg);
            }
        }
    }
	
	@Override
	public void onStart(){
		super.onStart();
		easy.doBindService();
	}
	@Override
	public void onStop(){
		super.onStop();
		easy.doUnbindService();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		easy.easyStopService();
	}

}
