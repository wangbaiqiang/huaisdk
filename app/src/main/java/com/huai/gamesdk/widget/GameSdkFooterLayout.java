package com.huai.gamesdk.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huai.gamesdk.bean.ConfBean;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

public class GameSdkFooterLayout extends LinearLayout {

	private GameSdkFooterLayout(Context context) {
		super(context);
	}

	public static class Builder {
		private Context context;
		private ConfBean left;
		private ConfBean right;
		

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setLeft(ConfBean left) {
			this.left = left;
			return this;
		}

		public Builder setRight(ConfBean right) {
			this.right = right;
			return this;
		}

		

		public GameSdkFooterLayout build() {
			GameSdkFooterLayout mainlayout = new GameSdkFooterLayout(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,-2,1);
			int hmargin = (int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.02);
			int vmargin = (int)(GameSdkConstants.DEVICE_INFO.windowHeightPx * 0.04);
			params.setMargins(hmargin,0, hmargin, 0);
			mainlayout.setLayoutParams(params);
			mainlayout.setOrientation(LinearLayout.VERTICAL);
			mainlayout.setGravity(Gravity.CENTER);
			//横线
			View v= new ImageView(context);
			v.setLayoutParams(new LinearLayout.LayoutParams(-1,1));
			v.setBackgroundColor(Color.rgb(179, 179, 179));
			mainlayout.addView(v);
			//footer
			GameSdkFooterLayout layout = new GameSdkFooterLayout(context);
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1);
			linearParams.setMargins(0, vmargin, 0, vmargin);
			layout.setLayoutParams(linearParams);
			//设置上边距
			layout.setPadding(0, 0, 0, 0);
			layout.setOrientation(LinearLayout.HORIZONTAL);
//			layout.setGravity(Gravity.CENTER);

			if (left != null) {
				LinearLayout	mainleftLayout=new LinearLayout(context);
				mainleftLayout.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1));
				mainleftLayout.setGravity(Gravity.CENTER);
				LinearLayout leftLayout = new LinearLayout(context);
				LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, -1);
				leftParams.setMargins(0, 0, 0, 0);
				leftLayout.setLayoutParams(leftParams);
				leftLayout.setGravity(Gravity.LEFT);
				leftLayout.setOrientation(LinearLayout.HORIZONTAL);
				

				Button leftBtn = new Button(context);
				leftBtn.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
				leftBtn.setPadding(vmargin*2, 0, 0, 0);
				leftBtn.setText(left.text);
				leftBtn.setTextColor(left.textColor);
				if (left.textSize <= 0) {
					leftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));
				} else {
					leftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, left.textSize);
				}
				leftBtn.setBackgroundColor(Color.rgb(250, 251, 252));
				leftBtn.setGravity(Gravity.CENTER_VERTICAL);
				if (left.rect != null) {
					leftBtn.setCompoundDrawablesWithIntrinsicBounds(left.rect.left, left.rect.top, left.rect.right, left.rect.bottom);
				}

				if (left.isClickable && left.activity != null) {
					leftBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Dispatcher.getInstance().showActivity(context, left.activity, null);
						}
					});
				}
                
				leftLayout.addView(leftBtn);
				TextView recommend = new TextView(context);
				
				//recommend.setText(GameassetTool.getInstance().getLangProperty(context, "account_login_recommend"));
				recommend.setText("推荐");
				recommend.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(12F, false));
				recommend.setTextColor(Color.RED);
				recommend.setBackgroundResource(GameSdkRes.getRes().getDrawableId(context, "gamesdk_corner_recommend"));
//                leftLayout.addView(recommend);
                mainleftLayout.addView(leftLayout);
//                leftLayout.setVisibility(View.GONE);
				layout.addView(mainleftLayout);
			}
			if (right != null) {
				LinearLayout rightLayout = new LinearLayout(context);
				LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT, 1);
				rightLayout.setOrientation(LinearLayout.HORIZONTAL);
				rightParams.setMargins(0, 0, 0, 0);
				rightLayout.setLayoutParams(rightParams);
				rightLayout.setGravity(Gravity.CENTER);

				Button rightBtn = new Button(context);
				rightBtn.setLayoutParams(new LinearLayout.LayoutParams(-2,-1));
				rightBtn.setPadding(0, 0, 0, 0);
				rightBtn.setText(right.text);
				rightBtn.setTextColor(right.textColor);
				rightBtn.setBackgroundColor(Color.rgb(250, 251, 252));
				rightBtn.setGravity(Gravity.CENTER);
				
				if (right.textSize <= 0) {
					rightBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(16F, false));
				} else {
					rightBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, right.textSize);
				}

				

				if (right.rect != null) {
					rightBtn.setCompoundDrawablesWithIntrinsicBounds(right.rect.left, right.rect.top, right.rect.right, right.rect.bottom);
				}

				if (right.isClickable && right.activity != null) {
					rightBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
								
								try {
									Dispatcher.getInstance().getAccount((Activity)context);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
			   		});
				}

				rightLayout.addView(rightBtn);
				layout.addView(rightLayout);
			}
			mainlayout.addView(layout);
			return mainlayout;
		}
	}

}
