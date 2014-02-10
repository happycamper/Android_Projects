package array.research.accel;


import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.os.Handler;
import android.os.SystemClock;



public class accel extends Activity {
	     private TextView accText;
	     private SensorManager myManager;
	     private List<Sensor> sensors;
	     private Sensor accSensor;
	     private float oldX, oldY, oldZ = 0f;
	     private Handler mHandler = new Handler();
	     public float thisX,thisY,thisZ;
	     public long mStartTime = 0L;
	     

	
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);        
	        setContentView(R.layout.main);
	        
	        Button startbutton = (Button) findViewById(R.id.start);
		     Button stopbutton = (Button) findViewById(R.id.stop);
		     
	        accText = (TextView)findViewById(R.id.output);  
	
	        // Set Sensor + Manager
	        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	
	        sensors = myManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
	
	        if(sensors.size() > 0)
	        {
	          accSensor = sensors.get(0);
	        }
	        startbutton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mStartTime == 0L) {
			            mStartTime = System.currentTimeMillis();
			            mHandler.removeCallbacks(mUpdateTimeTask);
			            mHandler.postDelayed(mUpdateTimeTask, 100);
			       }

					
				}
			});
	        
	        stopbutton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mHandler.removeCallbacks(mUpdateTimeTask);
			       }
			});
	        
	    }
	    
	    private Runnable mUpdateTimeTask = new Runnable() {
	    	   public void run() {
	    	       final long start = mStartTime;
	    	       long millis = SystemClock.uptimeMillis() - start;
	    	       int seconds = (int) (millis / 1000);
	    	       int minutes = seconds / 60;
	    	       seconds     = seconds % 60;
	    	       accText.setText("x: " + Math.round(thisX) + ";\n y:" + Math.round(thisY) + ";\n z: " + Math.round(thisZ));
	    	       mHandler.postDelayed(this,3000);
	    	   }
	    	};
	
	    public void updateTV(float x, float y, float z)
	    {
	     thisX = x - oldX * 10;
	     thisY = y - oldY * 10;
	     thisZ = z - oldZ * 10;

	    // accText.setText("x: " + Math.round(thisX) + ";\n y:" + Math.round(thisY) + ";\n z: " + Math.round(thisZ));	
	     oldX = x;
	     oldY = y;
	     oldZ = z;
	    }
	
	    private final SensorEventListener mySensorListener = new SensorEventListener()
	    {
	     public void onSensorChanged(SensorEvent event)
	     {
	          updateTV(event.values[0],
	                    event.values[1],
	                    event.values[2]);
	     }
	
	     public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	    };
	
	    @Override
	    protected void onResume()
	    {
	     super.onResume();
	     myManager.registerListener(mySensorListener, accSensor, SensorManager.SENSOR_DELAY_GAME);      	
	    }
	
	    @Override
	    protected void onStop()
	    {    
	     myManager.unregisterListener(mySensorListener);
	     super.onStop();
	    }
	
	 
	
	} 
