package research.example.ipcameraview;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ipcameraview extends Activity {
	
	public CameraSource cs;
	public Canvas c;
	public Drawable d;
	public InputStream is;
	public Handler mHandler = new Handler();
	public Canvas canvas2;
	public boolean isready = true;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Panel(this));
       // cs = new SocketCamera("http://192.168.1.118/snapshot.cgi?user=admin&pwd=n0wa1n3v3r&resolution=32&rate=6", 80, 640, 480, true);
        
        /*
        Bitmap b = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        CameraSource cs = new SocketCamera("http://192.168.1.118/videostream.cgi?user=admin&pwd=n0wa1n3v3r&resolution=32&rate=6", 80, 640, 480, true); 
        //if (!cs.open()) { } 
        while(cs.open()) 
        { cs.capture(c); //capture the frame onto the canvas 
        	}
        
        cs.close();*/
        }
    
    private static final int INVALID_POINTER_ID = -1;
    Runnable drawnew;
    private Drawable mImage;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: {
            final float x = ev.getX();
            final float y = ev.getY();

            mLastTouchX = x;
            mLastTouchY = y;
            if(mLastTouchX > 0){
            	try{
                	URL url = new URL("http://192.168.1.118/decoder_control.cgi?command=6&user=admin&pwd=n0wa1n3v3r");
                	Object content = url.getContent();
                	mHandler.postDelayed(drawnew,1000);
        			 //d = Drawable.createFromStream(is, "src");
                	} catch (MalformedURLException e) {
            			e.printStackTrace();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            }
            mActivePointerId = ev.getPointerId(0);
            break;
        }

       /* case MotionEvent.ACTION_MOVE: {
            final int pointerIndex = ev.findPointerIndex(mActivePointerId);
            final float x = ev.getX(pointerIndex);
            final float y = ev.getY(pointerIndex);

            // Only move if the ScaleGestureDetector isn't processing a gesture.
            if (!mScaleDetector.isInProgress()) {
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                //invalidate();
            }

            mLastTouchX = x;
            mLastTouchY = y;

            break;
        }

        case MotionEvent.ACTION_UP: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_POINTER_UP: {
            final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            final int pointerId = ev.getPointerId(pointerIndex);
            if (pointerId == mActivePointerId) {
                // This was our active pointer going up. Choose a new
                // active pointer and adjust accordingly.
                final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                mLastTouchX = ev.getX(newPointerIndex);
                mLastTouchY = ev.getY(newPointerIndex);
                mActivePointerId = ev.getPointerId(newPointerIndex);
            }
            break;
        }*/
        }

        return true;
    }

    
    

    
    class Panel extends View {
        public Panel(Context context) {
            super(context);
        }
        
       
        @Override
        public void onDraw(Canvas canvas) {
        	if(isready){
        	try{
        	is = (InputStream) fetch("http://192.168.1.118/snapshot.cgi?user=admin&pwd=n0wa1n3v3r&resolution=16&rate=6");
			 //d = Drawable.createFromStream(is, "src");
        	} catch (MalformedURLException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            Bitmap _scratch = BitmapFactory.decodeStream(is);
        	/*while(true) 
            { cs.open();cs.capture(canvas); //capture the frame onto the canvas 
            	 if(!cs.open())	
            		 	break;
            	}
            
            cs.close();*/
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(_scratch, 0, 0, null);
            //canvas2 = canvas;
            mHandler.postDelayed(drawnew, 10);
        	}
        }
        
         Runnable drawnew= new Runnable(){
        	public void run(){
        	/*try{
            	is = (InputStream) fetch("http://192.168.1.118/snapshot.cgi?user=admin&pwd=n0wa1n3v3r&resolution=16&rate=6");
    			 //d = Drawable.createFromStream(is, "src");
            	} catch (MalformedURLException e) {
        			e.printStackTrace();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
                Bitmap _scratch = BitmapFactory.decodeStream(is);
            	/*while(true) 
                { cs.open();cs.capture(canvas); //capture the frame onto the canvas 
                	 if(!cs.open())	
                		 	break;
                	}
                
                cs.close();*/
                //canvas2.drawColor(Color.BLACK);
                //canvas2.drawBitmap(_scratch, 0, 0, null);
        		isready = true;
        		invalidate();
        	}
        	};
        
    }
    
    
    private Drawable ImageOperations(Context ctx, String url, String saveFilename) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}


    }
