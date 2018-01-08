package com.huai.gamesdk.services;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameChannelUtil;
import com.huai.gamesdk.tool.GameSdkLog;


public class DeviceInfo {

	private Map<Integer, Windows> densityWin;
	public String sid;
	public String ip;
	public String mac;
	public String imei;
	public String phoneNum;
	public String channel;
	public int densityDpi = -1;
	public int widthPixels = -1;
	public int heightPixels = -1;
	public int windowWidthPx = 0;
	public int windowHeightPx = 0;

	private static WifiManager wifiManager;

	@SuppressLint("UseSparseArrays")
	private DeviceInfo() {
		densityWin = new HashMap<Integer, Windows>();
//		densityWin.put(640, new Windows(1628, 1000));
	}


	public static String getDeviceImei(Context context) {
		TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imie = telephonyMgr.getDeviceId();
		if(imie == null || "".equals(imie)){
			final IDataService operatData = Dispatcher.getInstance().getIdaoFactory(context);
			if(operatData.getImie() == null || "".equals(operatData.getImie())){
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				Random r = new Random();
			    imie = "autoimei"+sdf.format(date).toString()+String.valueOf(r.nextInt(100));
			    operatData.writeImie(imie);
			    return imie;
			}else{
				return operatData.getImie();
			}
			
		}else{
			return imie;
		}
	}

	@SuppressLint("DefaultLocale")
	public static String getDeviceIP(Context context) {
		if (wifiManager == null) {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		}
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";

	}


	public static String getDeviceMac(Context context) {
		if (wifiManager == null) {
			wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}

		if (wifiManager.isWifiEnabled()) {
			WifiInfo info = wifiManager.getConnectionInfo();
			return info.getMacAddress();
		}
		return "";
	}
	

	public static String getMetaData(Context activity, String key) {
		try {
			ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
			return "" + appInfo.metaData.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public String getPhoneNum(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneManager.getLine1Number();
	}
	/**
	 * 计算窗口的宽高  调整大小
	 */
	public void calculateWindows() {
		Windows windows = densityWin.get(densityDpi);
//		windows = new Windows((int) (widthPixels * 0.675), (int) (heightPixels * 0.73));
			if (GameSdkConstants.isPORTRAIT) {
//			竖屏:	//
					windows = new Windows((int) (widthPixels * 0.53), (int) (heightPixels * 0.60));
				}else{
//			横屏:
					windows = new Windows((int) (widthPixels * 0.50), (int) (heightPixels * 0.58));
				}
		
		
		
		if (widthPixels < windows.width) {
			windowWidthPx = widthPixels;
		} else {
			windowWidthPx = windows.width;
		}

		if (heightPixels < windows.height) {
			windowHeightPx = heightPixels;
		} else {
			windowHeightPx = windows.height;
		}
		windowHeightPx=(int) (windowHeightPx*0.9);
	}

	/**
	 * check hashttp?
	 * @param context
	 * @return
	 */
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static DeviceInfo init(Context context) {
		DeviceInfo device = new DeviceInfo();
		device.ip = device.getDeviceIP(context);
		device.mac = device.getDeviceMac(context);
		device.imei = device.getDeviceImei(context);
		device.phoneNum = device.getPhoneNum(context);
		device.channel=device.getApkChannel(context);

		
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int currentOrientantion = context.getResources().getConfiguration().orientation;
		device.densityDpi = metrics.densityDpi;
		float density = metrics.density;
		GameSdkLog.getInstance().i("density=" + density + ", densityDpi=" + metrics.densityDpi);


		if (currentOrientantion == Configuration.ORIENTATION_PORTRAIT) {
			device.widthPixels = metrics.heightPixels;
			device.heightPixels = metrics.widthPixels;
		} else {
			device.widthPixels = metrics.widthPixels;
			device.heightPixels = metrics.heightPixels;
		}
		device.calculateWindows();
		return device;
	}
	
	public static String getApkChannel(Context context) {
		return GameChannelUtil.getChannel(context);
	}

	private static class Windows {
		public int width;
		public int height;

		public Windows(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
