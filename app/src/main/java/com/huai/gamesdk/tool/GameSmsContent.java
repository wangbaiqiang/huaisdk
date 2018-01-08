package com.huai.gamesdk.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.widget.EditText;
/**
 *自动读取短信验证码
 *
 */
public class GameSmsContent extends ContentObserver {
	 public static final String SMS_URI_INBOX = "content://sms/inbox";
	 private Activity activity = null;
	 private String smsContent = "";
	 private EditText verifyText = null;
	 public GameSmsContent(Activity activity, Handler handler, EditText verifyText) {
	  super(handler);
	  this.activity = activity;
	  this.verifyText = verifyText;
	 }
	 @Override
	 public void onChange(boolean selfChange) {
	  super.onChange(selfChange);
	  	Cursor cursor = null;// 光标
	  	ContentResolver cr=activity.getContentResolver();
	  	String[] projection=new String[]{"body"};
	  	String where=" read = 0";
	  	cursor=cr.query(GameSmsTool.smsUri, projection, where, null, "date desc limit 3");
	  if (cursor != null){// 如果短信为未读模式
		  cursor.moveToFirst();
		  if (cursor.moveToFirst()) {
			  smsContent = cursor.getString(cursor.getColumnIndex("body"));
			  if(smsContent.indexOf("游戏")!=-1){
				  Pattern p=Pattern.compile("[0-9]{4,}");
				  Matcher m = p.matcher(smsContent);
				  if(m.find()){
					  String smsbody=m.group(0);
					  if(verifyText != null && smsbody != null && smsbody.length()>0){
					   verifyText.setText(smsbody);
					  }
			      }
	    
	          }

	      }

	   }
     }
}

