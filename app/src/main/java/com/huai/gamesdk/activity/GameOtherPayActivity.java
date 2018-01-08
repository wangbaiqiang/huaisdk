package com.huai.gamesdk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.huai.gamesdk.callback.SdkRequestCallback;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameSdkRes;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameSdkToast;
import com.ulopay.android.h5_library.manager.CheckOderManager;


final class GameOtherPayActivity extends ActivityUI implements SdkRequestCallback {
	private boolean isUnionWeixinload = false;
	private static Integer payType = 0;
	private String weixin_pre_id;
	public String ULO_URL;
	private float myCoin;
	private Activity activity;
	private String toastMsg="";//微信回调消息
	GameOtherPayActivity() {
	}
	
	@Override
	public LinearLayout onCreate(final Activity activity) {
		this.activity=activity;
		Intent intent = activity.getIntent();
		final String cpOrderId = GameSdkConstants.ORDER_INFO.cpOrderId;
		final String uid = GameSdkConstants.ORDER_INFO.uid;
		final String gameName = GameSdkConstants.ORDER_INFO.gameName;
		final String goodsName = GameSdkConstants.ORDER_INFO.goodsName;
		final int price = GameSdkConstants.ORDER_INFO.price;
		GameSdkLog.getInstance().i("cpOrderId:"+cpOrderId);
		LinearLayout.LayoutParams payParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		TextView dottedLine = new TextView(activity);
		dottedLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		dottedLine.setText(" ");
		dottedLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(10, false));
		dottedLine.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_bottom_solid_border_e8e9ed"));

		// 主layout
		LinearLayout parentLinearLayout = new LinearLayout(activity);
		parentLinearLayout.setLayoutParams(payParams);
		parentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		parentLinearLayout.setBackgroundColor(Color.rgb(250, 251, 252));
		parentLinearLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_corner_windows"));
		
	
		
		
		
