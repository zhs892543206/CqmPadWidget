package com.ewell.cqmpadwidget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewell.padwidget.callback.LicCallback;
import com.ewell.padwidget.date.DatetimePickerUtil;
import com.ewell.padwidget.dialog.LicDialog;
import com.ewell.padwidget.dialog.WpDialog;
import com.ewell.padwidget.keyboard.security.KeyboardType;
import com.ewell.padwidget.keyboard.security.SecurityConfigure;
import com.ewell.padwidget.keyboard.security.SecurityKeyboard;
import com.ewell.padwidget.popupwindow.MenuPopwindow;
import com.ewell.padwidget.popupwindow.MyPopMenu;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    WpDialog wpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = findViewById(R.id.main_rly);
        SecurityConfigure configure = new SecurityConfigure()
                .setDefaultKeyboardType(KeyboardType.NUMBER)
                .setLetterEnabled(false);
        SecurityKeyboard securityKeyboard = new SecurityKeyboard(relativeLayout, configure);
        final TextView textView = findViewById(R.id.content);
        final String[] texts = {"dfa", "分科分拣"
                , "说的方式范德萨", "e3"};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        setMenuPop(textView, texts, null);
                    }
                });
            }
        });
        thread.start();

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
        //textHintDialog.show();



    }
    /**
     * 设置弹出pop
     */
    public void setMenuPop(View showView, String[] texts, PopupMenu.OnMenuItemClickListener onItemClickListener){
        //MyPopMenu popup=new MyPopMenu(this,showView);
        MyPopMenu popup = null;
        //改变内容风格方法是个activity或者application的主题里添加属性。类似
//        <item name="android:popupMenuStyle">@style/popMenu_style</item>
//        <item name="android:textAppearanceSmallPopupMenu">@style/popMenu_txt_style</item>
//        <item name="android:textAppearanceLargePopupMenu">@style/popMenu_txt_style</item>
        //可以查看style文件
        popup=new MyPopMenu(this,showView);

//        for (int i = 0; i < texts.length; i++) {
//            if(icons.length>i) {
//                popup.getMenu().add(texts[i]).setIcon(icons[i]);
//            }else {
//                popup.getMenu().add(texts[i]);
//            }
//        }
        //将R.menu.popup_menu菜单资源加载到popup菜单中
        getMenuInflater().inflate(R.menu.fkfj_menu,popup.getMenu());

        //为popup菜单的菜单项单击事件绑定事件监听器
        popup.setOnMenuItemClickListener(onItemClickListener);
        //设置菜单图片显示
        popup.setIconEnable(popup.getMenu(), true);
        popup.show();
    }
}
