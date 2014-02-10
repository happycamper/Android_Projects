package com.seateasyapp.SEDesigner;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ReservationFragment extends ListFragment {

	private static ArrayList<Reservation> reservations;
	private static ReservationDBManager resman;
	private Context mContext;
	seatTableInterface mCallback;
	public static ReservationListAdapter adapter;
	private Handler mHandler;
	private int REFRESH_RATE = 6000;
	private Button addRes;

	public interface seatTableInterface {
		public void onRequestToSeatTable(Reservation res, int tableNumber);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		mHandler = new Handler();

		try {
			mCallback = (seatTableInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.reservationlist, container, false);
		
		addRes = (Button) view.findViewById(R.id.addReservation);
		
		addRes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddReservationDialog newRes = new AddReservationDialog(mContext);
				newRes.show();
			}
			
		});
		
		
		reservations = new ArrayList<Reservation>();
		resman = new ReservationDBManager(mContext);
		resman.open();

		reservations.clear();
		resman.clearReservationTable();
		for (int i = 0; i < 10; ++i) {
			Reservation temp = new Reservation();
			temp.setFirstName("Jeff");
			temp.setLastName("Jensen");
			temp.setPartyNumber("4");
			temp.setTelephone("9702070797");
			temp.setTimeIn(resman.getTime());
			temp.setReservationNumber(i);

			resman.addReservation(temp);
			// reservations.add(temp);
		}

		reservations = resman
				.getAllReservations(ReservationDBManager.GET_RESERVATIONS);

		adapter = new ReservationListAdapter(mContext, 0, reservations);
		setListAdapter(adapter);

		view.setX(0.0f);
		view.setY(0.0f);
		
		startCountingDown();

		return view;
	}

	public static void updateReservationView() {
		reservations = resman
				.getAllReservations(ReservationDBManager.GET_RESERVATIONS);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		resman.close();
	}

	Runnable countDown = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!adapter.isEmpty()) {
				for (int i = 0; i < adapter.getCount(); ++i) {
					Reservation temp = adapter.getItem(i);
					if (!temp.getIsSeated() & temp.getIsWaitTimeSet()) {
						temp.subtractWaitTime();
						temp.setWaitTimeText(temp.getWaitTime());
					}
				}
				mHandler.postDelayed(countDown, REFRESH_RATE);
			}else{
				mHandler.postDelayed(this, REFRESH_RATE);
			}
		}

	};

	public void startCountingDown() {
		mHandler.postDelayed(countDown, REFRESH_RATE);
	}
	
	public static void addReservation(Reservation res){
		reservations.add(res);
	}
	
	/*private class UpdateWaitTimeTexts extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
        	
              return true;
        }      

        @Override
        protected void onPostExecute(Boolean result) {
        	mHandler.postDelayed(countDown, REFRESH_RATE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }*/

	

}
