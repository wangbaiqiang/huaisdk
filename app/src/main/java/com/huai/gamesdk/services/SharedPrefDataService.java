package com.huai.gamesdk.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.tool.GameDesTool;
import com.huai.gamesdk.tool.GameSdkLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public final class SharedPrefDataService implements IDataService {
	private static final String SP_FILE_NAME = "gamesdk_sharepren_info";
	private static final String KEY = "gamedeskey";
	private static final String IMIE = "gamesdkimie";

	private GameSdkLog log = null;
	private SharedPreferences preferences = null;
	
	public SharedPrefDataService(Context context) {
		log = GameSdkLog.getInstance();
		preferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public boolean isFirstLoad() {
		boolean isisFirstLoad = preferences.getBoolean("isFirstLoad", true);
		if (isisFirstLoad) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("isFirstLoad", false);
			editor.commit();
		}
		return isisFirstLoad;
	}

	@Override
	public String currentLoginType() {
		return preferences.getString("currentLoginType", "");
	}

	@Override
	public JSONObject readCurntUid(UidType type) {
		String currentKey = null;
		if (type == UidType.account) {
			currentKey = "currentAccount";
		} else if (type == UidType.phone) {
			currentKey = "currentPhone";
		} else {
			return null;
		}

		String currentUid = preferences.getString(currentKey, "");
		if (TextUtils.isEmpty(currentUid)) {
			return null;
		}

		try {
			JSONObject uidJson = new JSONObject(GameDesTool.decode(KEY, currentUid));
			return uidJson;
		} catch (Exception e) {
			log.e("读取单个\"" + type.toString() + "\"类型用户信息异常：", e);
			return null;
		}
	}

	@Override
	public List<JSONObject> readUids(UidType type) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		String historyKey = null;
		if (type == UidType.account) {
			historyKey = "historyAccount";
		} else if (type == UidType.phone) {
			historyKey = "historyPhone";
		} else {
			return null;
		}

		String uids = preferences.getString(historyKey, "");
		if (TextUtils.isEmpty(uids)) {
			return list;
		}

		try {
			uids = GameDesTool.decode(KEY, uids);
			JSONArray array = new JSONArray(uids);
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getJSONObject(i));
			}
			return list;
		} catch (Exception e) {
			log.e("读取\"" + type.toString() + "\"类型用户信息列表异常：", e);
			return list;
		}
	}

	@Override
	public void writeUid(UidType type, String uid, String pwd) {
		String currentKey = null;
		String historyKey = null;
		String currentLoginType = null;
		JSONObject userinfo = new JSONObject();

		if (!TextUtils.isEmpty(pwd)) {
			currentKey = "currentAccount";
			historyKey = "historyAccount";
			currentLoginType = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
			try {
				userinfo.put("username", uid);
				userinfo.put("password", pwd);
			} catch (Exception e) {
				log.e("保存" + currentLoginType + "类型用户信息异常，不进行保存：", e);
				return;
			}
		} else {
			currentKey = "currentPhone";
			historyKey = "historyPhone";
			currentLoginType = ActivityFactory.ACCOUNT_LOGIN_ACTIVITY.toString();
			try {
				userinfo.put("username", uid);
				userinfo.put("password", pwd);
			} catch (Exception e) {
				log.e("保存" + currentLoginType + "类型用户信息异常，不进行保存：", e);
				return;
			}
		}

		String enHistory = preferences.getString(historyKey, "");
		String deHistory = "";
		if (!TextUtils.isEmpty(enHistory)) {
			try {
				deHistory = GameDesTool.decode(KEY, enHistory);
			} catch (Exception e) {
				log.e("无法解密信息：" + enHistory, e);
				deHistory = "";
			}
		}

		deHistory = refresh(deHistory, userinfo.toString());
		SharedPreferences.Editor editor = preferences.edit();
		try {
			editor.putString(currentKey, GameDesTool.encode(KEY, userinfo.toString()));
			editor.putString(historyKey, GameDesTool.encode(KEY, deHistory));
		} catch (Exception e) {
			log.e("写入异常：", e);
		}
		editor.putString("currentLoginType", currentLoginType);
		editor.commit();
	}

	@Override
	public void delteUid(String uid) {
		if (TextUtils.isEmpty(uid)) {
			return;
		}
		String currentKey = null;
		String historyKey = null;
		if (uid.contains("password")) {
			currentKey = "currentAccount";
			historyKey = "historyAccount";
		} else {
			currentKey = "currentPhone";
			historyKey = "historyPhone";
		}

		String enCurrent = preferences.getString(currentKey, "");
		String enHistory = preferences.getString(historyKey, "");
		String deCurrent = "";
		String deHistory = "";
		
		if (TextUtils.isEmpty(enHistory) && TextUtils.isEmpty(enCurrent)) {
			return;
		}

		if (!TextUtils.isEmpty(enHistory)) {
			try {
				deHistory = GameDesTool.decode(KEY, enHistory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!TextUtils.isEmpty(enCurrent)) {
			try {
				deCurrent = GameDesTool.decode(KEY, enCurrent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		SharedPreferences.Editor editor = preferences.edit();
		if (deHistory.contains(uid)) {
			String deletedUid = uid.replaceAll("[{]", "[{]").replaceAll("[}]", "[}]");
			deHistory = deHistory.replaceAll(deletedUid + ",", "");
			deHistory = deHistory.replaceAll(deletedUid, "");
			try {
				editor.putString(historyKey, GameDesTool.encode(KEY, deHistory));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (TextUtils.isEmpty(deHistory)) {
			editor.remove(historyKey);
		}

		if (deCurrent.equals(uid)) {
			editor.remove(currentKey);
		}
		editor.commit();
	}

	/**
	 * @param list
	 * @param json
	 * @return
	 */
	private String refresh(String list, String json) {
		if (TextUtils.isEmpty(list)) {
			return "[" + json + "]";
		}

		if (list.contains(json)) {
			list = list.replaceAll(json.replaceAll("[{]", "[{]").replaceAll("[}]", "[}]") + ",", "");
			list = list.replaceAll(json.replaceAll("[{]", "[{]").replaceAll("[}]", "[}]"), "");
		}

		if ("[]".equals(list)) {
			return "[" + json + "]";
		}
		return "[" + json + "," + list.substring(1, list.lastIndexOf("}") + 1) + "]";
	}

	@Override
	public void writeImie(String imie) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(IMIE, imie);
		editor.commit();
	}

	@Override
	public String getImie() {
		return preferences.getString(IMIE, "");
	}

}
