package com.huai.gamesdk.widget;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 自定义Toast，控制重复弹出
 */
public class GameSdkToast {
	private static GameSdkToast customer = null;

	private Toast toast = null;

	private Handler handler = new Handler();

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (toast != null) {
				toast.cancel();
				toast = null;
			}
		}
	};

	private GameSdkToast() {
	}

	public void show(Context context, String text) {
		handler.removeCallbacks(runnable);
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		} else {
			toast.setText(text);
		}
		
		long delayMillis = 2000;
		if (!TextUtils.isEmpty(text)) {
			delayMillis = delayMillis + text.length() / 10 * 1000;
		}

		handler.postDelayed(runnable, delayMillis);
		toast.show();
	}

	public static GameSdkToast getInstance() {
		if (customer == null) {
			customer = new GameSdkToast();
		}
		return customer;
	}
}
