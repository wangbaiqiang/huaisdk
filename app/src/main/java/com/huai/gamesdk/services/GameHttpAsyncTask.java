package com.huai.gamesdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameHttpTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkLog;
import com.huai.gamesdk.tool.GameHttpTool.HttpResult;
import com.huai.gamesdk.widget.GameSdkToast;



abstract class GameHttpAsyncTask extends AsyncTask<String, Integer, HttpResult> {
	private static GameAssetTool asset = GameAssetTool.getInstance();

	Activity activity = null;
	private String url = null;
	private GameSdkLog log = null;
	private Dialog dialog = null;

	GameHttpAsyncTask(Activity activity, String url) {
		this(activity, url, asset.getLangProperty(activity, "progress"));
	}

	GameHttpAsyncTask(Activity activity, String url, String loadingMsg) {
		super();
		this.url = url;
		this.activity = activity;
		this.log = GameSdkLog.getInstance();
		this.dialog = GameUiTool.getLoadingDialog(loadingMsg, activity);
	}

	@Override
	protected HttpResult doInBackground(String... param) {
		if (param == null || param.length != 1 || param[0] == null) {
			log.e("requestbody exception please check");
			return null;
		}
		if(!DeviceInfo.isNetAvailable(activity)){
			Toast.makeText(activity, "网络失去链接，请检查", Toast.LENGTH_SHORT).show();
			return null;
		}

		try {
			return GameHttpTool.post(url, GameHttpTool.CONNECT_TIMEOUT, param[0]);
		} catch (Exception e) {
			log.e("链接" + url + "失败：", e);
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "http_error"));
			return null;
		}
	}
   
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}
	@Override
	protected void onPostExecute(HttpResult result) {
		super.onPostExecute(result);
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		if (result == null) {
			return;
		}

		if (result.code <= 0) {
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "http_error") );
			return;
		}

		if (result.code != 200) {
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "response_error") );
			return;
		}

		//log.i("responsebody：" + result.message);
		if (TextUtils.isEmpty(result.message)) {
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "result_format_error"));
			return;
		}
		try {
			JSONObject json = new JSONObject(result.message);
			onHandleResult(json);
		} catch (JSONException e) {
			log.e("responsebody format error：" + result.message, e);
			GameSdkToast.getInstance().show(activity, asset.getLangProperty(activity, "result_format_error"));
		} catch (Exception e) {
			GameSdkToast.getInstance().show(activity, "网络连接不可用");
			
		}
	}

	protected abstract void onHandleResult(JSONObject message) throws JSONException;

}
