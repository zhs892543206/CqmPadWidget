package com.ewell.padwidget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.ewell.padwidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * popupwindow想设置指定宽度需要布局最外层用match，然后第二层设置指定。然后this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
 * 或者
 * this.setWidth(width);
 *
 *  //String[] texts = {getResources().getString(R.string.slps), "更新当天发放信息", "更新全部发放信息"};
 List<MenuPopwindow.MyMenuPopwindowBean> list = new ArrayList<MenuPopwindow.MyMenuPopwindowBean>();
 MenuPopwindow.MyMenuPopwindowBean bean = null;
 for (int i = 0; i < texts.length; i++) {
 bean = new MenuPopwindow.MyMenuPopwindowBean();
 if(icons.length>i) {
 bean.setIconId(icons[i]);
 }
 bean.setContent(texts[i]);
 list.add(bean);
 }
 */
public class MenuPopwindow extends PopupWindow {
    private View conentView;
    private ListView lvContent;
    private boolean isShowImg = false;
    public MenuPopwindow(Activity context, List<MyMenuPopwindowBean> list, boolean isShowImg) {
        this.isShowImg = isShowImg;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.menu_popwindow, null);
        lvContent = (ListView) conentView.findViewById(R.id.lv_toptitle_menu);
        lvContent.setAdapter(new MyAdapter(context, list));
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//w / 2+8

        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
    }

    public void setOnItemClick(AdapterView.OnItemClickListener myOnItemClickListener) {
        lvContent.setOnItemClickListener(myOnItemClickListener);
    }

    class MyAdapter extends BaseAdapter {
        private List<MyMenuPopwindowBean> list = new ArrayList<>();
        private LayoutInflater inflater;
        public MyAdapter(Context context, List<MyMenuPopwindowBean> list) {
            inflater = LayoutInflater.from(context);
            this.list = list;
        }
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.menu_popwindow_item, null);
                holder = new Holder();
                holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_menu_item);
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_menu_item);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if(holder.ivItem!=null) {
                if(isShowImg){
                    holder.ivItem.setVisibility(View.VISIBLE);
                }else{
                    holder.ivItem.setVisibility(View.GONE);
                }
                if(list.get(position).getIconId()>=0) {
                    holder.ivItem.setImageResource(list.get(position).getIconId());
                }
            }
            if(holder.tvItem!=null) {
                holder.tvItem.setText(list.get(position).getContent());
            }
            return convertView;
        }
        class Holder {
            ImageView ivItem;
            TextView tvItem;
        }
    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }


    /**
     * item的model
     */
    public static class MyMenuPopwindowBean{
        public int iconId;//左侧图片id
        public String content;//item内容

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}