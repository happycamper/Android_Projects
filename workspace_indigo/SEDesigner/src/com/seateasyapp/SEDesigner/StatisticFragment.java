package com.seateasyapp.SEDesigner;


import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticFragment extends Fragment {
	
	private ArrayList<String> FILES;
	private ReservationDBManager resman;
	private Context mContext;
	private TextView totalCount,twoAverageWait,fourAverageWait,fourPlusAverageWait;
	private Handler mHandler;


	
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
		
		
		mHandler = new Handler();
		
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.statistic_fragment,
		        container, false);
		totalCount = (TextView) view.findViewById(R.id.statisticTotalCountNumber);
		twoAverageWait = (TextView) view.findViewById(R.id.statisticTwoAverageWait);
		resman = new ReservationDBManager(mContext);
		//totalCount.setText(resman.getNumberOfSeated());
		//twoAverageWait.setText(resman.getAverageWaitTime());
		//mHandler.postDelayed(updateInfo,5000);
		resman.open();
		int countNum = resman.getNumberOfSeated();
		int waitTime = resman.getAverageWaitTime();
		totalCount.setText(String.valueOf(countNum));
		twoAverageWait.setText(String.valueOf(waitTime));
		resman.close();
		
		
			view.setX(500.0f);
			view.setY(550.0f);
			
		    return view;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		//mHandler.removeCallbacks(updateInfo);
	}
	
	public void updateText() {
			// TODO Auto-generated method stub
		
			
		}

	
	
}
