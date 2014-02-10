package com.seateasyapp.designer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class BlueprintSurfaceView extends SurfaceView implements Runnable {
	
		private Thread t;
		private Context mContext;
		private SurfaceHolder sh;
		private Boolean isRunning;
		public Table table;
		
	public BlueprintSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		isRunning = true;
		sh = getHolder();
		t = null;
		table = new Table(mContext);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainView);
		layout.addView(table);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			while(isRunning){
				if(!sh.getSurface().isValid()){
					continue;
				}
				
				Canvas c = sh.lockCanvas();
				
				c.drawARGB(255,255,255,255);
				Paint p = new Paint();
				p.setARGB(255, 0, 0, 0);
				c.drawRect(table.getRect(),table.getPaint());
				sh.unlockCanvasAndPost(c);
			}
		
	}
	
	
	public void onStop(){
		try{
		t.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		isRunning = false;
		t = null;
	}
	
	public void onResume(){
		isRunning = true;
		t = new Thread(this);
		t.start();
		
	}
	
	public void onPause(){
	while(true){	
		try{
			t.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}finally{
				break;
			}
	}
		isRunning = false;
		t = null;
	}

}
