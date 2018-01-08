package com.huai.gamesdk.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;
import com.huai.gamesdk.widget.GameSdkButton;
import com.huai.gamesdk.widget.GameSdkToast;

final class GameCardPayActivity extends ActivityUI {

	GameCardPayActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
		final String cpOrderId = intent.getStringExtra("cpOrderId");
		final String uid = intent.getStringExtra("uid");
		final int price = intent.getIntExtra("price", 0);
		final String gameName = intent.getStringExtra("gameName");
		final String goodsName = intent.getStringExtra("goodsName");
		int textColor = Color.rgb(91, 102, 127);

		int tenWidthPx = GameUiTool.getInstance().widthPixelsPercent(0.008);
		int tenHeightPx = GameUiTool.getInstance().heightPixelsPercent(0.01);

		// 主layout
		LinearLayout cardPayLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams payParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		cardPayLayout.setLayoutParams(payParams);
		cardPayLayout.setPadding(2 * tenWidthPx, 2 * tenHeightPx, 2 * tenWidthPx, tenHeightPx);
		cardPayLayout.setOrientation(LinearLayout.VERTICAL);
		cardPayLayout.setBackgroundColor(Color.rgb(250, 251, 252));

		RelativeLayout title = new RelativeLayout(activity);
		title.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
		title.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_bottom_solid_border_fafbfc"));

		RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		TextView leftTxvw = new TextView(activity);
		leftTxvw.setLayoutParams(leftParams);
		leftTxvw.setPadding(0, 0, 0, (int) (1.5 * tenHeightPx));
		leftTxvw.setText(" " + asset.getLangProperty(activity, "pay_shenzhoufu"));
		leftTxvw.setTextColor(textColor);
		leftTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20, false));
		leftTxvw.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDensityDpiDrawable
				(activity, "gamesdk_pay_card_icon.png"), null, null, null);

		TextView rightTxvw = new TextView(activity);
		rightTxvw.setLayoutParams(rightParams);
		rightTxvw.setPadding(0, 0, 0, (int) (1.5 * tenHeightPx));
		rightTxvw.setText(" " + asset.getLangProperty(activity, "pay_other_type"));
		rightTxvw.setTextColor(Color.rgb(112, 155, 234));
		rightTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20, false));
		rightTxvw.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_money_icon.png"), null, null, null);
		rightTxvw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isFastClick()) {
					return;
				}
				Map<String, String> intent = new HashMap<String, String>();
				intent.put("cpOrderId", cpOrderId);
				intent.put("uid", uid);
				intent.put("price", price + "");
				intent.put("gameName", gameName);
				intent.put("goodsName", goodsName);
				Dispatcher.getInstance().showActivity(activity, ActivityFactory.PAY_ACTIVITY, intent);
			}
		});
		title.addView(leftTxvw);
		title.addView(rightTxvw);

		// 支付信息
		LinearLayout infoLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		infoParams.setMargins(0, (int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.05), 0, (int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.05));
		infoLayout.setOrientation(LinearLayout.HORIZONTAL);
		infoLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
		TextView priceLabelTV = new TextView(activity);
		priceLabelTV.setLayoutParams(params);
		priceLabelTV.setText(asset.getLangProperty(activity, "pay_card_price_label"));
		priceLabelTV.setTextColor(Color.BLACK);
		priceLabelTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, true));

		final TextView priceTV = new TextView(activity);
		priceTV.setText(String.format(asset.getLangProperty(activity, "pay_card_price"), (price / 100.0)));
		priceTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(26F, false));
		priceTV.setTextColor(Color.parseColor("#FF6600"));

		infoLayout.addView(priceLabelTV);
		infoLayout.addView(priceTV, params);

		LinearLayout body = new LinearLayout(activity);
		LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		bodyParams.setMargins(25 * tenWidthPx, 0, 25 * tenWidthPx, 0);
		body.setLayoutParams(bodyParams);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.CENTER_HORIZONTAL);

		
		RadioGroup.LayoutParams groupParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.FILL_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
		groupParams.setMargins(0, tenHeightPx, 0, 0);

		final RadioGroup firstGroup = new RadioGroup(activity);
		firstGroup.setLayoutParams(groupParams);
		firstGroup.setPadding(0, 0, 0, 0);
		firstGroup.setGravity(Gravity.CENTER);
		firstGroup.setOrientation(LinearLayout.HORIZONTAL);

		RadioButton tenRBtn = createMoneyRadio(activity, 10);
		RadioButton twentyRBtn = createMoneyRadio(activity, 20);
		RadioButton thirtyRBtn = createMoneyRadio(activity, 30);

		firstGroup.addView(tenRBtn);
		firstGroup.addView(radioBorder(activity));
		firstGroup.addView(twentyRBtn);
		firstGroup.addView(radioBorder(activity));
		firstGroup.addView(thirtyRBtn);

		final RadioGroup secondGroup = new RadioGroup(activity);
		secondGroup.setLayoutParams(groupParams);
		secondGroup.setPadding(0, 0, 0, 0);
		secondGroup.setGravity(Gravity.CENTER);
		secondGroup.setOrientation(LinearLayout.HORIZONTAL);

		RadioButton fiftyRBtn = createMoneyRadio(activity, 50);
		RadioButton handredRBtn = createMoneyRadio(activity, 100);
		RadioButton threeHandredRBtn = createMoneyRadio(activity, 300);

		secondGroup.addView(fiftyRBtn);
		secondGroup.addView(radioBorder(activity));
		secondGroup.addView(handredRBtn);
		secondGroup.addView(radioBorder(activity));
		secondGroup.addView(threeHandredRBtn);

		RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton firstRadio = (RadioButton) firstGroup.findViewById(checkedId);
				RadioButton secondRadio = (RadioButton) secondGroup.findViewById(checkedId);
				if (group == firstGroup) {
					if (secondGroup.getCheckedRadioButtonId() != -1 && firstRadio != null && firstRadio.isChecked()) {
						secondGroup.clearCheck();
					}
				} else if (secondGroup == group) {
					if (firstGroup.getCheckedRadioButtonId() != -1 && secondRadio != null && secondRadio.isChecked()) {
						firstGroup.clearCheck();
					}
				}

				if (firstRadio != null && firstRadio.isChecked()) {
					priceTV.setText(String.format(asset.getLangProperty(activity, "pay_card_price"), firstRadio.getContentDescription()));
				}

				if (secondRadio != null && secondRadio.isChecked()) {
					priceTV.setText(String.format(asset.getLangProperty(activity, "pay_card_price"), secondRadio.getContentDescription()));
				}

			}
		};

		firstGroup.setOnCheckedChangeListener(checkedChangeListener);
		secondGroup.setOnCheckedChangeListener(checkedChangeListener);

		int showPrice = price / 100;
		if (showPrice <= 10) {
			firstGroup.check(tenRBtn.getId());
		} else if (showPrice <= 20) {
			firstGroup.check(twentyRBtn.getId());
		} else if (showPrice <= 30) {
			firstGroup.check(thirtyRBtn.getId());
		} else if (showPrice <= 50) {
			secondGroup.check(fiftyRBtn.getId());
		} else if (showPrice <= 100) {
			secondGroup.check(threeHandredRBtn.getId());
		} else {
			secondGroup.check(threeHandredRBtn.getId());
		}

		body.addView(firstGroup);
		body.addView(secondGroup);

		// 类型、序列号、密码
		final EditText accountEdtx = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "pay_card_no_edtx"),
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER, null);
		accountEdtx.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));
		final EditText passwdEdtxT = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "pay_card_pwd_edtx"),
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER, null);
		passwdEdtxT.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));

		LinearLayout accountLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true, accountEdtx);
		LinearLayout passwdLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true, passwdEdtxT);

		LinearLayout.LayoutParams edtxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		edtxParams.setMargins(0, tenHeightPx, 0, 0);
		accountLayout.setLayoutParams(edtxParams);
		passwdLayout.setLayoutParams(edtxParams);

		body.addView(accountLayout);
		body.addView(passwdLayout);

		TextView hintView = new TextView(activity);
		hintView.setLayoutParams(edtxParams);
		hintView.setText(asset.getLangProperty(activity, "pay_card_hint"));
		hintView.setTextColor(Color.rgb(90, 102, 127));
		hintView.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(15F, true));

		body.addView(hintView);

		// 立即支付按钮
		final GameSdkButton payBtn = new GameSdkButton(activity, asset.getLangProperty(activity, "pay_card_paybtn")) {

			@Override
			public void click(View view) {
				RadioButton priceRBtn = null;

				if (firstGroup.getCheckedRadioButtonId() == -1 && secondGroup.getCheckedRadioButtonId() == -1) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "pay_card_toast_sel_price"));
					return;
				}

				if (firstGroup.getCheckedRadioButtonId() != -1) {
					priceRBtn = (RadioButton) activity.findViewById(firstGroup.getCheckedRadioButtonId());
				}

				if (secondGroup.getCheckedRadioButtonId() != -1) {
					priceRBtn = (RadioButton) activity.findViewById(secondGroup.getCheckedRadioButtonId());
				}

				int cardPrice = Integer.valueOf(priceRBtn.getContentDescription().toString());
				String account = accountEdtx.getText().toString();
				if (account.equals("")) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "pay_card_toast_input_card_no"));
					return;
				}
				String passwd = passwdEdtxT.getText().toString();
				if (passwd.equals("")) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "pay_card_toast_input_card_pwd"));
					return;
				}
				try {
					Dispatcher.getInstance().CardPay(activity, cpOrderId, uid, price, cardPrice, account, passwd);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		body.addView(payBtn);

		cardPayLayout.addView(title);
		cardPayLayout.addView(infoLayout, infoParams);
		cardPayLayout.addView(body);
		return cardPayLayout;

	}

	@Override
	public void onSetWindows(Activity activity) {
		uitool.setFullScreen(activity);
	}

	private RadioButton createMoneyRadio(final Activity activity, int price) {
		String text = "" + price;
		int end = text.length();
		SpannableStringBuilder builder = new SpannableStringBuilder(text + "元");
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 102, 0)), 0, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		final RadioButton radio = new RadioButton(activity);
		radio.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1));
		radio.setPadding(0, 0, 0, 0);
		radio.setText(builder);
		radio.setTextColor(Color.BLACK);
		radio.setGravity(Gravity.CENTER);
		radio.setContentDescription(price + "");
		radio.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		radio.setBackgroundDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_money_uncheck.png"));
		radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					radio.setBackgroundDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_money_checked.png"));
				} else {
					radio.setBackgroundDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_money_uncheck.png"));
				}
			}
		});
		return radio;
	}

	private RadioButton radioBorder(Activity activity) {
		final RadioButton radio = new RadioButton(activity);
		radio.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1));
		radio.setText("  ");
		radio.setGravity(Gravity.CENTER);
		radio.setBackgroundColor(Color.rgb(250, 251, 252));
		radio.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		radio.setEnabled(false);
		return radio;
	}
}
