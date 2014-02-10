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

public class SEDesignerActivity extends Activity implements EditBar.closeEditFragment {
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
	//public static EditBar editFrag;
	public static DesignBar designFrag;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.main);
		
		
		activeTables = new ArrayList<View>();
		softActiveTables = new ArrayList<Table>();
		mContext = this;
		refresher = new Handler();
		

		LONG_CLICK = false;
		TWO_FINGER = false;

		main = (RelativeLayout) findViewById(R.id.mainPage);*/
		//editFrag = new EditBar();
		mContext = this;
		BlueprintLayout bpLayout = new BlueprintLayout(this);
		setContentView(bpLayout);


		fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		 designFrag = new DesignBar();
		ft.add(bpLayout.getId(), designFrag);
		ft.commit();
		
		
		Intent i = getIntent();
		intentFile = i.getStringExtra("filename");
		System.out.println("GOT FILE"+intentFile);
		
		
		if(!intentFile.contains("CREATE_NEW_FILE")){
			bpLayout.loadXML(intentFile);
		}

		// View table = createTable();

		// main.addView(table);

		/*findViewById(R.id.blueprintCanvas).setOnDragListener(
				new OnDragListener() {

					@Override
					public boolean onDrag(View v, DragEvent de) {
						// TODO Auto-generated method stub
						int action = de.getAction();

						switch (de.getAction()) {
						case DragEvent.ACTION_DRAG_STARTED:
							System.out.println("DRAG ACTION STARTED");
							return true;
						case DragEvent.ACTION_DRAG_LOCATION:
							System.out.println("DRAG LOCATION");
							return true;
						case DragEvent.ACTION_DROP:
							System.out.println("DRAG DROPPED");
							View view = (View) de.getLocalState();
							ClipData cd = de.getClipData();
							if (cd.getDescription().getLabel().toString()
									.contains("CREATErect")) {
								View table = createTable(de.getX(), de.getY(),
										CREATE_RECTANGLE);
								main.invalidate();
								main.addView(table);
								table.setVisibility(View.VISIBLE);
							} else if (cd.getDescription().getLabel()
									.toString().contains("CREATEcircle")) {
								View table = createTable(de.getX(), de.getY(),
										CREATE_CIRCLE);
								main.invalidate();
								main.addView(table);
								table.setVisibility(View.VISIBLE);
							} else {
								ViewGroup owner = (ViewGroup) view.getParent();
								owner.removeView(view);
								view.setX(de.getX()
										- (dragNoise * view.getWidth() / 100));
								view.setY(de.getY()
										- (dragNoise * view.getHeight() / 100));

								owner.invalidate();
								main.invalidate();
								main.addView(view);
								view.setVisibility(View.VISIBLE);
							}

							return true;
						}
						return true;
					}

				});*/

	}

	/*private View createTable(float x, float y, int tableCode) {
		TextView text, text2;

		View table = new View(this);
		Table softTable = new Table(this);
		final Table softcopy = softTable;

		table.setId(TABLE_COUNT);
		TABLE_COUNT += 1;
		softTable.setTableNumber(table.getId());
		softTable.setTableId(table.getId());

		switch (tableCode) {
		case CREATE_RECTANGLE:
			table = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.table, null);
			text = (TextView) table.findViewById(R.id.tableNumberText);
			text.setText(text.getText() + " " + softTable.getTableNumber());
			text2 = (TextView) table.findViewById(R.id.tableNumberSeats);
			text2.setText(text2.getText() + " " + softTable.getSeatsX());
			break;
		case CREATE_CIRCLE:
			softTable.setTableType(CREATE_CIRCLE);
			table = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.circletable, null);
			text = (TextView) table.findViewById(R.id.circleTableNumberText);
			text.setText(text.getText() + " " + softTable.getTableNumber());
			text2 = (TextView) table.findViewById(R.id.circleTableNumberSeats);
			text2.setText(text2.getText() + " " + softTable.getSeatsX());
			break;
		case CREATE_BOOTH:
			table = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.table, null);
			break;
		default:
			table = LayoutInflater.from(getBaseContext()).inflate(
					R.layout.table, null);
			break;
		}

		table.setX(x);
		table.setY(y);
		softTable.setAnchorX(x);
		softTable.setAnchorY(y);
		final Table softTableCopy = softTable;
		final View tableCopy = table;

		table.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(!TWO_FINGER){
				ag = new ActionsDialog(mContext, softTableCopy);
				ag.show();
				refresher.postDelayed(refreshActionDialog, 100);
				LONG_CLICK = true;
				return false;
				}else{
					return false;
				}
			}

		});

		table.setOnTouchListener(new OnTouchListener() {
			float[] origin = new float[2];
			float[] now = new float[2];
			float[] pointerOrigin = new float[2];
			float[] pointerNow = new float[2];
			Boolean twoFinger = false;
			float dist;

			@Override
			public boolean onTouch(View v, MotionEvent me) {
				// TODO Auto-generated method stub

				int action = me.getAction();

				switch (me.getAction() & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_DOWN:
					// if(!EDITMODE){
					LONG_CLICK = false;
					TWO_FINGER = false;
					origin[0] = me.getX(0);
					origin[1] = me.getY(0);
					// }
					return false;

				case MotionEvent.ACTION_POINTER_DOWN:
					System.out.println("ACTION_POINTER_DOWN");
					pointerOrigin[0] = me.getX(1);
					pointerOrigin[1] = me.getY(1);
					TWO_FINGER = true;

					return true;

				case MotionEvent.ACTION_POINTER_UP:
					System.out.println("ACTION_POINTER_UP");
					pointerNow[0] = me.getX(1);
					pointerNow[1] = me.getY(1);
					System.out.println("SPACING "
							+ getSpacing(pointerOrigin, pointerNow));
					if (getSpacing(pointerOrigin, pointerNow) >= 7.0f) {
						scaleImage(softTableCopy, pointerOrigin, pointerNow);
					}

					return false;

				case MotionEvent.ACTION_MOVE:
					// tableCopy.setSelected(true);
					now[0] = me.getX(0);
					now[1] = me.getY(0);
					
					dist = getSpacing(origin, now);

					if (dist > 15.0f) {
						ClipData data = ClipData.newPlainText("", "");
						DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
								v);
						v.startDrag(data, shadowBuilder, v, 0);
						v.setVisibility(View.INVISIBLE);
					}
					return false;

				case MotionEvent.ACTION_UP:
					// if(EDITMODE){
					updateAnchors(softTableCopy);
					tableCopy.setSelected(false);
					now[0] = me.getX(0);
					now[1] = me.getY(0);
					dist = getSpacing(origin, now);
					if (dist <= 15.0f & !TWO_FINGER & !LONG_CLICK) {
						dg = new EditDialog(mContext, softcopy);
						dg.show();
						refresher.postDelayed(refreshEditDialog, 100);
						// }
					}
					return false;
				}

				return false;

			}

		});// for the ontouch listener and drag event

		activeTables.add(table);
		softActiveTables.add(softTable);

		return table;

	}*/

	/*public void updateAnchors(Table table){
		View view = null;
		Table mTable = null;
		for (int i = 0; i < activeTables.size(); ++i) {
			if (softActiveTables.get(i).getTableId() == table.getTableId()) {
				view = activeTables.get(i);
				mTable = softActiveTables.get(i);
			}
		}
		mTable.setAnchorX(view.getX());
		mTable.setAnchorY(view.getY());
		
	}
	
	public float getSpacing(float[] old, float[] current) {
		float spacing;
		float x = current[0] - old[0];
		float y = current[1] - old[1];

		return FloatMath.sqrt(x * x + y * y);
	}

	public void updateTableInfo(Table table) {
		System.out.println("Table ID" + table.getTableId());
		TextView text, text2;
		View view = null;
		Table mTable = null;
		for (int i = 0; i < activeTables.size(); ++i) {
			if (softActiveTables.get(i).getTableId() == table.getTableId()) {
				view = activeTables.get(i);
				mTable = softActiveTables.get(i);
			}
		}

		mTable.setTableNumber(table.getTableNumber());
		mTable.setSeatsX(table.getSeatsX());

		switch (table.getTableType()) {
		case CREATE_RECTANGLE:
			text = (TextView) view.findViewById(R.id.tableNumberText);
			text.setText("Table " + table.getTableNumber());
			text2 = (TextView) view.findViewById(R.id.tableNumberSeats);
			text2.setText("Seats " + table.getSeatsX());
			view.invalidate();
			break;
		case CREATE_CIRCLE:
			text = (TextView) view.findViewById(R.id.circleTableNumberText);
			text.setText("Table " + table.getTableNumber());
			text2 = (TextView) view.findViewById(R.id.circleTableNumberSeats);
			text2.setText("Seats " + table.getSeatsX());
			view.invalidate();
			break;
		}

	}

	Runnable refreshEditDialog = new Runnable() {
		public void run() {

			if (dg.getIsDone()) {
				updateTableInfo(dg.returnTable());
				refresher.removeCallbacks(refreshEditDialog);
			} else {
				refresher.postDelayed(this, 1000);
			}

		}
	};

	Runnable refreshActionDialog = new Runnable() {
		public void run() {

			if (ag.getIsDone()) {
				if (ag.getIsDelete()) {
					deleteTable(ag.returnTable());
					refresher.removeCallbacks(refreshActionDialog);
				} else if (ag.getIsCopy()) {
					copyTable(ag.returnTable());
					refresher.removeCallbacks(refreshActionDialog);
				} else if (ag.getIsCancel()) {
					refresher.removeCallbacks(refreshActionDialog);
				}
			} else {
				refresher.postDelayed(this, 1000);
			}

		}
	};*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_Settings:
			SaveBlueprintDialog bp = new SaveBlueprintDialog(mContext,BlueprintLayout.activeTables);
			bp.show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCloseButtonClick(Boolean isClicked,EditBar thisbar) {
		// TODO Auto-generated method stub
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(thisbar);
		ft.commit();
	}

	/*private void scaleImage(Table table, float[] origin, float[] moved) {

		View view = null;
		Table mTable = null;
		for (int i = 0; i < activeTables.size(); ++i) {
			if (softActiveTables.get(i).getTableId() == table.getTableId()) {
				view = activeTables.get(i);
				mTable = softActiveTables.get(i);
			}
		}
		ImageView background;
		switch (table.getTableType()) {
		case CREATE_RECTANGLE:
			System.out.println("made it create rectangle");
			background = (ImageView) view.findViewById(R.id.tableSurface);
			break;
		case CREATE_CIRCLE:
			background = (ImageView) view.findViewById(R.id.circleTableSurface);
			break;
		default:
			background = (ImageView) view.findViewById(R.id.tableSurface);
			break;
		}

		// Get the ImageView and its bitmap
		Drawable drawing = background.getBackground().mutate();
		if (drawing == null) {
			System.out.println("returned");
			return; // Checking for null & return, as suggested in comments
		}
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions AND the desired bounding box
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int bounding = dpToPx(250);

		float screenScale = 50.0f; // choose a value that is constant for
									// increasing and decreasing size

		float xdist = moved[0] - origin[0];
		
		float ydist = moved[1] - origin[1];
		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = (float) (xdist) / screenScale;
		float yScale = (float) (ydist) / screenScale;
		
		if(xScale < 0){
			xScale = 0.9f;
		}
		if(yScale < 0){
			yScale = 0.9f;
		}
		
		Matrix matrix = mTable.getSavedMatrix();

		if (xScale == 0) {
			xScale = 1.0f;
		}
		if (yScale == 0) {
			yScale = 1.0f;
		}
		
		if(Math.abs((double) xdist) > 10.0f){
		matrix.postScale(xScale, 1.0f);
		}
		
		if(Math.abs((double) ydist) > 10.0f){
			matrix.postScale(1.0f, yScale);
			}
		matrix.postScale(xScale, yScale);
		

		mTable.setSavedMatrix(matrix);

		// Create a matrix for the scaling and add the scaling data

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		if(width < 100){
			width = 100; //min width
		}
		
		if(height < 100){
			height = 100;
		}
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		width = scaledBitmap.getWidth(); // re-use
		height = scaledBitmap.getHeight(); // re-use
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);

		mTable.setTableWidth(width);
		mTable.setTableHeight(height);

		// Apply the scaled bitmap
		background.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) background
				.getLayoutParams();
		params.width = width;
		params.height = height;
		background.setLayoutParams(params);
		view.invalidate();
		main.invalidate();
	}*/

	/*private int dpToPx(int dp) {
		float density = getApplicationContext().getResources()
				.getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	public void deleteTable(Table table) {
		for (int i = 0; i < activeTables.size(); ++i) {
			if (softActiveTables.get(i).getTableId() == table.getTableId()) {
				View view = activeTables.get(i);
				softActiveTables.remove(i);
				activeTables.remove(i);
				main.removeView(view);
				main.invalidate();

			}
		}
	}

	public void copyTable(Table table) {
	
		TextView text, text2;
		float halfwayX = (float) Math.round(getApplicationContext()
				.getResources().getDisplayMetrics().widthPixels / 2);
		float halfwayY = (float) Math.round(getApplicationContext()
				.getResources().getDisplayMetrics().heightPixels / 2);
		View newTable = createTable(halfwayX, halfwayY, table.getTableType());
		main.invalidate();
		main.addView(newTable);
		

		int lastone = activeTables.size() - 1;
		View view = activeTables.get(lastone);
		Table mTable = softActiveTables.get(lastone);
		mTable.copyTable(table);

		switch (mTable.getTableType()) {
		case CREATE_RECTANGLE:
			text = (TextView) view.findViewById(R.id.tableNumberText);
			text.setText("Table " + table.getTableNumber());
			text2 = (TextView) view.findViewById(R.id.tableNumberSeats);
			text2.setText("Seats " + table.getSeatsX());
			view.invalidate();
			break;
		case CREATE_CIRCLE:
			text = (TextView) view.findViewById(R.id.circleTableNumberText);
			text.setText("Table " + table.getTableNumber());
			text2 = (TextView) view.findViewById(R.id.circleTableNumberSeats);
			text2.setText("Seats " + table.getSeatsX());
			view.invalidate();
			break;
		}

		float[] same = new float[2];
		same[0] = 0.0f;
		same[1] = 0.0f;
		scaleImage(mTable, same, same);

	}
	
	public void loadXML(String name){
		BlueprintHandler bh = new BlueprintHandler(mContext);
		ArrayList<Table> tables = bh.getFile(intentFile.toString());
		
		float[] scaled = new float[2];
		float[] anchors = new float[2];
		
		for(int i = 0; i<tables.size();++i){
			View table = createTable(tables.get(i).getAnchorX(),tables.get(i).getAnchorY(),tables.get(i).getTableType());
			updateTableInfo(tables.get(i));
			main.invalidate();
			main.addView(table);
			
			if(tables.get(i).getTableType() == CREATE_CIRCLE){
				anchors[0] = 100.0f;
				anchors[1] = 100.0f;
				scaled[0] = anchors[0] + (float)tables.get(i).getTableWidth()/2.0f;
				scaled[1] = anchors[1] + (float)tables.get(i).getTableHeight()/2.0f;
			}else{
				anchors[0] = 200.0f;
				anchors[1] = 200.0f;
				scaled[0] = anchors[0] + (float)tables.get(i).getTableWidth()/2.0f;
				scaled[1] = anchors[1] + (float)tables.get(i).getTableHeight()/2.0f;
			}
			scaleImage(tables.get(i),anchors,scaled);
			
			
			
		}
		
	}*/
	

}