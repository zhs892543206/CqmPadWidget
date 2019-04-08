package com.ewell.padwidget.receivesort;

import java.io.Serializable;

public class BedSearchModel implements Serializable{

	//private Integer id;
    private InfoModel infoModel;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
	
	private String car_title_html;
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public InfoModel getInfoModel() {
		return infoModel;
	}
	public void setInfoModel(InfoModel infoModel) {
		this.infoModel = infoModel;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getCar_title_html() {
		return car_title_html;
	}
	public void setCar_title_html(String car_title_html) {
		this.car_title_html = car_title_html;
	}

	public class InfoModel{
		private String id;
		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
