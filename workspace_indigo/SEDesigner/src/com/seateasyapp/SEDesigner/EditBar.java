package com.seateasyapp.SEDesigner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class EditBar extends Fragment {

	private Button save, close;
	private EditText tableNum, seats,tableText;
	private SeekBar scaleX, scaleY, rotate;

	private int pBarMax = 300;
	private int ROT_MAX = 360;
	private int mProgressX = 0;
	private int mProgressY = 0;
	private int lastProgress = 0;
	private int lastProgressY = 0;
	private int lastProgressROT = 0;
	private int mProgressROT = 0;

	private Table editTable;
	private Context mContext;

	closeEditFragment mCallback;
	private float startScale = 1.0f;
	private EditBar thisbar;

	public void setTable(Table table) {
		editTable = table;
		thisbar = this;
	}

	public interface closeEditFragment {
		public void onCloseButtonClick(Boolean isClicked, EditBar bar);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();

		try {
			mCallback = (closeEditFragment) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}

	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view;
		System.out.println("TABLE TYPE" +editTable.getTableType());
		switch(editTable.getTableType()){
		case BlueprintLayout.CREATE_RECTANGLE:
			 view = loadTableLayout(inflater,container,savedInstanceState);
			return view;
			
		case BlueprintLayout.CREATE_CIRCLE:
			 view = loadTableLayout(inflater,container,savedInstanceState);
			return view;
			
		case BlueprintLayout.CREATE_BOOTH:
			 view = loadTableLayout(inflater,container,savedInstanceState);
			return view;
			
		case BlueprintLayout.WALL_HORIZONTAL:
			 view = loadObjectLayout(inflater,container,savedInstanceState);
			return view;
			
		case BlueprintLayout.WALL_VERTICAL:
			 view = loadObjectLayout(inflater,container,savedInstanceState);
			return view;
			
		case BlueprintLayout.RESTAURANT_OBJECT:
			 view = loadObjectLayout(inflater,container,savedInstanceState);
			return view;
		}
		
		return loadTableLayout(inflater,container,savedInstanceState);
	}
	
	private View loadTableLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.table_edit_fragment,
		        container, false);
		
		save = (Button) view.findViewById(R.id.editTableSave);
		close = (Button) view.findViewById(R.id.editTableCancel);
		tableNum = (EditText) view.findViewById(R.id.editTableNumber);
		seats = (EditText) view.findViewById(R.id.editTableSeats);
		
		
		
		scaleX = (SeekBar) view.findViewById(R.id.seekBar1);
		scaleY = (SeekBar) view.findViewById(R.id.seekBar2);
		rotate = (SeekBar) view.findViewById(R.id.seekBar3);
		
		scaleX.setMax(pBarMax);
		scaleY.setMax(pBarMax);
		rotate.setMax(ROT_MAX);
		
		scaleX.setProgress(editTable.getStretchX());
		scaleY.setProgress(editTable.getStretchY());
		rotate.setProgress((int)editTable.getRotation());
		
		if(editTable != null){
			tableNum.setText(String.valueOf(editTable.getTableNumber()));
			seats.setText(String.valueOf(editTable.getSeatsX()));
		}
		
		
		
		
		
		
		scaleX.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressX = progress - lastProgress;
				//startScale += (float)mProgressX/100.0f;
				editTable.stretchX((float)mProgressX);
				lastProgress = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		rotate.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressROT = progress - lastProgressROT;
				//startScale += (float)mProgressX/100.0f;
				editTable.rotate((float)mProgressROT);
				lastProgressROT = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		scaleY.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressY = progress - lastProgressY;
				//startScale += (float)mProgressX/100.0f;
				editTable.stretchY((float)mProgressY);
				lastProgressY = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!tableNum.getText().toString().isEmpty() & !seats.getText().toString().isEmpty()){
	    			editTable.setTableNumber(Integer.parseInt(tableNum.getText().toString()));
	    			editTable.setSeatsX(Integer.parseInt(seats.getText().toString()));	
	    			editTable.updateText();
	    		}else{
	    			Toast.makeText(mContext, "Please fill out the text fields", Toast.LENGTH_LONG).show();
	    		}
			}
			
		});
		
		close.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mCallback.onCloseButtonClick(true,thisbar);
			}
			
		});
		
		
		view.setX(800.0f);
		view.setY(20.0f);
		return view;
		
	}
	
	private View loadObjectLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.object_edit_fragment,
		        container, false);
		
		save = (Button) view.findViewById(R.id.editTableSave);
		close = (Button) view.findViewById(R.id.editTableCancel);
		tableText = (EditText) view.findViewById(R.id.editTableText);
		
		
		
		scaleX = (SeekBar) view.findViewById(R.id.seekBar1);
		scaleY = (SeekBar) view.findViewById(R.id.seekBar2);
		rotate = (SeekBar) view.findViewById(R.id.seekBar3);
		
		scaleX.setMax(pBarMax);
		scaleY.setMax(pBarMax);
		rotate.setMax(ROT_MAX);
		
		scaleX.setProgress(editTable.getStretchX());
		scaleY.setProgress(editTable.getStretchY());
		rotate.setProgress((int)editTable.getRotation());

		
		
		
		
		
		
		scaleX.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressX = progress - lastProgress;
				//startScale += (float)mProgressX/100.0f;
				editTable.stretchX((float)mProgressX);
				lastProgress = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		rotate.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressROT = progress - lastProgressROT;
				//startScale += (float)mProgressX/100.0f;
				editTable.rotate((float)mProgressROT);
				lastProgress = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		scaleY.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mProgressY = progress - lastProgressY;
				//startScale += (float)mProgressX/100.0f;
				editTable.stretchY((float)mProgressY);
				lastProgressY = progress;
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!tableText.getText().toString().isEmpty()){
	    			editTable.setTableText(tableText.getText().toString());
	    			editTable.updateText();
	    		}else{
	    			Toast.makeText(mContext, "Please fill out the text fields", Toast.LENGTH_LONG).show();
	    		}
			}
			
		});
		
		close.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mCallback.onCloseButtonClick(true,thisbar);
			}
			
		});
		
		
		view.setX(800.0f);
		view.setY(20.0f);
		return view;
		
	}
	

}
