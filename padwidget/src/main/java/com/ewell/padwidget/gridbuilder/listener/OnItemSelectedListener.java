package com.ewell.padwidget.gridbuilder.listener;

import android.view.View;

import com.ewell.padwidget.gridbuilder.GridItem;

/**
 * Created by EasonX on 15/5/20.
 */
public interface OnItemSelectedListener {

    void onItemSelected(GridItem gridItem, View view, boolean hasFocus);

}