		// 左侧 支付信息layout
		int hpadding = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.05);
		int vpadding = (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.03);
		LinearLayout infoLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(uitool.widthPixelsPercent(0.55), -1);
		infoLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_left_corner"));
		infoLayout.setLayoutParams(infoParams);
		infoLayout.setPadding(hpadding, vpadding, (int) (GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.02), vpadding);
		infoLayout.setOrientation(LinearLayout.VERTICAL);

		TextView infoTitle = new TextView(activity);
		infoTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		infoTitle.setText(" " + asset.getLangProperty(activity, "pay_title"));
		infoTitle.setTextColor(Color.rgb(90, 102, 127));
		infoTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(22F, false));
		infoTitle.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_shcart_icon.png"), null, null, null);

		LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		int tenHeightPx = (int) (GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.0167);

		// 商品名称
		TextView goodsNameTxvw = uitool.createPayInfoLabel(activity, goodsName, true);
		goodsNameTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(28, false));
		goodsNameTxvw.getPaint().setFakeBoldText(true);
		goodsNameTxvw.setPadding(0, 3 * tenHeightPx, 0, 0);

		// uid、gameName、price表格
		TableLayout infoTable = new TableLayout(activity);
		infoTable.setPadding(0, vpadding, 0, vpadding);

		// gameName行
		TableRow gameNameRow = new TableRow(activity);
		gameNameRow.setPadding(0, 2 * tenHeightPx, 0, 2 * tenHeightPx);
		gameNameRow.addView(uitool.createPayInfoLabel(activity, asset.getLangProperty(activity, "pay_game_label"), true));
		gameNameRow.addView(uitool.createPayInfoLabel(activity, gameName, false));

		// uid行
		TableRow uidRow = new TableRow(activity);
		uidRow.setPadding(0, 2 * tenHeightPx, 0, 2 * tenHeightPx);
		uidRow.addView(uitool.createPayInfoLabel(activity, asset.getLangProperty(activity, "pay_uid_label"), true));
		uidRow.addView(uitool.createPayInfoLabel(activity, uid, false));

		// price列
		TableRow priceRow = new TableRow(activity);
		priceRow.setPadding(0, 2 * tenHeightPx, 0, 2 * tenHeightPx);
		priceRow.addView(uitool.createPayInfoLabel(activity, asset.getLangProperty(activity, "pay_price_label"), true));

		TextView priceTV = new TextView(activity);
		priceTV.setText(String.format(asset.getLangProperty(activity, "pay_price_text"), (price / 100.0) + ""));
		priceTV.setTextColor(Color.rgb(255, 102, 0));
		priceTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(30F, false));
		priceRow.addView(priceTV);

		infoTable.addView(gameNameRow, commonParams);
		infoTable.addView(uidRow, commonParams);
		infoTable.addView(priceRow, commonParams);

		infoLayout.addView(infoTitle);
		infoLayout.addView(dottedLine);
		infoLayout.addView(goodsNameTxvw, commonParams);
		infoLayout.addView(infoTable, commonParams);

		// 右边支付方式
		LinearLayout rightLayout = new LinearLayout(activity);
		rightLayout.setLayoutParams(new LinearLayout.LayoutParams(uitool.widthPixelsPercent(0.45), -1, 1));
		rightLayout.setPadding(hpadding, vpadding, 0, vpadding);
		rightLayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout titleLayout = new RelativeLayout(activity);

		TextView payTypeTitle = new TextView(activity);
		RelativeLayout.LayoutParams payTypeTitleParams = new RelativeLayout.LayoutParams(-1, -2);
		payTypeTitleParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		payTypeTitle.setLayoutParams(payTypeTitleParams);
		payTypeTitle.setText(" " + asset.getLangProperty(activity, "pay_type_title"));
		payTypeTitle.setTextColor(Color.rgb(90, 102, 127));
		payTypeTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(22F, false));
		payTypeTitle.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_card_icon.png"), null, null, null);

		final Button closeIBtn = new Button(activity);
		closeIBtn.setPadding(0, 0, 0, 0);
		closeIBtn.setBackgroundDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_pay_icon_close.png"));
		RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		closeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		closeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		titleLayout.addView(payTypeTitle);
		titleLayout.addView(closeIBtn, closeParams);

		LinearLayout payTypeLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams payTypeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		payTypeParams.setMargins(0, 0, 0, 0);
		payTypeLayout.setOrientation(LinearLayout.VERTICAL);
		payTypeLayout.setPadding(0, 0, hpadding, 0);

		TextView rightDottedLine = new TextView(activity);
		rightDottedLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		rightDottedLine.setPadding(0, 0, 100, 0);
		rightDottedLine.setText(" ");
		rightDottedLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, uitool.textSize(10, false));
		rightDottedLine.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_bottom_solid_border_fafbfc"));

		final LinearLayout alipayBtn = uitool.createPayTypeButton(activity, "gamesdk_pay_icon_alipay.png", asset.getLangProperty(activity, "pay_alipay"));
		final LinearLayout unionBtn = uitool.createPayTypeButton(activity, "gamesdk_pay_icon_union.png", asset.getLangProperty(activity, "pay_union"));
		final LinearLayout xunibiBtn = uitool.createPayTypeButton(activity, "gamesdk_coin.png", "代金卷");
		final LinearLayout weixinpayBtn = uitool.createPayTypeButton(activity, "gamesdk_pay_icon_weixinpay.png", asset.getLangProperty(activity, "pay_wechat"));
		final LinearLayout weixinunionBtn = uitool.createPayTypeButton(activity, "gamesdk_pay_icon_weixinpay.png", asset.getLangProperty(activity, "pay_wechat"));
		
		
		//排序图标
		payTypeLayout.addView(xunibiBtn);
		payTypeLayout.addView(alipayBtn);
