 package com.huai.gamesdk.solid;



import com.huai.gamesdk.bean.AppInfo;
import com.huai.gamesdk.bean.Mode;
import com.huai.gamesdk.bean.OrderInfo;
import com.huai.gamesdk.bean.RoleInfo;
import com.huai.gamesdk.services.DeviceInfo;


public final class GameSdkConstants {

    public static final String TAG = "GameSdk";
    public static AppInfo APPINFO = new AppInfo();
    public static DeviceInfo DEVICE_INFO = null;
    public static OrderInfo ORDER_INFO = null;
    public static String COMPANY = null;
    public static RoleInfo ROLE_INFO= null;
    public static Mode mode = Mode.release;
    public final static int USERNAME_LOGIN_MIN_LEN = 4;
    public final static int USERNAME_LOGIN_MAX_LEN = 12;
    public final static int PASSWORD_MIN_LEN = 6;
    public final static int PASSWORD_MAX_LEN = 12;
    public static String AUTH_SERVER_URL ="www.baidu.com";
    public static String PAY_SERVER_URL ="www.baidu.com"  ;
    public static String USERCENTER_SERVER_URL ="www.baidu.com" ;
    public static String VERIFY ="www.baidu.com";
    public static String ULO_WX_URL;
    public static String TD_RY_CHANNEL;
    public static String USERAPPCENTER_URL = "www.baidu.com";
    public static boolean isPORTRAIT = false;
    public static boolean user_Logger = true;

    private GameSdkConstants() { }


    public static String getUsercenterUrl() {return USERCENTER_SERVER_URL + "secure/authenticate";}

    public static String getAllCouponCoinUrl() {
        return PAY_SERVER_URL + "12306/getallcoupon";
    }

    public static String getAPPIdwebUserInfo() {
        return USERAPPCENTER_URL + "12306/userinfo";
    }

    public static String getAPPIdwebMessageNew() {return USERAPPCENTER_URL + "12306/messageNew";}

    public static String getAPPIdwebMyGiftBags() {return USERAPPCENTER_URL + "12306/myGiftBags";}

    public static String getAPPIdwebHelpMessage() {return USERAPPCENTER_URL + "12306/helpMessage";}



    public static void init() {
        AUTH_SERVER_URL = "www.baidu.com";
        PAY_SERVER_URL = "www.baidu.com";
        USERCENTER_SERVER_URL = "www.baidu.com";
        USERAPPCENTER_URL = "www.baidu.com";
        VERIFY = "www.baidu.com";
    }
    public static String AUTH_SERVER_URL1 ="www.baidu.com";
    public static String AUTH_SERVER_URL2 ="www.baidu.com";
    public static final String VERSION ="1";
    public static final String PLATFORM ="1";
}
