package com.huai.gamesdk.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

public class GameSmsObserver extends ContentObserver{
	private Activity smsActivity;
	private Handler smsHandler;
	private static boolean success=false;
	public GameSmsObserver(Activity activity, Handler handler) {
		super(handler);
		smsActivity=activity;
		smsHandler=handler;
		//注册监听
		smsActivity.getContentResolver().registerContentObserver(GameSmsTool.smsUri, true, this);
		success=false;
	}
	
	@Override
	public void onChange(boolean selfChange){
		//读取时,有可能短信还未入库,作多一次尝试
		if(!success){//一条新短信,可能会触发两次onChange
			getSmsContent(1000);
			if(!success){
				getSmsContent(2000);
			}
		}
		//注销监听
		smsActivity.getContentResolver().unregisterContentObserver(this);
	}
	
	//读取短信
	public void getSmsContent(int delay){
		try{
			//有可能短信未入库,作延时
			Thread.sleep(delay);
			ContentResolver cr=smsActivity.getContentResolver();
			String[] projection=new String[]{"body"};
			String where=" read=0";
			Cursor cur=cr.query(GameSmsTool.smsUri, projection, where, null, "date desc limit 3");
			if(cur!=null){
				while(cur.moveToNext()){
					String smsBody=cur.getString(cur.getColumnIndex("body"));
					if(smsBody.indexOf("游戏")!=-1){
						Pattern p=Pattern.compile("[0-9]{4,}");
						Matcher m = p.matcher(smsBody);
						if(m.find()){
							smsBody=m.group(0);
							if(smsBody.length()<7){
								Message msg=new Message();
								msg.what=1;
								msg.obj=(Object)smsBody;
								smsHandler.sendMessage(msg);
								success=true;
								break;
							}
						}
					}
				}
				cur.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
