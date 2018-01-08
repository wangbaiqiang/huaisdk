package com.huai.gamesdk.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huai.gamesdk.bean.Coupon;
import com.huai.gamesdk.callback.SdkRequestCallback;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.solid.GameStatusCode;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameSdkLogger;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameOkDialog;
import com.huai.gamesdk.widget.GameSdkDialog;
import com.huai.gamesdk.widget.GameSdkToast;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.ulopay.android.h5_library.manager.CheckOderManager;

final class GamePayActivity extends ActivityUI implements SdkRequestCallback {
	private OnClickListener paytypeListener = null;
	private boolean isUnionWeixinload = false;
	private static Integer payType = 0;
	private String weixin_pre_id;
	private long needMoney = 0;// 分
	private Coupon chooseCoupon;
	private Activity activity;
	private String toastMsg = "";
	private boolean hasCoupon = false;
	private View alipayBtn;
	private View unionBtn;
	private View weixinunionBtn;
	private View payBtn;
	private LinearLayout couponLay;
	private LinearLayout itemNeedMoney;
    /**订单相关参数*/
	private int price;
	private String cpOrderId ;
	private String uid;
	private String gameName ;
	private String goodsName ;
	GamePayActivity() {
	}
	@Override
	public LinearLayout onCreate(final Activity activity) {
		this.activity = activity;
		Intent intent = activity.getIntent();
		hasCoupon = intent.getBooleanExtra("hasCoupon", true);
		cpOrderId = GameSdkConstants.ORDER_INFO.cpOrderId;
		uid = GameSdkConstants.ORDER_INFO.uid;
		gameName = GameSdkConstants.ORDER_INFO.gameName;
		goodsName = GameSdkConstants.ORDER_INFO.goodsName;
		price = GameSdkConstants.ORDER_INFO.price;
		GameSdkLog.getInstance().i("cpOrderId--->:" + cpOrderId);

		LinearLayout mainLayout = new LinearLayout(activity);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
		LinearLayout.LayoutParams mainLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mainLp.gravity=Gravity.CENTER;
		mainLayout.setLayoutParams(mainLp);
		// 标题栏
		View tiltebar = uitool.createPayTitleBar(activity);
		// 标题的线
		View lineIv = new View(activity);
		lineIv.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, uitool.dp2px(activity, 2)));
		lineIv.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity,
				"gamesdk_pay_line.png"));
		// 总价
		LinearLayout itemPrice = uitool.createLinearPayDesc(activity,
				String.valueOf(price / 100.0f), false);
		View line1 = uitool.createDividerLine(activity, 1);
		// 代金劵的栏
		couponLay = uitool.createLinearCoupon(activity, "0");
		couponLay.findViewWithTag("choseCoupon").setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							Dispatcher.getInstance().getMyCouponCoin(activity,
									uid, GamePayActivity.this);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		View line2 = uitool.createDividerLine(activity, 1);
		// 还需的现金
		needMoney = price;
		itemNeedMoney = uitool.createLinearPayDesc(activity,
				String.valueOf(needMoney / 100.0f), true);
		// 支付方式的层
		LinearLayout payLayout = new LinearLayout(activity);
		if (GameSdkConstants.isPORTRAIT) {
			payLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			payLayout.setOrientation(LinearLayout.HORIZONTAL);
		}
		LinearLayout.LayoutParams paramParent = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		payLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
				activity, "gamesdk_payitem_bg"));
		paramParent.setMargins(GameUiTool.dp2px(activity, 10), 0,
				GameUiTool.dp2px(activity, 10), GameUiTool.dp2px(activity, 6));
		payLayout.setLayoutParams(paramParent);
		LinearLayout paywarpLayout = new LinearLayout(activity);
		paywarpLayout.setOrientation(LinearLayout.HORIZONTAL);
		payLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(
				activity, "gamesdk_payitem_bg"));
		// 支付方式的文字
		TextView paytype = new TextView(activity);
		LinearLayout.LayoutParams paytypeLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if(GameSdkConstants.isPORTRAIT){
			paytypeLayoutParams.setMargins(GameUiTool.dp2px(activity, 8),
					GameUiTool.dp2px(activity, 8), 0, 0);
		}else {
				paytypeLayoutParams.setMargins(GameUiTool.dp2px(activity, 39),
				GameUiTool.dp2px(activity, 8), 0, 0);
		}
		
	
		paytype.setTextColor(Color.BLACK);
		paytype.setTextSize(16);
		paytype.setText("支付方式:  ");
		paytype.setLayoutParams(paytypeLayoutParams);

		alipayBtn = uitool.createPayItemView(activity,
				"gamesdk_pay_type_ali.png", "支付宝");
		unionBtn = uitool.createPayItemView(activity,
				"gamesdk_pay_type_union.png", "银联");
		weixinunionBtn = uitool.createPayItemView(activity,
				"gamesdk_pay_type_wechat.png", "微信");
		
		paytypeListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				choosePayType(v);
			}
		};
		if(GameSdkConstants.isPORTRAIT){
//			支付方式  :竖屏
			LinearLayout layout = new LinearLayout(activity);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams payParams = new LinearLayout.LayoutParams(-1,-1);	
			payParams.setMargins(GameUiTool.dp2px(activity, 26),0, 0, 0);
			layout.setLayoutParams(payParams);
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			layout.addView(weixinunionBtn);
			layout.addView(alipayBtn);
			layout.addView(unionBtn);
			paywarpLayout.addView(layout);
//			支付方式  :竖屏	
		}else{
//			支付方式  :横屏
			paywarpLayout.addView(weixinunionBtn);
			paywarpLayout.addView(alipayBtn);
			paywarpLayout.addView(unionBtn);
		}
		unionBtn.setOnClickListener(paytypeListener);
		alipayBtn.setOnClickListener(paytypeListener);
		weixinunionBtn.setOnClickListener(paytypeListener);

		
		LinearLayout.LayoutParams lpWarpLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpWarpLayoutParams.setMargins(0, GameUiTool.dp2px(activity, 5), 0,
				GameUiTool.dp2px(activity, 0));
		paywarpLayout.setLayoutParams(lpWarpLayoutParams);
		
		payLayout.addView(paytype);
		payLayout.addView(paywarpLayout);
		

		payBtn = uitool.createPayButton(activity, "立  即  支  付");
		payBtn.setSelected(false);

		mainLayout.addView(tiltebar); 
		mainLayout.addView(lineIv);
		mainLayout.addView(itemPrice);
