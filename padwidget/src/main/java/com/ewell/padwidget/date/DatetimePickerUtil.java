package com.ewell.padwidget.date;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.ewell.padwidget.R;
import com.ewell.padwidget.Timewheelview.NumericWheelAdapter;
import com.ewell.padwidget.Timewheelview.OnWheelScrollListener;
import com.ewell.padwidget.Timewheelview.TimeWheelView;

import java.util.Calendar;

public class DatetimePickerUtil {
	private static TimeWheelView year;
	private static TimeWheelView month;
	private static TimeWheelView day;
	private static TimeWheelView hour;
	private static TimeWheelView mins;
	private static TimeWheelView secs;
	private static LayoutInflater inflater = null;
	private static PopupWindow menuWindow;
	private static View currentDatePicker =null;
	private static Handler currentHandler =null;
	
	//DATE:年月日
	//TIME:时分秒
	//DATETIME：年月日 时分秒
	//TIME_HM： 时分
	public enum PopupType {DATE, TIME, DATETIME,TIME_HM}
	public static void  showPopwindow(PopupType type,View backview,Activity act) {
		showPopwindow(type,backview,act,null);
	}
	public static void  showPopwindow( PopupType type,View backview,Context context,Handler handler) {
		showPopwindow(1950, Calendar.getInstance().get(Calendar.YEAR)+2, type, backview, context, handler);
	}
	/**
	 * 初始化popupWindow
	 * 1.弹出类型  
	 * 2.回置的view
	 * handler真要传也不要传网络请求用的那个好
	 */
	public static void  showPopwindow(int startYear,int endYear,  PopupType type, View backview, Context context, Handler handler) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		currentDatePicker = backview;
		currentHandler = handler;
		View view = null;
		switch(type) 
		{ 
		case DATE:
			view = getDatePick(startYear, endYear, ((TextView) currentDatePicker).getText().toString());
			break;
		case TIME:
			view = getTimePick(((TextView) currentDatePicker).getText().toString());
		    break;
		case DATETIME:
			view = getDatetimePick(((TextView) currentDatePicker).getText().toString());
		    break;
		case TIME_HM:
			view = getTimeHmPick(((TextView) currentDatePicker).getText().toString());
		    break;		    
		default:
			view = getDatePick(startYear, endYear, ((TextView) currentDatePicker).getText().toString());
            break; 
		} 		
		if(menuWindow!=null){
			menuWindow.dismiss();
		}
		//为了防止为dialog遮盖框高设置fill，showAtLocation传入dialog中的控件
		menuWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(currentDatePicker, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});
	}	
	public static void  showPopwindow2(View view) {
		menuWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		currentDatePicker = view;
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public static View getTimePick(String timeStr) {
		Calendar c = Calendar.getInstance();
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		int curMins = c.get(Calendar.MINUTE);
		int curSecd = c.get(Calendar.SECOND);
		View view = inflater.inflate(R.layout.timeallpick, null);
		hour = (TimeWheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel("时");
		hour.setCyclic(true);
		mins = (TimeWheelView) view.findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel("分");
		mins.setCyclic(true);
		secs = (TimeWheelView) view.findViewById(R.id.secs);
		secs.setAdapter(new NumericWheelAdapter(0, 59));
		secs.setLabel("秒");
		secs.setCyclic(true);		
		if (timeStr.split(":", -1).length == 3) {
			curHour = Integer.parseInt(timeStr.split(":", -1)[0]);
			curMins = Integer.parseInt(timeStr.split(":", -1)[1]);
			curSecd = Integer.parseInt(timeStr.split(":", -1)[2]);
		}
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMins);
		secs.setCurrentItem(curSecd);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = // hour.getCurrentItem() + ":"+
								// mins.getCurrentItem();
				(((hour.getCurrentItem()) >= 10) ? (hour.getCurrentItem())
						: ("0" + (hour.getCurrentItem())))
						+ ":"
						+ (((mins.getCurrentItem()) >= 10) ? (mins
								.getCurrentItem()) : ("0" + (mins
								.getCurrentItem())))
                        + ":"								
						+ (((secs.getCurrentItem()) >= 10) ? (secs
								.getCurrentItem()) : ("0" + (secs
								.getCurrentItem())));

				// Toast.makeText(getApplicationContext(), str,
				// Toast.LENGTH_LONG).show();
				if (currentDatePicker instanceof EditText) {
					((EditText) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				} else if (currentDatePicker instanceof TextView) {
					((TextView) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				}
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});

		return view;
	}
	
	public static View getTimeHmPick(String timeStr) {
		Calendar c = Calendar.getInstance();
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		int curMins = c.get(Calendar.MINUTE);
		int curSecd = c.get(Calendar.SECOND);
		View view = inflater.inflate(R.layout.timepick, null);
		hour = (TimeWheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		hour.setLabel("时");
		hour.setCyclic(true);
		mins = (TimeWheelView) view.findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		mins.setLabel("分");
		mins.setCyclic(true);
		if (timeStr.split(":", -1).length == 3) {
			curHour = Integer.parseInt(timeStr.split(":", -1)[0]);
			curMins = Integer.parseInt(timeStr.split(":", -1)[1]);
			curSecd = Integer.parseInt(timeStr.split(":", -1)[2]);
		}
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMins);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = // hour.getCurrentItem() + ":"+
								// mins.getCurrentItem();
				(((hour.getCurrentItem()) >= 10) ? (hour.getCurrentItem())
						: ("0" + (hour.getCurrentItem())))
						+ ":"
						+ (((mins.getCurrentItem()) >= 10) ? (mins
								.getCurrentItem()) : ("0" + (mins
								.getCurrentItem()))) + ":00";

				// Toast.makeText(getApplicationContext(), str,
				// Toast.LENGTH_LONG).show();
				if (currentDatePicker instanceof EditText) {
					((EditText) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				} else if (currentDatePicker instanceof TextView) {
					((TextView) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				}
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});

		return view;
	}	

	/**
	 * 
	 * @return
	 */
	public static View getDatePick(final int startYear, int endYear, String dateStr) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		if (dateStr.split("-", -1).length == 3) {
			curYear = Integer.parseInt(dateStr.split("-", -1)[0]);
			curMonth = Integer.parseInt(dateStr.split("-", -1)[1]);
			curDate = Integer.parseInt(dateStr.split("-", -1)[2]);
		}
		final View view = inflater.inflate(R.layout.datapick, null);

		year = (TimeWheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(startYear, endYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (TimeWheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (TimeWheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth);
		day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - startYear);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String str = (year.getCurrentItem() + startYear)
						+ "-"
						+ (((month.getCurrentItem() + 1) >= 10) ? (month
								.getCurrentItem() + 1) : ("0" + (month
								.getCurrentItem() + 1)))
						+ "-"
						+ ((day.getCurrentItem() + 1) >= 10 ? (day
								.getCurrentItem() + 1) : ("0" + (day
								.getCurrentItem() + 1)));
				// Toast.makeText(getApplicationContext(), str,
				// Toast.LENGTH_LONG).show();
				if (currentDatePicker instanceof EditText) {
					((EditText) currentDatePicker).setText(str);
					sendQueryMessage("ok");//执行查询
				} else if (currentDatePicker instanceof TextView) {
					((TextView) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				}
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		return view;
	}
	
	public static View getDatetimePick(String datetimeStr) {
		Calendar c = Calendar.getInstance();
		int curYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
		int curDate = c.get(Calendar.DATE);
		int curHour = c.get(Calendar.HOUR_OF_DAY);
		int curMins = c.get(Calendar.MINUTE);
		int curSecd = c.get(Calendar.SECOND);
		if (datetimeStr.split(" ").length==2){
			String dateStr = datetimeStr.split(" ")[0];
			String timeStr = datetimeStr.split(" ")[1];
			if(dateStr.split("-", -1).length == 3) {		
				curYear = Integer.parseInt(dateStr.split("-", -1)[0]);
				curMonth = Integer.parseInt(dateStr.split("-", -1)[1]);
				curDate = Integer.parseInt(dateStr.split("-", -1)[2]);
			}
			if (timeStr.split(":", -1).length == 3) {
				curHour = Integer.parseInt(timeStr.split(":", -1)[0]);
				curMins = Integer.parseInt(timeStr.split(":", -1)[1]);
				curSecd = Integer.parseInt(timeStr.split(":", -1)[2]);
			}			
		}
		
		final View view = inflater.inflate(R.layout.datetimepick, null);
		year = (TimeWheelView) view.findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, c.get(Calendar.YEAR)+2));
		//year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (TimeWheelView) view.findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12));
		//month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (TimeWheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth);
		//day.setLabel("日");
		day.setCyclic(true);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		
		hour = (TimeWheelView) view.findViewById(R.id.hour);
		hour.setAdapter(new NumericWheelAdapter(0, 23));
		//hour.setLabel("时");
		hour.setCyclic(true);
		mins = (TimeWheelView) view.findViewById(R.id.mins);
		mins.setAdapter(new NumericWheelAdapter(0, 59));
		//mins.setLabel("分");
		mins.setCyclic(true);
		secs = (TimeWheelView) view.findViewById(R.id.secs);
		secs.setAdapter(new NumericWheelAdapter(0, 59));
		//secs.setLabel("秒");
		secs.setCyclic(true);		
		hour.setCurrentItem(curHour);
		mins.setCurrentItem(curMins);
		secs.setCurrentItem(curSecd);		

		Button bt = (Button) view.findViewById(R.id.set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String str = (year.getCurrentItem() + 1950)
						+ "-"
						+ (((month.getCurrentItem() + 1) >= 10) ? (month
								.getCurrentItem() + 1) : ("0" + (month
								.getCurrentItem() + 1)))
						+ "-"
						+ ((day.getCurrentItem() + 1) >= 10 ? (day
								.getCurrentItem() + 1) : ("0" + (day
								.getCurrentItem() + 1)))				
				        + " "
					// mins.getCurrentItem();
				        +(((hour.getCurrentItem()) >= 10) ? (hour.getCurrentItem())
						: ("0" + (hour.getCurrentItem())))
						+ ":"
						+ (((mins.getCurrentItem()) >= 10) ? (mins
								.getCurrentItem()) : ("0" + (mins
								.getCurrentItem())))
			            + ":"								
						+ (((secs.getCurrentItem()) >= 10) ? (secs
								.getCurrentItem()) : ("0" + (secs
								.getCurrentItem())));				
				// Toast.makeText(getApplicationContext(), str,
				// Toast.LENGTH_LONG).show();
				if (currentDatePicker instanceof EditText) {
					((EditText) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				} else if (currentDatePicker instanceof TextView) {
					((TextView) currentDatePicker).setText(str);
					sendQueryMessage("ok");
				}
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		Button cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(menuWindow!=null){
					menuWindow.dismiss();
				}
			}
		});
		return view;
	}
	public static final int MSG_QUERY = 1001;
	private static void sendQueryMessage(String retStr){
		if(currentHandler!=null){
			Message msg = Message.obtain();
			msg.obj = retStr;
            msg.what = MSG_QUERY;
            currentHandler.sendMessage(msg);
		}				
	}
	static OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(TimeWheelView wheel) {

		}

		@Override
		public void onScrollingFinished(TimeWheelView wheel) {
			// TODO Auto-generated method stub
			int n_year = year.getCurrentItem() + 1950;// 楠烇拷
			int n_month = month.getCurrentItem() + 1;// 閺堬拷
			initDay(n_year, n_month);
		}
	};

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private static int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private static void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}
}
