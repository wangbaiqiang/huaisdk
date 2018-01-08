package com.huai.gamesdk.widget;

import com.huai.gamesdk.activity.MyCouponLay;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 *  private use dialog
 */
public class GameSdkDialog extends Dialog{
	private Activity activity;
	private View view;
	private boolean cancel=false;
	public GameSdkDialog(Activity activity, View view) {
		super(activity);
		this.view = view;
		this.activity = activity;
	}
	public GameSdkDialog(Activity activity2, View viewLay, boolean b) {
		super(activity2);
		this.view = viewLay;
		this.activity = activity2;
		this.cancel=b;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setCanceledOnTouchOutside(cancel);
		setCancelable(cancel);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		setContentView(view);
	}
}
