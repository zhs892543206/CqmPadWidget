package com.ewell.padwidget.gridbuilder;

import java.io.Serializable;

public abstract class GridItem implements Serializable, Cloneable {

    public static final String TAG_FIRST_ITEM = "first_grid_item";

    public GridItem() {
    }

    private int rowSpec = 1;

    private int columnSpec = 1;
    private String content;    //显示的内容
    private String status;//状态
    private Object deviceBoxModel;//柜子信息
//    private DeviceBoxModel deviceBoxModel;//柜子信息
    /**
     * 行
     */
    private int row;

    /**
     * 列
     */
    private int column;

    private int width;

    private int height;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getDeviceBoxModel() {
        return deviceBoxModel;
    }

    public void setDeviceBoxModel(Object deviceBoxModel) {
        this.deviceBoxModel = deviceBoxModel;
    }

    public void setRowSpec(int rowSpec) {
        this.rowSpec = rowSpec;
    }

    public int getRowSpec() {
        return rowSpec;
    }

    public void setColumnSpec(int columnSpec) {
        this.columnSpec = columnSpec;
    }

    public int getColumnSpec() {
        return columnSpec;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * 获取列
     */
    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    /**
     * 获取行
     */
    public int getRow() {
        return row;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "GridItem{" +
                "rowSpec=" + rowSpec +
                ", columnSpec=" + columnSpec +
                ", row=" + row +
                ", column=" + column +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
