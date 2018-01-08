package com.huai.gamesdk.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huai.gamesdk.bean.ComDrawableRect;
import com.huai.gamesdk.bean.ConfBean;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.services.IDataService;
import com.huai.gamesdk.services.IDataService.UidType;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameStringTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GamePopupDropdown;
import com.huai.gamesdk.widget.GameSdkButton;
import com.huai.gamesdk.widget.GameSdkFooterLayout;
import com.huai.gamesdk.widget.GameSdkToast;

/***
 * 
 * @author tzw
 * MVP架构:已实现
 *
 */
final class GameLoginActivity extends ActivityUI {
//	private LoginPresenterImp loginPresenterImp;
	private GameSdkButton lgnBtn = null;
	private EditText uidEdtx = null;
	private EditText pwdEdtx = null;
	private ImageView dropDownSel = null;
	private TextView forgetpwdTxvw = null;
	private ImageView clean = null;
	private static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
	Activity me=null;
	String prefix  = "gamesdk/images/";
	GameLoginActivity() {
	}

//	显示View 层    
	@Override
	public LinearLayout onCreate(final Activity activity) {
		me= activity;
//		loginPresenterImp = new LoginPresenterImp();
//		loginPresenterImp.attachView(this);
		
		uidEdtx = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "uid_edtx_hint"),
				INPUT_TYPE , "gamesdk_username.png");
		uidEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		uidEdtx.setKeyListener(new NumberKeyListener() {
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
        //下拉
		LinearLayout.LayoutParams dropDownParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		dropDownParams.setMargins(0, 0, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03), 0);
		dropDownSel = new ImageView(activity);
		dropDownSel.setLayoutParams(dropDownParams);
		dropDownSel.setScaleType(ImageView.ScaleType.CENTER);
		dropDownSel.setImageDrawable(GameAssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"gamesdk_drop.png",0.5f));
        //uid布局包含文本输入框和下拉
		final LinearLayout uidLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true, uidEdtx, dropDownSel);
		//输入密码文本框
		 final int PASS_INPUT_TYPE = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		pwdEdtx = GameUiTool.getInstance().createEdtx(activity, asset.getLangProperty(activity, "pwd_edtx_hint"),
				PASS_INPUT_TYPE, "gamesdk_new_lock.png");
		pwdEdtx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		pwdEdtx.setKeyListener(new NumberKeyListener() {
			@Override
			public int getInputType() {
				return INPUT_TYPE;
			}
			@Override
			protected char[] getAcceptedChars() {
				String string = INPUT_TYPE_CONTENT;
				char[] acceptedChars = string.toCharArray();
				return acceptedChars;
			}
		});
         //忘记密码 
		LinearLayout.LayoutParams fgetPwdParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		fgetPwdParams.setMargins(0, 0, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03),0 );
		forgetpwdTxvw = new TextView(activity);
		forgetpwdTxvw.setLayoutParams(fgetPwdParams);
		forgetpwdTxvw.setPadding(0, 0, 0, 0);
		forgetpwdTxvw.setText(asset.getLangProperty(activity, "login_fgetpwd_txvw"));
		forgetpwdTxvw.setTextColor(Color.rgb(105, 155, 235));
		forgetpwdTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));
		forgetpwdTxvw.setGravity(Gravity.CENTER);
		 //下拉
		clean = new ImageView(activity);
		LinearLayout.LayoutParams cleanparam = new LinearLayout.LayoutParams(-2, -1);
		cleanparam.setMargins(0, 0, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03), 0);
		clean.setLayoutParams(cleanparam);
		clean.setScaleType(ImageView.ScaleType.CENTER);
		clean.setImageDrawable(GameAssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"gamesdk_reset_pwd.png",1.2f));
		//密码的输入框有效
		if(pwdEdtx.getText() != null && pwdEdtx.getText().length() != 0){
			forgetpwdTxvw.setVisibility(View.VISIBLE);
			clean.setVisibility(View.VISIBLE);
		}else{
			clean.setVisibility(View.GONE);
			forgetpwdTxvw.setVisibility(View.INVISIBLE);
		}
		forgetpwdTxvw.setVisibility(View.VISIBLE);
         //加入忘记密码布局中
		LinearLayout pwdLayout = GameUiTool.getInstance().edtxLinearLayout(activity, true, pwdEdtx, forgetpwdTxvw,clean);
		
		//登录按钮
		lgnBtn = new GameSdkButton(activity, asset.getLangProperty(activity, "login_account_btn")) {
			@Override
			public void click(View view) {
				/*MVPLoginBean bean = new MVPLoginBean(uidEdtx.getText().toString(),pwdEdtx.getText().toString());
				loginPresenterImp.login(bean,activity);*/
				if (uidEdtx != null && TextUtils.isEmpty(uidEdtx.getText())) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}
				if(!GameStringTool.isBetween(uidEdtx.getText().toString(), GameSdkConstants.USERNAME_LOGIN_MIN_LEN, GameSdkConstants.USERNAME_LOGIN_MAX_LEN)){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_uid_is_empty"));
					return;
				}			
				if (pwdEdtx != null && TextUtils.isEmpty(pwdEdtx.getText())) {
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				if(!GameStringTool.isBetween(pwdEdtx.getText().toString(), GameSdkConstants.PASSWORD_MIN_LEN, GameSdkConstants.PASSWORD_MAX_LEN)){
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_pwd_is_empty"));
					return;
				}
				try {
					Dispatcher.getInstance().login(activity, uidEdtx.getText(), pwdEdtx.getText());
				} catch (Exception e) {
					GameSdkLog.getInstance().e("提交登录信息异常：", e);
					GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "login_excep_toast"));
				}
			}
		};
