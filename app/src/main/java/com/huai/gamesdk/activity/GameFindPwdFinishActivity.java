package com.huai.gamesdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.widget.GameSdkHeadererLayout;

final class GameFindPwdFinishActivity extends ActivityUI {

	GameFindPwdFinishActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
	    GameSdkHeadererLayout header = GameUiTool.getInstance().getTitle(activity, "findpwd_vcode_btn");
	    LinearLayout Layout = new LinearLayout(activity);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -2,1);
		param.setMargins(0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.15), 0, 0);
		Layout.setLayoutParams(param);
		Layout.setOrientation(LinearLayout.HORIZONTAL);
		Layout.setGravity(Gravity.CENTER);
		TextView mailView = new TextView(activity);
		mailView.setLayoutParams(new LayoutParams(-2, -2));
		try {
			mailView.setBackgroundDrawable(asset.decodeDrawableFromAsset(activity, "gamesdk/images/gamesdk_send_mail.png", 1.2F));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Layout.addView(mailView);
		TextView note = new TextView(activity);
		note.setText(asset.getLangProperty(activity, "findpwd_email_note"));
		note.setTextColor(Color.BLACK);
		note.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, true));
		LinearLayout edtxLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2,2f);
		params.setMargins(0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.10), 0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.25));
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.setGravity(Gravity.CENTER);
		edtxLayout.addView(note);
		return uitool.parent(activity, header,Layout, edtxLayout);
	}
	
}
