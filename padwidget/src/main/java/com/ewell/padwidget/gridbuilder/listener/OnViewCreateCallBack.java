package com.ewell.padwidget.gridbuilder.listener;

import android.view.LayoutInflater;
import android.view.View;

import com.ewell.padwidget.gridbuilder.GridItem;

/**
 * Created by EasonX on 15/11/30.
 * Modified by EasonX on 16/5/24.
 */
public interface OnViewCreateCallBack {

    View onViewCreate(LayoutInflater inflater, View convertView, GridItem gridItem);
    void onClickDeal(GridItem gridItem);
}
