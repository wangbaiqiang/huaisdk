package com.huai.gamesdk.services;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.huai.gamesdk.activity.GameFloatWindowActivity;
import com.huai.gamesdk.bean.FloatSendBean;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameMD5Tool;

import org.json.JSONObject;



public class JavaScriptService {
	private Context context;
	private FloatSendBean floatSendBean;
	private GameFloatWindowActivity activity;
	private static IDataService dao = null;
	private String TAG = "Gamesdk";

	public JavaScriptService(Context c, FloatSendBean floatSendBean, GameFloatWindowActivity activity) {
		this.context = c;
		this.floatSendBean = floatSendBean;
		this.activity = activity;
	}


	@JavascriptInterface
	public String getAppIdAndKey() {
		String result = floatSendBean.getAppId() + "_" + floatSendBean.getAppKey();
		return result;
	}


	@JavascriptInterface
	public void changePasswd(String passwd) {
		dao = Dispatcher.getInstance().getIdaoFactory(context);
		JSONObject user = dao.readCurntUid(IDataService.UidType.account);
		try {
			String username = user.getString("username");
			dao.delteUid(username);
			dao.writeUid(IDataService.UidType.account, username, passwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(context, "修改密码成功！", Toast.LENGTH_LONG).show();
	}


	@JavascriptInterface
	public void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}


	@JavascriptInterface
	public void closeWebView() {
		//sdk.getInstance().showToolBar(context);
		activity.finish();
	}


	@JavascriptInterface
	public String md5GetSign(String paramStr) {
		String sign = GameMD5Tool.calcMD5(paramStr + "||" + floatSendBean.getAppKey(), "UTF-8");
		return sign;
	}


	@JavascriptInterface
	public String md5GetSignAuto(String paramStr) {
		String sign = GameMD5Tool.calcMD5(paramStr + floatSendBean.getAppKey(), "UTF-8");
		return sign;
	}


	@JavascriptInterface
	public String getChannelInfo() {
		String channel = GameSdkConstants.DEVICE_INFO.channel;
		return channel;
	}
	

	@JavascriptInterface
	public String getIPInfo() {
		String ip = GameSdkConstants.DEVICE_INFO.ip;
		return ip;
	}
	

	@JavascriptInterface
	public String getMacInfo() {
		String mac = GameSdkConstants.DEVICE_INFO.mac;
		return mac;
	}
	

	@JavascriptInterface
	public String getImeiInfo() {
		String imei = GameSdkConstants.DEVICE_INFO.imei;
		return imei;
	}
	

	@JavascriptInterface
	public String getPhoneNumInfo() {
		String phoneNum = GameSdkConstants.DEVICE_INFO.phoneNum;
		return phoneNum;
	}
}
