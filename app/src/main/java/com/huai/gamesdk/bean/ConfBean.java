package com.huai.gamesdk.bean;

import com.huai.gamesdk.activity.ActivityFactory;

public class ConfBean {
	/** 显示信息 */
	public String text;

	/** 显示字体颜色 */
	public int textColor;

	/** 字体大小 */
	public float textSize;

	/** 显示信息 */
	public ComDrawableRect rect;

	/** 要跳转页面 */
	public ActivityFactory activity;
	
	public String extra;
	
	public boolean isClickable = true;

}
