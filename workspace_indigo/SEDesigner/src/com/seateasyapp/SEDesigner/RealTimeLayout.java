package com.seateasyapp.SEDesigner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RealTimeLayout extends RelativeLayout {

	private float dragNoise = 35.0f;
	public static int TABLE_COUNT; // Keeps track of the number of tables in the
									// view
	public static String ACTIVE_LAYOUT;

	// Create codes for different visual items
	public static final int CREATE_RECTANGLE = 1;
	public static final int CREATE_CIRCLE = 2;
	public static final int CREATE_BOOTH = 3;
	public static final int WALL_VERTICAL = 4;
	public static final int WALL_HORIZONTAL = 5;
	public static final int RESTAURANT_OBJECT = 6;
	
	public static Boolean IN_RUNTIME = false;

	private static final int INVALID_POINTER_ID = -1;

	public static final int ID = 1;

	private Drawable mImage;
	private float mPosX;
	private float mPosY;
	private float MX,MY,MEX,MEY,MDX,MDY;
	private float mLastTouchX;
	private float mLastTouchY;
	private int mActivePointerId = INVALID_POINTER_ID;

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	private float startScale = 5.0f;

	public static ArrayList<Table> activeTables,seatedTables;
	public static HashMap<String, ArrayList<Table>> allTables;
	private RelativeLayout thisLayout, blankCanvas;
	private BlueprintHandler bh;

	private Resources r;

	public RealTimeLayout(Context context) {
		super(context);
		allTables = new HashMap<String, ArrayList<Table>>();
		seatedTables = new ArrayList<Table>();

		TABLE_COUNT = 1;
		bh = new BlueprintHandler(getContext());

		mPosX = 0.0f;
		mPosY = 0.0f;
		IN_RUNTIME = true;

		r = context.getResources();

		init(context);
		loadAllXMLFiles();
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(lp);
		this.setId(ID);
		this.setBackgroundColor(Color.WHITE);

		blankCanvas = new RelativeLayout(context);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				10000, 10000);
		blankCanvas.setLayoutParams(lp2);
		blankCanvas.setId(2);
		blankCanvas.setBackgroundColor(Color.GRAY);

		this.addView(blankCanvas);

		activeTables = new ArrayList<Table>();
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		// this.setOnDragListener(this);
		thisLayout = this;

		blankCanvas.setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent de) {
				int action = de.getAction();

				switch (de.getAction()) {

				case DragEvent.ACTION_DROP:
					System.out.println("DRAG DROPPED");
					View view = (View) de.getLocalState();
					ClipData cd = de.getClipData();
					if (cd.getDescription().getLabel().toString()
							.contains("CREATErect")) {
						View table = createTable((de.getX() - mPosX),
								(de.getY() - mPosY), CREATE_RECTANGLE);
						table.invalidate();
						v.invalidate();

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else if (cd.getDescription().getLabel().toString()
							.contains("CREATEcircle")) {
						View table = createTable(de.getX() - mPosX, de.getY()
								- mPosY, CREATE_CIRCLE);

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else if (cd.getDescription().getLabel().toString()
							.contains("CREATEbooth")) {
						View table = createTable(de.getX() - mPosX, de.getY()
								- mPosY, CREATE_BOOTH);

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else if (cd.getDescription().getLabel().toString()
							.contains("CREATEhorizontal")) {
						View table = createTable(de.getX() - mPosX, de.getY()
								- mPosY, WALL_HORIZONTAL);

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else if (cd.getDescription().getLabel().toString()
							.contains("CREATEvertical")) {
						View table = createTable(de.getX() - mPosX, de.getY()
								- mPosY, WALL_VERTICAL);

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else if (cd.getDescription().getLabel().toString()
							.contains("CREATEobject")) {
						View table = createTable(de.getX() - mPosX, de.getY()
								- mPosY, RESTAURANT_OBJECT);

						blankCanvas.invalidate();
						table.invalidate();
						blankCanvas.addView(table);
						table.setVisibility(View.VISIBLE);
					} else {
						ViewGroup owner = (ViewGroup) view.getParent();
						owner.removeView(view);
						float mainX = de.getX() - mPosX - view.getWidth() / 2;
						float mainY = de.getY() - mPosY - view.getHeight() / 2;
						view.setX(mainX);
						view.setY(mainY);

						System.out.println("LAST: " + mPosX + "," + mPosY
								+ " SET" + de.getX() + "," + de.getY());

						// owner.invalidate();
						blankCanvas.invalidate();
						blankCanvas.addView(view);
						view.setVisibility(View.VISIBLE);
					}

					return true;
				}
				return true;
			}
		});
	}

	private void init(Context con) {
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main, this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		mScaleDetector.onTouchEvent(ev);

		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			System.out.println("Touch Down");
			final float x = ev.getX();
			final float y = ev.getY();

			mLastTouchX = x;
			mLastTouchY = y;
			MX = x;
			MY = y;
			mActivePointerId = ev.getPointerId(0);
			return true;
		}

		case MotionEvent.ACTION_MOVE: {
			System.out.println("Moving");
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);

			// Only move if the ScaleGestureDetector isn't processing a gesture.
			if (!mScaleDetector.isInProgress()) {
				final float dx = x - mLastTouchX;
				final float dy = y - mLastTouchY;

				blankCanvas.scrollBy((int) -dx, (int) -dy);

				mPosX += dx;
				mPosY += dy;

				invalidate();
			}

			mLastTouchX = x;
			mLastTouchY = y;

			return true;
		}

		case MotionEvent.ACTION_UP: {
			mActivePointerId = INVALID_POINTER_ID;
			MEX = ev.getX();
			MEY = ev.getY();
			MDX = MEX - MX;
			MDY = MEY - MY;
			return true;
		}

		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = INVALID_POINTER_ID;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);

			}
			break;
		}
		}

		return false;
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));
			blankCanvas.setScaleX(mScaleFactor);
			blankCanvas.setScaleY(mScaleFactor);

			invalidate();
			return true;
		}
	}

	private View createTable(float x, float y, int tableCode) {
		TextView text, text2;

		// View table = new View(this);
		Table table = new Table(getContext(), tableCode);
		final Table softcopy = table;

		// ////////////ESTABLISH THE TABLE ID////////////////////
		// Table ID is not the table number, as the Table number can be changed

		if (tableCode == RESTAURANT_OBJECT || tableCode == WALL_VERTICAL
				|| tableCode == WALL_HORIZONTAL) {
			table.setTableText("");
		} else {
			table.setId(TABLE_COUNT);
			table.setTableNumber(table.getId());
			TABLE_COUNT += 1;
			table.updateText();
		}
		table.setTableId(table.getId());
		// ///////////////////////////////////////////////////////////////
		// updates the textViews

		table.setX(x);
		table.setY(y);
		table.setAnchorX(x);
		table.setAnchorY(y);

		activeTables.add(table);

		table.invalidate();

		return table;

	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(1.0f, 1.0f);
		canvas.restore();
	}

	public static void deleteTable(Table table) {
		for (int i = 0; i < activeTables.size(); ++i) {
			if (activeTables.get(i).getTableId() == table.getTableId()) {
				View view = activeTables.get(i);
				activeTables.remove(i);
				// removeView(view);
				// invalidate();

			}
		}
	}

	public void loadXMLFromFile(String name) {
		ACTIVE_LAYOUT = name; // recognize which xml file was loaded
		//TABLE_COUNT = 0; // clear the table count to load new tables into view

		ArrayList<Table> tables = bh.getFile(name.toString());
		populateTables(tables);
	}

	public void populateTables(ArrayList<Table> tables) {
		TABLE_COUNT = 1;
		float[] scaled = new float[2];
		float[] anchors = new float[2];

		System.out.println("SIZE: " + tables.size());
		for (int i = 0; i < tables.size(); ++i) {
			// View table =
			// createTable(tables.get(i).getAnchorX(),tables.get(i).getAnchorY(),tables.get(i).getTableType());
			Table temp = tables.get(i);
			
			temp.updateText();
			temp.setX(tables.get(i).getAnchorX());
			temp.setY(tables.get(i).getAnchorY());

			blankCanvas.invalidate();
			blankCanvas.addView(temp);
			++TABLE_COUNT;
			activeTables.add(temp);
		}

	}

	public void swapLayouts(String oldXML,String newXML) {
		ArrayList<Table> storeArray = new ArrayList<Table>();
		for(int i=0; i<activeTables.size();++i){
			storeArray.add(activeTables.get(i));
		}
		ACTIVE_LAYOUT = newXML;
		allTables.remove(oldXML);
		System.out.println("Putting "+oldXML+ "tables "+storeArray.size());
		allTables.put(oldXML,storeArray);
		activeTables.clear();
		ArrayList<Table> tables = allTables.get(newXML);
		blankCanvas.removeAllViewsInLayout();
		populateTables(tables);
		blankCanvas.invalidate();
	}

	public void loadAllXMLFiles(){
			ArrayList<String> FILES = new ArrayList<String>();
			FILES.clear();
			File[] files = bh.getAllBlueprintNames();
			if (files.length != 0) {
				for (int i = 0; i < files.length; ++i) {
					if (files[i].getName().toString().contains(".xml")) {
						String tempName = files[i].getName();
						FILES.add(tempName);
						String[] splitemp = tempName.split("[.]");
						ArrayList<Table> tables = bh.getFile(splitemp[0]);
						allTables.put(tempName, tables);

					}
				}
				System.out.println("alltables size " + allTables.size());

			}
	}
	
	public static void freeTable(int tableNumber){
		for(int i = 0; i<seatedTables.size();++i){
			Table temp = seatedTables.get(i);
			if(tableNumber == temp.getTableNumber()){
				temp.setAsUnseated();
			}
		}
	}
	

}
