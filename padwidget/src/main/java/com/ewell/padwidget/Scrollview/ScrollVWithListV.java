package com.ewell.padwidget.Scrollview;

import android.widget.ListView;

/**
 * 处理scrollview嵌套listview导致listview显示不全,listview
 * 高度用wrap还是match都不影响这个和ScrollViewStretch效果，而recyclerview只要在scrollview里添加android:fillViewport="true"属性就没问题了。这个属性是让scrollview里面的东西变成填满scrollview不过这样也限制了类似listview的大小延伸到看不到的区域。所以listview外面的scrollview可以不用加
 * 处理scrollview嵌套recyclerview导致没有显示。其处理不能像listview通过重写自己的onMeasure方法。试了下那样做其实高math_parent也是能显示的只是滚动有问题没试验wrap的
 * @author zhs
 *
 */
public class ScrollVWithListV extends ListView {
	public ScrollVWithListV(android.content.Context context, android.util.AttributeSet attrs) 
	{ 
		super(context, attrs); 
	} 
	/** 
	* Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec); 
	} 
}
