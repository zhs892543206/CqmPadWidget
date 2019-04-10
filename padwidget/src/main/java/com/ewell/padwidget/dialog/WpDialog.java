package com.ewell.padwidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;


import com.ewell.padwidget.R;
import com.ewell.padwidget.androidwheel.OnWheelChangedListener;
import com.ewell.padwidget.androidwheel.WheelView;
import com.ewell.padwidget.androidwheel.adapters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * 选择物品的dialog
 * @author zhs
 *
 */
public class WpDialog extends Dialog {

	public WpDialog(Context context) {
		super(context);
	}

	public WpDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder  implements OnWheelChangedListener {
		private WheelView mViewProvince;
		private WheelView mViewCity;
		/**
		 * 所有省
		 */
		protected String[] mWpflDatas;
		/**
		 * key - 省 value - 市
		 */
		protected HashMap<String, String[]> mWpxxDatasMap = new HashMap<String, String[]>();
	
		/**
		 * 当前物品分类的名称
		 */
		protected String mCurrentWpflName;
		/**
		 * 当前物品的名称
		 */
		protected String mCurrentWpName;

		protected String mCurrentWpflId;

		protected String mCurrentWpId;
		public TextView titleTxt;
	
		private Button confrimBtn;
		
		private WpDialogCallback mConfirmCallback;
		private WpDialog dialog;
		private String dialogTitle = "";
		public Context mContext;
		public Builder(Context context) {
			mContext = context;
			dialogTitle = mContext.getResources().getString(R.string.cqmpadwidget_article_sel);
		}
		
		/**
		 * type 决定使用的适配器 1是补录用的适配器也是默认的，2是显示wpfl信息的
		 * @param loginCallback
		 * @return
		 */
		public WpDialog create(WpDialogCallback loginCallback, String title) {

			mConfirmCallback = loginCallback;
			
			this.dialogTitle = title;

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			dialog = new WpDialog(mContext, R.style.sign_dialog);
			View layout = inflater.inflate(R.layout.dialog_wp_select, null);
			
			//KeyBoardUtils.setupUI(layout, mContext);没效果
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setCanceledOnTouchOutside(false);
			mViewProvince = (WheelView) layout.findViewById(R.id.id_province);
			mViewCity = (WheelView) layout.findViewById(R.id.id_city);
			titleTxt = (TextView)layout.findViewById(R.id.dialog_title_name);
			titleTxt.setText(dialogTitle);
			confrimBtn = (Button)layout.findViewById(R.id.login_confirm_btn);
			//wpRV.setEnabled(false); //设置listview的item不可点击没效果
			confrimBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mConfirmCallback.onClickCancel();
					dialog.dismiss();	
				}		
			});
			setUpListener();
			setUpData();
			dialog.setContentView(layout);		
			return dialog;
		}

//		public Wpxx getSelectWp(){
//			Wpxx wpxx = new Wpxx();
//			wpxx.setWpid(mCurrentWpId);
//			wpxx.setWpname(mCurrentWpName);

		public String getmCurrentWpflName() {
			return mCurrentWpflName;
		}

		public void setmCurrentWpflName(String mCurrentWpflName) {
			this.mCurrentWpflName = mCurrentWpflName;
		}

		public String getmCurrentWpName() {
			return mCurrentWpName;
		}

		public void setmCurrentWpName(String mCurrentWpName) {
			this.mCurrentWpName = mCurrentWpName;
		}

		public String getmCurrentWpflId() {
			return mCurrentWpflId;
		}

		public void setmCurrentWpflId(String mCurrentWpflId) {
			this.mCurrentWpflId = mCurrentWpflId;
		}

		public String getmCurrentWpId() {
			return mCurrentWpId;
		}

		public void setmCurrentWpId(String mCurrentWpId) {
			this.mCurrentWpId = mCurrentWpId;
		}
