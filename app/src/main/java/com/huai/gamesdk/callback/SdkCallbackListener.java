package com.huai.gamesdk.callback;


public interface SdkCallbackListener<T> {
	
	public void callback(int code, T response);
}
