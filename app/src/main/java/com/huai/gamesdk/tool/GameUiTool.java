package com.huai.gamesdk.tool;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.huai.gamesdk.activity.GameSdKActivity;
import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.bean.ComDrawableRect;
import com.huai.gamesdk.bean.ConfBean;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.solid.GameStatusCode;
import com.huai.gamesdk.widget.GameLoadingLay;
import com.huai.gamesdk.widget.GameSdkDialog;
import com.huai.gamesdk.widget.GameSdkHeadererLayout;

public final class GameUiTool {
	private static final int INVALID = -1;
	private static GameUiTool instance = null;

	/** 读取asset资源 */
	private GameAssetTool asset;

	/** 记录是否连续点击的时间 */
	private long lastClickTime;

	private GameUiTool() {
		asset = GameAssetTool.getInstance();
	}

	public int heightPixelsPercent(double percent) {
		return (int) (GameSdkConstants.DEVICE_INFO.heightPixels * percent);
	}

	public int widthPixelsPercent(double percent) {
		return (int) (GameSdkConstants.DEVICE_INFO.widthPixels * percent);
	}

	/**
	 * 以480作为参考，计算字号
	 * 
	 * @param refersize
	 * @param isSmall
	 *            是否小字体
	 * @return
	 */
	public float textSize(float refersize, boolean isSmall) {
		float textSize = 0;

		if (GameSdkConstants.DEVICE_INFO.densityDpi < 480) {
			textSize = refersize
					- (float) (Math
							.ceil(480 / GameSdkConstants.DEVICE_INFO.densityDpi) * 2);

		} else if (GameSdkConstants.DEVICE_INFO.densityDpi == 480) {
			textSize = refersize;
		} else {
			textSize = refersize
					+ (float) (Math
							.ceil(GameSdkConstants.DEVICE_INFO.densityDpi / 480) * 2);
		}

		if (GameSdkConstants.DEVICE_INFO.densityDpi >= 480 && isSmall) {
			return textSize - 2;
		}
		return textSize;
	}

	public boolean isFastClick() {
		long now = System.currentTimeMillis();
		long timediff = now - lastClickTime;
		lastClickTime = now;
		if (timediff < 1000) {
			return true;
		}
		return false;
	}

