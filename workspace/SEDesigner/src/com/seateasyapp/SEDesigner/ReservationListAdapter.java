package com.seateasyapp.SEDesigner;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationListAdapter extends ArrayAdapter<Reservation> {
	
		private Context mContext;
		private BlueprintHandler bh;
		public static ArrayList<Reservation> reservationList;
		private int Counter;
		public String name;
		private Resources r;
		private ReservationDBManager db;

		
	public ReservationListAdapter(Context context, int textViewResourceId, ArrayList<Reservation> reservations) {
		super(context, R.layout.reservation_list_adapter, reservations);
		// TODO Auto-generated constructor stub
		mContext = context;
		bh = new BlueprintHandler(mContext);
		reservationList = reservations;
		r = mContext.getResources();
		db = new ReservationDBManager(mContext);
		
	}
	

	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.reservation_list_adapter, parent, false);
	    TextView name = (TextView) rowView.findViewById(R.id.reservationName);
	    TextView partyNumber = (TextView) rowView.findViewById(R.id.partyNumber);
	    Button waitTime = (Button) rowView.findViewById(R.id.waitTime);
	    Button tableNum = (Button) rowView.findViewById(R.id.seatingTable);
	    Button SMS = (Button) rowView.findViewById(R.id.sendText);
	    Button clearRes = (Button) rowView.findViewById(R.id.clearReservation);
	    
	    
	    
	  
	    
	    Reservation temp = reservationList.get(position);
	    temp.waitTimeText = waitTime;
	    
	    name.setText(temp.getFirstName()+" "+temp.getLastName());
	    partyNumber.setText("Party of"+ temp.getPartyNumber().toString());
	    
	    waitTime.append("\n");
	    waitTime.setTextSize(16.0f);
	    waitTime.append(String.valueOf(temp.getWaitTime()));
	    waitTime.setTextSize(10.0f);
	    waitTime.append("\n");
	    waitTime.append("minutes");
	    
	    final Reservation copy = temp;
	    final int copyPosition = position;
	    //need a final to go in the button listeners
	    waitTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WaitTimeDialog diag = new WaitTimeDialog(mContext,copy);
				diag.show();
			}
	    	
	    });
	    
	    tableNum.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SeatTableDialog diag = new SeatTableDialog(mContext,copy,copyPosition);
				diag.show();
			}
	    	
	    });
	    
	    SMS.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "Sending text to "+copy.getTelephone(), Toast.LENGTH_LONG).show();
			}
	    	
	    });
	    
	    clearRes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.open();
				db.deleteReservation(copy.getReservationNumber());
				ReservationFragment.adapter.remove(copy);
				ReservationFragment.adapter.notifyDataSetChanged();
				db.close();
			}
	    	
	    });
	    

	    return rowView;
	    
	}


}