//			return wpxx;
//		}
//
//		 protected void initDatas()
//		{
//
//	    	WpflInfoModelDBApi wpflInfoModelDBApi = new WpflInfoModelDBApi(MyApplication.getInstance());
//			ArticleInfoModelDBApi articleInfoModelDBApi = new ArticleInfoModelDBApi(MyApplication.getInstance());
//	    	List<WpflInfoModel> wpflList = wpflInfoModelDBApi.getWpflByLevel("1", "2", null);
//
//			if(wpflList!= null && wpflList.size()>0){
//				for(int i = wpflList.size()-1; i>=0; i--){
//					//先移除在wpxx表中没有的wpflid这种是不显示的
//					List<ArticleInfoModel> wpxxList = articleInfoModelDBApi.getModelByWpflid(wpflList.get(i).getWpflid());
//					if(wpxxList==null || wpxxList.size()<=0){
//						wpflList.remove(i);
//					}else{
//						String[] wpxxStrs = new String[wpxxList.size()];
//						for(int j = 0; j < wpxxList.size(); j++){
//							wpxxStrs[j] = wpxxList.get(j).getWpname();
//						}
//						mWpxxDatasMap.put(wpflList.get(i).getWpflname(), wpxxStrs);
//					}
//				}
//				if(wpflList!= null && wpflList.size()>0){
//					mCurrentWpflName = wpflList.get(0).getWpflname();
//					mCurrentWpName = mWpxxDatasMap.get(mCurrentWpflName)[0];
//					mWpflDatas = new String[wpflList.size()];
//					for(int i = 0; i < wpflList.size(); i++){
//						String wpflname = wpflList.get(i).getWpflname();
//						mWpflDatas[i] = wpflname;
//
//					}
//				}
//			}
//
//		}
//
//		public String getWpIdByWpName(String wpName){
//			return CardUtil.getWpIdByWpName(mCurrentWpName);
//		}

		/**
		 * 设置滚动监听器
 		 */
		private void setUpListener() {
			// 添加change事件
			mViewProvince.addChangingListener(this);
			// 添加change事件
			mViewCity.addChangingListener(this);

		}

		/**
		 * 设置选择界面的基础数据
		 */
		private void setUpData() {
			mWpflDatas = mConfirmCallback.initDatas(mWpxxDatasMap);
			mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mWpflDatas));
			// 设置可见条目数量
			mViewProvince.setVisibleItems(7);
			mViewCity.setVisibleItems(7);

			if(mWpflDatas!=null && mWpflDatas.length>0){
				mViewProvince.setCurrentItem((mWpflDatas.length-1)/2);
			}
			updateCities();

		}

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			// TODO Auto-generated method stub
			if (wheel == mViewProvince) {
				if(mWpflDatas!=null && mWpflDatas.length>0){
					updateCities();
				}
			}else if(wheel == mViewCity){
				if(mWpxxDatasMap!=null && mWpxxDatasMap.size()>0){
					mCurrentWpName = mWpxxDatasMap.get(mCurrentWpflName)[newValue];
					mCurrentWpId = mConfirmCallback.getWpIdByWpName(mCurrentWpName);
				}
			}
		}

			

		/**
		 * 根据当前的省，更新市WheelView的信息
		 */
		private void updateCities() {
			if(mWpflDatas!=null &&mWpflDatas.length>0){
				int pCurrent = mViewProvince.getCurrentItem();
				mCurrentWpflName = mWpflDatas[pCurrent];
				String[] cities = mWpxxDatasMap.get(mCurrentWpflName);
				if (cities == null) {
					cities = new String[] { "" };
				}
				mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, cities));
				mViewCity.setCurrentItem(0);
				mCurrentWpName = cities[0];
				mCurrentWpId = mConfirmCallback.getWpIdByWpName(mCurrentWpName);
			}


		}
	}

//	/**
//     * 点击外部和dialog上任何位置关闭dialog。点击edit时不会
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//		 if(getCurrentFocus()!=null){
//            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//        return super.onTouchEvent(event);
//    }
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface WpDialogCallback {
		boolean onClickCancel();
		String[] initDatas(HashMap<String, String[]> mWpxxDatasMap);
		String getWpIdByWpName(String wpName);
	}
}