	/**
	 * 账号登录logo 界面显示中的标题部分 宽度包裹内容，竖直填充父元素权重为1 无内边距，靠左居中对齐
	 * 
	 * @param context
	 * @return
	 */
	public LinearLayout SDKlogo(Context context) {
		LinearLayout parent = new LinearLayout(context);
		parent.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
				-2, -1);
		parentParams.setMargins(0, 0, 0, 0);
		parent.setLayoutParams(parentParams);
		parent.setPadding(0, 0, 0, 0);
		parent.setGravity(Gravity.CENTER);
		TextView logoView = new TextView(context);
		logoView.setLayoutParams(new LayoutParams(-2, -2));
		parent.addView(logoView);
		return parent;
	}

	public GameSdkHeadererLayout getTitle(Context activity, String title) {
		ConfBean left = new ConfBean();
		left.activity = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY;
		left.text = asset.getLangProperty(activity, "back_to_account_login");
		left.textColor = Color.BLACK;
		left.textSize = GameUiTool.getInstance().textSize(17F, true);
		left.rect = new ComDrawableRect();
		left.rect.left = GameAssetTool.getInstance().decodeDensityDpiDrawable(
				activity, "gamesdk_back_to_login.png");
		left.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();

		ConfBean center = new ConfBean();
		center.activity = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY;
		center.text = asset.getLangProperty(activity, title);
		center.textColor = Color.BLACK;
		center.textSize = GameUiTool.getInstance().textSize(17F, true);
		center.extra = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		GameSdkHeadererLayout header = new GameSdkHeadererLayout.Builder(activity)
				.setLeft(left).setCenter(center).build();
		return header;
	}

	public LinearLayout getEditTextLine(Activity activity, String title,
			LinearLayout comlayout) {
		TextView tv = new TextView(activity);
		tv.setText(asset.getLangProperty(activity, title));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance()
				.textSize(15F, false));
		tv.setTextColor(Color.BLACK);
		LinearLayout edtxLayout = GameUiTool.getInstance().creatLayout(activity);
		edtxLayout.addView(tv);
		edtxLayout.addView(comlayout);
		return edtxLayout;
	}

	/**
	 * 账号登录页面整合body和logo成一个布局， 再与footer整合成最终页面
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param body
	 *            主体
	 * @param footer
	 *            脚部
	 * @return
	 */
	public LinearLayout homeLinearLayout(Context context, LinearLayout title,
			LinearLayout body, LinearLayout footer) {
		// 将title(logo)和body合并成一个layout
		LinearLayout commonlayout = new LinearLayout(context);
		commonlayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -2,
				2.0F);
		commonlayout.setLayoutParams(param);
		if (title != null) {
			commonlayout.addView(title);
		}
		if (body != null) {
			commonlayout.addView(body);
		}
		LinearLayout parent = new LinearLayout(context);
		parent.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context,
				"gamesdk_corner_windows"));
		parent.setOrientation(LinearLayout.VERTICAL);
		parent.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 2));
		int left = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03);
		int right = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03);
		int top = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.056);
		int bottom = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.056);

		parent.setPadding(left, top, right, bottom);
		if (commonlayout != null) {
			parent.addView(commonlayout);
		}
		LinearLayout footerlayout = new LinearLayout(context);
		parent.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context,
				"gamesdk_corner_windows"));
		parent.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
		footerlayout.setOrientation(LinearLayout.HORIZONTAL);
		params.gravity = Gravity.CENTER;
		params.setMargins(0, 0, 0, 0);
		footerlayout.setLayoutParams(params);
		if (footer != null) {
			footerlayout.addView(footer);
		}
		LinearLayout main = new LinearLayout(context);
		main.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context,
				"gamesdk_corner_windows"));
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		main.addView(parent);
		main.addView(footerlayout);
		return main;
	}

	/**
	 * 账号登录页面 这里是创建右边的输入框布局 宽度包裹内容，高度填充父元素 内部元素竖直布局，中间对齐
	 * 
	 * @param context
	 * @param footer
	 * @param bodyViews
	 * @return
	 */
	public LinearLayout homeparent(Context context, LinearLayout footer,
			View... bodyViews) {
		LinearLayout logo = SDKlogo(context);
		LinearLayout body = new LinearLayout(context);
		LinearLayout.LayoutParams bodyLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, -1, 8f);
		bodyLayoutParams.setMargins(0, 0, 0, 0);
		body.setLayoutParams(bodyLayoutParams);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.CENTER_VERTICAL);

		for (int i = 0; i < bodyViews.length; i++) {
			if (bodyViews[i] != null) {
				body.addView(bodyViews[i]);
			}
		}
		return homeLinearLayout(context, logo, body, footer);
	}

	/**
	 * 总体布局
	 * 

	 */
	public LinearLayout parentLinearLayout(Context context, LinearLayout body,
			LinearLayout header) {

		// 定义一个包含上下两个布局的线性布局
		LinearLayout main = new LinearLayout(context);
		main.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context,
				"gamesdk_corner_windows"));
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1f));
		if (header != null) {
			main.addView(header);
		}
		LinearLayout parent = new LinearLayout(context);
		// parent.setBackgroundColor(Color.GREEN);
		parent.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context,
				"gamesdk_corner_windows"));
		parent.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,
				-2, 3f);
		params.setMargins(0, 0, 0, 0);
		parent.setLayoutParams(params);
		int hpadding = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.04);
		// int bottom = (int) (constants.DEVICE_INFO.windowHeightPx * 0.025);
		parent.setPadding(hpadding, 0, hpadding, 0);
		if (body != null) {
			parent.addView(body);
		}
		main.addView(parent);
		return main;
	}

	// 有标题
	public LinearLayout parent(Context context, LinearLayout header,
			View... bodyViews) {
		LinearLayout body = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,
				-1, 7f);
		params.setMargins((int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05),
				0, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05), 0);
		body.setLayoutParams(params);
		body.setOrientation(LinearLayout.VERTICAL);
		body.setGravity(Gravity.CENTER_VERTICAL);
		// body.setBackgroundColor(Color.BLUE);

		for (int i = 0; i < bodyViews.length; i++) {
			if (bodyViews[i] != null) {
				body.addView(bodyViews[i]);
			}
		}
		return parentLinearLayout(context, body, header);
	}

	/**
	 * 输入框中的布局部分
	 * 
	 * @param context
	 *            上下文
	 * @param is4Corner
	 *            是否四个角都圆角
	 * @param views
	 *            添加的
	 * @return
	 */
	public LinearLayout edtxLinearLayout(Context context, boolean is4Corner,
			View... views) {
		LinearLayout edtxLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,
				-2, 1);
		// 下方设置间距
		params.setMargins(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.025), 0, 0);
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.setGravity(Gravity.CENTER_VERTICAL);
		edtxLayout.setPadding(0, 0, 0, 0);
		edtxLayout.setFocusable(false);
		if (is4Corner) {
			edtxLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
					context, "gamesdk_corner_edtx"));
		}
		for (View view : views) {
			if (view != null) {
				edtxLayout.addView(view);
			}
		}

		return edtxLayout;
	}

	/**
	 * 手机注册布局
	 * 
	 * @param context
	 *            上下文
	 * @param is4Corner
	 *            是否四个角都圆角
	 * @param views
	 *            添加的
	 * @return
	 */
	public LinearLayout phoneregLinearLayout(Context context,
			boolean is4Corner, View... views) {
		LinearLayout edtxLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 0,
				1);
		params.setMargins(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.02), 0, 0);
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.setGravity(Gravity.CENTER_VERTICAL);
		edtxLayout.setPadding(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.02), 0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.02));
		edtxLayout.setFocusable(false);
		if (is4Corner) {
			edtxLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
					context, "gamesdk_corner_edtx"));
		}
		for (View view : views) {
			if (view != null) {
				edtxLayout.addView(view);
			}
		}
		return edtxLayout;
	}

	/**
	 * 
	 * 创建手机注册使用的线性布局
	 * 
	 * 
	 */
	public LinearLayout creatLayout(Context activity) {
		LinearLayout edtxLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,
				-2, 1);
		params.setMargins(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.010), 0, 0);
		edtxLayout.setLayoutParams(params);
		edtxLayout.setOrientation(LinearLayout.HORIZONTAL);
		edtxLayout.setGravity(Gravity.CENTER);
		return edtxLayout;

	}

	/**
	 * 创建一个输入框
	 * 
	 * @param activity
	 * @param hint
	 *            输入框提示字符
	 * @param inputType
	 *            输入框输入类型
	 * @param leftIconPath
	 *            左边图标
	 * @return
	 */
	public EditText createEdtx(Activity activity, String hint,
			final int inputType, String leftIcon) {
		int vpadding = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.015);
		int hpadding = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.025);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, -1, 1.0F);
		EditText edtx = new EditText(activity);
		edtx.setLayoutParams(params);
		edtx.setPadding(hpadding, vpadding, hpadding, vpadding);
		edtx.setHint(hint);
		edtx.setHintTextColor(Color.rgb(204, 204, 204));
		edtx.setTextColor(Color.rgb(51, 51, 51));
		edtx.setBackgroundDrawable(null);
		edtx.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(17F, false));
		edtx.setSelection(edtx.getText().length());
		edtx.setInputType(inputType);
		// 配置输入框不切换edtx.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		if (!TextUtils.isEmpty(leftIcon)) {
			edtx.setCompoundDrawablesWithIntrinsicBounds(
					asset.decodeDensityDpiDrawable(activity, leftIcon),
//					GameLoginPictureTools.loadImageFromAsserts(activity, leftIcon),
					 null,
					null, null);
			edtx.setCompoundDrawablePadding((int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.02));
		}
		return edtx;
	}

	/**
	 * 注册页面
	 * 
	 * @param activity
	 * @param right
	 * @param from
	 * @return
	 */
	public LinearLayout regGuidLayout(final Activity activity,
			CheckBox checkbox, final ConfBean right, final ActivityFactory from) {
		int height = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.1);
		LinearLayout linear = new LinearLayout(activity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearParams.setMargins(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.1), 0, 0);
		linear.setLayoutParams(linearParams);
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setGravity(Gravity.BOTTOM);

		LinearLayout leftLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		leftLayout.setLayoutParams(leftParams);
		leftLayout.setGravity(Gravity.LEFT);

		LinearLayout rightLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 2F);
		rightLayout.setLayoutParams(rightParams);
		rightLayout.setGravity(Gravity.RIGHT);

		String regText = " "
				+ asset.getLangProperty(activity, "register_agreement_text");
		SpannableStringBuilder builder = new SpannableStringBuilder(regText);
		builder.setSpan(new ForegroundColorSpan(Color.rgb(111, 155, 235)),
				regText.indexOf("《"), regText.length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		final Button textBtn = new Button(activity);
		textBtn.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, height));
		textBtn.setPadding(0, 0, 0, 0);
		textBtn.setText(builder);
		textBtn.setTextColor(Color.rgb(102, 113, 136));
		textBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(18F, true));
		textBtn.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textBtn.setBackgroundColor(Color.rgb(250, 251, 252));

		final Button backBtn = new Button(activity);
		backBtn.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, height));
		backBtn.setPadding(0, 0, 0, 0);
		backBtn.setText(" " + right.text);
		backBtn.setTextColor(right.textColor);
		backBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(18F, true));
		backBtn.setGravity(Gravity.CENTER_VERTICAL);
		backBtn.setBackgroundColor(Color.rgb(250, 251, 252));
		if (right.rect != null) {
			backBtn.setCompoundDrawablesWithIntrinsicBounds(right.rect.left,
					right.rect.top, right.rect.right, right.rect.bottom);
		}

		View.OnClickListener clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
				if (view == textBtn) {
					Intent intent = new Intent(activity, GameSdKActivity.class);
					intent.putExtra("layoutId",
							ActivityFactory.AGREEMENT_ACTIVITY.toString());
					activity.startActivity(intent);
				} else if (view == backBtn) {
					Dispatcher.getInstance().showActivity(activity,
							right.activity, null);
				}
			}
		};

		textBtn.setOnClickListener(clickListener);
		backBtn.setOnClickListener(clickListener);

		leftLayout.addView(checkbox);
		leftLayout.addView(textBtn);
		rightLayout.addView(backBtn);
		linear.addView(leftLayout);
		linear.addView(rightLayout);
		return linear;
	}

	/**
	 * 找回密码footer
	 * 
	 * @param activity
	 * @return
	 */
	public LinearLayout createFindPwdGuidLayout(final Activity activity) {
		return createFindPwdGuidLayout(activity, false);
	}

	/**
	 * 
	 * 创建radiobutton
	 * 
	 */
	public RadioButton createTypeRadioButton(final Activity activity,
			String text, String value) {
		final RadioButton radio = new RadioButton(activity);
		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.MATCH_PARENT,
				RadioGroup.LayoutParams.MATCH_PARENT, 1);
		radio.setLayoutParams(params);
		radio.setPadding(0, 0, 0, 0);
		radio.setText(text);
		radio.setTextColor(Color.rgb(58, 140, 225));
		radio.setGravity(Gravity.CENTER);
		radio.setContentDescription(value);
		// radio.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		return radio;
	}

	/**
	 * 找回密码footer
	 * 
	 * @param activity
	 * @param isReGetType
	 * @return
	 */
	public LinearLayout createFindPwdGuidLayout(final Activity activity,
			final boolean isReGetType) {
		int height = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.1);
		LinearLayout linear = new LinearLayout(activity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearParams.setMargins(0,
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.1), 0, 0);
		linear.setLayoutParams(linearParams);
		linear.setOrientation(LinearLayout.HORIZONTAL);
		linear.setGravity(Gravity.BOTTOM);

		LinearLayout leftLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		leftLayout.setLayoutParams(leftParams);
		leftLayout.setGravity(Gravity.LEFT);

		LinearLayout rightLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1.5F);
		rightLayout.setLayoutParams(rightParams);
		rightLayout.setGravity(Gravity.RIGHT);

		final Button textBtn = new Button(activity);
		textBtn.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, height));
		textBtn.setPadding(0, 0, 0, 0);
		textBtn.setText(asset
				.getLangProperty(activity, "findpwd_customer_text") + "  ");
		textBtn.setTextColor(Color.rgb(102, 113, 136));
		textBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(18F, true));
		textBtn.setGravity(Gravity.CENTER_VERTICAL);
		textBtn.setBackgroundColor(Color.rgb(250, 251, 252));

		final Button backBtn = new Button(activity);
		backBtn.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, height));
		backBtn.setPadding(0, 0, 0, 0);
		if (isReGetType) {
			backBtn.setText(" "
					+ asset.getLangProperty(activity, "findpwd_back_to_type"));
		} else {
			backBtn.setText(asset.getLangProperty(activity,
					"findpwd_back_to_account"));
		}
		backBtn.setTextColor(Color.rgb(112, 154, 235));
		backBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(18F, true));
		backBtn.setGravity(Gravity.CENTER_VERTICAL);
		backBtn.setBackgroundColor(Color.rgb(250, 251, 252));
		backBtn.setCompoundDrawablesWithIntrinsicBounds(asset
				.decodeDensityDpiDrawable(activity, "gamesdk_back_to_login.png"),
				null, null, null);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
				// 从找回密码验证码跳转会到找回密码绑定类型的界面
				if (isReGetType) {
					try {
						Intent intent = activity.getIntent();
						Dispatcher.getInstance().loadFindPwdType(activity,
								intent.getStringExtra("account"));
					} catch (Exception e) {
						GameSdkLog.getInstance().e("跳转到账号绑定类型异常", e);
					}
				} else { // 正常情况是直接跳转会账号登录界面
					Dispatcher.getInstance().showActivity(activity,
							ActivityFactory.ACCOUNT_LOGIN_ACTIVITY, null);
				}
			}
		});
		leftLayout.addView(textBtn);
		rightLayout.addView(backBtn);
		linear.addView(leftLayout);
		linear.addView(rightLayout);

		return linear;
	}

	/**
	 * 支付页面字段
	 * 
	 * @param activity
	 * @param text
	 * @return
	 */
	public TextView createPayInfoLabel(Activity activity, String text,
			boolean isLabel) {
		TextView label = new TextView(activity);
		if (GameSdkConstants.DEVICE_INFO.densityDpi == 160 && isLabel) {
			label.setLayoutParams(new TableRow.LayoutParams(75, -2));
		} else if (GameSdkConstants.DEVICE_INFO.densityDpi == 160 && !isLabel) {
			label.setLayoutParams(new TableRow.LayoutParams(150, -2));
		} else {
			label.setLayoutParams(new TableRow.LayoutParams(-1, -2));
		}
		label.setPadding(0, 0, 0, 0);
		label.setText(text);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(24F, false));
		label.setTextColor(Color.rgb(111, 114, 120));
		return label;
	}

	public View createPayTitleBar(final Activity activity) {

		RelativeLayout lineBar = new RelativeLayout(activity);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		lineBar.setLayoutParams(lParams);

		TextView title = new TextView(activity);
		title.setText("支付内容");
		title.setTextColor(Color.BLACK);
		title.setTextSize(18);
		RelativeLayout.LayoutParams lPtv = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lPtv.addRule(RelativeLayout.CENTER_IN_PARENT);
		lPtv.setMargins(0, dp2px(activity, 6), 0, dp2px(activity, 6));
		title.setLayoutParams(lPtv);

		RelativeLayout.LayoutParams lPiv = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lPiv.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lPiv.addRule(RelativeLayout.CENTER_VERTICAL);
		LinearLayout layoutClose = new LinearLayout(activity);
		lPiv.setMargins(0, 0, dp2px(activity, 12), 0);
		layoutClose.setGravity(Gravity.CENTER);
		layoutClose.setLayoutParams(lPiv);

		ImageView closeIv = new ImageView(activity);
		closeIv.setImageDrawable(asset.decodeDrawableFromAsset(activity,
				"gamesdk/images/gamesdk_black_close.png", 1f));
		layoutClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Dispatcher.getInstance().listener.callback(
						GameStatusCode.PAY_CANCEL, "支付取消");
				activity.finish();
			}
		});
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(dp2px(activity, 2), dp2px(activity, 2),
				dp2px(activity, 2), dp2px(activity, 2));
		closeIv.setLayoutParams(lp);
		layoutClose.addView(closeIv);

		lineBar.addView(title);
		lineBar.addView(layoutClose);

		return lineBar;
	}


	public LinearLayout createLinearPayDesc(final Activity activity,
			String money, boolean isRemain) {

		LinearLayout lineBar = new LinearLayout(activity);
		lineBar.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (GameSdkConstants.isPORTRAIT) {
			lParams.setMargins(dp2px(activity, 13), dp2px(activity, 6),
					dp2px(activity, 10), dp2px(activity, 6));
		} else {
			lParams.setMargins(dp2px(activity, 40), dp2px(activity, 6),
					dp2px(activity, 40), dp2px(activity, 6));
		}
		lineBar.setLayoutParams(lParams);

		TextView title = new TextView(activity);
		if (isRemain) {
			title.setText("仍需支付：   ");
		} else {
			title.setText("支付金额：   ");
		}
		title.setTextColor(Color.BLACK);
		title.setTextSize(16);
		LinearLayout.LayoutParams lPtv = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lPtv.setMargins(dp2px(activity, 8), 0, 0, 0);
		title.setLayoutParams(lPtv);

		TextView moneyTv = new TextView(activity);
		moneyTv.setTag("money");
		moneyTv.setTextColor(Color.parseColor("#FE886F"));
		moneyTv.setTextSize(16);
		moneyTv.setCompoundDrawablesWithIntrinsicBounds(asset
				.decodeDrawableFromAsset(activity,
						"gamesdk/images/gamesdk_money_yellow.png", 2.0f), null,
				null, null);
		moneyTv.setCompoundDrawablePadding(dp2px(activity, 1));
		setMoneyString(moneyTv, money);

		lineBar.addView(title);
		lineBar.addView(moneyTv);
		return lineBar;
	}

	public LinearLayout createLinearCoupon(Activity activity, String money) {
		LinearLayout lineBar = new LinearLayout(activity);
		lineBar.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(-1,-2);
		if (GameSdkConstants.isPORTRAIT) {
			lParams.setMargins(dp2px(activity, 1), dp2px(activity, 6),
					dp2px(activity, 1), dp2px(activity, 6));
		} else {
			lParams.setMargins(dp2px(activity, 40), dp2px(activity, 6),
					dp2px(activity, 40), dp2px(activity, 6));
		}
		lineBar.setLayoutParams(lParams);

		
		
		TextView title = new TextView(activity);
		title.setText(Html
				.fromHtml("<font color='#00ffffff'>哈</font>代金劵：&nbsp;&nbsp;&nbsp;"));
		title.setTextColor(Color.BLACK);
		title.setTextSize(16);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		if (GameSdkConstants.isPORTRAIT) {
			titleParams.setMargins(0, 0, 0, 0);
		} else {

			titleParams.setMargins(dp2px(activity, 8), 0, 0, 0);
		}

		title.setLayoutParams(titleParams);

		TextView coupon = new TextView(activity);
		coupon.setTag("money");
		coupon.setTextColor(Color.parseColor("#FE886F"));
		coupon.setGravity(Gravity.CENTER);
		coupon.setTextSize(11);
		LinearLayout.LayoutParams lPtv = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lPtv.setMargins(0, 0, 0, 0);
		lPtv.gravity = Gravity.CENTER_VERTICAL;

		coupon.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity,
				"gamesdk_yellow_coupon_bg"));
		coupon.setLayoutParams(lPtv);
		setMoneyString(coupon, money, 3);

		TextView tip = new TextView(activity);
		if(GameSdkConstants.isPORTRAIT){
			LinearLayout.LayoutParams lpTiParams = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f);
			tip.setTag("tip");
			if (Float.valueOf(money) <= 0) {
				lpTiParams.setMargins(0, 0, 0, GameUiTool.dp2px(activity, 1));
				tip.setText("请选择代金劵");//
			} else {
				lpTiParams.setMargins(0, 0, 0, 0);
				tip.setText("可抵用");//
			}

			tip.setTextColor(Color.parseColor("#808184"));
			tip.setLayoutParams(lpTiParams);
			tip.setTextSize(8);
		}else{
			LinearLayout.LayoutParams lpTiParams = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f);
			tip.setTag("tip");
			if (Float.valueOf(money) <= 0) {
				lpTiParams.setMargins(0, 0, 0, GameUiTool.dp2px(activity, 1));
				tip.setText("请选择代金劵");//
			} else {
				lpTiParams.setMargins(0, 0, 0, 0);
				tip.setText("可抵用");//
			}
			tip.setTextColor(Color.parseColor("#808184"));
			tip.setLayoutParams(lpTiParams);
			tip.setTextSize(10);
		}

		TextView choseCoupon = new TextView(activity);
		choseCoupon.setTag("choseCoupon");
		choseCoupon.setText("选择代金劵");//
		choseCoupon.setTextColor(Color.WHITE);
		choseCoupon.setGravity(Gravity.CENTER);
		if (GameSdkConstants.isPORTRAIT) {
			choseCoupon.setTextSize(7);
		} else {
			choseCoupon.setTextSize(13);
		}
		choseCoupon.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
				activity, "gamesdk_blue_coupon_bg"));

		/*
		 * LinearLayout.LayoutParams lPCC = new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		 * lPCC.setMargins(0, 0, dp2px(activity, 20), 0);
		 */

		if (GameSdkConstants.isPORTRAIT) {
			LinearLayout.LayoutParams lPCC = new LinearLayout.LayoutParams(-2,
					dp2px(activity,13));
			lPCC.setMargins(0, 0, dp2px(activity, 1), 0);
			lPCC.gravity = Gravity.CENTER_VERTICAL;
			choseCoupon.setLayoutParams(lPCC);
		} else {
			LinearLayout.LayoutParams lPCC = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			lPCC.setMargins(0, 0, dp2px(activity, 20), 0);
			lPCC.gravity = Gravity.CENTER_VERTICAL;
			choseCoupon.setLayoutParams(lPCC);
		}

		lineBar.addView(title);
		lineBar.addView(coupon);
		lineBar.addView(tip);
		lineBar.addView(choseCoupon);
		if (Float.valueOf(money) <= 0) {
			coupon.setVisibility(View.GONE);
		} else {
			coupon.setVisibility(View.VISIBLE);
		}

		return lineBar;

	}

	public View createPayItemView(Activity activity, String icon, String name) {
		LinearLayout lineBar = new LinearLayout(activity);
		lineBar.setOrientation(LinearLayout.VERTICAL);
		lineBar.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (name.equals("微信")) {
			lParams.setMargins(dp2px(activity, 5), 0, dp2px(activity, 10), 0);
		} else if (name.equals("支付宝")) {
			lParams.setMargins(dp2px(activity, 10), 0, dp2px(activity, 10), 0);
		} else {
			lParams.setMargins(dp2px(activity, 10), 0, 0, 0);
		}
		lineBar.setLayoutParams(lParams);

		ImageView iv = new ImageView(activity);
		iv.setImageDrawable(asset.decodeDrawableFromAsset(activity,
				"gamesdk/images/" + icon, 1f));

		TextView nameTv = new TextView(activity);
		LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpTv.setMargins(0, dp2px(activity, 3), 0, 0);
		nameTv.setTag("payName");
		nameTv.setText(name);
		nameTv.setTextColor(Color.BLACK);
		nameTv.setGravity(Gravity.CENTER);
		nameTv.setTextSize(11);

		lineBar.addView(iv);
		lineBar.addView(nameTv);
		lineBar.setPadding(dp2px(activity, 8), dp2px(activity, 8),
				dp2px(activity, 8), dp2px(activity, 8));

		return lineBar;
	}

	/**
	 * 虚拟币的界面的分割线
	 * 
	 * @param activity
	 * @param text
	 * @return
	 */
	public View createDividerLine(Activity activity, int dip) {
		View line = new View(activity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, dp2px(activity, dip));
		linearParams.setMargins(dp2px(activity, 40), 0, dp2px(activity, 40), 0);
		line.setBackgroundColor(Color.parseColor("#e8e8e8"));
		line.setLayoutParams(linearParams);
		return line;
	}

	/**
	 * 虚拟币的界面的文本
	 * 
	 * @param activity
	 * @param text
	 * @return
	 */
	public View createPayTextWithLine(Activity activity, String html) {
		LinearLayout parentLayout = new LinearLayout(activity);
		LayoutParams paramParent = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		parentLayout.setOrientation(LinearLayout.VERTICAL);
		parentLayout.setLayoutParams(paramParent);

		TextView content = new TextView(activity);
		content.setText(Html.fromHtml(html));
		content.setTag("content");
		content.setTextSize(19f);
		content.setTextColor(Color.parseColor("#747579"));
		LinearLayout.LayoutParams contentparam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		contentparam.setMargins(
				(int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.15),
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.08), 0, 0);
		content.setLayoutParams(contentparam);

		View line = new View(activity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				GameSdkConstants.DEVICE_INFO.densityDpi / 160 * 1);
		linearParams.setMargins(
				(int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.13),
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.02),
				(int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.13), 0);
		line.setBackgroundColor(Color.parseColor("#e8e8e8"));
		line.setLayoutParams(linearParams);

		parentLayout.addView(content);
		parentLayout.addView(line);

		return parentLayout;
	}

	/**
	 * 虚拟币的界面的的按钮
	 * 
	 * @param activity
	 * @param text
	 * @return
	 */
	public View createButtonInGuild(Activity activity, String text) {
		LinearLayout parentLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams paramParent = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		parentLayout.setPadding(
				(int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.1),
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.04),
				(int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.1),
				(int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.04));
		parentLayout.setOrientation(LinearLayout.VERTICAL);
		parentLayout.setLayoutParams(paramParent);

		TextView content = new TextView(activity);
		content.setText(Html.fromHtml(text));
		content.setTextColor(Color.parseColor("#ffffff"));
		content.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance()
				.textSize(22F, false));
		content.setClickable(false);
		LinearLayout.LayoutParams paramContent = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramContent.gravity = Gravity.CENTER_HORIZONTAL;
		content.setLayoutParams(paramContent);

		parentLayout.addView(content);
		parentLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
				activity, "gamesdk_corner_submit_btn"));

		return parentLayout;
	}

	public View createPayButton(Activity activity, String text) {
		LinearLayout parentLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams paramParent = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramParent.setMargins(dp2px(activity, 40), dp2px(activity, 6),
				dp2px(activity, 40), dp2px(activity, 10));
		parentLayout.setOrientation(LinearLayout.VERTICAL);
		parentLayout.setGravity(Gravity.CENTER);
		parentLayout.setLayoutParams(paramParent);

		TextView content = new TextView(activity);
		content.setText(Html.fromHtml(text));
		content.setTextColor(Color.parseColor("#ffffff"));
		content.setTextSize(18);
		content.setClickable(false);
		content.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams paramContent = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramContent.gravity = Gravity.CENTER_HORIZONTAL;
		content.setLayoutParams(paramContent);
		content.setPadding(0, dp2px(activity, 6), 0, dp2px(activity, 6));

		parentLayout.addView(content);
		parentLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
				activity, "gamesdk_corner_submit_btn"));

		return parentLayout;
	}

	/**
	 * 支付类型按钮
	 * 
	 * @param activity
	 * @param image
	 * @param text
	 * @return
	 */
	public LinearLayout createPayTypeButton(Activity activity, String image,
			String text) {
		LinearLayout linear = new LinearLayout(activity);
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				-1, heightPixelsPercent(0.14));
		linearParams.setMargins(0, 3 * heightPixelsPercent(0.013), 0, 0);
		linear.setLayoutParams(linearParams);
		linear.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity,
				"gamesdk_corner_edtx"));
		linear.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout left = new LinearLayout(activity);
		LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1);
		left.setLayoutParams(leftParams);
		left.setGravity(Gravity.CENTER);

		LinearLayout right = new LinearLayout(activity);
		LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1);
		rightParams.setMargins(0, heightPixelsPercent(0.006),
				widthPixelsPercent(0.004), heightPixelsPercent(0.006));
		right.setLayoutParams(rightParams);
		right.setGravity(Gravity.CENTER | Gravity.RIGHT);
		right.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity,
				"gamesdk_left_border"));

		TextView imageView = new TextView(activity);
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		imageParams.setMargins(widthPixelsPercent(0.0167), 0, 0, 0);
		imageView.setLayoutParams(imageParams);
		imageView.setPadding(0, 0, 0, 0);
		imageView.setBackgroundDrawable(asset.decodeDensityDpiDrawable(
				activity, image));
		imageView.setGravity(Gravity.CENTER_VERTICAL);
		left.addView(imageView);

		TextView textView = new TextView(activity);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		textView.setLayoutParams(textParams);
		textView.setText(text);
		textView.setTextColor(Color.rgb(111, 114, 120));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(24F, false));
		textView.setGravity(Gravity.CENTER);
		right.addView(textView);

		linear.addView(left);
		linear.addView(right);
		return linear;
	}

	/**
	 * 设置窗体大小
	 * 
	 * @param activity
	 *            上下文
	 * @param currentOrientantion
	 *            屏幕状态
	 * @param width
	 *            宽度百分比
	 * @param height
	 *            高度百分比
	 */
	public void setFullScreen(Activity activity) {
		WindowManager.LayoutParams wmparams = activity.getWindow()
				.getAttributes();
		if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
			GameSdkLog.getInstance().i("cur windows is LANDSCAPE");
			wmparams.width = GameSdkConstants.DEVICE_INFO.widthPixels;
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = GameSdkConstants.DEVICE_INFO.heightPixels;
			}
		} else {
			GameSdkLog.getInstance().i("cur windows is portira");
			wmparams.width = GameSdkConstants.DEVICE_INFO.heightPixels;
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = GameSdkConstants.DEVICE_INFO.widthPixels;
			}
		}
		wmparams.alpha = 1F;
		wmparams.dimAmount = 0F;
		activity.getWindow().setAttributes(wmparams);
	}

	
	
	
	
	
	/**
	 * 当子类的窗口的大小要自定义时请调用
	 * 
	 * @param activity
	 * @param withPercent
	 *            整个屏幕的宽的百分比（0-1）
	 * @param heightPercent整个屏幕的高的百分比
	 *            （0-1）
	 */
	public void setCostumeScreen(Activity activity, float withPercent,
			float heightPercent) {
		WindowManager.LayoutParams wmparams = activity.getWindow()
				.getAttributes();
		if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
			GameSdkLog.getInstance().i("cur windows is LANDSCAPE");
			wmparams.width = (int) (GameSdkConstants.DEVICE_INFO.widthPixels * withPercent);
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = (int) (GameSdkConstants.DEVICE_INFO.heightPixels * heightPercent);
			}
		} else {
			GameSdkLog.getInstance().i("cur windows is portira");
			wmparams.width = (int) (GameSdkConstants.DEVICE_INFO.heightPixels * withPercent);
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = (int) (GameSdkConstants.DEVICE_INFO.widthPixels * heightPercent);
			}
		}
		wmparams.alpha = 1F;
		wmparams.dimAmount = 0F;
		activity.getWindow().setAttributes(wmparams);
	}
