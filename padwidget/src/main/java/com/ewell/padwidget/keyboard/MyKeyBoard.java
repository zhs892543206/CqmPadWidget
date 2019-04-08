package com.ewell.padwidget.keyboard;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.ewell.padwidget.R;
import com.ewell.padwidget.callback.KeyBoardCallback;

import java.lang.reflect.Method;

/**
 * 自定义数字输入，加输入框
 * @author zhs
 *
 */
public class MyKeyBoard extends LinearLayout{

	public LayoutInflater layoutInflater;
	public Context mContext;
	private Button[] buttons = new Button[10];
	private int[] btnIds = {R.id.keyboard_0, R.id.keyboard_1, R.id.keyboard_2, R.id.keyboard_3,
			R.id.keyboard_4, R.id.keyboard_5, R.id.keyboard_6, R.id.keyboard_7, R.id.keyboard_8, R.id.keyboard_9};
	private ImageView backspaceImgV;
	private ImageView confirmImgV;
	private EditText editText;
	//private String editContent = "";
	private KeyBoardCallback mKeyBoardCallback = null;
	
	public MyKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}// TODO Auto-generated constructor stub

	public MyKeyBoard(Context context) {
		this(context, null);
		
	}
	
	public MyKeyBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	
	
	public void initView(){
		layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.module_keyboard, this, false);
		addView(view);
		backspaceImgV = (ImageView)view.findViewById(R.id.keyboard_backspace);
		confirmImgV = (ImageView)view.findViewById(R.id.keyboard_confirm);
		editText = (EditText)view.findViewById(R.id.keyboard_edt);
	
		//默认edit不弹出软键盘
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		//editText.setInputType(InputType.TYPE_NULL); //始终不弹出软键盘，但游标也不见了
		//始终不弹出，光标还在
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			editText.setInputType(InputType.TYPE_NULL);
			} else {
				Class<EditText> cls = EditText.class;  
                Method method;
              try { 
                  method = cls.getMethod("setShowSoftInputOnFocus",boolean.class);  
                  method.setAccessible(true);  
                  method.invoke(editText, false);  
              }catch (Exception e) {
                  Log.e("setShowSoftInputOnFocus", e.toString());
          // TODO: handle exception
        	}
            
        } 
		
		//editText.setSelection(editText.getText().toString().length());
		//editContent = editText.getText().toString();
		if(buttons!=null){
			for(int i = 0; i < buttons.length; i++){
				buttons[i] = (Button)view.findViewById(btnIds[i]);
				buttons[i].setTag(i);
				buttons[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(editText.getText().toString().length()>=8){
							return;
						}
						String editContent = editText.getText().toString();
						int selectStart = editText.getSelectionStart();
						int selectEnd = editText.getSelectionEnd();
						//多选情况下不止清空一个字符
						if(selectStart>=0 && selectEnd>=0){				
							String str1 = editContent.substring(0, selectStart);
							str1 += v.getTag();
							String str2 = editContent.substring(selectEnd, editContent.length());
							editContent = str1.concat(str2);
							editText.setText(editContent);
							editText.setSelection(selectStart+1);//这里不能通过editText.getSelectionStart()重新获取selectStart，不然获取到的也是0
						}
					}
				});
			}
		}
		
		//删除按钮
		backspaceImgV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String editContent = editText.getText().toString();
				if(editContent.length() > 0){
					//getSelectionStart();这个是EditText选择的起始位置，还有个End，是终止位置。如果没有选择内容，则这两个返回值相等，也就是光标的位置，最小值0
					int selectStart = editText.getSelectionStart();
					int selectEnd = editText.getSelectionEnd();
						//editContent = editContent.substring(0, editContent.length() - 1);//第一个参数按0开始记忆，第二个按1开始
					//多选情况下不止清空一个字符
					
					if(selectStart!=selectEnd){
						if(selectStart>=0 && selectEnd>=0){
							String str1 = editContent.substring(0, selectStart);
							String str2 = editContent.substring(selectEnd, editContent.length());
							editContent = str1.concat(str2);
							editText.setText(editContent);
							editText.setSelection(selectStart);//这里不能通过editText.getSelectionStart()重新获取selectStart，不然获取到的也是0
						}
					}else{
						if(selectStart>0 && selectEnd>0){
							String str1 = editContent.substring(0, selectStart-1);
							String str2 = editContent.substring(selectEnd, editContent.length());
							editContent = str1.concat(str2);
							editText.setText(editContent);
							editText.setSelection(selectStart-1);//这里不能通过editText.getSelectionStart()重新获取selectStart，不然获取到的也是0
						}
					}

				}
			}
		});
	
		editText.setOnTouchListener(new OnTouchListener() {

            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            /**
             * 点击右侧图标
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() >= (editText.getRight() - editText
                        .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                        .width())){
                	editText.setText("");  
               
                }
                return false;
            }
        });
		
		confirmImgV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mKeyBoardCallback!=null){
					mKeyBoardCallback.confirmOnClick();
				}
			}
		});
		
	}
	
	public void setKeyBoardCallback(KeyBoardCallback keyBoardCallback){
		mKeyBoardCallback = keyBoardCallback;
	}
	
	public String getEditContent(){
		return editText.getText().toString();
	}

}
