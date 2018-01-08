package com.huai.gamesdk.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

final class GameAgreementActivity extends ActivityUI {

	GameAgreementActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		int vpadding = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.05);
		LinearLayout body = new LinearLayout(activity);
		LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(-1, -1, 1);
		body.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_top_bottom_solid_border"));
		body.setPadding(0, vpadding, 0, vpadding);
		body.setGravity(Gravity.CENTER_HORIZONTAL);
		body.setLayoutParams(bodyLayoutParams);
		body.setOrientation(LinearLayout.VERTICAL);

		TextView title = new TextView(activity);
		LinearLayout.LayoutParams titleparam = new LinearLayout.LayoutParams(-1, -2,1);
		titleparam.setMargins(0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.025), 0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.025));
		title.setLayoutParams(titleparam);
		title.setText(asset.getLangProperty(activity, "agreement_title"));
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, false));
		title.setTextColor(Color.rgb(58, 140, 225));
		title.setGravity(Gravity.CENTER_HORIZONTAL);

		TextView content = new TextView(activity);
		InputStream inputStream = null;
		StringBuilder builder = new StringBuilder();
		try {
			inputStream = activity.getResources().getAssets().open("gamesdk/conf/lang/gamesdk_service_agreement_zh_CN");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		content.setText(builder.toString());
		content.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(14F, false));
		content.setTextColor(Color.rgb(90, 102, 127));
		content.setVerticalScrollBarEnabled(true);
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
		body.addView(content);
		LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout header = new LinearLayout(activity);
		footerParams.setMargins((int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05), 0, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05), 0);
		header.setLayoutParams(footerParams);
		header.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		final Button backBtn = new Button(activity);
		LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		backBtn.setLayoutParams(rightParams);
		backBtn.setPadding(0, GameUiTool.getInstance().heightPixelsPercent(0.01), 0, 0);
		backBtn.setText("     ");
		backBtn.setTextColor(Color.rgb(111, 155, 235));
		backBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, false));
		backBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_back_to_login.png"), null, null, null);
		backBtn.setBackgroundColor(Color.rgb(250, 251, 252));

		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.finish();
			}
		});
		header.addView(backBtn);
		header.addView(title);

		return GameUiTool.getInstance().parentLinearLayout(activity, body, header);
	}

	
}
