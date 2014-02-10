package com.seateasyapp.SEDesigner;


import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SeatedFragment extends ListFragment {
	
	private static ArrayList<Reservation> seated;
	private static ReservationDBManager resman;
	private Context mContext;
	public static SeatedListAdapter adapter;
	private Handler mHandler;
	private int REFRESH_RATE = 6000;

	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mContext = activity;
		mHandler = new Handler();

		
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.seatedlist,
		        container, false);
		
		seated = new ArrayList<Reservation>();
		resman = new ReservationDBManager(mContext);
		resman.open();
		resman.clearSeatedReservationTable();
		
		seated.clear();
		
		
		seated = resman.getAllReservations(ReservationDBManager.GET_SEATED_RESERVATIONS);
		
		 adapter = new SeatedListAdapter(mContext,0,seated);
		setListAdapter(adapter);
		
			view.setX(0.0f);
			view.setY(250.0f);
			
			startCountingUp();
			
		    return view;
	}
	
	public static void updateReservationView(){
		seated = resman.getAllReservations(ReservationDBManager.GET_SEATED_RESERVATIONS);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		resman.close();
	}
	
	public void startCountingUp() {
		mHandler.postDelayed(countUp, REFRESH_RATE);
	}
	
	Runnable countUp = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!adapter.isEmpty()){
				for (int i = 0; i < adapter.getCount(); ++i) {
					Reservation temp = adapter.getItem(i);
					if (!temp.getIsCompleted()) {
						temp.addTimeSeated();
						temp.setSeatedTimeText(temp.getTimeSeated());
					}
				}
				mHandler.postDelayed(countUp, REFRESH_RATE);
			}else{
				mHandler.postDelayed(countUp, REFRESH_RATE);
			}

		}

	};
	
	/*private class UpdateWaitTimeTexts extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
        	
              return true;
        }      

        @Override
        protected void onPostExecute(Boolean result) {
        	mHandler.postDelayed(countUp, REFRESH_RATE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }*/

}