//		
        //手机注册
		ConfBean left = new ConfBean();
		left.activity = ActivityFactory.PHONE_REGISTER_ACTIVITY;
		left.text = asset.getLangProperty(activity, "phone_register_activity");
		left.textColor = Color.rgb(128, 128, 128);
		left.textSize = GameUiTool.getInstance().textSize(16F, true);
		left.rect = new ComDrawableRect();
		left.rect.left = GameAssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"gamesdk_register_phone.png",1.0f);
		left.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		
		//账号注册
		ConfBean right = new ConfBean();
		right.activity = ActivityFactory.ACCOUNT_REGISTER_ACTIVITY;
		right.text = " "+asset.getLangProperty(activity, "account_register_activity")+" ";
		right.textColor = Color.rgb(128, 128, 128);
		right.textSize = GameUiTool.getInstance().textSize(16F, true);
		right.rect = new ComDrawableRect();
		right.rect.left = GameAssetTool.getInstance().decodeDrawableFromAsset(activity, prefix+"gamesdk_register_count.png",1.0f);
		right.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		
		GameSdkFooterLayout.Builder builder = new GameSdkFooterLayout.Builder(activity);
		builder.setLeft(left);
		builder.setRight(right);
		addViewListener(activity, uidLayout, pwdLayout,left, right);
		//将所有元素加入到整个布局中，
		return GameUiTool.getInstance().homeparent(activity, builder.build(), uidLayout, pwdLayout, lgnBtn);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		loginPresenterImp.detachView();
	}
	
	   //事件处理   
	private void addViewListener(final Activity activity, 
			final LinearLayout uidLayout,
			final LinearLayout pwdLayout,
			ConfBean left,
			ConfBean right) {

		final IDataService operatData = Dispatcher.getInstance().getIdaoFactory(activity);
		Intent intent = activity.getIntent();
		String uid = intent.getStringExtra("username");
		String pwd = intent.getStringExtra("password");
		String type = intent.getStringExtra("type");

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
//				
				if (view == dropDownSel) {
					try {
						List<JSONObject> list = operatData.readUids(UidType.account);
						if (list == null || list.size() == 0) {
							return;
						}

						new GamePopupDropdown(activity, list, uidEdtx.getHeight()).create(uidLayout, uidEdtx, pwdEdtx);

					} catch (Exception e) {
						GameSdkLog.getInstance().e("[ 账号登录 ] 下拉列表出现异常：", e);
					}
				} else if (view == forgetpwdTxvw) {
					if(forgetpwdTxvw.getText()!=null && forgetpwdTxvw.length()!=0){
						Intent intent = new Intent(activity, GameSdKActivity.class);
						intent.putExtra("layoutId", ActivityFactory.FIND_PWD_ACTIVITY.toString());
					if (!TextUtils.isEmpty(uidEdtx.getText())) {
						intent.putExtra("uid", uidEdtx.getText().toString());
					}
					activity.startActivity(intent);
					activity.finish();
					}else{
						pwdEdtx.setText("");
						forgetpwdTxvw.setGravity(Gravity.CENTER);
						forgetpwdTxvw.setText(asset.getLangProperty(me, "login_fgetpwd_txvw"));
						forgetpwdTxvw.setTextColor(Color.rgb(105, 155, 235));
						forgetpwdTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));
						forgetpwdTxvw.setBackgroundDrawable(null);
					}
				}else if(view == clean){
					pwdEdtx.setText("");
				}
			}
		};

		try {
			JSONObject user = operatData.readCurntUid(UidType.account);
			String username = user.getString("username");
			String password = user.getString("password");
			uidEdtx.setText(username);
			pwdEdtx.setText(password);
			if(type!= null){
			    uidEdtx.setText(uid);
				pwdEdtx.setText(pwd);
				}
		} catch (Exception e) {
			uidEdtx.setText("");
			pwdEdtx.setText("");
		}

		uidEdtx.setSelection(uidEdtx.getText().length());
		dropDownSel.setOnClickListener(clickListener);
		forgetpwdTxvw.setOnClickListener(clickListener);
		clean.setOnClickListener(clickListener);
		pwdEdtx.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if (isFocus) {
					pwdEdtx.setText("");
				}
			}
		});
		
		pwdEdtx.addTextChangedListener(textwatcher);
	}
	
	TextWatcher textwatcher = new TextWatcher(){
		@Override
		public void afterTextChanged(Editable arg0) {
			if(pwdEdtx.getText() != null && pwdEdtx.getText().length() != 0){
				forgetpwdTxvw.setVisibility(View.VISIBLE);
				clean.setVisibility(View.VISIBLE);
			}else{
				forgetpwdTxvw.setVisibility(View.INVISIBLE);
				clean.setVisibility(View.GONE);
			}
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
		}
		
	};
	
	

	
}
