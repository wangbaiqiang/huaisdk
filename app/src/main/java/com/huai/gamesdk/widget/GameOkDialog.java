package com.huai.gamesdk.widget;

import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class GameOkDialog extends  Dialog {
    private String title;
    private String content;
    private TextView titleTv;
    private TextView contentTv;
    private TextView leftBtn;
    private TextView rightBtn;
    
    
    
    private  View.OnClickListener rightListener;
    private  View.OnClickListener leftListener;
    boolean  isButton=false;
    private  LinearLayout mainLayout;
	
	/**
	 * @param context
	 * @param title
	 * @param content
	 * @param isButton
	 */
	public GameOkDialog(Context context, String title, String content, boolean isButton) {
		super(context);
		this.title=title;
		this.content=content;
		this.isButton=isButton;
		init();
	}
	
	
	
	/*初始化布局*/
	private void init() {
		
		LinearLayout.LayoutParams lpmainLay=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	     
		
		mainLayout =new LinearLayout(getContext());
		mainLayout.setMinimumWidth(GameUiTool.dp2px(getContext(), 250));
		mainLayout.setMinimumHeight(GameUiTool.dp2px(getContext(), 140));
		mainLayout.setLayoutParams(lpmainLay);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundResource(GameSdkRes.getRes().getDrawableId(getContext(),
				"gamesdk_corner_windows"));
		
		LinearLayout  titleLay =new LinearLayout(getContext());
		LinearLayout.LayoutParams lptitleLay=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleLay.setLayoutParams(lptitleLay);
		titleLay.setOrientation(LinearLayout.VERTICAL);
		titleLay.setBackgroundResource(GameSdkRes.getRes().getDrawableId(getContext(),
				"gamesdk_top_concer"));
		
		LinearLayout.LayoutParams lptitletv=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lptitletv.gravity=Gravity.CENTER_HORIZONTAL;
		lptitletv.setMargins(GameUiTool.dp2px(getContext(), 8), GameUiTool.dp2px(getContext(), 10), GameUiTool.dp2px(getContext(), 8), GameUiTool.dp2px(getContext(), 10));
		titleTv=new TextView(getContext());
		titleTv.setText(title);
		titleTv.setTextSize(16);
		titleTv.setTextColor(Color.BLACK);
		titleTv.setLayoutParams(lptitletv);
		
		titleLay.addView(titleTv);
		
		RelativeLayout  contentLay =new RelativeLayout(getContext());
		LinearLayout.LayoutParams lpcontentLay=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
		lpcontentLay.setMargins(GameUiTool.dp2px(getContext(), 16), GameUiTool.dp2px(getContext(), 6), GameUiTool.dp2px(getContext(), 16), GameUiTool.dp2px(getContext(), 6));
		contentLay.setLayoutParams(lpcontentLay);
		
		
		RelativeLayout.LayoutParams lpcontenttv=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpcontenttv.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentTv=new TextView(getContext());
		contentTv.setText(content);
		contentTv.setSingleLine(false);
		contentTv.setTextSize(15);
		contentTv.setTextColor(Color.BLACK);
		contentTv.setMaxWidth(GameUiTool.dp2px(getContext(), 200));
		contentTv.setLayoutParams(lpcontenttv);
		contentLay.addView(contentTv);
		
		
		LinearLayout  footerLay =new LinearLayout(getContext());
		LinearLayout.LayoutParams lpfooterLay=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		footerLay.setPadding(0, GameUiTool.dp2px(getContext(), 6), 0, GameUiTool.dp2px(getContext(), 20));
		footerLay.setLayoutParams(lpfooterLay);
		footerLay.setOrientation(LinearLayout.HORIZONTAL);
		if (!isButton) {
			LinearLayout  leftLay =new LinearLayout(getContext());
			LinearLayout.LayoutParams lpleftLay=new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,1);
			leftLay.setLayoutParams(lpleftLay);
			leftLay.setOrientation(LinearLayout.VERTICAL);
			
			LinearLayout  rightLay =new LinearLayout(getContext());
			LinearLayout.LayoutParams lprightLay=new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT,1);
			rightLay.setLayoutParams(lprightLay);
			
			rightLay.setOrientation(LinearLayout.HORIZONTAL);
			
			LinearLayout.LayoutParams lpLbtn=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpLbtn.setMargins(0, 0, GameUiTool.dp2px(getContext(), 8), 0);
			lpLbtn.gravity=Gravity.RIGHT;
			
			LinearLayout.LayoutParams lpRbtn=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpRbtn.setMargins(GameUiTool.dp2px(getContext(), 8), 0, 0, 0);
			lpRbtn.gravity=Gravity.LEFT;
			
			rightBtn=new TextView(getContext());
			rightBtn.setText("确定");
			rightBtn.setTextSize(16);
			rightBtn.setTextColor(Color.WHITE);
			rightBtn.setBackgroundResource(GameSdkRes.getRes().getDrawableId(getContext(),
					"gamesdk_corner_submit_btn"));
			rightBtn.setPadding(GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(),3), GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(), 3));
			rightBtn.setLayoutParams(lpRbtn);
			rightBtn.setSelected(true);
			rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (rightListener!=null) {
						rightListener.onClick(rightBtn);
					}
					dismiss();
				}
			});
			rightLay.addView(rightBtn);
			
			leftBtn=new TextView(getContext());
			leftBtn.setText("取消");
			leftBtn.setTextSize(16);
			leftBtn.setTextColor(Color.WHITE);
			leftBtn.setPadding(GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(), 3), GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(), 3));
			leftBtn.setBackgroundResource(GameSdkRes.getRes().getDrawableId(getContext(),
					"gamesdk_corner_submit_btn"));
			leftBtn.setLayoutParams(lpLbtn);
			leftBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (leftListener!=null) {
						leftListener.onClick(leftBtn);
					}
					dismiss();
				}
			});
			leftLay.addView(leftBtn);
			
			footerLay.addView(leftLay);
			footerLay.addView(rightLay);
			
		}else {
			footerLay.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams lprightv=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lprightv.gravity=Gravity.CENTER_HORIZONTAL;
			rightBtn=new TextView(getContext());
			rightBtn.setText("确定");
			rightBtn.setTextSize(16);
			rightBtn.setTextColor(Color.WHITE);
			rightBtn.setLayoutParams(lprightv);
			rightBtn.setSelected(true);
			rightBtn.setPadding(GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(), 3), GameUiTool.dp2px(getContext(), 26), GameUiTool.dp2px(getContext(), 3));
			rightBtn.setBackgroundResource(GameSdkRes.getRes().getDrawableId(getContext(),
					"gamesdk_corner_submit_btn"));
			rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (rightListener!=null) {
						rightListener.onClick(rightBtn);
					}
					dismiss();
				}
			});
			footerLay.addView(rightBtn);
		}
		
	
		mainLayout.addView(titleLay);
		mainLayout.addView(contentLay);
		mainLayout.addView(footerLay);
	}


     
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		if (mainLayout!=null) {
			setContentView(mainLayout);
		}
	
	}
	
  	public void setOnConfirmClickListener(android.view.View.OnClickListener listener){
		this.rightListener=listener;
	}

}
