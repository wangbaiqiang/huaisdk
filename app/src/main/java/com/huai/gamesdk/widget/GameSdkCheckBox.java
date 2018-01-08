package com.huai.gamesdk.widget;

import com.huai.gamesdk.activity.GameSdKActivity;
import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public abstract class GameSdkCheckBox extends LinearLayout implements OnCheckedChangeListener, View.OnClickListener {
	private Activity activity = null;

	private GameAssetTool asset = null;
	private GameUiTool uitool = null;

	public GameSdkCheckBox(Activity activity) {
		super(activity);
		this.activity = activity;
		asset = GameAssetTool.getInstance();
		uitool = GameUiTool.getInstance();
		init(activity);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(activity, GameSdKActivity.class);
		intent.putExtra("layoutId", ActivityFactory.AGREEMENT_ACTIVITY.toString());
		activity.startActivity(intent);
	}

	private void init(Context context) {
		final CheckBox checkbox = new CheckBox(context);
		checkbox.setPadding(0, 0, 0, 0);
		checkbox.setText("");
		checkbox.setGravity(Gravity.CENTER);
		checkbox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		checkbox.setOnCheckedChangeListener(this);
		checkbox.setChecked(true);

		String regText = "  " + asset.getLangProperty(context, "register_agreement_text");
		SpannableStringBuilder builder = new SpannableStringBuilder(regText);
		builder.setSpan(new ForegroundColorSpan(Color.rgb(111, 155, 235)), regText.indexOf("《"), regText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		final Button textBtn = new Button(context);
		textBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		textBtn.setPadding(0, 0, 0, 0);
		textBtn.setText(builder);
		textBtn.setTextColor(Color.rgb(102, 113, 136));
		textBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(14F, true));
		textBtn.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textBtn.setBackgroundColor(Color.rgb(250, 251, 252));
		textBtn.setOnClickListener(this);

		int pdtop = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.025);
		this.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
		this.setGravity(Gravity.LEFT);
		this.setPadding(0, pdtop, 0, 0);
		this.addView(checkbox);
		this.addView(textBtn);
	}

}
