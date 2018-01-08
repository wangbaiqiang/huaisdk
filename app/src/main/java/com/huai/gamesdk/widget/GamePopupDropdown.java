package com.huai.gamesdk.widget;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.services.IDataService;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameAssetTool;
import com.huai.gamesdk.tool.GameUiTool;
import com.huai.gamesdk.tool.GameSdkRes;

/**
 * 下拉列表
 */
public final class GamePopupDropdown {
	private Activity activity;
	private List<JSONObject> data;
	private int height;

	public GamePopupDropdown(Activity activity, List<JSONObject> data, int height) {
		this.activity = activity;
		this.height = height;
		this.data = data;
		
	}

	private RelativeLayout layout(TextView view, ImageView button) {
		RelativeLayout layout = new RelativeLayout(activity);
		layout.setPadding(20, 10, 0, 10);
		layout.addView(view);
		layout.addView(button);
		return layout;
	}

	private TextView createUidTxvw(int delBtnId) {
		TextView uidTxvw = new TextView(activity);
		RelativeLayout.LayoutParams uidParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, height);
		uidParams.addRule(RelativeLayout.LEFT_OF, delBtnId);
		uidTxvw.setLayoutParams(uidParams);
		uidTxvw.setSingleLine(true);
		uidTxvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, GameUiTool.getInstance().textSize(19, false));
		uidTxvw.setTextColor(Color.BLACK);
		uidTxvw.setGravity(Gravity.CENTER_VERTICAL);
		return uidTxvw;
	}

	private ImageView createButton() {
		RelativeLayout.LayoutParams delParams = new RelativeLayout.LayoutParams((int)(GameSdkConstants.DEVICE_INFO.windowWidthPx * 0.1), height);
		delParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		delParams.addRule(RelativeLayout.CENTER_VERTICAL);
		delParams.setMargins(0, 0, 0, 0);
		ImageView delBtn = new ImageView(activity);
		delBtn.setLayoutParams(delParams);
		delBtn.setScaleType(ImageView.ScaleType.CENTER);
		delBtn.setImageDrawable(GameAssetTool.getInstance().decodeDensityDpiDrawable(activity, "gamesdk_del_normal.png"));
		return delBtn;
	}

	public void create(final View dropDownView, final EditText uidEdtx, final EditText pwdEdtx) {
		final ListView listView = new ListView(activity);
		final PopupWindow window = new PopupWindow(listView, dropDownView.getWidth(), LayoutParams.WRAP_CONTENT);

		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				final ViewHolder holder;
				if (convertView == null) {
					holder = new ViewHolder();
					holder.btn = createButton();
					holder.tv = createUidTxvw(holder.btn.getId());
					convertView = layout(holder.tv, holder.btn);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				try {
					holder.tv.setText(data.get(position).getString("username"));
				} catch (Exception e) {
					Log.e(GameSdkConstants.TAG, "下拉列表异常：", e);
					return convertView;
				}

				holder.tv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							String uid = data.get(position).getString("username");
							uidEdtx.setText(uid);
							uidEdtx.setSelection(uid.length());
							if (pwdEdtx != null) {
								String pwd = data.get(position).getString("password");
								pwdEdtx.setText(pwd);
								pwdEdtx.setSelection(pwd.length());
							}
						} catch (Exception e) {
							Log.e(GameSdkConstants.TAG, "点击Item列表异常：", e);
						}
						if (window.isShowing()) {
							window.dismiss();
						}
					}
				});

				holder.btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (position < 0 || position >= data.size()) {
							return;
						}
						try {
							IDataService operat = Dispatcher.getInstance().getIdaoFactory(activity);
							operat.delteUid(data.get(position).toString());
							if (data.get(position).getString("username").equals(uidEdtx.getText().toString())) {
								uidEdtx.setText("");
							}

							// 账号登录，因为手机登录没有密码输入框
							if (pwdEdtx != null) {
								pwdEdtx.setText("");
							}
							data.remove(position);
							notifyDataSetChanged();
						} catch (Exception e) {
							Log.e(GameSdkConstants.TAG, "删除Item选项异常：", e);
						}
					}
				});
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public int getCount() {
				return data.size();
			}
		};

		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.rgb(214, 220, 222)));
		listView.setDividerHeight(1);
		listView.setBackgroundResource(GameSdkRes.getRes().getDrawableId(activity, "gamesdk_drop_list"));

		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.showAsDropDown(dropDownView);
	}

	private class ViewHolder {
		public TextView tv;
		public ImageView btn;
	}
}
