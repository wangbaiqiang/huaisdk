package com.huai.gamesdk.widget;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.huai.gamesdk.tool.GameSdkRes;
import com.huai.gamesdk.tool.GameSdkLog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GameBrowserNotification extends Notification {
	private Context context;

	public GameBrowserNotification(Context context) {
		this.context = context;
	}

	public void showNotification(String title, String content, String url) {
		final int NOTIFY_ID = 999;
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = getBrowserAppIntent(url);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

		this.tickerText = title;
		this.icon = GameSdkRes.getRes().getDrawableId(context, "yhsdk_notifi_logo");
		this.defaults |= DEFAULT_SOUND;
		this.defaults |= DEFAULT_LIGHTS;

		this.ledARGB = 0x00FF00;
		this.ledOffMS = 1000;
		this.ledOnMS = 1000;
		this.flags |= FLAG_SHOW_LIGHTS;
		this.flags |= FLAG_AUTO_CANCEL;
		this.setLatestEventInfo(context, title, content, pendingIntent);

		ViewGroup group = (ViewGroup) contentView.apply(context, null);
		findView(group, new ViewVisitor() {
			@Override
			public void onFindView(View view) {
				// 查找这个布局下的ImageView就是icon控件
				if (view instanceof ImageView) {
					InputStream inputStream = null;
					try {
						inputStream = context.getResources().getAssets().open("gamesdk/images/gamesdk_notifi_logo.png");
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						Matrix matrix = new Matrix();
						matrix.postScale(0.8F, 0.8F);
						Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
						contentView.setImageViewBitmap(view.getId(), newbm);// 设置icon图片
					} catch (Exception e) {
						GameSdkLog.getInstance().e("读取\"gamesdk/images/gamesdk_notifi_logo.png\"资源异常：", e);
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e) {
								GameSdkLog.getInstance().e("输入流关闭异常", e);
							}
						}
					}
				} else if (view instanceof TextView) {
					TextView textView = ((TextView) view);

					if (textView.getTextSize() <= 13f) {
						contentView.setFloat(view.getId(), "setTextSize", 14);
					}
					contentView.setBoolean(view.getId(), "setSingleLine", false);
				}
			}
		});

		manager.notify(NOTIFY_ID, this);
	}

	private Intent getBrowserAppIntent(String strUrl) {
		ActivityInfo browserActivity = getBrowserApp();
		if (browserActivity != null) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_uri_browsers = Uri.parse(strUrl);
			intent.setData(content_uri_browsers);
			intent.setClassName(browserActivity.packageName, browserActivity.name);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			return intent;
		}
		return null;
	}

	private ActivityInfo getBrowserApp() {
		String defaultBrowser = "android.intent.category.DEFAULT";
		String browsable = "android.intent.category.BROWSABLE";
		String view = "android.intent.action.VIEW";

		Intent intent = new Intent(view);
		intent.addCategory(defaultBrowser);
		intent.addCategory(browsable);
		Uri uri = Uri.parse("http://");
		intent.setDataAndType(uri, null);

		// 找出手机当前安装的所有浏览器程序
		List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

		if (resolveInfoList.size() > 0) {
			ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
			// String packageName = activityInfo.packageName;
			// String className = activityInfo.name;
			return activityInfo;
		} else {
			return null;
		}
	}

	private interface ViewVisitor {
		void onFindView(View view);

	}

	/**
	 * 查找视图里面的所有子视图
	 * 
	 * @param group
	 * @param visitor
	 */
	private void findView(ViewGroup group, ViewVisitor visitor) {

		for (int i = 0; i < group.getChildCount(); i++) {
			View view = group.getChildAt(i);
			if (visitor != null) {
				visitor.onFindView(view);
			}
			if (view instanceof ViewGroup) {
				findView((ViewGroup) view, visitor);
			}
		}

	}

}
