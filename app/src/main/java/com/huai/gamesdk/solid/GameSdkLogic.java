package com.huai.gamesdk.solid;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.huai.gamesdk.activity.GameSdKActivity;
import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.activity.GameSurePayActivity;
import com.huai.gamesdk.bean.AppInfo;
import com.huai.gamesdk.bean.Mode;
import com.huai.gamesdk.bean.OrderInfo;
import com.huai.gamesdk.bean.RoleInfo;
import com.huai.gamesdk.bean.GameVerifyBean;
import com.huai.gamesdk.callback.SdkCallbackListener;
import com.huai.gamesdk.exception.GameSDKException;
import com.huai.gamesdk.services.DeviceInfo;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameMD5Tool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameFloatWindowMgr;

public final class GameSdkLogic {
	private static GameSdkLogic instance = null;
	private boolean isINIT = false;
	private GameAssetTool prop = null;
	private GameSdkLog log = null;
	public static final String TAG =  "GameSdk";

	private GameSdkLogic() {
		prop = GameAssetTool.getInstance();
		log = GameSdkLog.getInstance();
	}

	private void checkNullListener(Context context, SdkCallbackListener<?> listener) throws GameSDKException {
		if (context == null) {
			throw new GameSDKException(prop.getLangProperty(context, "context_is_null"));
		}

		if (listener == null) {
			throw new GameSDKException(prop.getLangProperty(context, "listener_is_null"));
		}
	}


	public void setSdkMode(Mode mode) {
		if (mode == null) {
			mode = Mode.release;
			return;
		}
		GameSdkConstants.mode = mode;
	}


//	初始化
	public void init(final Context context, final AppInfo info, final SdkCallbackListener<String> listener) throws GameSDKException {

		Dispatcher.getInstance().resetErrorTime();

		if (info == null) {
			throw new GameSDKException(prop.getLangProperty(context, "init_appinfo_is_null"));
		}



		if (!DeviceInfo.isNetAvailable(context)) {
			listener.callback(GameStatusCode.NET_UNAVAILABLE, prop.getLangProperty(context, "net_unavailable"));
			return;
		}

		GameSdkConstants.DEVICE_INFO = DeviceInfo.init(context);
		GameSdkConstants.init();

		StringBuilder preSign = new StringBuilder();
		preSign.append("appId=").append(info.appId);
		preSign.append("&type=").append(GameSdkConstants.PLATFORM);
		preSign.append("&packageName=").append(context.getPackageName());
		preSign.append("&version=").append(GameSdkConstants.VERSION);
		preSign.append("&ip=").append(GameSdkConstants.DEVICE_INFO.ip);
		preSign.append("&mac=").append(GameSdkConstants.DEVICE_INFO.mac);
		preSign.append("&imei=").append(GameSdkConstants.DEVICE_INFO.imei);
		preSign.append("||").append(info.appKey);
		String sign = GameMD5Tool.calcMD5(preSign.toString().getBytes());

		JSONObject json = new JSONObject();
		try {
			json.put("appId", info.appId);
			json.put("type", GameSdkConstants.PLATFORM);
			json.put("packageName", context.getPackageName());
			json.put("version", GameSdkConstants.VERSION);
			json.put("ip", GameSdkConstants.DEVICE_INFO.ip);
			json.put("mac", GameSdkConstants.DEVICE_INFO.mac);
			json.put("imei", GameSdkConstants.DEVICE_INFO.imei);
			json.put("channel", GameSdkConstants.DEVICE_INFO.channel);
			json.put("sign", sign);
		} catch (JSONException e) {
			throw new GameSDKException(prop.getLangProperty(context, "param_error"));
		}

		
		String param = json.toString();
		Toast.makeText(context, "初始化成功", Toast.LENGTH_SHORT).show();
		listener.callback(GameStatusCode.SUCCESS, "初始化成功");
		GameSdkConstants.APPINFO = info;
		isINIT = true;


	}


	public void login(Context context, SdkCallbackListener<String> listener) throws GameSDKException {
		if (GameCommonTool.isFastClick()) {
			return;
		}
		if (!isINIT) {
			return;
		}
		
		checkNullListener(context, listener);
		Dispatcher.getInstance().listener = listener;
		
		Intent intent = new Intent(context, GameSdKActivity.class);
		String layoutId = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
		intent.putExtra("layoutId", layoutId);
		context.startActivity(intent);
	}