//		payTypeLayout.addView(weixinpayBtn);
		payTypeLayout.addView(weixinunionBtn);
		payTypeLayout.addView(unionBtn);
		
		

		rightLayout.addView(titleLayout, payParams);
		rightLayout.addView(rightDottedLine);
		rightLayout.addView(payTypeLayout, payTypeParams);

		parentLinearLayout.addView(infoLayout);
		parentLinearLayout.addView(rightLayout);

		
		
		closeIBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.finish();
			}
		});

		View.OnClickListener clickListener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (isFastClick()) {
					return;
				}
				if (view == alipayBtn) {
					try {
//						Dispatcher.getInstance().AlipaySign(activity, cpOrderId, uid, price, goodsName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (view == unionBtn) {
					payType = 1;
					try {
//						Dispatcher.getInstance().UnionSign(activity, cpOrderId, uid, price, goodsName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (view == xunibiBtn) {
					try {
						payType = 3;
//						Dispatcher.getInstance().getMyCouponCoin(activity, uid, YPayActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
//					activity.finish();
				}else if (view == weixinunionBtn){
					try {
//						Dispatcher.getInstance().WeixinUnionPaySign(activity, cpOrderId, uid, price, goodsName,YPayActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}	
					
			   }
				else if (view == weixinpayBtn){
						payType = 2;
						try {
							Dispatcher.getInstance().WeixinPaySign(activity, cpOrderId, uid, price, goodsName);
						} catch (Exception e) {
							e.printStackTrace();
						}	
				} else if (view == closeIBtn) {
					activity.finish();
				}
			}
		};

		closeIBtn.setOnClickListener(clickListener);
		alipayBtn.setOnClickListener(clickListener);
		unionBtn.setOnClickListener(clickListener);
		xunibiBtn.setOnClickListener(clickListener);
		weixinpayBtn.setOnClickListener(clickListener);
		weixinunionBtn.setOnClickListener(clickListener);
		return parentLinearLayout;
	}
	@Override
	public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		if(payType == 1){
			// 如果respCode参数不为空则走银联支付结果
			onActivityResultUnion(activity, requestCode, resultCode, data);
			payType = 0;
			return;
		}
		if(payType == 2){
			// 如果respCode参数不为空则走微信支付结果
			onActivityResultWeixin(activity, requestCode, resultCode, data);
			payType = 0;
			return;
		}
		if(payType == 3&&requestCode==3){
			// 
			activity.finish();
			payType = 0;
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
	protected void onActivityResultUnion(final Activity activity, int requestCode, int resultCode, Intent data) {
		String msg = "";
		/* 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消 */
		String str = data.getExtras().getString("pay_result");
		GameAssetTool tool = GameAssetTool.getInstance();
		if (str.equalsIgnoreCase("success")) {
			msg = tool.getLangProperty(activity, "pay_success");
		} else if (str.equalsIgnoreCase("fail")) {
			msg = tool.getLangProperty(activity, "pay_failure");
		} else if (str.equalsIgnoreCase("cancel")) {
			// msg = "取消支付";
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					activity.finish();
					return true;
				}
				return false;
			}
		});
		
		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.finish();
			}
		});
		AlertDialog ad = builder.create();
		ad.setCanceledOnTouchOutside(false);
		ad.show();
	}
	
	/**
	 * 微信支付回调生命周期
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	protected void onActivityResultWeixin(final Activity activity, int requestCode, int resultCode, Intent data) {
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
		builder.setMessage(temp.toString());
		builder.setInverseBackgroundForced(true);
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					if(!respCode.equals("02")){
						activity.finish();
						}
					return true;
				}
				return false;
			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(!respCode.equals("02")){
					  activity.finish();
					}
			}
		});
		builder.create().show();
	}
	
	@Override
	public void onSetWindows(Activity activity) {
		uitool.setFullScreen(activity);
	}
	@Override
	public void onResume() {
		  GameSdkLog.getInstance().i("onResume callled and call is:"+isUnionWeixinload);
			 if (isUnionWeixinload) {/*
				 SdkLog.getInstance().i("支付查询回调开始--->weixin_pre_id："+weixin_pre_id);
				  new CheckOderManager().checkState(activity, weixin_pre_id, new CheckOderManager.QueryPayListener() {
					  @Override
		                public void getPayState(String trade_state) {
		                	SdkLog.getInstance().i("支付收到回调trade_state--->"+trade_state);
		                	toastMsg="";
		                	if ("SUCCESS".equalsIgnoreCase(trade_state)) {
		     	                toastMsg="支付成功";
		     	            } else if ("NOTPAY".equalsIgnoreCase(trade_state)) {
		     	            	toastMsg="未支付";
		     	            } else if ("CLOSED".equalsIgnoreCase(trade_state)) {
		     	            	toastMsg="已关闭";
		     	            } else if ("PAYERROR".equalsIgnoreCase(trade_state)) {
		     	            	toastMsg="支付失败";
		     	            }
		                	SdkLog.getInstance().i("trade_state:"+trade_state+"Toastmsg:"+toastMsg);
		     	            if (toastMsg!=null&&!"".equals(toastMsg)) {
		     	            	activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										 SdkToast.getInstance().show(activity, toastMsg);
										 if (toastMsg.equals("支付成功")) {
											 activity.finish();
										   }
									}
									
								});
		     				}
		                	isUnionWeixinload=false;
		                }
		            });
		        */
				 GameSdkLog.getInstance().i("支付查询回调开始--->weixin_pre_id："+weixin_pre_id);
				 new CheckOderManager().checkState(activity, GameSdkConstants.ULO_WX_URL, weixin_pre_id, new CheckOderManager.QueryPayListener() {
					
					@Override
					public void getPayState( String trade_state) {
						// TODO Auto-generated method stub
						GameSdkLog.getInstance().i("支付收到回调trade_state--->"+trade_state);
	                	toastMsg="";
	                	if ("SUCCESS".equalsIgnoreCase(trade_state)) {
	     	                toastMsg="支付成功";
	     	            } else if ("NOTPAY".equalsIgnoreCase(trade_state)) {
	     	            	toastMsg="未支付";
	     	            } else if ("CLOSED".equalsIgnoreCase(trade_state)) {
	     	            	toastMsg="已关闭";
	     	            } else if ("PAYERROR".equalsIgnoreCase(trade_state)) {
	     	            	toastMsg="支付失败";
	     	            }
	                	GameSdkLog.getInstance().i("trade_state:"+trade_state+"Toastmsg:"+toastMsg);
	     	            if (toastMsg!=null&&!"".equals(toastMsg)) {
	     	            	activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									 GameSdkToast.getInstance().show(activity, toastMsg);
									 if (toastMsg.equals("支付成功")) {
										 activity.finish();
									   }
								}
								
							});
	     				}
	                	isUnionWeixinload=false;
					}
				});
			 
			 }
			 GameSdkLog.getInstance().i("on end of onResume callled and call is:"+isUnionWeixinload);
	}
	    @Override
	    public void onDestroy() {
	    	  GameSdkLog.getInstance().i("----->onDestroy");
	    }
	    
	   
