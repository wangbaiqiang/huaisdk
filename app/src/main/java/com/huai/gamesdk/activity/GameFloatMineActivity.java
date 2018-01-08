package com.huai.gamesdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameSdkRes;


public class GameFloatMineActivity extends Activity {
	private ImageView mIvBack;
	private RelativeLayout mRlName;
	private RelativeLayout mRlUid;
	private RelativeLayout mRlPW;
	private RelativeLayout mRlBind;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(GameSdkRes.getRes().getLayoutId(this, "gamesdk_layout_mine_ui"));
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		setFinishOnTouchOutside(false);
		initView();
		initData();
		initEvent();
	}
	private void initView(){
		mIvBack = (ImageView) findViewById(GameCommonTool.getResourceId(this,
				"iv_back", "id"));
		mRlName = (RelativeLayout) findViewById(GameCommonTool.getResourceId(this,
				"rl_name", "id"));
		mRlUid = (RelativeLayout) findViewById(GameCommonTool.getResourceId(this,
				"rl_uid", "id"));
		mRlPW = (RelativeLayout) findViewById(GameCommonTool.getResourceId(this,
				"rl_pw", "id"));
		mRlBind = (RelativeLayout) findViewById(GameCommonTool.getResourceId(this,
				"rl_bind", "id"));
	}
	private void initData(){
		if (mIntent == null) {
			mIntent = new Intent(GameFloatMineActivity.this, GameFloatWindowActivity.class);
		}
	}
	private void initEvent(){
		mIvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				GameFloatMineActivity.this.finish();
			}
		});
		mRlName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
		mRlPW.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
		mRlBind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mIntent.putExtra("url", "www.baidu.com");
				startActivity(mIntent);
			}
		});
	}
}
