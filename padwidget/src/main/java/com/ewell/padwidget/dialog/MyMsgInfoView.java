package com.ewell.padwidget.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewell.padwidget.R;


/**
 * 消息提示框，通过text属性设置显示内容
 * @author zhs
 *
 */
public class MyMsgInfoView extends RelativeLayout{

	private TypedArray typedArray;
	private CharSequence msgInfoStr;
	private int textColorId;
	private float textSize;
	private Context mContext;
	public MyMsgInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.module_msginfo, this, true);
		typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyMsgInfoView);
		initView();
		
	}

	public MyMsgInfoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}// TODO Auto-generated constructor stub

	public MyMsgInfoView(Context context) {
		this(context, null);
		
	}
	
	public void initView(){

		TextView msgInfoTxt = (TextView)findViewById(R.id.msginfo_txt);
		ImageView cancelImgV = (ImageView)findViewById(R.id.msginfo_cancel);
		cancelImgV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisibility(View.GONE);
			}
		});
		msgInfoStr = typedArray.getText(R.styleable.MyMsgInfoView_text);
		textColorId = typedArray.getColor(R.styleable.MyMsgInfoView_textcolor, -1);
		textSize = typedArray.getFloat(R.styleable.MyMsgInfoView_textsize, -1);
		if(msgInfoStr != null){
			msgInfoTxt.setText(msgInfoStr);
		}
		if(textColorId > 0){
			msgInfoTxt.setTextColor(textColorId);
		}
		if(textSize > 0){
			msgInfoTxt.setTextSize(textSize);
		}
		typedArray.recycle();//取完值记得回收资源
		
	}
}
