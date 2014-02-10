package heffay.research.lightdimmer;



import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import android.widget.EditText;
import android.R;


public class LightDimmer extends Activity {
	/** Called when the activity is first created. */
    private static final String BRIGHTNESS_PREFERENCE_KEY = "brightness";
    private View brightnessPanel;
    private SeekBar brightnessControl;
    private TextView displaytext;
    private Boolean connected = false;
    private int brightness = 50;
    private String serveripaddress;
    private EditText serverip;
    private Socket socket;
    private PrintWriter out;
    private SensorManager myManager;
    private List<Sensor> sensors;
    private Sensor accSensor;
    private float oldX,oldY,oldZ;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        brightnessPanel = findViewById(R.id.panel);
        brightnessControl = (SeekBar) findViewById(R.id.seek);
        displaytext = (TextView) findViewById(R.id.display);

        Button btn = (Button) findViewById(R.id.Button01);
        Button btn2 = (Button) findViewById(R.id.Button02);
        Button connectb = (Button) findViewById(R.id.connect);
        Button disconnectb = (Button) findViewById(R.id.disconnect);
        serverip = (EditText) findViewById(R.id.ipaddress);
        
        
        myManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
	 	//in newer versions of android TYPE_ORIENTATION deprecated
        sensors = myManager.getSensorList(Sensor.TYPE_ORIENTATION);

        if(sensors.size() > 0)
        {
          accSensor = sensors.get(0);
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
		mHandler.postDelayed(mUpdateTimeTask,100);
        
        		//displaytext.setText(oldX+","+oldY+","+oldZ);
        
        btn2.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		hideBrightnessPanel();
        	}
        });
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBrightnessPanel();

            }
        });
        
        connectb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				connecttoserver();
				
			}
		});
        
        disconnectb.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View V) {
        		disconnectserver();
        	}
        });

        

        brightnessControl
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                            int progress, boolean fromUser) {
                        setBrightness(progress);
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                       // hideBrightnessPanel();
                    }
                });
    }

    private void showBrightnessPanel() {
        Animation animation = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        brightnessControl.setProgress(this.brightness);
        brightnessPanel.setVisibility(View.VISIBLE);
        brightnessPanel.startAnimation(animation);
    }

    private void setBrightness(int value) {
       /* if (value < 10) {
            value = 10;
        } else if (value > 100) {
            value = 100;
        }*/
    	//int arduino = value*2;
    	//Amarino.sendDataToArduino(LightDimmer.this,"00:07:80:99:56:34", 'B',arduino);
    	displaytext.setText(oldX+","+oldY+","+oldZ);
        brightness = value;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = (float) value / 100;       
        lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        getWindow().setAttributes(lp);
    }

    private void hideBrightnessPanel() {
        Animation animation = AnimationUtils.loadAnimation(LightDimmer.this,
                android.R.anim.slide_out_right);
        brightnessPanel.startAnimation(animation);
        brightnessPanel.setVisibility(View.GONE);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
                BRIGHTNESS_PREFERENCE_KEY, brightnessControl.getProgress())
                .commit();
    }
    
    private void connecttoserver(){
    		if(!connected){
    			serveripaddress = serverip.getText().toString();
    			if (!serveripaddress.equals("")) {
                	//Thread cThread = new Thread(new ClientThread());
       	              //      cThread.start();
                	try{
    			   		InetAddress serverAddr = InetAddress.getByName(serveripaddress);
		                Log.d("ClientActivity", "C: Connecting...");
   		                //standard socket connection, random port 4444
		                
   		                socket =  new Socket(serverAddr, 4444);
   		                //set global var connected to true
		                connected = true;
		                
		                //confirm to the user that we have connected to serverIpAddress
		                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
		                //assuming user running processing "BEAM", this starts the beam appropriately
                        out.println("180,180,0,100");
                	}catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
    			}
    		}
    }
    
    private void disconnectserver(){
    		if(connected){
    			try{
    				socket.close();
    				connected = false;
    			}catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
    		}
    }
    
    public void updateTV(float x, float y, float z)
    {
     //thisX = x - oldX * 10;
     //thisY = y - oldY * 10;
     //thisZ = z - oldZ * 10;

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
    
    private Runnable mUpdateTimeTask = new Runnable() {
  	   public void run() {
  		   displaytext.setText(oldX+","+oldY+","+oldZ);
  		   if(connected){
  			   	out.println(oldX+","+oldY+","+oldZ+","+brightness);
  		   }
  		 mHandler.postDelayed(this,100);
  	   }
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