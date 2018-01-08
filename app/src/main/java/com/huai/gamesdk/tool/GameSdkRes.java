package com.huai.gamesdk.tool;

import android.content.Context;

public class GameSdkRes {
	private static GameSdkRes resource = null;
	
	private GameSdkRes(){
	}
	
	public int getLayoutId(Context context, String name) {
		return context.getResources().getIdentifier(name, "layout", context.getPackageName());
	}
	
	public int getRStringId(Context context, String name) {
		return context.getResources().getIdentifier(name, "string", context.getPackageName());
	}
	
	public int getDrawableId(Context context, String name) {
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
	
	public int getResId(Context context, String name) {
		return context.getResources().getIdentifier(name, "id", context.getPackageName());
	}
	
	public String getRString(Context context, String name) {
		return context.getApplicationContext().getString(getRStringId(context, name));
	}
	
	public static GameSdkRes getRes() {
		if (resource == null) {
			resource = new GameSdkRes();
		}
		return resource;
	}
}
