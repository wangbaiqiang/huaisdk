package com.huai.gamesdk.tool;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

public class GameSmsTool {
	
	public static Uri smsUri= Uri.parse("content://sms/");
	public static boolean hasPermission=false;
	//尝试取得短信权限,若用户没授权,会弹窗提示
	public static void startSmsListen(Activity activity){
		try{
			hasSmsPermission(activity);
			if(hasPermission){
				ContentResolver cr=activity.getContentResolver();
				String[] projection=new String[]{"body"};
				String where=" read=0";
				Cursor cur=cr.query(GameSmsTool.smsUri, projection, where, null, "date desc limit 1");
				cur.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//权限是否已取得判断(manifest有配置,即true,但用户不一定授权)
	public static void  hasSmsPermission(Activity activity){
		PackageManager pm = activity.getPackageManager();
		boolean rece=(PackageManager.PERMISSION_GRANTED==pm.checkPermission("android.permission.RECEIVE_SMS", activity.getPackageName()));
		boolean read=(PackageManager.PERMISSION_GRANTED==pm.checkPermission("android.permission.READ_SMS", activity.getPackageName()));
		if(read&&rece){
			hasPermission=true;
		}
	}
}
