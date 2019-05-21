package com.ewell.padwidget.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import java.lang.reflect.Method;

/**
 * Created by zhs89 on 2019/5/21.
 */

public class MyPopMenu extends PopupMenu{
    public MyPopMenu(Context context, View anchor) {
        super(context, anchor);
    }


    /**
     * 利用反射原理设置菜单图片可见
     * @param menu  popup.getMenu()
     * @param enable
     */
    public void setIconEnable(Menu menu, boolean enable)
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //传入参数
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    public void setTextInfo(Menu menu){
//        //这是一个SpannableStringBuilder 的构造器，不用在意这个，大家可以自己写一个，网上也有好多类似的
//        SpannableStringBuilder builder = SpannableStringUtils.getBuilder(getString(R.string.string_key_527))
//                .setForegroundColor(Color.parseColor("#E64545"))
//                .create();
//
//        //这样就可以更改了id为report的这个menu的字体颜色了
//        menu.findItem(R.id.report).setTitle(builder);
//    }

}
