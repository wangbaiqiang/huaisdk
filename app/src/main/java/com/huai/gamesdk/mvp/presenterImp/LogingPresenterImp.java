package com.huai.gamesdk.mvp.presenterImp;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.huai.gamesdk.activity.GameSdKActivity;
import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.callback.SdkCallbackListener;
import com.huai.gamesdk.mvp.model.MVPLogingBean;
import com.huai.gamesdk.mvp.presenter.LogingPresenter;
import com.huai.gamesdk.mvp.view.LoginView;
import com.huai.gamesdk.mvpbase.BasePresenterImpl;
import com.huai.gamesdk.services.IDataService;
import com.huai.gamesdk.services.SharedPrefDataService;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameMD5Tool;

public class LogingPresenterImp extends BasePresenterImpl<LoginView> implements
		LogingPresenter {
	private static final MediaType JSON = MediaType
			.parse("application/json;charset=utf-8");

	private LoginView mainBaseView;
	private int errorTime = 0;
	private static IDataService dao = null;
	private Context mContext;
	private String userName;
	private String passWord;

	private String userParams;
	private String requestParams;
	public SdkCallbackListener<String> listener = null;


	public IDataService getIdaoFactory(Context context) {
		if (dao == null) {
			dao = new SharedPrefDataService(context);
		}
		return dao;
	}

	private void loginHttp(String param, String Url, final Activity activity) {
		RequestBody requestBody = RequestBody.create(JSON, param);

	}

	public String createdPrams(String name, String word) {
		StringBuilder preSign = new StringBuilder();
		preSign.append("appId=").append(GameSdkConstants.APPINFO.appId);
		preSign.append("&type=").append(GameSdkConstants.PLATFORM);
		preSign.append("&account=").append(name);
		preSign.append("&password=").append(word);
		preSign.append("&ext=").append(GameSdkConstants.APPINFO.ext);
		preSign.append("||").append(GameSdkConstants.APPINFO.appKey);

		String sign = GameMD5Tool.calcMD5(preSign.toString().getBytes());

		try {
			JSONObject param = new JSONObject();
			param.put("appId", GameSdkConstants.APPINFO.appId);
			param.put("type", GameSdkConstants.PLATFORM);
			param.put("account", name);
			param.put("password", word);
			param.put("ext", GameSdkConstants.APPINFO.ext);
			param.put("version", GameSdkConstants.VERSION);

			param.put("platformType", "2");
			param.put("sign", sign);

			requestParams = param.toString();

		} catch (Exception e) {

		}

		return requestParams;
	}

	// 跳转到验证的Activity
	public void showActivity(Context context, ActivityFactory layoutId,
			Map<String, String> param) {
		Intent intent = new Intent(context, GameSdKActivity.class);

		intent.putExtra("layoutId", layoutId.toString());
		if (param != null) {
			Set<Map.Entry<String, String>> keySet = param.entrySet();
			Iterator<Map.Entry<String, String>> iterator = keySet.iterator();
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

	@Override
	public void login(MVPLogingBean user, Context context) {
		// TODO Auto-generated method stub
		
	}



}
