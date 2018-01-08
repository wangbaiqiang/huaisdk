package com.huai.gamesdk;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.huai.gamesdk.bean.AppInfo;
import com.huai.gamesdk.bean.Mode;
import com.huai.gamesdk.callback.SdkCallbackListener;
import com.huai.gamesdk.exception.GameSDKException;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.solid.GameSdkLogic;
import com.huai.gamesdk.solid.GameStatusCode;
/**
 * 测试的activity
 * */
public class TestGameMainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static boolean loginStats = false;
	private Button testbtu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//		全局配置横竖屏 默认是横屏 这个是竖屏的设置 GameSdkConstants.isPORTRAIT = true;
//      (因为很多游戏都是横屏嘛~但是有些游戏是竖屏，所以也要设计竖屏的界面以及对外设置的接口)
		GameSdkConstants.isPORTRAIT = true;

		AppInfo appInfo = new AppInfo();

//		这里是渠道SDK的请求参数 需要渠道后台自己提供

		appInfo.appId = "12306";
		appInfo.appKey = "3x5975397917b2f62031d0";
		appInfo.ext = "1|2";


//		初始化接口
		try {
			
			GameSdkLogic.getInstance().setSdkMode(Mode.debug);
			
			GameSdkLogic.getInstance().init(this, appInfo,
					new SdkCallbackListener<String>() {
						@Override
						public void callback(int code, String response) {
							Log.i(TAG, "初始化处理代码：" + code + "，返回信息：" + response);
							if (GameStatusCode.SUCCESS == code) {
								Toast.makeText(TestGameMainActivity.this, "初始化成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(TestGameMainActivity.this, response,
										Toast.LENGTH_SHORT).show();
							}
						}
					});
		} catch (Exception e) {
			Toast.makeText(TestGameMainActivity.this,
					"SDK初始化异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		

//		登录接口
		/***
		 * 一般来说 需要在用户登录成功以后，才可以登录
		 */
		Button button = (Button) findViewById(R.id.gameLoginBtn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					GameSdkLogic.getInstance().login(TestGameMainActivity.this,
							new SdkCallbackListener<String>() {
								@Override
								public void callback(int code, String response) {
									Log.i(TAG, "登录返回代码：" + code + "，返回信息是："
											+ response);
									if (GameStatusCode.SUCCESS == code) {
										loginStats = true;
										// TODO: 登录成功，response为sid，进行处理
										Toast.makeText(TestGameMainActivity.this,
												"登录成功", Toast.LENGTH_SHORT)
												.show();
										
									} else if (GameStatusCode.REG_SUCCESS == code) {
										// 登录成功，response为错误信息
										Toast.makeText(TestGameMainActivity.this,
												"注册成功", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(TestGameMainActivity.this,
												response, Toast.LENGTH_SHORT)
												.show();
									}
								}
							});
				} catch (Exception e) {
					Toast.makeText(TestGameMainActivity.this,
							"SDK登录异常：" + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

//		支付接口
		/***
		 * 一般来说 需要在用户登录成功以后，才可以支付
		 */
		Button payBtn = (Button) findViewById(R.id.gamePayBtn);
		payBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					GameSdkLogic.getInstance().startPay(TestGameMainActivity.this,
							"CP_3456345",
							"12306" ,
							"游戏金币",
							1,
							"梦幻西游",
							new SdkCallbackListener<String>() {
								@Override
								public void callback(int code, String response) {
									// TODO Auto-generated method stub
									
								}
							});
				} catch (GameSDKException e) {
					Toast.makeText(TestGameMainActivity.this, e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});

//		提交游戏信息
		Button sub = (Button) findViewById(R.id.subInfo);
		sub.setOnClickListener(new OnClickListener() {
			/**
			 * @param context
			 * @param uid  
			 * @param roleId  角色ID 
			 * @param roleName 角色名
			 * @param roleLevel 角色等级
			 * @param zoneId  区ID
			 * @param zoneName 区名称 
			 * @param dataType 数据类型
			 * @throws Exception
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					GameSdkLogic.getInstance().submitGameInfo(TestGameMainActivity.this, "uid:111", "123", "张三", "129", "9527", "齐云楼", "1");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


//		显示浮标
		/***
		 * 一般来说 需要在用户登录成功以后，才可以显示浮标
		 */
		Button showFloatBtn = (Button) findViewById(R.id.showFloatBtn);
		showFloatBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GameSdkLogic.getInstance().showToolBar(TestGameMainActivity.this);
			}
		});

//		隐藏浮标
		/***
		 * 一般来说 需要在用户登录成功以后，才可以隐藏浮标
		 */
		Button hideFloatBtn = (Button) findViewById(R.id.hideFloatBtn);
		hideFloatBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				GameSdkLogic.getInstance().hideToolBar(TestGameMainActivity.this);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		GameSdkLogic.getInstance().onSdkDestory(this);
		super.onDestroy();
	}
}
