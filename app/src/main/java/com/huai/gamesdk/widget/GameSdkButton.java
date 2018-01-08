package com.huai.gamesdk.widget;

import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 自定义按钮控件
 */
public abstract class GameSdkButton extends Button implements View.OnClickListener {
	private Context context = null;
	
	private LinearLayout.LayoutParams params = null;
	public GameSdkButton(Context context, String text) {
		super(context);
		this.context = context;
		int mmtop = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.05);
		
		if (GameSdkConstants.DEVICE_INFO.densityDpi == 160) {
			params = new LinearLayout.LayoutParams(-1, 35, 1);
			this.setPadding(0, 0, 0, 0);
		} else {
			params = new LinearLayout.LayoutParams(-1, -2);
		}
		
		params.setMargins(0, mmtop, 0, 5);
		this.setLayoutParams(params);
		this.setBackgroundColor(Color.rgb(247, 178, 179));

		this.setText(text);
		this.setTextColor(Color.WHITE);
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(22F, false));
		this.setGravity(Gravity.CENTER);
		this.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context, "gamesdk_corner_submit_btn"));
		this.setOnClickListener(this);
		setSelected(true);
	}

	@Override
	public void onClick(View view) {
		if (GameUiTool.getInstance().isFastClick()) {
			return;
		}
		click(view);
	}

	public abstract void click(View view);

	public void clickable(boolean clickable) {
		super.setClickable(clickable);
		if (clickable) {
			this.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context, "gamesdk_corner_submit_btn"));
		} else {
			this.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context, "gamesdk_corner_not_submit_btn"));
		}
	}
	
	public void setMargins(int left, int top, int right, int bottom) {
		params.setMargins(left, top, right, bottom);
	}
}
