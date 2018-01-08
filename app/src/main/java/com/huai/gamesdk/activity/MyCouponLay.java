package com.huai.gamesdk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.huai.gamesdk.bean.Coupon;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MyCouponLay extends LinearLayout {
	private TextView tiltle;
	private ListView couponList;
	private CouponAdapter adapter;
	private LinearLayout listWrapLayout;
	private boolean hasSetScrollBar=false;
	public MyCouponLay(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
		DrawUI();
		regEvents();
	}

	public MyCouponLay(Context context) {
		this(context, null);
	}

	@SuppressLint("NewApi")
	public void initViews() {
		setOrientation(LinearLayout.HORIZONTAL);
		// setBackgroundDrawable(assetTool.getInstance().decodeDrawableFromAsset(getContext(),
		// "sdk/images/gamesdk_dialog_coupon_left.png", 1.5f));代金券
		setBackgroundColor(Color.parseColor("#00000000"));
		tiltle = new TextView(getContext());
		LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, GameUiTool.dp2px(getContext(), 200));
		tiltle.setGravity(Gravity.CENTER);
		tiltle.setText("代\n\n金\n\n劵");
		tiltle.setTextColor(Color.parseColor("#457DE0"));
		tiltle.setTextSize(20);
		tiltle.setLayoutParams(lpTitle);
		tiltle.setBackgroundDrawable(GameAssetTool.getInstance()
				.decodeDrawableFromAsset(getContext(),
						"gamesdk/images/gamesdk_dialog_coupon_left.png", 2.0f));
		LinearLayout.LayoutParams lpList = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, GameUiTool.dp2px(getContext(), 200));
		lpList.setMargins(GameUiTool.dp2px(getContext(), 4), 0,
				GameUiTool.dp2px(getContext(), 4), 0);
		listWrapLayout = new LinearLayout(getContext());
		couponList = new ListView(getContext());

		couponList.setLayoutParams(lpList);
		couponList.setVerticalScrollBarEnabled(true);
		listWrapLayout.addView(couponList);
		listWrapLayout.setBackgroundDrawable(GameAssetTool.getInstance()
				.decodeDrawableFromAsset(getContext(),
						"gamesdk/images/gamesdk_dialog_coupon_right.png", 2.0f));
		listWrapLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, GameUiTool.dp2px(getContext(), 200)));
		couponList.setDivider(null);
		couponList.setSelector(GameSdkRes.getRes().getDrawableId(getContext(),
				"gamesdk_white_bg"));
		adapter = new CouponAdapter(getContext());
		couponList.setAdapter(adapter);
	
		
		
	}

	

	public void DrawUI() {
		addView(tiltle);
		addView(listWrapLayout);
		
		setListCostumeScrollBar();
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, GameUiTool.dp2px(getContext(), 200));
		setLayoutParams(lParams);
	}

	private void setListCostumeScrollBar() {
		try {
			 Field f = View.class.getDeclaredField("mScrollCache");
			 f.setAccessible(true); Object scrollabilityCache = f.get(couponList);
			 f = f.getType().getDeclaredField("scrollBar"); 
			 f.setAccessible(true);
			 Object scrollBarDrawable = f.get(scrollabilityCache); 
			 f = f.getType().getDeclaredField("mVerticalThumb"); 
			 f.setAccessible(true);
			 Drawable drawable = (Drawable) f.get(scrollBarDrawable); 
			 drawable = getResources().getDrawable(
						GameSdkRes.getRes().getDrawableId(getContext(),
								"gamesdk_scroll_bar"));
			 f.set(scrollBarDrawable, drawable); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void regEvents() {
	}

	public void addData(Coupon coupon) {
		adapter.add(coupon);
	}

	public void addDatas(ArrayList<Coupon> coupons) {
		adapter.addAll(coupons);
	}

	public void setItemChecked(int pos) {
		adapter.setItemChecked(pos);
	}

	public void setOnListItemClickListener(OnItemClickListener listener) {
		couponList.setOnItemClickListener(listener);
	}

	public void setItemCheckedByCoupon(int id) {
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).id == id) {
				if (adapter.getItem(i).state != 0) {
					pos = 0;
				} else {
					pos = i;
				}
				break;
			}
		}
		adapter.setItemChecked(pos);
	}
}
