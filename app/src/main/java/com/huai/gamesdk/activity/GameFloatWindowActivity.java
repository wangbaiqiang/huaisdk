package com.huai.gamesdk.activity;

import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.huai.gamesdk.bean.FloatSendBean;
import com.huai.gamesdk.services.JavaScriptService;
import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameSdkRes;


public class GameFloatWindowActivity extends Activity {
	private ImageView mIvBack;
	private ImageView mIvLeft;
	private WebView mWvMain;
	private String mUrl;
	private String paramStr;
	private TextView mTvTitle;
	private String TAG = "Gamesdk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(GameSdkRes.getRes().getLayoutId(this, "gamesdk_layout_float_ui"));
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		setFinishOnTouchOutside(false);

		initView();
		initData();
		initEvent();
		
		
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(GameCommonTool.getResourceId(this, "iv_back", "id"));
		mIvLeft = (ImageView) findViewById(GameCommonTool.getResourceId(this, "iv_left", "id"));
		mWvMain = (WebView) findViewById(GameCommonTool.getResourceId(this, "wv_main", "id"));
		mTvTitle = (TextView) findViewById(GameCommonTool.getResourceId(this, "gamesdk_float_main", "id"));
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		mUrl = getIntent().getStringExtra("mUrl");
		paramStr = getIntent().getStringExtra("paramStr");
		FloatSendBean floatSendBean = (FloatSendBean) getIntent().getSerializableExtra("floatSendBean");

		mWvMain.getSettings().setJavaScriptEnabled(true);
		mWvMain.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWvMain.getSettings().setBuiltInZoomControls(false);
		mWvMain.getSettings().setUseWideViewPort(true);
		mWvMain.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWvMain.loadData("", "text/html", null);
		mWvMain.setDrawingCacheEnabled(true);
		mWvMain.addJavascriptInterface(new JavaScriptService(this, floatSendBean,
				GameFloatWindowActivity.this), "android");
		mWvMain.loadUrl("www.baidu.com");
		mWvMain.reload();
		mTvTitle.setText("");
		
		mWvMain.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				mWvMain.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				//Log.d(TAG, "监控URL:" + url);
				if (url.contains("isshowback=true")) {
					Log.d(TAG, "设置显示返回按钮");
					mIvLeft.setVisibility(View.VISIBLE);
				}

				if (url.contains("isshowback=false")) {
					Log.d(TAG, "设置隐藏返回按钮");
					mIvLeft.setVisibility(View.GONE);
				}

				String decodeInfo = "utf-8";
				if (url.contains("decodeinfo=")) {
					Log.d(TAG, "设置URL编码信息");
					try {
						decodeInfo = Uri.parse(url).getQueryParameter("decodeinfo");
					} catch (Exception e) {
						Log.d(TAG, "设置URL编码信息出错");
						e.printStackTrace();
					}
				}

				if (url.contains("titlecontent=")) {
					try {
						String titleInfo = Uri.parse(url).getQueryParameter("titlecontent");
						titleInfo = URLDecoder.decode(titleInfo, decodeInfo);
						mTvTitle.setText(titleInfo);
						Log.d(TAG, "设置标题信息:" + titleInfo);
					} catch (Exception e) {
						Log.d(TAG, "解析URL标题信息出错");
						e.printStackTrace();
					}
				}
				if (url.contains("/UserAPP/userinfo")) {
					Log.d(TAG, "浮标：用户中心页面");
					mTvTitle.setText("账户");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/removeBindMobile")) {
					Log.d(TAG, "浮标：变更绑定页面");
					mTvTitle.setText("密保手机");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/bindMobileNum")) {
					Log.d(TAG, "浮标：添加绑定手机");
					mTvTitle.setText("密保手机");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/changeAppPW")) {
					Log.d(TAG, "浮标：设定密码");
					mTvTitle.setText("设定密码");
					mIvLeft.setVisibility(View.VISIBLE);
				} else if (url.contains("/UserAPP/messageNew")) {
					Log.d(TAG, "浮标：消息");
					mTvTitle.setText("消息");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/myGiftBags")) {
					Log.d(TAG, "浮标：进入礼包页面");
					mTvTitle.setText("礼包");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/helpMessage")) {
					Log.d(TAG, "浮标：进入帮助页面");
					mTvTitle.setText("帮助");
					mIvLeft.setVisibility(View.GONE);
				} else if (url.contains("/UserAPP/restartLogin")) {
					Log.d(TAG, "浮标：进入提示页面");
					mTvTitle.setText("提示");
					mIvLeft.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void initEvent() {
		mIvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GameFloatWindowActivity.this.finish();
			}
		});

		mIvLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mWvMain.goBack();
			}
		});
	}
}
