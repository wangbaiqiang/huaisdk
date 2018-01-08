package com.huai.gamesdk.tool;

import android.util.Log;

import com.huai.gamesdk.solid.GameSdkConstants;

/**
 * 
 * @author tzw
 * 全局日志
 */
public class GameSdkLogger {
//	普通调试信息
	public static void i(String msg){
        if (GameSdkConstants.user_Logger){
            Log.i("GameSdk", msg);
        }
    }
	
//	普通调试：http请求参数
	public static void httpRequestParams(String msg){
		 if (GameSdkConstants.user_Logger){
	            Log.i("GameRequestParams", msg);
	        }
	}

//	普通调试：http响应体
	public static void httpResponseBody(String msg){
		 if (GameSdkConstants.user_Logger){
	            Log.i("GameResponseBody", msg);
	        }
	}
}