//		代金券
		if (hasCoupon) {
			mainLayout.addView(line1);
			mainLayout.addView(couponLay);
			mainLayout.addView(line2);
			mainLayout.addView(itemNeedMoney);
		}
		mainLayout.addView(payLayout);
		mainLayout.addView(payBtn);

		ScrollView mainScroll = new ScrollView(activity);
		LinearLayout.LayoutParams mainSLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainSLp.gravity=Gravity.CENTER_VERTICAL;
		mainScroll.setLayoutParams(mainSLp);
		mainScroll.addView(mainLayout);
		
		
		LinearLayout mainsLayout = new LinearLayout(activity);
		mainsLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainsLayout.setBackgroundColor(Color.parseColor("#00ffffff"));
		LinearLayout.LayoutParams mainssLp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mainsLayout.setLayoutParams(mainssLp);
		mainsLayout.setGravity(Gravity.CENTER);
		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payBtn.isSelected()) {
					doPayAction(price, needMoney);
				}else {
					GameSdkLog.getInstance().i("payBtn is no Selected" );
				}
			}
		});
		mainsLayout.addView(mainScroll);
		return mainsLayout;
	}
     /**
      * 
      * @param price2
      * @param needMoney2
      */
	protected void doPayAction(long price2, long needMoney2) {
		//直接代金劵
		if (needMoney2==0&&chooseCoupon!=null) {
			try {
				Dispatcher.getInstance().payMyCouponCoin(activity, cpOrderId, uid, price, goodsName,chooseCoupon.id, GamePayActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (needMoney2>0) {
			int couponMoney=0;
			int couponId=0;
			if (chooseCoupon!=null) {
				couponId=chooseCoupon.id;
				couponMoney=chooseCoupon.money;
			}
			if (payType == 3) {
				try {
					Dispatcher.getInstance().AlipaySign(activity, cpOrderId, uid,
							price, goodsName,couponMoney,couponId);
				  } catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (payType == 1) {
				try {
					Dispatcher.getInstance().UnionSign(activity, cpOrderId, uid,
							price, goodsName,couponMoney,couponId);
				  } catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (payType == 2) {
				try {
					Dispatcher.getInstance().WeixinUnionPaySign(activity, cpOrderId, uid,
							price, goodsName,couponMoney,couponId,GamePayActivity.this);
				  } catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void choosePayType(View v) {
		payBtn.setSelected(true);
		if (v == unionBtn) {
			payType = 1;

		} else if (v == alipayBtn) {
			payType = 3;

		} else if (v == weixinunionBtn) {
			payType = 2;

		}
		// 设置背景和文字颜色
		if (payType == 1) {
			setPayItemSelected(unionBtn, true);
		} else {
			setPayItemSelected(unionBtn, false);
		}
		if (payType == 2) {
			setPayItemSelected(weixinunionBtn, true);
		} else {
			setPayItemSelected(weixinunionBtn, false);
		}
		if (payType == 3) {
			setPayItemSelected(alipayBtn, true);
		} else {
			setPayItemSelected(alipayBtn, false);
		}
	}

	public void setPayItemSelected(View v, boolean b) {
		if (b) {
			v.setBackgroundDrawable(asset.decodeDensityDpiDrawable(activity,
					"gamesdk_pay_item_selected.png"));
			TextView tView = (TextView) v.findViewWithTag("payName");
			tView.setTextColor(Color.parseColor("#457DE0"));
		} else {
			v.setBackgroundDrawable(null);
			TextView tView = (TextView) v.findViewWithTag("payName");
			tView.setTextColor(Color.parseColor("#000000"));
		}
	}

	@Override
	public void onActivityResult(final Activity activity, int requestCode,
			int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		if (payType == 1) {
			// 如果respCode参数不为空则走银联支付结果
			onActivityResultUnion(activity, requestCode, resultCode, data);
			return;
		}
		if (payType == 2) {
			// 如果respCode参数不为空则走微信支付结果
			onActivityResultWeixin(activity, requestCode, resultCode, data);
			return;
		}
		if (payType == 3 && requestCode == 3) {
			activity.finish();
			return;
		}
	}
	/**
	 * 银联回调生命周期
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResultUnion(final Activity activity,
			int requestCode, int resultCode, Intent data) {
		String msg = "";
		/* 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消 */
		String str = data.getExtras().getString("pay_result");
		GameAssetTool tool = GameAssetTool.getInstance();
		if (str.equalsIgnoreCase("success")) {
			msg = tool.getLangProperty(activity, "pay_success");
//			热云：银联支付
			TalkingDataAppCpa.onPay(uid, cpOrderId, price, "CNY", "unionpay");
//			热云：微信支付
//			ReYunTrack.setPayment(cpOrderId,"unionpay","CNY",price+0.0f);
			Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_SUCCESS, msg);
			GameSdkLogger.i("Union  pay success");
		} else if (str.equalsIgnoreCase("fail")) {
			msg = tool.getLangProperty(activity, "pay_failure");
			Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_FAIL, msg);
			GameSdkLogger.i("Union  pay failue");
		} else if (str.equalsIgnoreCase("cancel")) {
			// msg = "取消支付";
			if (hasCoupon) {
				setCouponNull();
			}
			Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_CANCEL, "银联支付取消");
			GameSdkLogger.i("Union pay cancel");
			return;
		}
		final GameOkDialog dialog=new GameOkDialog(activity, "银联支付结果通知", msg, true);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					activity.finish();
					return true;
				}
				return false;
			}
		});
		dialog.setOnConfirmClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();
			}
		});
		
		dialog.show();
	}

	/**
	 * 微信支付回调生命周期
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResultWeixin(final Activity activity,
			int requestCode, int resultCode, Intent data) {
		final String respCode = data.getExtras().getString("respCode");
		String respMsg = data.getExtras().getString("respMsg");
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("支付结果");
		StringBuilder temp = new StringBuilder();
		if (respCode.equals("00")) {
			temp.append("支付成功");
		} else if (respCode.equals("02")) {
			temp.append("支付取消");
		} else if (respCode.equals("01")) {
			temp.append("支付失败").append("\n").append("原因:" + respMsg);
		} else if (respCode.equals("03")) {
			temp.append("未知").append("\n").append("原因:" + respMsg);
		}
		final GameOkDialog dialog=new GameOkDialog(activity, "支付结果", temp.toString(), true);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					if (!respCode.equals("02")) {
						activity.finish();
					}else {
						if (hasCoupon) {
							setCouponNull();
						}
					}
					return true;
				}
				return false;
			}
		});
		dialog.setOnConfirmClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (!respCode.equals("02")) {
					activity.finish();
				}else {
					if (hasCoupon) {
						setCouponNull();
					}
				}
				
			}
		});
		dialog.show();
	}

	@Override
	public void onSetWindows(Activity activity) {
		uitool.setCostumeScreen(activity, 0.8f, 0.80f);
	}
	@Override
	public void onResume() {
		GameSdkLog.getInstance().i("onResume callled and call is:" + isUnionWeixinload);
		if (isUnionWeixinload) {
			
			new CheckOderManager().checkState(activity, GameSdkConstants.ULO_WX_URL, weixin_pre_id, new CheckOderManager.QueryPayListener() {
				@Override
				public void getPayState( final String trade_state) {
					// TODO Auto-generated method stub
					toastMsg = "";
					if ("SUCCESS".equalsIgnoreCase(trade_state)) {
						TalkingDataAppCpa.onPay(uid, cpOrderId, price, "CNY", "微信");
//						热云：微信支付
//						ReYunTrack.setPayment(cpOrderId,"weixinpay","CNY",price+0.0f);
						toastMsg = "支付成功";
						GameSdkLogger.i("WeChat pay success");
					} else if ("NOTPAY".equalsIgnoreCase(trade_state)) {
						toastMsg = "未支付";
					} else if ("CLOSED".equalsIgnoreCase(trade_state)) {
						toastMsg = "已关闭";
						GameSdkLogger.i("WeChat pay failue");
					} else if ("PAYERROR".equalsIgnoreCase(trade_state)) {
						toastMsg = "支付失败";
						GameSdkLogger.i("WeChat pay failue");
					}
					
					
					
					
					GameSdkLog.getInstance().i("trade_state:" + trade_state + "Toastmsg:"+ toastMsg);
					if (toastMsg != null && !"".equals(toastMsg)) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								GameSdkToast.getInstance().show(activity,toastMsg);
								if ("SUCCESS".equalsIgnoreCase(trade_state)) {
									Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_SUCCESS, "微信支付成功");
								} else if ("NOTPAY".equalsIgnoreCase(trade_state)) {
									Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_CANCEL, "微信支付取消");
								} else if ("CLOSED".equalsIgnoreCase(trade_state)) {
									Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_FAIL, "微信支付失败");
								} else if ("PAYERROR".equalsIgnoreCase(trade_state)) {
									Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_FAIL, "微信支付失败");
								}
								
								if (toastMsg.equals("支付成功")) {
									activity.finish();
								}
							}

						});

					}
					isUnionWeixinload = false;
				}
			});
			
		}
		GameSdkLog.getInstance().i("on end of onResume callled and call is:" + isUnionWeixinload);
	}

	@Override
	public void onDestroy() {
		GameSdkLog.getInstance().i("----->onDestroy");
	}

	// 请求回调接口的接受
	@Override
	public void callback(String code, String msg) {
		GameSdkLog.getInstance().i(
				"----->callback--->code：" + "" + code + ",msg:" + msg);
		if ("weixin".equals(code)) {
			isUnionWeixinload = true;
			weixin_pre_id = msg;
		} else if ("getAllMyCoupon".equals(code)) {
			showCouponsDialog(msg);
		}
	}

	/**
	 * 显示代金卷的对话框
	 * 
	 * @param msg
	 */
	public void showCouponsDialog(String msg) {
		MyCouponLay viewLay = new MyCouponLay(activity);
		// a:解析请求数据
		ArrayList<Coupon> datas = new ArrayList<Coupon>();
		// 加头部
		Coupon head = new Coupon();
		head.state = 2;
		datas.add(head);
		
		ArrayList<Coupon> dataCoupons = GameCommonTool.getCouponsFromJsonStr(msg);
		sortCouponData(dataCoupons);
		
		if (dataCoupons != null) {
			datas.addAll(dataCoupons);
		}
		// b:填充数据到对话框
		viewLay.addDatas(datas);
		if (chooseCoupon!=null) {
			viewLay.setItemCheckedByCoupon(chooseCoupon.id);
		}else {
			viewLay.setItemChecked(0);
		}
		
		final Dialog cpDialog = new GameSdkDialog(activity, viewLay, true);
		cpDialog.show();
		viewLay.setOnListItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CouponAdapter adapter = (CouponAdapter) parent.getAdapter();
				Coupon dataCoupon = adapter.getItem(position);
				if (dataCoupon.state == 0) {
					chooseCoupon = dataCoupon;
					couponLay.findViewWithTag("money").setVisibility(
							View.VISIBLE);
					TextView tip = (TextView) couponLay.findViewWithTag("tip");
					LinearLayout.LayoutParams lpTiParams = new LinearLayout.LayoutParams(
							0, LayoutParams.WRAP_CONTENT, 1f);
					lpTiParams.setMargins(GameUiTool.dp2px(activity, 10), 0, 0, 0);
					tip.setText("可抵用");//
					tip.setLayoutParams(lpTiParams);
					GameUiTool.setMoneyString(
							(TextView) couponLay.findViewWithTag("money"),
							GameUiTool.getFormatMoney(dataCoupon.money), 3);
					TextView needMoneyTV = (TextView) itemNeedMoney
							.findViewWithTag("money");
					if (price <= dataCoupon.money) {
						needMoney = 0;
						payBtn.setSelected(true);
						GameUiTool.setMoneyString(needMoneyTV, "0.00");
					} else {
						if (payType>0) {
							payBtn.setSelected(true);
						}else {
							payBtn.setSelected(false);
						}
						needMoney = price - dataCoupon.money;
						GameUiTool.setMoneyString(needMoneyTV,
								GameUiTool.getFormatMoney(needMoney));
					}
				} else if (dataCoupon.state == 1) {
					GameSdkToast.getInstance().show(activity, "代金劵被锁定，暂时无法使用");
				} else if (dataCoupon.state == 2) {
					setCouponNull();
				}
				cpDialog.dismiss();
			}
		});
	}
	//清空代金卷
    protected void setCouponNull() {
    	chooseCoupon=null;
		couponLay.findViewWithTag("money").setVisibility(View.GONE);
		TextView tip = (TextView) couponLay.findViewWithTag("tip");
		LinearLayout.LayoutParams lpTiParams = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT, 1f);
		lpTiParams.setMargins(0, 0, 0, 0);
		tip.setLayoutParams(lpTiParams);
		tip.setText("请选择代金劵");//
		if (payType == 0) {
			payBtn.setSelected(false);
		}
		needMoney = price;
		TextView needMoneyTv = (TextView) itemNeedMoney
				.findViewWithTag("money");
		GameUiTool.setMoneyString(needMoneyTv,
				GameUiTool.getFormatMoney(price));
	}
	//排序这个代金卷
	private void sortCouponData(ArrayList<Coupon> datas) {
		Collections.sort(datas,new Comparator<Coupon>() {
			@Override
			public int compare(Coupon lhs, Coupon rhs) {
				if (lhs.state<rhs.state) {
					return  -1;
				}else if (lhs.state==rhs.state) {
					if (lhs.deadLine.getTime()<rhs.deadLine.getTime()) {
						return  -1;
					}else if (lhs.deadLine.getTime()==rhs.deadLine.getTime()) {
						return  0;
					}else {
						return  1;
					}
				}else {
					return 1;
				}
			}
		});
	}
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	Dispatcher.getInstance().listener.callback(GameStatusCode.PAY_CANCEL, "支付取消");
    }
}
