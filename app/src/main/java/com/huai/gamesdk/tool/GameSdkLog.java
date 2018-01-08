package com.huai.gamesdk.tool;

import com.huai.gamesdk.bean.Mode;
import com.huai.gamesdk.solid.GameSdkConstants;

import android.util.Log;

public final class GameSdkLog {
	private static GameSdkLog log = null;
	private boolean isDebug;

	private GameSdkLog() {
		isDebug = (GameSdkConstants.mode == Mode.debug);
	}

	/**
	 * NSDK内部调试日志
	 * 
	 * @param msg
	 */
	public void v(String msg) {
		if (isDebug) {
			Log.v(GameSdkConstants.TAG, msg);
		}
	}

	/**
	 * NSDK内部调试日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void v(String msg, Throwable throwable) {
		if (isDebug) {
			Log.v(GameSdkConstants.TAG, msg, throwable);
		}
	}

	/**
	 * 游戏接入SDK时需要查看的日志
	 * 
	 * @param msg
	 */
	public void i(String msg) {
		Log.i(GameSdkConstants.TAG, msg);
	}

	/**
	 * 游戏接入SDK时需要查看的日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void i(String msg, Throwable throwable) {
		Log.i(GameSdkConstants.TAG, msg, throwable);
	}

	/**
	 * 正式上线所需要的日志
	 * 
	 * @param msg
	 */
	public void w(String msg) {
		Log.w(GameSdkConstants.TAG, msg);
	}

	/**
	 * 正式上线所需要的日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void w(String msg, Throwable throwable) {
		Log.w(GameSdkConstants.TAG, msg, throwable);
	}

	/**
	 * 异常错误日志
	 * 
	 * @param msg
	 */
	public void e(String msg) {
		Log.e(GameSdkConstants.TAG, msg);
		if (!isDebug) {
			// TODO: 上传到服务器中
		}
	}

	/**
	 * 异常错误日志
	 * 
	 * @param msg
	 * @param throwable
	 */
	public void e(String msg, Throwable throwable) {
		Log.e(GameSdkConstants.TAG, msg, throwable);
		if (!isDebug) {
			// TODO: 上传到服务器中
		}
	}

	public static GameSdkLog getInstance() {
		if (log == null) {
			log = new GameSdkLog();
		}
		return log;
	}
}
