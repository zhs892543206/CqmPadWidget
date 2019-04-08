package com.ewell.padwidget.gridbuilder.calculator;


import com.ewell.padwidget.gridbuilder.GridItem;

import java.util.List;

/**
 * Created by EasonX on 16/5/26.
 */
public interface PositionCalculator {

    void calculate(List<? extends GridItem> gridItemList);

}
