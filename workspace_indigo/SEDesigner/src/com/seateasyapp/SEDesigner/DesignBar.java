package com.seateasyapp.SEDesigner;


import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DesignBar extends Fragment {
	
	private View mRect,mCirc,mBooth,hWall,vWall,restaurantObj;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.design_bar,
		        container, false);
		
		mRect = (View) view.findViewById(R.id.designBarRect);
		mCirc = (View) view.findViewById(R.id.designBarCirc);
		mBooth = (View) view.findViewById(R.id.designBarBooth);
		hWall = (View) view.findViewById(R.id.designBarWallHorizontal);
		vWall = (View) view.findViewById(R.id.designBarWallVertical);
		restaurantObj = (View) view.findViewById(R.id.designBarObject);
		
		mRect.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent me) {
					// TODO Auto-generated method stub
					int action = me.getAction();
					
					switch(me.getAction()){
					case MotionEvent.ACTION_DOWN:
						ClipData data = ClipData.newPlainText("CREATErectangle", "hi");
					      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
					      v.startDrag(data, shadowBuilder, v, 0);
					      return true;
					}
					return false;
				}
				
			});
		
		hWall.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub
				int action = me.getAction();
				
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("CREATEhorizontal", "hi");
				      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				      v.startDrag(data, shadowBuilder, v, 0);
				      return true;
				}
				return false;
			}
			
		});
		
		vWall.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub
				int action = me.getAction();
				
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("CREATEvertical", "hi");
				      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				      v.startDrag(data, shadowBuilder, v, 0);
				      return true;
				}
				return false;
			}
			
		});
		
		restaurantObj.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub
				int action = me.getAction();
				
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("CREATEobject", "hi");
				      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				      v.startDrag(data, shadowBuilder, v, 0);
				      return true;
				}
				return false;
			}
			
		});
		
		mBooth.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub
				int action = me.getAction();
				
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("CREATEbooth", "hi");
				      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				      v.startDrag(data, shadowBuilder, v, 0);
				      return true;
				}
				return false;
			}
			
		});
		
		mCirc.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub
				int action = me.getAction();
				
				switch(me.getAction()){
				case MotionEvent.ACTION_DOWN:
					ClipData data = ClipData.newPlainText("CREATEcircle", "hi");
				      DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				      v.startDrag(data, shadowBuilder, v, 0);
				      return true;
				}
				return false;
			}
			
		});
		view.setX(5.0f);
		view.setY(5.0f);
			
		    return view;
	}

}
