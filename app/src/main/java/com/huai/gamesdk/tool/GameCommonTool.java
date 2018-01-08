package com.huai.gamesdk.tool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.huai.gamesdk.bean.Coupon;

public class GameCommonTool {

	public static  long lastClickTime = 0;
	/**
	 * 判断按钮是否快速点击
	 * @return true, 快速点击
	 */
	public static  boolean isFastClick() {
		long now = System.currentTimeMillis();
		long timediff = now - lastClickTime;
		lastClickTime = now;
		GameSdkLog.getInstance().i("last:"+lastClickTime+"----now:"+now );
		if (timediff < 1000) {
			return true;
		}
		return false;
	}
	public static String getIp(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager.isWifiEnabled()) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				// 获取32位整型IP地址
				int ipAddress = wifiInfo.getIpAddress();
				return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
						(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
						(ipAddress >> 24 & 0xff));
			}
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "unknow";

	}
	
	public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    } 
	
	
	public static ArrayList<Coupon> getCouponsFromJsonStr(String couponLsitArray){
		ArrayList<Coupon> coupons=new ArrayList<Coupon>();
		if ("".equals(couponLsitArray)||couponLsitArray==null) {
			return null;
		}
		try {
			JSONArray  array=new JSONArray(couponLsitArray);
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < array.length(); i++) {
				Coupon coupon=new Coupon();
				JSONObject object= (JSONObject) array.get(i);
				if (object!=null) {
					coupon.setId(object.getInt("id"));
					coupon.setMoney(object.getInt("money"));
					coupon.setState(object.getInt("state"));
					coupon.setDeadLine(format.parse(object.getString("deadLine")));
					coupons.add(coupon);
				}
			}
			return coupons;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    } 
	// 应用资源 layout drwable id 等等....
		public static int getResourceId(Context context, String name, String defType) {
			return context.getResources().getIdentifier(name, defType,
					context.getPackageName());
		}
	
}
