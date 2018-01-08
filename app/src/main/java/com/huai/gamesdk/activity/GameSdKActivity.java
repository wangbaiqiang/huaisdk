package com.huai.gamesdk.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.huai.gamesdk.activity.ActivityFactory;
import com.huai.gamesdk.activity.ActivityUI;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameSdkLogger;

public final class GameSdKActivity extends Activity {
	private ActivityUI specificActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		if(GameSdkConstants.isPORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// android2.3需要设置setFinishOnTouchOutside(false)
		if (Build.VERSION.SDK_INT >= 11) {
			setFinishOnTouchOutside(false);
		}
		
		Intent intent = getIntent();
		String specificLayout = intent.getStringExtra("layoutId");
		GameSdkLogger.i("replaceActivty：" + specificLayout);
		
		
		try {
			ActivityFactory activityType = ActivityFactory.valueOf(specificLayout.toUpperCase(Locale.getDefault()));
			specificActivity = activityType.getService();
			if (specificActivity == null) {
				return;
			}
			LinearLayout layout = specificActivity.onCreate(this);
			setContentView(layout);

		} catch (Exception e) {
			GameSdkLogger.i("display viewLayout exception："+e.toString());
			return; 
		}
		
		
		specificActivity.onSetWindows(this);
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		specificActivity.onActivityResult(this, requestCode, resultCode, data);
	}
	@Override
	protected void onResume() {
		super.onResume();
		specificActivity.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		specificActivity.onDestroy();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		specificActivity.onBackPressed();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		specificActivity.onSetWindows(this);
	}
	
	

	
}