//	登录界面--》输入框
	public EditText createLoginEdtx(Activity activity, String hint,
			final int inputType, String leftIcon) {
		/*int vpadding = (int) (constants.DEVICE_INFO.windowHeightPx * 0.02);
		int hpadding = (int) (constants.DEVICE_INFO.windowWidthPx * 0.03);*/
		int vpadding = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.04);
		int hpadding = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.04);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, -1, 1.0F);
//		params.setMargins(-3, 0, 0, 0);
		EditText edtx = new EditText(activity);
		edtx.setLayoutParams(params);
		edtx.setPadding(hpadding, vpadding, hpadding, vpadding);
		edtx.setHint(hint);
		edtx.setBackgroundColor(Color.argb(0, 255, 255, 255));
		edtx.setHintTextColor(Color.rgb(134,134,134));
		edtx.setTextColor(Color.rgb(51, 51, 51));
		edtx.setBackgroundDrawable(null);
		edtx.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(16F, false));
		edtx.setSelection(edtx.getText().length());
		edtx.setInputType(inputType);
		// 配置输入框不切换edtx.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		if (!TextUtils.isEmpty(leftIcon)) {
			edtx.setCompoundDrawablesWithIntrinsicBounds(
					asset.decodeDensityDpiDrawable(activity, leftIcon), null,
					null, null);
			edtx.setCompoundDrawablePadding((int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.01));
		}
		return edtx;
	}
	public static synchronized GameUiTool getInstance() {
		if (instance == null) {
			instance = new GameUiTool();
		}
		return instance;
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * px转sp 设计图按1280*720 的分辨率也就是 desenty 为2
	 * 
	 * @param context
	 *            上下文
	 * @param pxValue
	 *            px值
	 * @return sp值
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = 2;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * dp转px
	 *
	 * @param context
	 *            上下文
	 * @param dpValue
	 *            dp值
	 * @return px值
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dp
	 *
	 * @param context
	 *            上下文
	 * @param pxValue
	 *            px值
	 * @return dp值
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void setMoneyString(TextView couponA, String money) {
		setMoneyString(couponA, money, 0);
	}

	/**
	 * 
	 * @param couponA
	 * @param money
	 * @param decreasedeltSize
	 *            (unit is sp)
	 */

	public static void setMoneyString(TextView couponA, String money,
			int decreaseDeltSize) {
		String[] moneyArr = money.split("\\.");
		int len = moneyArr.length;
		if (len == 1 || (len == 2 && "00".equals(moneyArr[1]))) {
			Spannable texSpannable = new SpannableString(moneyArr[0] + ".00元");
			texSpannable.setSpan(
					new AbsoluteSizeSpan(sp2px(couponA.getContext(),
							16 - decreaseDeltSize)), 0, moneyArr[0].length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			texSpannable.setSpan(
					new AbsoluteSizeSpan(sp2px(couponA.getContext(), 9)),
					moneyArr[0].length(), moneyArr[0].length() + 3,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			texSpannable.setSpan(
					new AbsoluteSizeSpan(sp2px(couponA.getContext(),
							16 - decreaseDeltSize)), moneyArr[0].length() + 3,
					moneyArr[0].length() + 4,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			couponA.setText(texSpannable);
		} else if (len == 2) {
			couponA.setTextSize(16 - decreaseDeltSize);
			couponA.setText(money + "元");
		}
	}

	/**
	 * This will be called in order to create view, if the given view is not
	 * null, it will be used directly, otherwise it will check the resourceId
	 *
	 * @return null if both resourceId and view is not set
	 */
	public static View getView(Context context, int resourceId, View view) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if (view != null) {
			return view;
		}
		if (resourceId != INVALID) {
			view = inflater.inflate(resourceId, null);
		}
		return view;
	}

	public static Dialog getLoadingDialog(String msg, Context activity) {
		GameLoadingLay viewLay = new GameLoadingLay(activity);
		viewLay.setMsg(msg);
		return new GameSdkDialog((Activity) activity, viewLay, false);
	}

	public static String getFormatMoney(long data) {
		Float money = data / 100.0f;
		DecimalFormat format = new DecimalFormat(".00");

		String moneyStr = format.format(money);
		if (money < 1) {
			moneyStr = "0" + moneyStr;
		} else {

		}

		return moneyStr;
	}
}
