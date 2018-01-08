package com.huai.gamesdk.activity;

import java.text.SimpleDateFormat;

import com.huai.gamesdk.bean.Coupon;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class CouponAdapter extends ArrayAdapter<Coupon>{
    private Context context;
	public CouponAdapter(Context context) {
		super(context, 0);
		this.context=context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final ViewHolder holder;
		 if (convertView==null) {
			 holder=new ViewHolder();
			 LinearLayout mainLayout=new LinearLayout(context);
			 mainLayout.setOrientation(LinearLayout.VERTICAL);
			 LinearLayout contentLayout=new LinearLayout(context); 
			 contentLayout.setOrientation(LinearLayout.HORIZONTAL);
			 
			 ImageView checkTv=new ImageView(context);
			 LinearLayout.LayoutParams lpmoney=new  LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			 lpmoney.setMargins(0, 0, GameUiTool.dp2px(context, 8), 0);
			 checkTv.setLayoutParams(lpmoney);
			 
			 TextView moneyTv=new TextView(context);
			 TextView state=new TextView(context);
			 LinearLayout.LayoutParams lpMoney=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
			 moneyTv.setLayoutParams(lpMoney); 
			 View lineView = GameUiTool.getInstance().createDividerLine((Activity)context, 1);
			 LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 1);
			 linearParams.setMargins(GameUiTool.dp2px(context, 15), 0, GameUiTool.dp2px(context, 15), 0);
			 lineView.setLayoutParams(linearParams);	
			 checkTv.setImageDrawable(GameAssetTool.getInstance()
					.decodeDrawableFromAsset(context,
							"gamesdk/images/gamesdk_money_unselected.png", 2.0f));
			 moneyTv.setTextColor(Color.parseColor("#FE886F"));
			 moneyTv.setSingleLine(true);
			 moneyTv.setTextSize(GameUiTool.px2sp(context, 36));
			 moneyTv.setCompoundDrawablesWithIntrinsicBounds(GameAssetTool.getInstance()
					.decodeDrawableFromAsset(context,
							"gamesdk/images/gamesdk_money_yellow.png", 2.0f), null,
					null, null);
			 moneyTv.setCompoundDrawablePadding(GameUiTool.dp2px(context, 1));
			 
			 
			 state.setText("有效期至:yyyy-MM-DD");
			 state.setTextSize(9);
			 state.setTextColor(Color.parseColor("#C8C8C8"));
			 LinearLayout.LayoutParams lpstate=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			 lpstate.setMargins(0, 0, GameUiTool.dp2px(getContext(), 5), 0);
			 state.setLayoutParams(lpstate);
			 
			 holder.money=moneyTv;
			 holder.checkBox=checkTv;
			 holder.state=state;
			 
			 contentLayout.addView(checkTv);
			 contentLayout.addView(moneyTv);
			 contentLayout.addView(state);
			 contentLayout.setGravity(Gravity.CENTER_VERTICAL);
			 LinearLayout.LayoutParams lpCon=new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			 lpCon.setMargins(GameUiTool.dp2px(context,15), GameUiTool.dp2px(context, 6), GameUiTool.dp2px(context, 15), GameUiTool.dp2px(context, 4));
			 contentLayout.setLayoutParams(lpCon);
			 mainLayout.addView(contentLayout);
			 mainLayout.addView(lineView);
			 
			 convertView=mainLayout;
			 
			 
			 convertView.setTag(holder);
		 }else {
			holder=(ViewHolder) convertView.getTag();
		}
		 
		 holder.money.setVisibility(View.VISIBLE);
		 holder.state.setTextSize(9);
		 holder.state.setTextColor(Color.parseColor("#C8C8C8"));
		 //逻辑处理
		 final Coupon dataCoupon=getItem(position);
		
		 GameUiTool.setMoneyString(holder.money, GameUiTool.getFormatMoney(dataCoupon.money));
		 
		 if (dataCoupon.checkState==1) {
			 holder.checkBox.setImageDrawable(GameAssetTool.getInstance()
					.decodeDrawableFromAsset(context,
                            "gamesdk/images/gamesdk_money_selected.png", 2.0f));
		}else {
			holder.checkBox.setImageDrawable(GameAssetTool.getInstance()
					.decodeDrawableFromAsset(context,
							"gamesdk/images/gamesdk_money_unselected.png", 2.0f));
		}
		 
		if (dataCoupon.state==1)//锁定
		{
			holder.checkBox.setImageDrawable(GameAssetTool.getInstance()
					.decodeDrawableFromAsset(context,
							"gamesdk/images/gamesdk_money_unselected.png", 2.0f));
			holder.state.setText("已锁定,半小时之后解锁");
		}else if (dataCoupon.state==2) //第一项为不用代金卷
		{
			 holder.money.setVisibility(View.GONE);
			 holder.state.setTextSize(14);
			 holder.state.setTextColor(Color.parseColor("#000000"));
			 holder.state.setText("暂不使用代金劵支付");
		}else
		{
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			if (dataCoupon.deadLine!=null) {
				holder.state.setText("有效期至:"+dateFormat.format(dataCoupon.deadLine));
			}
			
		}
		
		convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus&&dataCoupon.state!=1) {
					 holder.checkBox.setImageDrawable(GameAssetTool.getInstance()
							.decodeDrawableFromAsset(context,
                                    "gamesdk/images/gamesdk_money_selected.png", 2.0f));
				}else {
					holder.checkBox.setImageDrawable(GameAssetTool.getInstance()
							.decodeDrawableFromAsset(context,
									"gamesdk/images/gamesdk_money_unselected.png", 2.0f));
				}
			}
		});
	
		
		return convertView;
	}
   static class ViewHolder {
	   ImageView checkBox;
	    TextView money;
	    TextView state;
   }
   
  public void  setItemChecked(int pos){
	   for (int i = 0; i < getCount(); i++) {
		if ( i==pos) {
			getItem(i).checkState=1;
		}else {
			getItem(i).checkState=0;
		}
	   }
	   notifyDataSetChanged();
   }
}
