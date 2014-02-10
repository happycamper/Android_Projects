package com.seateasyapp.SEDesigner;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TableRunTime extends Activity implements
		FloorsFragment.loadFloorXML, ReservationFragment.seatTableInterface {
	/** Called when the activity is first created. */

	private View myView;
	private RelativeLayout main;
	private Drawable icon;
	public static FragmentManager fm;
	public int TABLE_COUNT = 1;

	private Boolean EDITMODE = true;
	private Context mContext;
	private EditDialog dg;
	private ActionsDialog ag;
	private Handler refresher;
	private float dragNoise = 35.0f;
	private ArrayList<Table> activeTables;
	private ArrayList<Table> softActiveTables;
	private Boolean LONG_CLICK;
	private Boolean TWO_FINGER;
	private String intentFile;
	// public static EditBar editFrag;
	public static DesignBar designFrag;
	private RealTimeLayout rtLayout;
	private BlueprintHandler bpHandler;
	private FloorsFragment ff;
	private SeatedFragment sf;
	private ReservationFragment rf;
	private StatisticFragment statFrag;

	private Boolean showingStats,showingRes,showingFloors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * setContentView(R.layout.main);
		 * 
		 * 
		 * activeTables = new ArrayList<View>(); softActiveTables = new
		 * ArrayList<Table>(); mContext = this; refresher = new Handler();
		 * 
		 * 
		 * LONG_CLICK = false; TWO_FINGER = false;
		 * 
		 * main = (RelativeLayout) findViewById(R.id.mainPage);
		 */
		// editFrag = new EditBar();
		mContext = this;
		rtLayout = new RealTimeLayout(this);
		bpHandler = new BlueprintHandler(this);
		setContentView(rtLayout);

		 rf = new ReservationFragment();
		fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(rtLayout.getId(), rf);
		ft.commit();

		ff = new FloorsFragment();
		ft = fm.beginTransaction();
		ft.add(rtLayout.getId(), ff);
		ft.commit();

		sf = new SeatedFragment();
		ft = fm.beginTransaction();
		ft.add(rtLayout.getId(), sf);
		ft.commit();

		statFrag = new StatisticFragment();
		showingStats = false;
		showingRes = true;
		showingFloors = true;

		Intent i = getIntent();
		intentFile = i.getStringExtra("filename");
		System.out.println("GOT FILE" + intentFile);

		if (!intentFile.contains("CREATE_NEW_FILE")) {
			rtLayout.loadXMLFromFile(intentFile);
		}

		// View table = createTable();

		// main.addView(table);

		/*
		 * findViewById(R.id.blueprintCanvas).setOnDragListener( new
		 * OnDragListener() {
		 * 
		 * @Override public boolean onDrag(View v, DragEvent de) { // TODO
		 * Auto-generated method stub int action = de.getAction();
		 * 
		 * switch (de.getAction()) { case DragEvent.ACTION_DRAG_STARTED:
		 * System.out.println("DRAG ACTION STARTED"); return true; case
		 * DragEvent.ACTION_DRAG_LOCATION: System.out.println("DRAG LOCATION");
		 * return true; case DragEvent.ACTION_DROP:
		 * System.out.println("DRAG DROPPED"); View view = (View)
		 * de.getLocalState(); ClipData cd = de.getClipData(); if
		 * (cd.getDescription().getLabel().toString() .contains("CREATErect")) {
		 * View table = createTable(de.getX(), de.getY(), CREATE_RECTANGLE);
		 * main.invalidate(); main.addView(table);
		 * table.setVisibility(View.VISIBLE); } else if
		 * (cd.getDescription().getLabel() .toString().contains("CREATEcircle"))
		 * { View table = createTable(de.getX(), de.getY(), CREATE_CIRCLE);
		 * main.invalidate(); main.addView(table);
		 * table.setVisibility(View.VISIBLE); } else { ViewGroup owner =
		 * (ViewGroup) view.getParent(); owner.removeView(view);
		 * view.setX(de.getX() - (dragNoise * view.getWidth() / 100));
		 * view.setY(de.getY() - (dragNoise * view.getHeight() / 100));
		 * 
		 * owner.invalidate(); main.invalidate(); main.addView(view);
		 * view.setVisibility(View.VISIBLE); }
		 * 
		 * return true; } return true; }
		 * 
		 * });
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.run_time_menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RealTimeLayout.IN_RUNTIME = false;
		RealTimeLayout.activeTables.clear();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.viewStatistics:
			if (!showingStats) {
				showingStats = true;
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.add(rtLayout.getId(), statFrag);
				ft.commit();
				statFrag.updateText();
				
				ReservationFragment.adapter.notifyDataSetChanged();
			} else {
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(statFrag);
				ft.commit();
				showingStats = false;
				
				ReservationFragment.adapter.notifyDataSetChanged();
			}
			// SaveBlueprintDialog bp = new
			// SaveBlueprintDialog(mContext,BlueprintLayout.activeTables);
			// bp.show();
			return true;
			
		case R.id.viewReservations:
			if (!showingRes) {
				showingRes = true;
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.add(rtLayout.getId(), rf);
				ft.add(rtLayout.getId(), sf);
				ft.commit();
			} else {
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(rf);
				ft.remove(sf);
				ft.commit();
				showingRes = false;
			}
			// SaveBlueprintDialog bp = new
			// SaveBlueprintDialog(mContext,BlueprintLayout.activeTables);
			// bp.show();
			return true;
			
		case R.id.viewFloors:
			if (!showingFloors) {
				showingFloors = true;
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.add(rtLayout.getId(), ff);
				ft.commit();
			} else {
				FragmentTransaction ft = fm.beginTransaction();
				ft.remove(ff);
				ft.commit();
				showingFloors = false;
			}
			// SaveBlueprintDialog bp = new
			// SaveBlueprintDialog(mContext,BlueprintLayout.activeTables);
			// bp.show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemSelection(String xmlname) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Wanting to load " + xmlname, Toast.LENGTH_LONG)
				.show();
		// bpHandler.saveFile(BlueprintLayout.activeTables, intentFile);
		String oldFile = RealTimeLayout.ACTIVE_LAYOUT;
		if (!oldFile.contains(".xml")) {
			oldFile = RealTimeLayout.ACTIVE_LAYOUT + ".xml";
		}
		System.out.println("Old " + oldFile + " NEW" + xmlname);
		rtLayout.swapLayouts(oldFile, xmlname);

	}

	@Override
	public void onRequestToSeatTable(Reservation res, int tableNumber) {
		// TODO Auto-generated method stub

	}

}