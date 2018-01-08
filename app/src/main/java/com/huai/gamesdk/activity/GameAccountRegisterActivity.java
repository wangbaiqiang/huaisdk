package com.huai.gamesdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameStringTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameSdkButton;
import com.huai.gamesdk.widget.GameSdkCheckBox;
import com.huai.gamesdk.widget.GameSdkHeadererLayout;
import com.huai.gamesdk.widget.GameSdkToast;

final class GameAccountRegisterActivity extends ActivityUI {
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    private String uid="";
	GameAccountRegisterActivity() {
	}

	@Override
	public LinearLayout onCreate(final Activity activity) {
		Intent intent = activity.getIntent();
		uid = intent.getStringExtra("uid");
		String account = intent.getStringExtra("account");
		//用户名
		final EditText usrEdtx = GameUiTool.getInstance().createEdtx(activity,
				asset.getLangProperty(activity, "register_phone_user"),
				INPUT_TYPE,
				"gamesdk_uid_icon.png");
		usrEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		usrEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				char[] acceptedChars = INPUT_TYPE_CONTENT.toCharArray();
				return acceptedChars;
			}
		});
		usrEdtx.setText(account);
		usrEdtx.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus){
					if (usrEdtx != null && TextUtils.isEmpty(usrEdtx.getText())) {
						GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					if(!GameStringTool.isBetween(usrEdtx.getText().toString(), GameSdkConstants.USERNAME_LOGIN_MIN_LEN, GameSdkConstants.USERNAME_LOGIN_MAX_LEN)){
						GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					if(!GameStringTool.isLetterOrNumer(usrEdtx.getText().toString())){
						GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
						return;
					}
					try {
						Dispatcher.getInstance().checkAccount(activity,usrEdtx.getText().toString(), uid);
					} catch (Exception e) {
						GameSdkLog.getInstance().e("注册时提交信息异常：", e);
						GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "register_excep_toast") + e.getMessage());
					}
				}
				
			}
		});
		LinearLayout usrLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true, usrEdtx);
		//密码
		 final int PASS_INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		final EditText regPwdEdtx = GameUiTool.getInstance().createEdtx(activity,
				asset.getLangProperty(activity, "register_pwd_edtx_hint"),
				PASS_INPUT_TYPE,
				"gamesdk_pwd_icon.png");
		regPwdEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		regPwdEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return PASS_INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				char[] acceptedChars = INPUT_TYPE_CONTENT.toCharArray();
				return acceptedChars;
			}
		});
		LinearLayout regPwdLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true,regPwdEdtx);
         //注册按钮
		final GameSdkButton regBtn = new GameSdkButton(activity, asset.getLangProperty(activity, "register_account_comfirm")) {
			@Override
			public void click(View view) {
				GameSdkToast toast = GameSdkToast.getInstance();
				if (usrEdtx != null && TextUtils.isEmpty(usrEdtx.getText())) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!GameStringTool.isBetween(usrEdtx.getText().toString(), GameSdkConstants.USERNAME_LOGIN_MIN_LEN, GameSdkConstants.USERNAME_LOGIN_MAX_LEN)){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!GameStringTool.isLetterOrNumer(usrEdtx.getText().toString())){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if (TextUtils.isEmpty(regPwdEdtx.getText())) {
					toast.show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				
				if(!GameStringTool.isBetween(regPwdEdtx.getText().toString(), GameSdkConstants.PASSWORD_MIN_LEN, GameSdkConstants.PASSWORD_MAX_LEN)){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				//注册时只能是数字和字母
				if(!GameStringTool.isLetterOrNumer(regPwdEdtx.getText().toString())){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "register_pwd_is_null"));
					return;
				}
				try {
					Dispatcher.getInstance().register(activity,usrEdtx.getText().toString(), regPwdEdtx.getText().toString() ,uid);
				} catch (Exception e) {
					GameSdkLog.getInstance().e("用户名注册时提交信息异常：", e);
					toast.show(activity, asset.getLangProperty(activity, "register_excep_toast") + e.getMessage());
				}
			}
		};
		regBtn.setMargins(0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.06), 0, (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.11));
		GameSdkCheckBox checkBox = new GameSdkCheckBox(activity) {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				regBtn.clickable(isChecked);
				if (isChecked) {
					buttonView.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity, "gamesdk_checkbox_checked.png"));
				} else {
					buttonView.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity, "gamesdk_checkbox_uncheck.png"));
				}
			}
		};
		int top = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.02);
		checkBox.setPadding(0, top, 0, 0);
		GameSdkHeadererLayout header = GameUiTool.getInstance().getTitle(activity, "register_account_btn");
		LinearLayout body = new LinearLayout(activity);
		LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(	-1, -1);
		bodyLayoutParams.setMargins(0, 0, 0,(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.01));
		body.setLayoutParams(bodyLayoutParams);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.LEFT);
		body.addView(usrLayout);
		body.addView(regPwdLayout);
//		body.addView(checkBox);
		body.addView(regBtn);
		return uitool.parent(activity,header,body);
	}

}
