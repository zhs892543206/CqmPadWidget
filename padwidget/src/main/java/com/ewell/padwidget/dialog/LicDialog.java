package com.ewell.padwidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewell.padwidget.R;
import com.ewell.padwidget.callback.LicCallback;
import com.ewell.padwidget.date.DatetimePickerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 普通文本提示dialog
 * @author zhs
 *
 */
public class LicDialog extends Dialog {

	public LicDialog(Context context) {
		super(context);
	}

	public LicDialog(Context context, int theme) {
		super(context, theme);
	}


	public static class Builder {
		public Context mContext;
		private RecyclerView wpRV;
		public TextView titleTxt;
		private TextView contentTxt;
		public Button licCancelBtn;
		public Button licRegisterBtn;
		public Button licDownloadBtn;
		public Button registerCancelBtn;
		public Button registerConfirmBtn;
		public LinearLayout licLLy, registerLLy;
		public EditText register_peopleEdt;
		public EditText phoneEdt;
		public EditText deviceTypeEdt;
		public EditText deviceNoEdt;
		public EditText customer_nameEdt;
		public EditText end_dateEdt;
		private List<EditText> editTextList = new ArrayList<>();
		private List<Integer> erorStrList = new ArrayList<>();
		private LicCallback mLicCallback;
		private LicDialog dialog;
		private String dialogTitle = "提示";
		public static String deptRfid = "";
		public int position = -1;//点击item弹出时记录点击pos

		public Builder(Context context) {
			mContext = context;
		}

		public void setTitleContent(String title) {
			titleTxt.setText(title);
		}

		/**
		 * @param licCallback type 1是补录，2是wwlx选择
		 * @return
		 */
		public LicDialog create(final LicCallback licCallback, String title, String content) {

			mLicCallback = licCallback;

			this.dialogTitle = title;

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			dialog = new LicDialog(mContext, R.style.lic_dialog);
			View layout = inflater.inflate(R.layout.dialog_lic, null);

			//KeyBoardUtils.setupUI(layout, mContext);没效果
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			dialog.setCanceledOnTouchOutside(false);
			titleTxt = (TextView) layout.findViewById(R.id.dialog_title_name);
			contentTxt = (TextView) layout.findViewById(R.id.dialog_content);
			licRegisterBtn = (Button) layout.findViewById(R.id.lic_register_btn);
			licCancelBtn = (Button) layout.findViewById(R.id.lic_cancel_btn);
			licDownloadBtn = (Button) layout.findViewById(R.id.lic_download_btn);
			registerCancelBtn = (Button) layout.findViewById(R.id.register_cancel_btn);
			registerConfirmBtn = (Button) layout.findViewById(R.id.register_confirm_btn);
			licLLy = (LinearLayout) layout.findViewById(R.id.lic_info_lly);
			registerLLy = (LinearLayout) layout.findViewById(R.id.lic_register_lly);
			register_peopleEdt = (EditText) layout.findViewById(R.id.register_people);
			phoneEdt = (EditText) layout.findViewById(R.id.phone);
			deviceTypeEdt = (EditText) layout.findViewById(R.id.device_type);
			deviceNoEdt = (EditText) layout.findViewById(R.id.device_no);
			customer_nameEdt = (EditText) layout.findViewById(R.id.customer_name);
			end_dateEdt = (EditText) layout.findViewById(R.id.end_date);

			editTextList.add(register_peopleEdt);erorStrList.add(new Integer(R.string.register_people));
			editTextList.add(phoneEdt);erorStrList.add(new Integer(R.string.phone));
			editTextList.add(deviceTypeEdt);erorStrList.add(new Integer(R.string.device_type));
			editTextList.add(deviceNoEdt);erorStrList.add(new Integer(R.string.device_no));
			editTextList.add(customer_nameEdt);erorStrList.add(new Integer(R.string.customer_name));
			editTextList.add(end_dateEdt);erorStrList.add(new Integer(R.string.end_date));
			for(int i=0; i < editTextList.size(); i++) {
				editTextList.get(i).addTextChangedListener(new MyTextWatcher(editTextList.get(i)));
			}
			licLLy.setVisibility(View.VISIBLE);
			registerLLy.setVisibility(View.GONE);
			titleTxt.setText(dialogTitle);
			contentTxt.setText(content);
			licCancelBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mLicCallback.onClickCancel();
					dialog.dismiss();
				}
			});
			licRegisterBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					licLLy.setVisibility(View.GONE);
					registerLLy.setVisibility(View.VISIBLE);
					mLicCallback.onClickRegister();
				}

			});
			licDownloadBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mLicCallback.onClickDownload();
				}

			});
			registerCancelBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					licLLy.setVisibility(View.VISIBLE);
					registerLLy.setVisibility(View.GONE);
					mLicCallback.onClickRegisterCancel();
				}

			});
			registerConfirmBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String[] contentStrs = new String[editTextList.size()];
					//填写患者信息和预约时间、检查项目；备注和电话内容为选填，其他都是必填项目。
					for(int i=0; i < contentStrs.length; i++) {
						contentStrs[i] = editTextList.get(i).getText().toString();
						if (contentStrs[i] == null || contentStrs[i].length() <= 0) {
							editTextList.get(i).setError(mContext.getResources().getString(R.string.please_write) + mContext.getResources().getString(erorStrList.get(i)));
							return;
						}
					}
					mLicCallback.onClickRegisterConfirm(register_peopleEdt.getText().toString(),
							phoneEdt.getText().toString(), deviceNoEdt.getText().toString() + "," + deviceTypeEdt.getText().toString(),
							customer_nameEdt.getText().toString(), end_dateEdt.getText().toString());
				}

			});

			//end_dateEdt.setText(fromDate2YYYYMMDD(new Date()));//默认不显示好，开始也让他们自己选，不然容易忘记选
//			end_dateEdt.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					DatetimePickerUtil.showPopwindow(DatetimePickerUtil.PopupType.DATE,
//							end_dateEdt, mContext, null);// 弹出时间选择器
//
//				}
//			});
			end_dateEdt.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					// 解决dialog中edittext点击两次才响应事件的问题
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						DatetimePickerUtil.showPopwindow(Calendar.getInstance().getTime().getYear()+1900, Calendar.getInstance().getTime().getYear()+1900+50, DatetimePickerUtil.PopupType.DATE,
								end_dateEdt, mContext, null);// 弹出时间选择器
					}
					return false;
				}

			});
			dialog.setContentView(layout);
//			dialog.setOnShowListener(new OnShowListener() {
//				@Override
//				public void onShow(DialogInterface dialog) {
//					//按袋扫描界面进入时降低输出功率
//					DeviceManager.changePower(20);
//				}
//			});
//			dialog.setOnDismissListener(new OnDismissListener() {
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					//离开该act时将输出功率还原为设置界面设置的功率
//					DeviceManager.changePower(SetPreferences.getInstance(mContext).GetUhfPower());
//				}
//			});
			return dialog;
		}
	}


	/**
	 * edittext的文本改变监听器
	 * 当需要填的都填写后确认按钮变亮
	 */
	private static class MyTextWatcher implements TextWatcher {
		private EditText editText;
		public MyTextWatcher(EditText editText){
			this.editText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void afterTextChanged(Editable editable) {
			//内容改变清除错误提示，正常edittext这种情况其实自己会没了。只是我这里有的edit不是通过键盘输出传入值的
			if(editText!=null) {
				editText.setError(null);
			}
		}
	}

	public static String fromDate2YYYYMMDD(Date date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			return sdf.format(date);
		} catch (Exception e) {
			return null;
		}
	}
}
