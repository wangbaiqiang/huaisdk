package com.huai.gamesdk.activity;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameSdkButton;
import com.huai.gamesdk.widget.GameSdkHeadererLayout;
import com.huai.gamesdk.widget.GameSdkToast;


/**
 *找回密码  选择找回方式
 * 
 */
class GameFindPwdActivity extends ActivityUI {
	private String prefix  = "gamesdk/images/";
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
	GameFindPwdActivity() {
	}
	 EditText findPwdphoneEdtx=null;
	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
	
//		findpwd_vcode_btn 找回密码   头部的view findpwd_vcode_btn
		GameSdkHeadererLayout header = GameUiTool.getInstance().getTitle(activity, "findpwd_vcode_btn");
//		RadioGroup--》radioGroup
		final RadioGroup radioGroup = new RadioGroup(activity);
		radioGroup.setOrientation(RadioGroup.HORIZONTAL);
		LinearLayout edtxLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams radio = new LinearLayout.LayoutParams(-1, -2);
		edtxLayout.setLayoutParams(radio);
		edtxLayout.setGravity(	Gravity.CENTER);
//		-----------addView
		edtxLayout.addView(radioGroup);
		
		
		final RadioButton phoneFiPwdBtn = new RadioButton(activity);
		final RadioButton usrBtn = new RadioButton(activity);
		RadioGroup.LayoutParams radioParams = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		radioParams.setMargins((int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.08), (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05), (int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.08), 0);

//		RadioButton--》findpwd_phone_radio_text1--》手机号
		phoneFiPwdBtn.setLayoutParams(radioParams);
		String text = "  "+asset.getLangProperty(activity, "findpwd_phone_radio_text1");
		phoneFiPwdBtn.setText(text);
		phoneFiPwdBtn.setContentDescription("phone");
		phoneFiPwdBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		phoneFiPwdBtn.setPadding((int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.0125), 0, 0, 0);
		phoneFiPwdBtn.setTextColor(Color.rgb(51, 51, 51));
		phoneFiPwdBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, false));
		phoneFiPwdBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDrawableFromAsset(activity,
				prefix+"gamesdk_radio_uncheck.png",1.2f), null, null, null);
	
//		RadioButton--》findpwd_user_radio_text--》账号
		usrBtn.setLayoutParams(radioParams);
		usrBtn.setText("  "+asset.getLangProperty(activity, "findpwd_user_radio_text"));
		usrBtn.setContentDescription("user");
		usrBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		usrBtn.setPadding((int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.0125), 0, 0, 0);
		usrBtn.setTextColor(Color.rgb(51, 51, 51));
		usrBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(20F, false));
		usrBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDrawableFromAsset(activity,
				prefix+"gamesdk_radio_uncheck.png",1.2f), null, null, null);

//		findPwdUidEdtx 输入手机号/账号，输入框
		final EditText findPwdUidEdtx = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "findpwd_uid_edtx_hint"),
				InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, "");
		findPwdUidEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		findPwdUidEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT+"_";
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});
		findPwdUidEdtx.setVisibility(View.GONE);
		 findPwdphoneEdtx = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "findpwd_phone_edtx_hint"),
				InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL, "");
		findPwdphoneEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(11) });
		
		LinearLayout findPwdLayout = GameUiTool.getInstance().phoneregLinearLayout(activity, true, findPwdUidEdtx,findPwdphoneEdtx);
		
		LinearLayout body = new LinearLayout(activity);
		LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, -2, 0.7f);
		
		bodyLayoutParams.setMargins(GameUiTool.dp2px(activity,40),
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.03), GameUiTool.dp2px(activity,40),
				(int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.03));
		
		body.setLayoutParams(bodyLayoutParams);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.CENTER);
