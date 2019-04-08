package com.ewell.padwidget.Scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 头尾可拉伸回弹的activity
 * @author zhs
 *
 */
public class ScrollViewLinearLayout extends LinearLayout implements OnTouchListener{

	private LinearLayout top;
	//private LinearLayout.LayoutParams top_lp ;
	private ListView sv;//改为scrollview。然后布局子布局都用scrollview这样通用
	private boolean isfrist =true;
	private float y1,y2;
	private int hight=60;
	private Scroller mScroller;

	public ScrollViewLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setClickable(true); 
		setLongClickable(true);
		mScroller = new Scroller(context);
	
	}

	protected void smoothScrollBy(int dx, int dy) { 
		//滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间，可以改为通过高度改变量来得出时间  
		mScroller.startScroll(0, mScroller.getFinalY(), 0, dy); //使用默认完成时间250ms  
		invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果 
	} 
	
	protected void smoothScrollTo(int fx, int fy) { 
		int dx = fx - mScroller.getFinalX(); 
		/**
		 * 获取mScroller最终停止的竖直位置
		 * 而fy是这里手指滑动量乘负1.  
		 * 假设开始getFinalY为0，fy为-2（即向下移动2）。
		 * 则dy为-2。mScroller.startScroll(0, mScroller.getFinalY(), 0, dy); 即y为从0偏移-2单位。即下拉2个单位。
		 * 之后 getFinalY为-2。手继续下拉继续上面步骤
		 * 
		 */
		int dy = fy - mScroller.getFinalY(); 
		smoothScrollBy(0, dy); 
	} 
	
	/**
	 * 实例化子视图，调用该方法需要传入放置View的矩形空间左上角left、top值和右下角right、bottom值。这四个值是相对于父控件而言的。
	 * onMesure, onSizeChanged 后调用 
	 * 子视图不止一个这里可能被调用多次
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed&&isfrist){//只需实例化一次
			sv= (ListView) getChildAt(0);//该自定义布局写入xml文件时，其子布局的第一个必须是ScrollView时，这里才能getChildAt(0），实例化ScrollView
			sv.setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉ScrollView 滑动到底部或顶部 继续滑动时会出现渐变的蓝色颜色快
			
			sv.setOnTouchListener(this);
			isfrist=false;
		
		}
	
	}
	
	
	@Override 
	public void computeScroll() { 
		if (mScroller.computeScrollOffset()) { //判断mScroller滚动是否完成
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());//这里调用View的scrollTo()完成实际的滚动
			postInvalidate(); 
		} 
		super.computeScroll(); 
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: 
				y1= event.getY();
				break; 
			case MotionEvent.ACTION_MOVE:
				y2= event.getY();
				int scrollY=v.getScrollY(); //手机屏幕左上角坐标 - v左上角坐标。所以向下滑是负的。开始是0
				int height=v.getHeight(); 
				float firstY = sv.getChildAt(0).getY();
				float endY = sv.getChildAt(sv.getChildCount()-1).getBottom();
				int scrollViewMeasuredHeight=sv.getMeasuredHeight();//如scrollview是sv.getChildAt(0).getMeasuredHeight();
				if(y2-y1>0&&v.getScrollY()<=0 && firstY >=0){//头部回弹效果
					smoothScrollTo(0,-(int) ((y2-y1)/2));
					System.out.println("topMargin="+((int) ((y2-y1)/2)));
					return false;
				}
				/**
				 * 如果一直向上滚动，当最后一项也显示出来后，算滚动到底部。而此时scrollY+height为整个高度包括看不到的
				 * 1.如果前面的v是scrollview是通过(scrollY+height)==scrollViewMeasuredHeight则scrollViewMeasuredHeight=sv.getChildAt(0).getMeasuredHeight();
				 * scrollViewMeasuredHeight是通过getMeasuredHeight得出的即为其子控件如linearlayout的高度包括看不到的
				 * 2.而这里是listview则判断则通过firstY和endY
				 * 
				 */
				if(y2-y1<0&& endY==height){
					smoothScrollTo(0,-(int) ((y2-y1)/2));
					return false;
				}
				y1 = y2;//不加这个会有各种异常，原代码没有的
				break;
			case MotionEvent.ACTION_UP:
				smoothScrollTo(0, 0);//松开手指，自动回滚
				break;
			default: 
				break;
		}
		return false;
	}

}
