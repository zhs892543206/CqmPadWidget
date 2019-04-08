package com.ewell.padwidget.table;


import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.ewell.padwidget.R;


/**
 * 动态tablerow，支持多行多列
 *
 * @author zhs
 */
public class DynamicTableLy extends GridLayout {

    private Context mContext;

    public DynamicTableLy(Context context) {
        this(context, null);
    }

    public DynamicTableLy(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    public void initView() {
        int[][][] tableInfo = {{{0,3, 0,3}, {0,1, 3,1}, {0,1, 4,2}}
                , {{1,2, 3,1}, {1,2, 4,2}}};
        int maxRows = 1;
        for (int i = 0; i < tableInfo.length; i++) {

            for (int j = 0; j < tableInfo[i].length; j++) {
                if (tableInfo[i][j].length == 2) {
                    if (tableInfo[i][j][1] > maxRows) {
                        maxRows = tableInfo[i][j][1];
                    }
                }
            }
        }
        setColumnCount(6);
        setRowCount(3);
//
//        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
//        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, 1.0f);
//        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 6, 1.0f);
//        setLayoutParams(layoutParams);
        for (int i = 0; i < tableInfo.length; i++) {
            int useRow = 0;
            for (int j = 0; j < tableInfo[i].length; j++) {
                if (tableInfo[i][j].length == 4) {
                    View tabRowChildView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_tablerow, null);
                    //View tableRowHeader = tabRowChildView.findViewById(R.id.item_dynamic_tablerow_header);
                    Button tableRowHeader = new Button(mContext);
                    if (j % 2 == 0) {
                        tableRowHeader.setBackgroundResource(R.drawable.bg_tablerow_header1);
                    } else {
                        tableRowHeader.setBackgroundResource(R.drawable.bg_tablerow_header2);
                    }
                    LayoutParams tabRowChildLayoutParams = new LayoutParams(
                            GridLayout.spec(tableInfo[i][j][0], tableInfo[i][j][1])
                            , GridLayout.spec(tableInfo[i][j][2], tableInfo[i][j][3]));
                    useRow+=tableInfo[i][j][1];
//                    tabRowChildLayoutParams.width= GridLayout.LayoutParams.WRAP_CONTENT;
//                    tabRowChildLayoutParams.height=GridLayout.LayoutParams.WRAP_CONTENT;
//					tabRowChildLayoutParams.width = 200;
                    tabRowChildView.setLayoutParams(tabRowChildLayoutParams);
                    addView(tabRowChildView);
                }

            }

        }

//		for(int i=0; i < tableInfo.length; i++){
//			TableRow tableRow = new TableRow(mContext);
//
//			for(int j=0; j < tableInfo[i].length; j++){
//				if(tableInfo[i][j].length==2){
//					View  tabRowChildView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_tablerow, null);
//					View tableRowHeader = tabRowChildView.findViewById(R.id.item_dynamic_tablerow_header);
//					if(j%2==0){
//						tableRowHeader.setBackgroundResource(R.drawable.bg_tablerow_header1);
//					}else{
//						tableRowHeader.setBackgroundResource(R.drawable.bg_tablerow_header2);
//					}
//					TableRow.LayoutParams tabRowChildLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//(TableRow.LayoutParams)tableRow.getLayoutParams();
//					tabRowChildLayoutParams.column = tableInfo[i][j][0];
//					tabRowChildLayoutParams.span = tableInfo[i][j][1];
////					tabRowChildLayoutParams.width = 200;
//					tabRowChildView.setLayoutParams(tabRowChildLayoutParams);
//					tableRow.addView(tabRowChildView);
//				}
//
//			}
//			addView(tableRow);
//		}

    }

}
