package com.ewell.padwidget.callback;

/**
 * 自定义控件licdialog的按钮回调
 * @author zhs
 *
 */
public interface LicCallback {
	public void onClickDownload();
	public void onClickRegister();
	public void onClickCancel();
	public void onClickRegisterCancel();
	public void onClickRegisterConfirm(String app_name, String app_phone, String cus_hdinfo
            , String cus_name, String cus_validdate);

}
