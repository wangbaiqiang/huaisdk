package com.huai.gamesdk.activity;

import com.huai.gamesdk.callback.SdkRequestCallback;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameCommonTool;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;


import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameGuildPayActivity extends ActivityUI implements SdkRequestCallback {
	boolean canPay=false;
	View payWayView;
	@Override
	public LinearLayout onCreate(final Activity activity) {
		//获取支付的基本信息
		Intent intent = activity.getIntent();
		final String cpOrderId = intent.getStringExtra("cpOrderId");
		final String uid = intent.getStringExtra("uid");
		final int price = intent.getIntExtra("price", 0);//单位为分
		final String gameName = intent.getStringExtra("gameName");
		final String goodsName = intent.getStringExtra("goodsName");
		
	
		final float myCoin=intent.getFloatExtra("myCoin", 0f);
		if ((price/100.0)<=myCoin) {
			canPay=true;
		}
		//主布局
		LinearLayout  main=new LinearLayout(activity);
		LinearLayout.LayoutParams mainparams=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mainparams.gravity=Gravity.CENTER_VERTICAL;
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(mainparams);
		main.setBackgroundColor(Color.parseColor("#ffffff"));
		//付款详情
		LinearLayout laytitle=new LinearLayout(activity);
		laytitle.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams laytitleparam=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		laytitleparam.setMargins((int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.12), (int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.15),(int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.12), 0);
		laytitle.setLayoutParams(laytitleparam);
		
		TextView title=new TextView(activity);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(23F, false));
		LinearLayout.LayoutParams titleparams=new  LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
		
		titleparams.gravity=Gravity.CENTER_VERTICAL;
		title.setText(asset.getLangProperty(activity, "pay_detail"));
		title.setTextColor(Color.parseColor("#333333"));
		title.setLayoutParams(titleparams);
		
		
		ImageView closeTV=new ImageView(activity);
		LinearLayout.LayoutParams closeparams=new  LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		closeparams.gravity=Gravity.CENTER_VERTICAL;
		closeTV.setBackgroundDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(
				activity, "gamesdk_pay_coupon_close.png"));
		closeTV.setLayoutParams(closeparams);
		closeTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		
		laytitle.addView(title);
		laytitle.addView(closeTV);
		View line1=uitool.createDividerLine(activity, 2);
		View orderView;
		View payWayView;
		View payNumberView;
		View buttonpayView;
		TextView tip=new TextView(activity);
		
		LinearLayout.LayoutParams params=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, (int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.03), 0, 0);
		tip.setGravity(Gravity.CENTER);
		tip.setLayoutParams(params);
		main.removeAllViews();
		
		main.addView(laytitle);
		main.addView(line1);
		LinearLayout  btnLaywrap=new LinearLayout(activity);
		btnLaywrap.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams btnParamw=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		btnLaywrap.setGravity(Gravity.CENTER);
		btnParamw.gravity=Gravity.CENTER_VERTICAL;
		btnLaywrap.setLayoutParams(btnParamw);
		
		LinearLayout  btnLay=new LinearLayout(activity);
		btnLay.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams btnParam=new  LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		btnLay.setLayoutParams(btnParam);
		float fenPrice=(price/100.0f);
		if (canPay) {
			 //订单信息
			 orderView=uitool.createPayTextWithLine(activity, "<font color='#ffffff'  style='display:none;'>哈</font>订单信息：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+goodsName+"</font>");
			 //付款方式
			 payWayView=uitool.createPayTextWithLine(activity, "剩余代金卷：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+ GameCommonTool.subZeroAndDot(String.valueOf(myCoin))+" </font>代金卷");
			 //付数目
			 payNumberView=uitool.createPayTextWithLine(activity, "<font color='#ffffff'>哈哈</font>需付款：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+ GameCommonTool.subZeroAndDot(String.valueOf(fenPrice))+" </font>代金卷");
			 
			
			 buttonpayView=uitool.createButtonInGuild(activity, "确认付款");
			 tip.setTextColor(Color.parseColor("#333333"));
			 tip.setText("(提示：1代金卷=1元)");
			 
			 main.addView(orderView); 
			 main.addView(payWayView); 
			 main.addView(payNumberView); 
		}else {
			 payWayView=uitool.createPayTextWithLine(activity, "剩余代金券：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+ GameCommonTool.subZeroAndDot(String.valueOf(myCoin))+" </font>代金卷");
			 payNumberView=uitool.createPayTextWithLine(activity, "<font color='#ffffff'>哈</font>支付金额：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+ GameCommonTool.subZeroAndDot(String.valueOf(fenPrice))+" </font>元");
			 buttonpayView=uitool.createButtonInGuild(activity, "其他方式支付");
			 
			 tip.setTextColor(Color.parseColor("#FF0000"));
			 tip.setText("代金卷不足。(提示：1代金卷=1元)");
			 
			 
			 main.addView(payWayView); 
			 main.addView(payNumberView);
		}
		
		 btnLay.addView(buttonpayView);
		 btnLay.addView(tip);
		 btnLaywrap.addView(btnLay);
		 main.addView(btnLaywrap);
		
		buttonpayView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//如果可以就进行支付
				if (canPay) {
					try {
						Dispatcher.getInstance().payMyCouponCoin(activity, cpOrderId, uid, price, goodsName,0, GameGuildPayActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					activity.finish();
				}
			}
		});
	
		 LinearLayout mainwrap=new LinearLayout(activity);
		 LayoutParams mainwrapparams=new  LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		 mainwrap.setOrientation(LinearLayout.HORIZONTAL);
		 mainwrap.setLayoutParams(mainwrapparams);
		 mainwrap.setBackgroundColor(Color.parseColor("#ffffff"));
		 mainwrap.addView(main);
		 return mainwrap;
	}
	
	@Override
	public void onSetWindows(Activity activity) {
		uitool.setFullScreen(activity);
	}

	@Override
	public void callback(String code, String msg) {
		if ("couponpay".equals(code)) {
			if (null!=payWayView)//刷新界面
			{
			  TextView contentTV=	(TextView) payWayView.findViewWithTag("content");
			  contentTV.setText(Html.fromHtml("剩余代金卷：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#333333'>"+(Integer.valueOf(msg)/100f)+"代金卷</font>"));
			}
		}
		
	}

}
