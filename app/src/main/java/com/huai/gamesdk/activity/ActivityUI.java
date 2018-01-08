package com.huai.gamesdk.activity;

import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;

import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.LinearLayout;

public abstract class ActivityUI {
	public static final String INPUT_TYPE_CONTENT = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String INPUT_TYPE_NUMBER = "0123456789";
	protected GameAssetTool asset;
	protected GameUiTool uitool;
	private long lastClickTime = 0;
	
	public ActivityUI() {
		asset = GameAssetTool.getInstance();
		uitool = GameUiTool.getInstance();
	}

	
//	！！！生成具体的布局(子类)！！！
	public abstract LinearLayout onCreate(Activity activity);

	/**
	 * set window params
	 * @param activity
	 */
	public void onSetWindows(Activity activity) {
		WindowManager.LayoutParams wmparams = activity.getWindow().getAttributes();
		
		// 竖屏:
		if (GameSdkConstants.isPORTRAIT) {
			wmparams.width = GameSdkConstants.DEVICE_INFO.windowWidthPx ;
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = GameSdkConstants.DEVICE_INFO.windowHeightPx;
			}
		} else {
			wmparams.width = GameSdkConstants.DEVICE_INFO.windowWidthPx;
			if (GameSdkConstants.DEVICE_INFO.windowHeightPx > 0) {
				wmparams.height = GameSdkConstants.DEVICE_INFO.windowHeightPx;
			}
			/*		
//			layout set alpha
			wmparams.alpha = 1F;
			wmparams.dimAmount = 0F;
			activity.getWindow().setAttributes(wmparams);
		}*/
			
		}
	}
	
	/**
	 *proxy Activity method imp
	 */
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {}
	public void  onResume(){}
	public void  onDestroy(){}
	public void onBackPressed() {}

	/**
	 * the button is fastClick?
	 */
	protected boolean isFastClick() {
		long now = System.currentTimeMillis();
		long timediff = now - lastClickTime;
		lastClickTime = now;
		if (timediff < 1000) {
			return true;
		}
		return false;
	}
}
