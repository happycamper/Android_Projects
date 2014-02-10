package com.seateasyapp.SEDesigner;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.FloatMath;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RuntimeTable extends View implements OnDragListener, OnTouchListener {

	private Context mContext;
	private Boolean touchFocus;
	private int tableNumber;
	private int seatsX;
	private int Id;
	private int tableType;
	private float anchorX, anchorY;
	private int width, height;
	private Matrix savedMatrix;
	private int xscale, yscale, rotate;

	private View selfInflate;
	private TextView textTableNumber, textTableSeats;

	private Handler refresher;
	private ActionsDialog ag;
	private EditDialog ed;
	private Boolean LONG_CLICK, TWO_FINGER;

	private RuntimeTable mTable;

	private Bitmap background;
	private String TABLE_NUMBER;
	private String SEATSX;
	private String DRAW_STRING;
	private Paint paint;

	private float mScaleX, mScaleY;
	private Matrix matrix;

	private RelativeLayout.LayoutParams lp;

	public RuntimeTable(Context context, int objectCode) {
		super(context);
		touchFocus = false;
		tableNumber = 0;
		Id = 0;
		seatsX = 4;
		LONG_CLICK = false;
		TWO_FINGER = false;

		refresher = new Handler();

		init(objectCode);
		
		rotate = 0;
		mScaleX = mScaleY = 1.0f;
		anchorX = anchorY = 0.0f;
		matrix = new Matrix();
		matrix.postScale(mScaleX, mScaleY);
		width = height = 40;
		savedMatrix = new Matrix();
		xscale = yscale = 1;
		mTable = this;
		this.setOnTouchListener(this);
		this.setOnDragListener(this);
		TABLE_NUMBER = "";
		SEATSX = String.valueOf(getSeatsX());
		paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setTextSize(16.0f);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		// background.draw(canvas);
		canvas.drawBitmap(background, matrix, null);
		canvas.drawText(TABLE_NUMBER, 20.0f, 20.0f, paint);
		canvas.restore();
	}
	
	public void rotate(float degree){
		if(degree > 0.0f){
			++rotate;
		}else if(degree < 0.0f){
			--rotate;
		}
		matrix.postRotate(degree);
		invalidate();
	}
	
	public void doRotation(int times){
		for(int i=0;i<times;++i){
			rotate(1);
		}
	}

	public void stretchX(float xval) {
		if (xval > 0.0f) {
			lp = new RelativeLayout.LayoutParams(++width, height);
			this.setLayoutParams(lp);
			matrix.postScale(1.01f, 1.0f);
			++xscale;
		} else if (xval < 0.0f) {
			lp = new RelativeLayout.LayoutParams(--width, height);
			this.setLayoutParams(lp);
			matrix.postScale(.99f, 1.0f);
			--xscale;
		} else {
			matrix.postScale(1.0f, 1.0f);
		}
		invalidate();
	}

	public void stretchY(float yval) {
		if (yval > 0.0f) {
			lp = new RelativeLayout.LayoutParams(width, ++height);
			this.setLayoutParams(lp);
			matrix.postScale(1.0f, 1.01f);
			++yscale;
		} else if (yval < 0.0f) {
			lp = new RelativeLayout.LayoutParams(width, --height);
			this.setLayoutParams(lp);
			matrix.postScale(1.0f, .99f);
			--yscale;
		} else {
			matrix.postScale(1.0f, 1.0f);
		}
		invalidate();
	}

	public void init(int tableCode) {

		switch (tableCode) {
		case BlueprintLayout.CREATE_RECTANGLE:
			tableType = BlueprintLayout.CREATE_RECTANGLE;
			/*
			 * (getResources().getLayout(R.layout.table)); selfInflate =
			 * LayoutInflater.from(getContext()).inflate( R.layout.table, null);
			 * textTableNumber = (TextView)
			 * selfInflate.findViewById(R.id.tableNumberText); textTableSeats =
			 * (TextView) selfInflate.findViewById(R.id.tableNumberSeats);
			 */
			lp = new RelativeLayout.LayoutParams(40, 40);
			this.setLayoutParams(lp);
			// background =
			// getResources().getDrawable(R.drawable.new_rect_edit);
			// background.setBounds(0,0,background.getIntrinsicWidth(),background.getIntrinsicHeight());
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.standard_table);

			break;

		case BlueprintLayout.CREATE_CIRCLE:
			tableType = BlueprintLayout.CREATE_CIRCLE;
			lp = new RelativeLayout.LayoutParams(40, 40);
			this.setLayoutParams(lp);
			// background =
			// getResources().getDrawable(R.drawable.new_round_edit);
			// background.setBounds(0,0,background.getIntrinsicWidth(),background.getIntrinsicHeight());
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.standard_circle);
			break;

		case BlueprintLayout.CREATE_BOOTH:
			tableType = BlueprintLayout.CREATE_BOOTH;
			lp = new RelativeLayout.LayoutParams(40, 40);
			this.setLayoutParams(lp);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.standard_table);
			break;
			
		case BlueprintLayout.RESTAURANT_OBJECT:
			tableType = BlueprintLayout.RESTAURANT_OBJECT;
			lp = new RelativeLayout.LayoutParams(40, 40);
			this.setLayoutParams(lp);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.standard_table);
			TABLE_NUMBER = "";
			seatsX = 0;
			break;
			
		case BlueprintLayout.WALL_HORIZONTAL:
			tableType = BlueprintLayout.WALL_HORIZONTAL;
			lp = new RelativeLayout.LayoutParams(80, 40);
			this.setLayoutParams(lp);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.horizontal_wall);
			TABLE_NUMBER = "";
			seatsX = 0;
			break;
			
		case BlueprintLayout.WALL_VERTICAL:
			tableType = BlueprintLayout.WALL_VERTICAL;
			lp = new RelativeLayout.LayoutParams(40, 80);
			this.setLayoutParams(lp);
			background = BitmapFactory.decodeResource(getResources(),
					R.drawable.wall_vertical);
			TABLE_NUMBER = "";
			seatsX = 0;
			break;
		}
	}

	public void updateText() {
		if(this.getTableType() != BlueprintLayout.RESTAURANT_OBJECT){
		TABLE_NUMBER = String.valueOf(getTableNumber());
		}
		SEATSX = String.valueOf(getSeatsX());
		invalidate();
	}
	
	public void setTableText(String text){
		TABLE_NUMBER = text;
		updateText();
	}

	public int getStretchY() {
		return yscale;
	}

	public int getStretchX() {
		return xscale;
	}

	public void setStretchX(int val) {
		for (int i = 0; i < val; ++i) {
			this.stretchX(1.0f);
		}
	}

	public void setStretchY(int val) {
		for (int i = 0; i < val; ++i) {
			this.stretchY(1.0f);
		}
	}

	public void setFocus() {
		touchFocus = true;
	}

	public void setSavedMatrix(Matrix mat) {
		savedMatrix = mat;
	}

	public Matrix getSavedMatrix() {
		return savedMatrix;
	}

	public void setTableWidth(int x) {
		width = x;
	}

	public void setTableHeight(int y) {
		height = y;
	}

	public int getTableWidth() {
		return width;
	}

	public int getTableHeight() {
		return height;
	}

	public void setAnchorX(float x) {
		anchorX = x;
	}

	public void setAnchorY(float y) {
		anchorY = y;
	}

	public float getAnchorX() {
		return anchorX;
	}

	public float getAnchorY() {
		return anchorY;
	}

	public void setTableType(int type) {
		this.tableType = type;
	}

	public int getTableType() {
		return this.tableType;
	}

	public void setTableId(int id) {
		this.Id = id;
	}

	public int getTableId() {
		return this.Id;
	}

	public void unFocus() {
		touchFocus = false;
	}

	public Boolean getFocus() {
		return touchFocus;
	}

	public void setTableNumber(int number) {
		tableNumber = number;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setSeatsX(int seats) {
		seatsX = seats;
	}

	public int getSeatsX() {
		return seatsX;
	}


	@Override
	public boolean onTouch(View v, MotionEvent me) {
		float[] origin = new float[2];
		float[] now = new float[2];
		float[] pointerOrigin = new float[2];
		float[] pointerNow = new float[2];
		Boolean twoFinger = false;
		float dist;

		int action = me.getAction();

		switch (me.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			// if(!EDITMODE){
			LONG_CLICK = false;
			TWO_FINGER = false;
			origin[0] = me.getX();
			origin[1] = me.getY();
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

			return false;

		case MotionEvent.ACTION_MOVE:
			// tableCopy.setSelected(true);
			now[0] = me.getX();
			now[1] = me.getY();

			dist = getSpacing(origin, now);
			System.out.println("DIST " + dist);
			if (dist > 15.0f) {

				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				v.startDrag(data, shadowBuilder, v, 0);
				v.setVisibility(View.INVISIBLE);
			}
			return false;

		case MotionEvent.ACTION_UP:
			// if(EDITMODE){

			now[0] = me.getX(0);
			now[1] = me.getY(0);
			dist = getSpacing(origin, now);
			if (dist <= 30.0f & !TWO_FINGER & !LONG_CLICK) {
				// ed = new EditDialog(getContext(), this);
				// ed.show();
				// refresher.postDelayed(refreshEditDialog, 100);
				// }
				//SEDesignerActivity.editFrag.setTable(mTable);
			}
			return false;
		}

		return false;

	}


	

	

	public float getSpacing(float[] old, float[] current) {
		float spacing;
		float x = current[0] - old[0];
		float y = current[1] - old[1];

		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	public boolean onDrag(View view, DragEvent event) {
		// TODO Auto-generated method stub
		
		switch(event.getAction()){
		case DragEvent.ACTION_DROP:
			ClipData cd = event.getClipData();
			ViewGroup owner = (ViewGroup) view.getParent();
			owner.removeView(view);
			Toast.makeText(mContext, "Received", Toast.LENGTH_LONG).show();
		}
		return false;
	}

}
