package cn.edu.zucc.TPF.util;

import cn.edu.zucc.TPF.GpsApp.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SlipButton extends View implements OnTouchListener{
    private float DownX, NowX;    
    private boolean Nowchoose = false;
    private boolean OnSlip = false;
    private boolean isChgLsnOn = false;
    private OnChangedListener ChgLsn;
    
    private Bitmap bg_on;
    private Bitmap bg_off;
    private Bitmap slip_btn;
    
    private Rect onRect = new Rect();
    private Rect offRect = new Rect();
    private Paint paint = new Paint();
	
	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}
    
	private void init(){
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.on);
		bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.off);
		slip_btn = BitmapFactory.decodeResource(getResources(), R.drawable.slip);		
		setOnTouchListener(this);
	}
	
	protected void onDraw(Canvas canvas) {// 绘图函数
       super.onDraw(canvas);		
	   float x=0;			
	   x = NowX;
	   if(x < slip_btn.getWidth()/2){
		   x = slip_btn.getWidth()/2;
	   }
	   else if(x > bg_on.getWidth() - slip_btn.getWidth()/2){
		   x = bg_on.getWidth() - slip_btn.getWidth()/2;
	   }
	   else;
	   
	   if(OnSlip){	   
	      onRect.set(0, 0, (int)(x - slip_btn.getWidth()/2), bg_on.getHeight());
	      offRect.set((int)(x + slip_btn.getWidth()/2) , 0, bg_off.getWidth(), bg_off.getHeight());
	   
	      canvas.drawBitmap(bg_on, onRect, onRect, paint);	 
	      x = x - slip_btn.getWidth()/2;
	      canvas.drawBitmap(slip_btn, x, 0, paint);// 画出游标.		  
	      canvas.drawBitmap(bg_off, offRect, offRect, paint);
	   }
	   else{
		   if(x < bg_on.getWidth()/2){
			   offRect.set(0, 0, bg_on.getWidth(), bg_on.getHeight());
			   canvas.drawBitmap(bg_off, offRect, offRect, paint);			   
		   }
		   else{
			   onRect.set(0, 0, bg_on.getWidth(), bg_on.getHeight());
			   canvas.drawBitmap(bg_on, onRect, onRect, paint);
		   }			   
	   }
	   
	}
	
	public interface OnChangedListener{
		abstract void OnChanged(boolean CheckState);
	}
	
	public void setOnChangedListener(OnChangedListener l){
		isChgLsnOn = true;
		ChgLsn = l;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(event.getX() > bg_on.getWidth()||event.getY()>bg_on.getHeight()){
				return false;
			}
			OnSlip = true;
			DownX = event.getX();
			NowX = DownX;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d("David", "event.getX="+event.getX());
			Log.d("David", "event.getY="+event.getY());
			NowX = event.getX();
			boolean LastChoose = Nowchoose;
			
			if(NowX >= (bg_on.getWidth()/2)){
				Nowchoose = true;
			}
			else{
				Nowchoose = false;
			}
			
			if(isChgLsnOn && (LastChoose!=Nowchoose)){
				ChgLsn.OnChanged(Nowchoose);
			}
			break;
		case MotionEvent.ACTION_UP:
			OnSlip = false;
			break;
		default:;
		}
		
		invalidate();
		return true;
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int measuredHeight = measureHeight(heightMeasureSpec);
	    int measuredWidth = measureWidth(widthMeasureSpec);
	    setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	private int measureHeight(int measureSpec){
		return bg_on.getHeight();
	}
	
	private int measureWidth(int measureSpec){
		return bg_on.getWidth();
	}

}
