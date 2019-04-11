package com.ewell.cqmpadwidget;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ewell.padwidget.callback.LicCallback;
import com.ewell.padwidget.date.DatetimePickerUtil;
import com.ewell.padwidget.dialog.LicDialog;
import com.ewell.padwidget.dialog.WpDialog;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends Activity {
    WpDialog wpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LicDialog textHintDialog=null;
        LicDialog.Builder builder = new LicDialog.Builder(this);
        if (textHintDialog == null) {
            textHintDialog = builder.create(new LicCallback() {


                @Override
                public void onClickDownload() {


                }

                @Override
                public void onClickRegister() {

                }

                @Override
                public void onClickCancel() {

                }

                @Override
                public void onClickRegisterCancel() {

                }

                @Override
                public void onClickRegisterConfirm(String app_name, String app_phone, String cus_hdinfo, String cus_name, String cus_validdate) {

                }


            },getResources().getString(R.string.register_lic), "\n" + "设备信息：" );


        }
        textHintDialog.setCancelable(false);
        textHintDialog.setCanceledOnTouchOutside(false);
        textHintDialog.show();



    }
}