	public void startPay(final Context context, String cpOrderId, String uid, 
			String goodsName, int price, String gameName, SdkCallbackListener<String> listener) throws GameSDKException {
		if (GameCommonTool.isFastClick()) {
			return;
		}
		checkNullListener(context, listener);
		Dispatcher.getInstance().listener = listener;

		if (context == null) {
			throw new GameSDKException(prop.getLangProperty(context, "context == null"));
		}

		if (TextUtils.isEmpty(cpOrderId)) {
			throw new GameSDKException(prop.getLangProperty(context, "pay_cporderid == empty"));
		}

		if (TextUtils.isEmpty(uid)) {
			throw new GameSDKException(prop.getLangProperty(context, "pay_uid == empty"));
		}

		if (TextUtils.isEmpty(goodsName)) {
			throw new GameSDKException(prop.getLangProperty(context, "pay_goodsname == empty"));
		}

		if (TextUtils.isEmpty(gameName)) {
			throw new GameSDKException(prop.getLangProperty(context, "pay_gamename == empty"));
		}

		if (price <= 0) {
			throw new GameSDKException(prop.getLangProperty(context, "pay_price_value <= 0"));
		}



//		根据渠道后台集成的第三方支付业务修改即可
		
		final Intent intent = new Intent(context, GameSdKActivity.class);
		intent.putExtra("layoutId", ActivityFactory.PAY_ACTIVITY.toString());
		OrderInfo info = new OrderInfo();
		info.cpOrderId = cpOrderId;
		info.uid = uid;
		info.gameName = gameName;
		info.goodsName = goodsName;
		info.price = price;
		GameSdkConstants.ORDER_INFO = info;

		Bundle bundle = new Bundle();
		bundle.putString("voucher", "测试" + "");
		bundle.putString("price", price + "");
		bundle.putString("gameId", gameName);
		bundle.putString("userName", "用户");

		skipActivity( (Activity) context, GameSurePayActivity.class, bundle);

	}


	public  void verify(final Context context,String sid,String channel ,String version,String userId,String gameId)  throws Exception{
		if (context == null) {
			throw new GameSDKException(prop.getLangProperty(context, "context == null"));
		}
		if (TextUtils.isEmpty(sid)) {
			throw new GameSDKException("sid == null");
		}
		if (TextUtils.isEmpty(channel)) {
			throw new GameSDKException("channel == null");
		}
		if (TextUtils.isEmpty(version)) {
			throw new GameSDKException("version == null");
		}
		if (TextUtils.isEmpty(userId)) {
			throw new GameSDKException("userId == null");
		}
		if (TextUtils.isEmpty(gameId)) {
			throw new GameSDKException("gameId == null");
		}

		GameVerifyBean bean = new GameVerifyBean();
		bean.sid = sid;
		bean.userId = userId;
		bean.channel = channel;
		bean.gameId = gameId;
		bean.version = version;
		
		try {
			Dispatcher.getInstance().verify((Activity) context, bean);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void skipActivity(Activity activity, Class<?> gotoClass, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(activity, gotoClass);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

	public void submitGameInfo(final Context context, String userId, String roleId, String roleName, String roleLevel, String zoneId, String zoneName, String dataType) throws Exception {
		if (context == null) {
			throw new GameSDKException(prop.getLangProperty(context, "context_is_null"));
		}

		if (TextUtils.isEmpty(roleId)) {
			throw new GameSDKException("roleId == null");
		}
		if (TextUtils.isEmpty(roleName)) {
			throw new GameSDKException("roleName == null");
		}
		if (TextUtils.isEmpty(roleLevel)) {
			throw new GameSDKException("roleLevel == null");
		}
		if (TextUtils.isEmpty(zoneId)) {
			throw new GameSDKException("zoneId == null");
		}
		if (TextUtils.isEmpty(zoneName)) {
			throw new GameSDKException("zoneName == null");
		}
		if (TextUtils.isEmpty(dataType)) {
			throw new GameSDKException("dataType == null");
		}

		RoleInfo roleinfo = new RoleInfo();
		roleinfo.userId = userId;
		roleinfo.roleId = roleId;
		roleinfo.roleName = roleName;
		roleinfo.roleLevel = roleLevel;
		roleinfo.zoneId = zoneId;
		roleinfo.zoneName = zoneName;
		roleinfo.dataType = dataType;

		try {
			Dispatcher.getInstance().submitGameInfo((Activity) context, roleinfo);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void logout() {
		isINIT = false;
		GameSdkConstants.APPINFO = null;
		GameSdkConstants.DEVICE_INFO = null;
	}

	public static synchronized GameSdkLogic getInstance() {
		if (instance == null) {
			instance = new GameSdkLogic();
		}
		return instance;
	}

	public void showToolBar(Context context) {
		if (isINIT && GameSdkConstants.DEVICE_INFO != null) {
			GameFloatWindowMgr.showSmallwin(context);
		}
	}

	public void hideToolBar(Context context) {
		GameFloatWindowMgr.removeAllwin(context);
	}

	public void onSdkDestory(Context context) {
		GameFloatWindowMgr.removeAllwin(context);
	}
}
