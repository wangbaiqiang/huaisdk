package com.huai.gamesdk.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.huai.gamesdk.activity.GameSdKActivity;
import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.bean.RoleInfo;
import com.huai.gamesdk.bean.GameVerifyBean;
import com.huai.gamesdk.callback.SdkRequestCallback;
import com.huai.gamesdk.callback.SdkCallbackListener;
import com.huai.gamesdk.services.IDataService.UidType;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.solid.GameStatusCode;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameDesTool;
import com.huai.gamesdk.tool.GameSdkLogger;
import com.huai.gamesdk.tool.GameMD5Tool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.widget.GameBrowserNotification;
import com.huai.gamesdk.widget.GameSdkToast;

/**
 * !!!注意!!!
 * 本项目没有使用任何商业接口!!!
 * 实际的开发项目,需根据渠道后台按照规则拼接请求参数、发网络请求、解析响应体、回调通知结果等。
 * 所以，你做的事情:
 * 只需更改UI(有些渠道公司,UI都是程序猿设计......)、
 * 只需替换成自己家的后台接口、
 * 定制渠道个性化接口,
 * 即可完成项目.
 */
public final class Dispatcher {
	private static Dispatcher processor = null;
	private static IDataService dao = null;
	private static final MediaType JSON = MediaType
			.parse("application/json;charset=utf-8");
	private static final String TAG = "GameSDK";

	public SdkCallbackListener<String> listener = null;

	private int errorTime = 0;
	private GameSdkLog log = null;

	private Dispatcher() {
		log = GameSdkLog.getInstance();
	}

