package com.ewell.cqmpadwidget;

import android.app.Activity;
import android.os.Bundle;

import com.ewell.padwidget.date.DatetimePickerUtil;
import com.ewell.padwidget.dialog.WpDialog;

import java.util.HashMap;

public class MainActivity extends Activity {
    WpDialog wpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WpDialog.Builder builder = new WpDialog.Builder(this);
        wpDialog = builder.create(new WpDialog.WpDialogCallback() {
            @Override
            public boolean onClickCancel() {

                return false;
            }

            @Override
            public String[] initDatas(HashMap<String, String[]> mWpxxDatasMap) {
                mWpxxDatasMap.put("wpflname1", new String[]{"wpname1"});
                String wpflname = "wpflname1";
                String[] mWpflDatas;
                mWpflDatas = new String[mWpxxDatasMap.size()];
                builder.setmCurrentWpflName("wpflname1");
                builder.setmCurrentWpName("wpname1");

                mWpflDatas[0] = "wpname1";


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                DatetimePickerUtil.showPopwindow(DatetimePickerUtil.PopupType.DATE,builder.titleTxt, MainActivity.this, null);;
                            }
                        });
                    }
                }).start();

                return mWpflDatas;
            }

            @Override
            public String getWpIdByWpName(String wpName) {
                return null;
            }
        },"测试");

        wpDialog.show();


    }
}
