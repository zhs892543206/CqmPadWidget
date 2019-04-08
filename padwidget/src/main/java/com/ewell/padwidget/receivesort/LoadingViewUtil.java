package com.ewell.padwidget.receivesort;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewell.padwidget.R;

public class LoadingViewUtil {

    public static View showinlack(Activity act,int pic,String msg,View preview, int groupViewId) {
        if (act==null) {
            return null;
        }
        //获取需要插入的布局
        ViewGroup layout = (ViewGroup)act.findViewById(groupViewId);//R.id.showin
        if (layout==null) {
            return null;
        }

        //设置lack
        View loadingView = LayoutInflater.from(act).inflate(R.layout.layout_lack, null, false);
        ImageView img_lack = (ImageView)loadingView.findViewById(R.id.img_lack);
        TextView text_lack = (TextView)loadingView.findViewById(R.id.text_lack);
        img_lack.setImageResource(pic);
        text_lack.setText(msg);

        //删除上次的中心视图
        if (preview!=null) {
            layout.removeView(preview);
        }

        //隐藏其余项目
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            if (i>0) {
                layout.getChildAt(i).setVisibility(View.GONE);
            }
        }
        //添加lack
        layout.addView(loadingView, 1);

        return loadingView;
    }

    public static void showout(Activity act,View viewin, int groupViewId) {
        if (act==null || viewin==null) {
            return;
        }
        //获取需要插入的布局
        ViewGroup layout = (ViewGroup)act.findViewById(groupViewId);
        if (layout==null) {
            return;
        }
        //删除中心视图
        layout.removeView(viewin);
        //显示其余项目
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            if (layout.getChildAt(i).getVisibility()!=View.VISIBLE)
                layout.getChildAt(i).setVisibility(View.VISIBLE);
        }

        //把中心视图置为空
        //		viewin = null;
    }
}