//		--------------addView
		body.addView(findPwdLayout);
		
		phoneFiPwdBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					findPwdUidEdtx.setVisibility(View.GONE);
					findPwdphoneEdtx.setVisibility(View.VISIBLE);
					phoneFiPwdBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().
							decodeDrawableFromAsset(activity, prefix+"gamesdk_radio_checked.png",1.25f), null, null, null);
				} else {
					phoneFiPwdBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().
							decodeDrawableFromAsset(activity, prefix+"gamesdk_radio_uncheck.png",1.25f), null, null, null);
				}
			}
		});
		usrBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					findPwdUidEdtx.setVisibility(View.VISIBLE);
					findPwdphoneEdtx.setVisibility(View.GONE);
					usrBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDrawableFromAsset(activity,
							prefix+"gamesdk_radio_checked.png",1.25f), null, null, null);
				} else {
					usrBtn.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().
							decodeDrawableFromAsset(activity, prefix+"gamesdk_radio_uncheck.png",1.25f), null, null, null);
				}
			}
		});
		radioGroup.addView(phoneFiPwdBtn);
		radioGroup.addView(usrBtn);
		
		radioGroup.check(phoneFiPwdBtn.getId());//默认手机号找回

		final GameSdkButton subBtn = new GameSdkButton(activity, asset.getLangProperty(activity, "findpwd_sub_btn")) {
			@Override
			public void click(View view) {

				RadioButton typeRBtn = (RadioButton) activity.findViewById(radioGroup.getCheckedRadioButtonId());
				try {
					if("phone".equals(typeRBtn.getContentDescription())){
						if (!isPhoneEdtxVlaidata(activity)) {
							return;
						}
						//手机注册将账号和手机号码，传递过去，然后调起发送短信的界面
						if (!TextUtils.isEmpty(findPwdphoneEdtx.getText())) {
						   Dispatcher.getInstance().sendPhoneResetPwdVcode(activity, findPwdphoneEdtx.getText().toString().trim());
						}else{
							GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "findpwd_phone_edtx_note"));
							return;
						}
						
					}else{
						//用户名走原来的流程,传递过去账号，跳转到选择找回密码的类型界面，根据账号绑定情况，选择找回方式(手机找回->发送短信验证码，邮箱找回->发送邮箱)
					   if (!TextUtils.isEmpty(findPwdUidEdtx.getText())) {
						Dispatcher.getInstance().loadFindPwdType(activity, findPwdUidEdtx.getText().toString());
					   }else{
						   GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "findpwd_uid_edtx_hint"));
							return;
					   }
					}
				} catch (Exception e) {
					GameSdkLog.getInstance().e("找回密码时提交账号异常：", e);
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "findpwd_sub_excpt") + e.getMessage());
				}
			}

		};
		subBtn.setMargins(0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.03),0,(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.03));
		body.addView(subBtn);


/*//      底部的坐标图标
		TextView footer = new TextView(activity);
//		客服求助电话
		footer.setText("客服求助电话:010-12306");
		footer.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(14F, false));
		footer.setTextColor(Color.BLACK);
		LinearLayout footerLayout = GameUiTool.getInstance().creatLayout(activity);
//		---------------addView
		footerLayout.addView(footer);*/
		
//		最外层的LinearLayout
		LinearLayout mainLayout = new LinearLayout(activity);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		LinearLayout.LayoutParams mainLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mainLp.gravity=Gravity.CENTER;
		mainLayout.setLayoutParams(mainLp);
//		添加标题
		mainLayout.addView(header);
		mainLayout.addView(edtxLayout);
		mainLayout.addView(body);
//		mainLayout.addView(footerLayout);

		
//		添加一个ScrollView，解决部分机型适配问题
		ScrollView mainScroll = new ScrollView(activity);
		LinearLayout.LayoutParams mainSLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainSLp.gravity=Gravity.CENTER_VERTICAL;
		mainScroll.setLayoutParams(mainSLp);
		mainScroll.addView(mainLayout);

		
//		最终展示的LinearLayout
		LinearLayout mainsLayout = new LinearLayout(activity);
		mainsLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainsLayout.setBackgroundColor(Color.parseColor("#00ffffff"));
		LinearLayout.LayoutParams mainssLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainsLayout.setLayoutParams(mainssLp);	
		mainsLayout.addView(mainScroll);
	
		return mainsLayout;
	}
	/**
	 * 判断手机号输入的情况
	 * 
	 * @param activity
	 * @return
	 */
	private boolean isPhoneEdtxVlaidata(Activity activity) {
		GameSdkToast toast = GameSdkToast.getInstance();

		String phoneNum = findPwdphoneEdtx.getText().toString();
		if (TextUtils.isEmpty(phoneNum)) {
			toast.show(activity, asset.getLangProperty(activity, "phone_num_is_empty"));
			return false;
		}

		if (!Pattern.matches("^[1][3-9][0-9]{9}$", phoneNum)) {
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "phone_num_format_error"));
			return false;
		}

		return true;
	}

}