	/**
	 * 首页账户登录
	 * @param activity
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void login(Activity activity, final CharSequence username,
			final CharSequence password) throws Exception {
		Intent intent = activity.getIntent();
		dao = getIdaoFactory(activity);
		if (ActivityFactory.PHONE_REGISTER_ACTIVITY.toString().equals(
				intent.getStringExtra("from"))) {
			String sid = intent.getStringExtra("sid");
			GameSdkConstants.DEVICE_INFO.sid = sid;
			listener.callback(GameStatusCode.SUCCESS, sid);
			TalkingDataAppCpa.onLogin(username.toString());
			activity.finish();
			return;
		}


		// 记录错误次数，如果超过5次则需要输入验证码,开启找回密码界面，自己拓展即可
		if (errorTime >= 4) {
			Map<String, String> intentMap = new HashMap<String, String>();
			intentMap.put("username", username.toString());
			intentMap.put("password", password.toString());
			showActivity(activity, ActivityFactory.PWD_ERROR_ACTIVITY,
					intentMap);
			return;
		}

//		在這裡所有的登录都会成功，所以需换成自家渠道后台接口进行账号密码验证，
//      这里不做任何接口处理
//		也就是说在这里做登录请求处理，将响应结果回调出去
		activity.finish();
		errorTime = 0;

//		这里通过接口,将结果回调出去。。。后面的类似
		listener.callback(GameStatusCode.SUCCESS, "1515451515451");

	}

	public void verify(Activity activity, GameVerifyBean verifyBean) {
		String appKey = GameSdkConstants.APPINFO.appKey;
		String gameId = verifyBean.gameId;
		String sid = verifyBean.sid;
		String userId = verifyBean.userId;
		String channel = verifyBean.channel;
		String version = verifyBean.version;
		StringBuilder sbSign = new StringBuilder();
		try {
			JSONObject object = new JSONObject();
			object.put("sid", sid);
			object.put("channel", channel);
			object.put("version", version);
			object.put("userId", userId);
			object.put("gameId", gameId);
		}catch (Exception e){

		}

		GameSdkLogger.i("sidverify success :" );

	}

	// 提交游戏信息
	public void submitGameInfo(Activity activity, RoleInfo roleinfo)
			throws Exception {

		String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;

		String userId = roleinfo.userId;
		String roleId = roleinfo.roleId;
		String roleName = roleinfo.roleName;
		String roleLevel = roleinfo.roleLevel;
		String zoneId = roleinfo.zoneId;
		String zoneName = roleinfo.zoneName;
		String dataType = roleinfo.dataType;

		StringBuilder sbSign = new StringBuilder();
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("roleId=").append(roleId).append("&");

		sbSign.append("dataType=").append(dataType).append("||").append(appKey);

		JSONObject param = new JSONObject();
		try {
			param.put("appId", appId);
			param.put("userId", userId);
			param.put("roleId", roleId);
			param.put("roleName", roleName);
			GameSdkLogger.i("subgameinfo request param:" + param);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("提交游戏信息-数据组装异常", e);
		}

		GameSdkLogger.i("submitGameInfo success :" );

	}

	/**
	 * 输错5次密码后，
	 */
	public void pwdError(Activity activity) throws Exception {
		Intent intent = activity.getIntent();
		final String username = intent.getStringExtra("username");
		final String password = intent.getStringExtra("password");

		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("account", username));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();


	}

	public void resetErrorTime() {
		errorTime = 0;
	}

	/**
	 * 检查账户是否存在
	 * @param activity
	 * @return
	 */
	public void checkAccount(Activity activity, final String account,
			final String uid) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("请输入您想创建的账号", account));
				return list;
			}
		}.create();

	}

	/**
	 * 账号注册获取uid和账号,页面转向账号注册
	 * @param activity
	 * @return
	 */
	public void getAccount(Activity activity) throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				return list;
			}
		}.create();

		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", "uid");
		map.put("account","account");
		showActivity(activity,
				ActivityFactory.ACCOUNT_REGISTER_ACTIVITY, map);
	}

	/**
	 * 账号注册提交
	 */
	public void register(Activity activity, final CharSequence account,
			final CharSequence password, final CharSequence uid)
			throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("uid", uid.toString()));
				list.add(new Entry("password", password.toString()));
				list.add(new Entry("newAccount", account.toString()));
				return list;
			}
		}.create();

		GameSdkToast.getInstance().show(
				activity,
				GameAssetTool.getInstance().getLangProperty(activity,
						"register_success"));

		listener.callback(GameStatusCode.SUCCESS,
				"sid");
		activity.finish();

	}

	/**
	 * 手机号注册获取验证码
	 */
	public void registerByMobileVcode(Activity activity, final Button regBtn,
			final CountDownTimer timer, final EditText regPhoneEdtx,
			final boolean isRepost) throws Exception {
		final String mobile = regPhoneEdtx.getText().toString();
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("mobile", mobile));
				return list;
			}
		}.create();
		final GameAssetTool asset = GameAssetTool.getInstance();

		GameSdkToast.getInstance().show(activity,"验证码已发送，请填写信息");
		timer.start();

	}

	/**
	 * 手机注册并登录
	 */
	public void phoneRegisterAndLogin(Activity activity, final String mobile,
			final String vcode, final String password, final String status)
			throws Exception {
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("mobile", mobile));
				list.add(new Entry("vcode", vcode));
				list.add(new Entry("password", password));
				return list;
			}
		}.create();

		GameSdkConstants.DEVICE_INFO.sid = "sid";
		dao.writeUid(UidType.account, mobile, password);
		try {
			listener.callback(GameStatusCode.REG_SUCCESS, "sid");

		} catch (Exception e) {
		}
		listener.callback(GameStatusCode.SUCCESS, "sid");
		activity.finish();

	}

	/**
	 * 重发手机重置密码的验证码
	 * 
	 * @param activity
	 * @param mobile
	 *            手机号
	 * @param timer
	 *            倒数器，如果重发验证码情况，timer不为空。
	 * @throws Exception
	 */
	public void reSendPhoneResetPwdVcode(Activity activity,
			final String mobile, final CountDownTimer timer) throws Exception {
		dao = getIdaoFactory(activity);
		String param = new JsonParameters() {
			@Override
			public List<Entry> other() {
				List<Dispatcher.Entry> list = new ArrayList<Dispatcher.Entry>();
				list.add(new Entry("mobile", mobile.toString()));
				return list;
			}
		}.create();

		if (timer != null) {
			timer.start();
		}

	}

	/**
	 * 找回密码-手机密码找回 需要根据渠道接口去做相应处理
	 * 手机验证码+密码 重置密码
	 * @param activity
	 * @param mobile
	 * @param vcode
	 * @throws Exception
	 */
	public void phoneResetPassword(Activity activity, final String mobile,
			final String vcode, final String password, final String status)
			throws Exception {
		Map<String, String> intent = new HashMap<String, String>();
		intent.put("username", mobile);
		intent.put("password", password);
		intent.put("type", "m");

		showActivity(
				activity,
				ActivityFactory.ACCOUNT_LOGIN_ACTIVITY,
				intent);
	}

	/**
	 * 往服务器请求该账户信息绑定信息情况
	 */
	public void loadFindPwdType(Activity activity, final String account)
			throws Exception {

		Map<String, String> intent = new HashMap<String, String>();
		intent.put("account", account);
		intent.put("phone", "phone");
		intent.put("email", "email");
		showActivity(activity,
				ActivityFactory.FIND_PWD_TYPE_ACTIVITY, intent);
	}

	/**
	 * 手机验证码+重置密码第一次发送验证码转到验证码验证界面
	 *
	 */
	public void sendPhoneResetPwdVcode(Activity activity, final String contract)
			throws Exception {
		Map<String, String> intent = new HashMap<String, String>();
		intent.put("contract", contract);
		showActivity(activity, ActivityFactory.PHONE_VERIFY_ACTIVITY, intent);

	}

	/**
	 * 账号重置密码 --绑定的邮箱 & 绑定的手机
	 */
	public void resetPwd(Activity activity, final String account,
			final int resetType, final String contract) throws Exception {

		Map<String, String> intent = new HashMap<String, String>();
		GameSdkLog.getInstance().i("--->找回类型：" );
		intent.put("contract", contract);
		intent.put("account", account);
		showActivity(activity,
					ActivityFactory.FIND_PWD_VCODE_ACTIVITY, intent);

	}

	/**
	 * 用户账号找回 ----手机重置密码-输入验证码+密码
	 */
	public void resetUserPhonePwd(Activity activity, final String account,
			final String vcode, final String password, final String mobile)
			throws Exception {

		Map<String, String> intent = new HashMap<String, String>();
		intent.put("username", account);
		intent.put("password", password);
		intent.put("type", "u");
		showActivity(
				activity,
				ActivityFactory.ACCOUNT_LOGIN_ACTIVITY,
				intent);
	}

	/**
	 * alipay sign
	 */
	@SuppressLint("HandlerLeak")
	public void AlipaySign(final Activity activity, final String cpOrderId,
			final String uid, final int price, String goodsName,
			int couponmoney, int couponId) throws Exception {
		final int SDK_PAY_FLAG = 1;
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;

		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("ip", GameSdkConstants.DEVICE_INFO.ip);
		} catch (Exception e) {
			throw new Exception("alipay sign-数据组装异常", e);
		}
	}

	/**
	 * union支付sign
	 */
	public void UnionSign(Activity activity, final String cpOrderId,
			final String uid, final int price, final String goodsName,
			int couponmoney, int couponId) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;

		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("type", GameSdkConstants.PLATFORM);
		} catch (Exception e) {
			throw new Exception("银联签名-数据组装异常", e);
		}

		Log.i(TAG, "UnionSign: pay");


	}

	public void WeixinUnionPaySign(Activity activity, final String cpOrderId,
			final String uid, final int price, String goodsName,
			final int couponmoney, final int couponId,
			final SdkRequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("deviceIp=").append(GameCommonTool.getIp(activity))
				.append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("coupon_id", couponId);
			param.put("coupon_amount", couponmoney);
			param.put("uid", uid);
			param.put("price", price);
		} catch (Exception e) {
			throw new Exception("wechat签名-数据组装异常", e);
		}


	}


	@Deprecated
	public void WeixinPaySign(Activity activity, final String cpOrderId,
			final String uid, final int price, final String goodsName)
			throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
			param.put("platformType", "2");
			param.put("sign",
					GameMD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("wechatsign-数据组装异常", e);
		}

	}


	@Deprecated
	public void CardPay(Activity activity, String cpOrderId, String uid,
			int price, int cardPrice, String account, String passwd)
			throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		StringBuilder sbInfo = new StringBuilder();
		String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;
		String cardInfo = "";
		sbInfo.append(price).append("|").append(cardPrice).append("|");
		sbInfo.append(account).append("|").append(passwd);
		try {
			if (appKey.length() > 8) {
				cardInfo = GameDesTool.encode(appKey.substring(0, 8),
						sbInfo.toString());
			} else {
				cardInfo = GameDesTool.encode(appKey, sbInfo.toString());
			}
		} catch (Exception e) {
			throw new Exception("card支付-加密支付信息异常：", e);
		}
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("cardType=").append(0).append("&");
		sbSign.append("cardInfo=").append(cardInfo).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("platformType", "2");
			param.put("sign",
					GameMD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("pay签名-数据组装异常", e);
		}

	}

	/**
	 */
	public void showActivity(Context context, ActivityFactory layoutId,
			Map<String, String> param) {
		Intent intent = new Intent(context, GameSdKActivity.class);

		intent.putExtra("layoutId", layoutId.toString());
		if (param != null) {
			Set<java.util.Map.Entry<String, String>> keySet = param.entrySet();
			Iterator<java.util.Map.Entry<String, String>> iterator = keySet
					.iterator();
			while (iterator.hasNext()) {
				java.util.Map.Entry<String, String> entry = iterator.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}

		context.startActivity(intent);
		if (context instanceof Activity) {
			((Activity) context).finish();
		}
	}

	public IDataService getIdaoFactory(Context context) {
		if (dao == null) {
			dao = new SharedPrefDataService(context);
		}
		return dao;
	}

	private void pushRegMsg(Activity activity, String account) {
		GameAssetTool asset = GameAssetTool.getInstance();
		String title = asset.getLangProperty(activity,
				"register_finish_notifi_title");
		String content = String.format(asset.getLangProperty(activity,
				"register_finish_notifi_content"), account);
		String url = GameSdkConstants.getUsercenterUrl() + "?from=yhsdk&username="
				+ account;
		GameBrowserNotification notification = new GameBrowserNotification(activity);
		notification.showNotification(title, content, url);
	}

	public static Dispatcher getInstance() {
		if (processor == null) {
			processor = new Dispatcher();
		}
		return processor;
	}

	private abstract class JsonParameters {
		public abstract List<Entry> other();

		public String create() throws Exception {
			List<Entry> entries = other();

			StringBuilder preSign = new StringBuilder();
			preSign.append("appId=").append(GameSdkConstants.APPINFO.appId);
			preSign.append("&type=").append(GameSdkConstants.PLATFORM);

			for (Entry entry : entries) {
				preSign.append("&" + entry.key + "=").append(entry.value);
			}
			preSign.append("&ext=").append(GameSdkConstants.APPINFO.ext);
			preSign.append("&version=").append(GameSdkConstants.VERSION);
			preSign.append("&ip=").append(GameSdkConstants.DEVICE_INFO.ip);
			preSign.append("&mac=").append(GameSdkConstants.DEVICE_INFO.mac);
			preSign.append("&imei=").append(GameSdkConstants.DEVICE_INFO.imei);
			preSign.append("&channel=").append(GameSdkConstants.DEVICE_INFO.channel);
			preSign.append("||").append(GameSdkConstants.APPINFO.appKey);
			String sign = GameMD5Tool.calcMD5(preSign.toString().getBytes());

			try {
				JSONObject param = new JSONObject();
				param.put("appId", GameSdkConstants.APPINFO.appId);
				param.put("type", GameSdkConstants.PLATFORM);
				for (Entry entry : entries) {
					param.put(entry.key, entry.value);
				}
				param.put("ext", GameSdkConstants.APPINFO.ext);
				param.put("version", GameSdkConstants.VERSION);
				param.put("ip", GameSdkConstants.DEVICE_INFO.ip);
				return param.toString();
			} catch (JSONException e) {
				throw new Exception("数据组装异常", e);
			}
		}
	}

	private class Entry {
		String key;
		String value;

		Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	public void getMyCouponCoin(Activity activity, String username,
			final SdkRequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("uid=").append(username).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("uid", username);
			param.put("type", GameSdkConstants.PLATFORM);
			param.put("channel", GameSdkConstants.DEVICE_INFO.channel);
			param.put("ext", GameSdkConstants.APPINFO.ext);
			param.put("deviceIp", GameCommonTool.getIp(activity));
			param.put("version", GameSdkConstants.VERSION);
			param.put("sign",
					GameMD5Tool.calcMD5(sbSign.toString().getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception("获取代金卷异常", e);
		}

	}

	public void payMyCouponCoin(Activity activity, final String cpOrderId,
			final String uid, final int price, String goodsName, int couponid,
			final SdkRequestCallback callback) throws Exception {
		JSONObject param = new JSONObject();
		StringBuilder sbSign = new StringBuilder();
		final String appId = GameSdkConstants.APPINFO.appId;
		String appKey = GameSdkConstants.APPINFO.appKey;
		sbSign.append("appId=").append(appId).append("&");
		sbSign.append("cpOrderId=").append(cpOrderId).append("&");
		sbSign.append("uid=").append(uid).append("&");
		sbSign.append("price=").append(price).append("&");
		sbSign.append("goodsName=").append(goodsName).append("&");
		sbSign.append("channel=").append(GameSdkConstants.DEVICE_INFO.channel)
				.append("||").append(appKey);
		try {
			param.put("appId", appId);
			param.put("coupon_id", couponid);
			param.put("cpOrderId", cpOrderId);
			param.put("uid", uid);
			param.put("price", price);
			param.put("goodsName", goodsName);
		} catch (Exception e) {
			throw new Exception("数据组装异常", e);
		}


	}

}
