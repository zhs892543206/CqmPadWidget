package com.ewell.padwidget.menu;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ewell.cqmpad.R;
import com.ewell.cqmpad.adapter.WpCategorysRvAdapter;
import com.ewell.cqmpad.listener.LySelCallback;
import com.ewell.cqmpad.model.WpCategorys;
import com.ewell.padwidget.edittext.NumberPickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量上架自定义控件
 * 撤销上架在柜子管理里撤销
 * @author zhs
 *
 */
public class LeftMenuRLy extends RelativeLayout {
	private RecyclerView selWpRV;//选择上架物品
	private WpCategorysRvAdapter selWpRvAdapter;
	private NumberPickerView openCabinetNumPick;
	private List<WpCategorys> wpCategorysList = new ArrayList<>();
	private WpCategorys.WpBfxhDetail curWpBfxhDetail;//当前选择的上架物品类型
	private Button sjBtn, resetBtn;
	private Context mContext;

	public WpCategorys.WpBfxhDetail getCurWpBfxhDetail() {
		return curWpBfxhDetail;
	}

	public void setCurWpBfxhDetail(WpCategorys.WpBfxhDetail curWpBfxhDetail) {
		this.curWpBfxhDetail = curWpBfxhDetail;
	}

	public LeftMenuRLy(Context context) {
		this(context, null);

	}

	public LeftMenuRLy(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}
	public LeftMenuRLy(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		initView();
	}
	
	public void initView(){

		View view = LayoutInflater.from(mContext).inflate(R.layout.module_batch_sj, null);
		addView(view);
		sjBtn = (Button)view.findViewById(R.id.module_batch_sj_opencabinet_btn);
		resetBtn = (Button)view.findViewById(R.id.module_batch_sj_resetopencabinet_btn);
		openCabinetNumPick = (NumberPickerView) view.findViewById(R.id.frag_opencabinetnum_numpick);
		selWpRV = (RecyclerView)view.findViewById(R.id.module_batch_sj_selwp_rv);
		RecyclerView.LayoutManager detailLayoutManager = new LinearLayoutManager(mContext);
		selWpRV.setLayoutManager(detailLayoutManager);
		selWpRV.setVerticalScrollBarEnabled(false);//滚动条无论任何情况下都隐藏
		//传入type为2代表批量上架
		selWpRvAdapter = new WpCategorysRvAdapter(mContext, wpCategorysList
				, R.layout.item_cabinet_detail, 2, new LySelCallback() {
			@Override
			public void selectWp(WpCategorys.WpBfxhDetail wpBfxhDetail) {
				curWpBfxhDetail = wpBfxhDetail;
				selWpRvAdapter.notifyDataSetChanged();
			}
		});
		selWpRV.setAdapter(selWpRvAdapter);

	}

	/**
	 * 获取设置的数量
	 * @return
	 */
	public int getOpenNum(){
		if(openCabinetNumPick!=null){
			return openCabinetNumPick.getNumText();
		}
		return -1;
	}

	/**
	 * 设置批量上架开门数量限制
	 * 没放物品且有效的柜子数量
	 */
	public void setOpenNumLimit(int nullBoxNum){
		//默认显示最大能开数量
		openCabinetNumPick.setCurrentNum(0);// 模拟开柜数为空
		openCabinetNumPick.setMinDefaultNum(1);//至少开一个柜门
		//设置最大可开数量
		openCabinetNumPick.setMaxValue(nullBoxNum);//无效柜故障柜子不算入可开柜子fcsjFragCompl.ffcsjFragModel.getBoxNum());
	}

	/**
	 * 设置批量上架数据
	 * @param wpCategorysList
	 */
	public void setAdapterData(List<WpCategorys> wpCategorysList){
		this.wpCategorysList = wpCategorysList;
		selWpRvAdapter.setDatasList(wpCategorysList);
		selWpRvAdapter.notifyDataSetChanged();
	}

	public void setNotify(){
		selWpRvAdapter.notifyDataSetChanged();
	}

	interface BatchSjInf{
		public void clickSj(int boxNum);
		public void clickReset(int boxNum);
	}
}
