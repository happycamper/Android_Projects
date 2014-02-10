package com.seateasyapp.designer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.MotionEvent;
import android.view.View;

public class Table extends View {
	
	public RoundRectShape mRect;
	public RectF rect;
	private float mLeft,mTop,mRight,mBottom,initialX,initialY,offsetX,offsetY;
	public Paint p;
	

	public Table(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mLeft = mTop = 20.0f;
		mRight = mLeft + 50.0f;
		mBottom = mTop + 50.0f;
		 rect = new RectF(mLeft,mTop,mRight,mBottom);
		//mRect = new RoundRectShape(null,rect,null);
		p = new Paint();
		p.setARGB(255, 0, 0, 0);
		this.setMinimumHeight(getMinHeight());
		this.setMinimumWidth(getMinWidth());
		this.setX(mLeft);
		this.setY(mTop);

	}
	
	public RectF getRect(){
		return new RectF(mLeft,mTop,mRight,mBottom);
	}
	
	public Paint getPaint(){
		return p;
	}
	
	public int getMinHeight(){
		int n = (int)((int) mBottom - mTop);
		return n;
	}
	
	public int getMinWidth(){
		int n = (int)((int) mRight - mLeft);
		return n;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    int action = event.getAction();
	    switch (action) {
	    case MotionEvent.ACTION_DOWN:
	      initialX = mLeft;
	      initialY = mTop;
	      offsetX = event.getX();
	      offsetY = event.getY();
	      break;
	    case MotionEvent.ACTION_MOVE:
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_CANCEL:
	      mLeft = initialX + event.getX() - offsetX;
	      mTop = initialY + event.getY() - offsetY;
	      setX(mLeft);
	      setY(mTop);
	      break;
	    }
	    return (true);
	  }
	 
	
	

}
