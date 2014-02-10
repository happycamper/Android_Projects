package jensen.research.pharandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashpage extends Activity {
	
	private Handler mHandler;
	private Activity mActivity;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashpage);
        
        mHandler = new Handler();
        mActivity = this;
        
        mHandler.postDelayed(stopped,2000);
        
        
	}
	
	Runnable stopped = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent i = new Intent(mActivity,ChoicePage.class);
			startActivity(i);
		}
		
	};
	
	@Override
	public void onResume(){
		super.onResume();
		mHandler.postDelayed(stopped, 2000);
	}

}