//请求回调接口的接受
		@Override
		public void callback(String code,String msg) {
			GameSdkLog.getInstance().i("----->callback--->code："
					+ ""+code+",msg:"+msg);
			if ("weixin".equals(code)) {
				 isUnionWeixinload=true;
				 weixin_pre_id=msg;
			}else if ("coupon".equals(code)) {
				Intent intent = new Intent(activity, GameSdKActivity.class);
				final String cpOrderId = GameSdkConstants.ORDER_INFO.cpOrderId;
				final String uid = GameSdkConstants.ORDER_INFO.uid;
				final String gameName = GameSdkConstants.ORDER_INFO.gameName;
				final String goodsName = GameSdkConstants.ORDER_INFO.goodsName;
				final int price = GameSdkConstants.ORDER_INFO.price;
				intent.putExtra("layoutId", ActivityFactory.GUILD_PAY_ACTIVITY.toString());
				intent.putExtra("cpOrderId", cpOrderId);
				intent.putExtra("uid", uid);
				intent.putExtra("price", price);
				intent.putExtra("gameName", gameName);
				intent.putExtra("goodsName", goodsName);
				intent.putExtra("myCoin", Integer.valueOf(msg)/100f);
				activity.startActivityForResult(intent, 3);
			}
			 
		}
}
