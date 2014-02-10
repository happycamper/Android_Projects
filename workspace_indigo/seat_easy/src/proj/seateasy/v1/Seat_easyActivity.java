package proj.seateasy.v1;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;




public class Seat_easyActivity extends Activity {
	
	private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Context mContext;
    private Handler mHandler;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        
        fragmentManager = getFragmentManager();
        mHandler = new Handler();
       // fragmentTransaction = fragmentManager.beginTransaction();
        
        mContext = this.getApplicationContext();
        
        
        new TestNetwork().execute();

        
    }
    
    final Runnable newIntent = new Runnable(){
    	public void run(){
    		Intent login_intent = new Intent(mContext,Login_Screen.class);
            login_intent.putExtra("network", false);
            login_intent.putExtra("email_logon","false");
            login_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(login_intent);
            finish();
    	}
    };
    
    public class TestNetwork extends AsyncTask<String, Void, Boolean> {
    	private URL url;
        @Override
        protected Boolean doInBackground(String... args) {
            // updating UI from Background Thread
            try {  
                url = new URL(Communication_Handler.WEBSITE);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                //conn.connect();     
                return true;
            }
            catch (IOException e)
            {    
                e.printStackTrace();  
            }

            return false;   
        }
            @Override       
    protected void onPostExecute(Boolean result) {
        // dismiss the dialog after getting all products
            	
            	if(result.booleanValue()){
            	    //do something here eventually...
            		mHandler.postDelayed(newIntent, 3000);
            		
            	
            	}else{
            		NetworkWarning nw = new NetworkWarning(mContext,Seat_easyActivity.class);
                	nw.show(fragmentManager, Communication_Handler.NETWORK_WARNING);
            	}

    }
    }
   
}
