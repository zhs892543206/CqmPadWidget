package com.ewell.padwidget.recyclerview;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import com.ewell.padwidget.callback.ZoomCallback;

import java.util.List;

/**
 *
 *
 */
public class ZoomRecyclerView extends RecyclerView{

	public LayoutInflater layoutInflater;
	public Context mContext;
	private ZoomCallback zoomCallback;
	private double nLenStart;//按下时两指距离
	private double nLenEnd;//抬起时两指距离

	
	public ZoomRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}// TODO Auto-generated constructor stub

	public ZoomRecyclerView(Context context) {
		this(context, null);
		
	}
	
	public ZoomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public void initView(int layoutId, List<String> strings){

	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {	
		//手指数量
		int nCnt = arg0.getPointerCount();
		
		int n = arg0.getAction();
		if( (arg0.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN && 2 == nCnt)//<span style="color:#ff0000;">2表示两个手指</span>
		{
					
			for(int i=0; i< nCnt; i++)
			{
				float x = arg0.getX(i);
				float y = arg0.getY(i);
				
				Point pt = new Point((int)x, (int)y);
	
			}
			
			int xlen = Math.abs((int)arg0.getX(0) - (int)arg0.getX(1));
			int ylen = Math.abs((int)arg0.getY(0) - (int)arg0.getY(1));
			
			nLenStart = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);
			
			
		}else if( (arg0.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP  && 2 == nCnt)
		{
			
			for(int i=0; i< nCnt; i++)
			{
				float x = arg0.getX(i);
				float y = arg0.getY(i);
				
				Point pt = new Point((int)x, (int)y);
	
			}
			
			int xlen = Math.abs((int)arg0.getX(0) - (int)arg0.getX(1));
			int ylen = Math.abs((int)arg0.getY(0) - (int)arg0.getY(1));
			
			nLenEnd = Math.sqrt((double)xlen*xlen + (double)ylen * ylen);
			
			if(nLenEnd > nLenStart)//通过两个手指开始距离和结束距离，来判断放大缩小
			{
				if(zoomCallback!=null){
					zoomCallback.changeBig();
				}
			}else
			{
				if(zoomCallback!=null){
					zoomCallback.changeSmall();
				}
			}
		}

		return super.onTouchEvent(arg0);
	}
	
	public void setZoomCallBack(ZoomCallback zoomCallback){
		this.zoomCallback = zoomCallback;
	}
}
