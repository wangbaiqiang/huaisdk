package com.huai.gamesdk.widget;


import com.huai.gamesdk.tool.GameCommonTool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GameFloatWindowHideView extends LinearLayout {

	/**
	 * 记录系统状态栏的高度
	 */

	private ImageView hideview;
	private Context context;
	private final int LEFT = 0;
	private final int RIGHT = 1;
	private int leftOrRight;

	public GameFloatWindowHideView(Context _context, int _leftOrRight) {
		super(_context);
		this.leftOrRight = _leftOrRight;
		
		this.context = _context;
		if (_leftOrRight == LEFT) {
			LayoutInflater.from(context).inflate(
					GameCommonTool.getResourceId(context, "gamesdk_layout_hide_float_left",
							"layout"), this);
		} else {
			LayoutInflater.from(context).inflate(
					GameCommonTool.getResourceId(context, "gamesdk_layout_hide_float_right",
							"layout"), this);
		}

		hideview = (ImageView) findViewById(GameCommonTool.getResourceId(context,
				"game_hide_float_iv", "id"));
		setupViews();
	}
	private void setupViews() {
		hideview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GameFloatWindowMgr.removeAllwin(context);
				GameFloatWindowMgr.createBigWindow(context);
				GameFloatWindowMgr.winStatus= GameFloatWindowMgr.WIN_BIG;
			}
		});

	}
	
}
