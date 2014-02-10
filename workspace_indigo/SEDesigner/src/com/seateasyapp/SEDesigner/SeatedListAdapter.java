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

public class SeatedListAdapter extends ArrayAdapter<Reservation> {
	
		private Context mContext;
		private BlueprintHandler bh;
		public static ArrayList<Reservation> reservationList;
		private int Counter;
		public String name;
		private Resources r;
		private Button clear;
		private ReservationDBManager db;

		
	public SeatedListAdapter(Context context, int textViewResourceId, ArrayList<Reservation> reservations) {
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
	    View rowView = inflater.inflate(R.layout.seated_list_adapter, parent, false);
	    TextView name = (TextView) rowView.findViewById(R.id.seatedReservationName);
	    TextView partyNumber = (TextView) rowView.findViewById(R.id.seatedPartyNumber);
	    TextView waitTime = (TextView) rowView.findViewById(R.id.seatedTime);
	    TextView tableNum = (TextView) rowView.findViewById(R.id.seatedTable);
	    clear = (Button) rowView.findViewById(R.id.completeReservation);
	    
	    
	    
	    
	    
	    if(reservationList.size() != 0){
	    Reservation temp = reservationList.get(position);
	    final Reservation copy = temp;
	    temp.seatedTimeText = waitTime;
	    
	    name.setText(temp.getFirstName()+" "+temp.getLastName());
	    partyNumber.setText("Party of"+ temp.getPartyNumber().toString());
	    
	    waitTime.append("\n");
	    waitTime.setTextSize(16.0f);
	    waitTime.append(String.valueOf(temp.getWaitTime()));
	    waitTime.setTextSize(10.0f);
	    waitTime.append("\n");
	    waitTime.append("minutes");
	    
	    final int copyPosition = position;
	    tableNum.setText("Table "+temp.getSeatedTableNumber());
	    
	    clear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.open();
				db.addCompletedReservation(copy);
				db.deleteSeatedReservation(copy.getReservationNumber());
				SeatedFragment.adapter.remove(copy);
				SeatedFragment.adapter.notifyDataSetChanged();
				RealTimeLayout.freeTable(Integer.valueOf(copy.getSeatedTableNumber()));
				db.close();
			}
	    	
	    });
	    }else{
	    	name.setText("No Current Seated Reservations");
	    	partyNumber.setText("");
	    	waitTime.setText("");
	    	tableNum.setText("");
	    	
	    }
	    
	    return rowView;
	    
	}


}
