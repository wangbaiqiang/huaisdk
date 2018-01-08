package com.huai.gamesdk.widget;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huai.gamesdk.activity.GameFloatWindowActivity;
import com.huai.gamesdk.bean.FloatSendBean;
import com.huai.gamesdk.services.Dispatcher;
import com.huai.gamesdk.services.IDataService;
import com.huai.gamesdk.solid.GameSdkConstants;
import com.huai.gamesdk.tool.GameCommonTool;
import com.huai.gamesdk.tool.GameMD5Tool;
import com.huai.gamesdk.tool.GameSdkLog;

/**
 * big floatwindow
 */
public class GameFloatWindowBigView extends LinearLayout {
    private View bigview;
    private View contentView;
    private Context context;
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private int leftOrRight;
    private LinearLayout layout0, layout1, layout2, layout3;
    private ImageView imageView;
    private Handler handler;
    Runnable closeRunable = new Runnable() {
        @Override
        public void run() {
            try {
                bigFloatExitAnimation(leftOrRight);
            } catch (Exception e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
                GameSdkLog.getInstance().e("exception...");
            }
        }
    };

    public GameFloatWindowBigView(Context _context, int _leftOrRight) {
        super(_context);
        // TODO Auto-generated constructor stub
        this.leftOrRight = _leftOrRight;

        this.context = _context;
        if (_leftOrRight == LEFT) {
            LayoutInflater.from(context).inflate(
                    GameCommonTool.getResourceId(context, "gamesdk_layout_big_float_left",
                            "layout"), this);
        } else {
            LayoutInflater.from(context).inflate(
                    GameCommonTool.getResourceId(context, "gamesdk_layout_big_float_right",
                            "layout"), this);
        }

        bigview = findViewById(GameCommonTool.getResourceId(context,
                "game_big_float_view", "id"));
        contentView = findViewById(GameCommonTool.getResourceId(context,
                "game_float_content", "id"));
        bigFloatShowAnimation(leftOrRight);
        setupViews();

    }
    // 进入动画
    private void bigFloatShowAnimation(int type) {
        Animation mAnimation = null;
        if (type == LEFT) {
            mAnimation = AnimationUtils.loadAnimation(context,
                    GameCommonTool.getResourceId(context, "gamesdk_slide_in_left", "anim"));
        } else if (type == RIGHT) {
            mAnimation = AnimationUtils
                    .loadAnimation(context, GameCommonTool.getResourceId(context,
                            "gamesdk_slide_in_right", "anim"));
        }
        if (bigview != null){
            bigview.startAnimation(mAnimation);
            handler = new Handler();
        }



//		handler.postDelayed(closeRunable, 4*1000);//2s无操作自动关闭

    }

    // 退出动画
    private void bigFloatExitAnimation(int type) {

        Animation mAnimation = null;
        if (type == LEFT) {
            mAnimation = AnimationUtils
                    .loadAnimation(context, GameCommonTool.getResourceId(context,
                            "gamesdk_slide_out_left", "anim"));
        } else if (type == RIGHT) {
            mAnimation = AnimationUtils.loadAnimation(context, GameCommonTool
                    .getResourceId(context, "gamesdk_slide_out_right", "anim"));
        }
        bigview.startAnimation(mAnimation);
        mAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                GameFloatWindowMgr.winStatus = GameFloatWindowMgr.WIN_SMALL;
                GameFloatWindowMgr.removeBigWindow(context);
                GameFloatWindowMgr.createSmallWindow(context);
            }
        });


    }

    private void bigFloatExitAnimationShadow(int type) {
        Animation mAnimation = null;
        if (type == LEFT) {
            mAnimation = AnimationUtils
                    .loadAnimation(context, GameCommonTool.getResourceId(context,
                            "gamesdk_slide_out_left", "anim"));
        } else if (type == RIGHT) {
            mAnimation = AnimationUtils.loadAnimation(context, GameCommonTool
                    .getResourceId(context, "gamesdk_slide_out_right", "anim"));
        }
        bigview.startAnimation(mAnimation);
        mAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                GameFloatWindowMgr.removeAllwin(context);
                GameFloatWindowMgr.winStatus = GameFloatWindowMgr.WIN_SHAKE_INVISBLE;
            }
        });
    }

    private void setupViews() {
        imageView = (ImageView) findViewWithTag("game_big_float_iv");

        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                bigFloatExitAnimation(leftOrRight);
            }
        });

        layout0 = (LinearLayout) findViewWithTag("game_float_news");
        layout1 = (LinearLayout) findViewWithTag("game_float_mine");
        layout2 = (LinearLayout) findViewWithTag("game_float_gift");
        layout3 = (LinearLayout) findViewWithTag("game_float_help");

        layout0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                backSmall();
                FloatSendBean floatSendBean = getFloatSendBean();
                Intent intent = new Intent();
                intent.putExtra("mUrl", GameSdkConstants.getAPPIdwebMessageNew());
                StringBuilder bodySign = new StringBuilder();
                bodySign.append("appId=").append(floatSendBean.getAppId()).append("&");
                bodySign.append("type=").append("1").append("&");
                bodySign.append("account=").append(floatSendBean.getUserId()).append("&");
                bodySign.append("loginType=").append("1").append("&");
                bodySign.append("version=").append("0").append("&");
                bodySign.append("ip=").append("").append("&");
                bodySign.append("mac=").append("").append("&");
                bodySign.append("imei=").append("");
                bodySign.append("||").append(floatSendBean.getAppKey());
                String sign = GameMD5Tool.calcMD5(bodySign.toString(), "UTF-8");

                StringBuilder paramStr = new StringBuilder();
                paramStr.append("appId=").append(floatSendBean.getAppId()).append("&");
                paramStr.append("userId=").append(floatSendBean.getUserId()).append("&");
                paramStr.append("password=").append(floatSendBean.getPassword()).append("&");
                paramStr.append("sign=").append(sign);
                intent.putExtra("paramStr", paramStr.toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("floatSendBean", floatSendBean);
                intent.putExtras(bundle);
                intent.setClass(context, GameFloatWindowActivity.class);
                context.startActivity(intent);
            }
        });
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backSmall();
                FloatSendBean floatSendBean = getFloatSendBean();
                Intent intent = new Intent();
                intent.putExtra("mUrl", GameSdkConstants.getAPPIdwebUserInfo());

                StringBuilder bodySign = new StringBuilder();
                bodySign.append("appId=").append(floatSendBean.getAppId()).append("&");
                bodySign.append("type=").append("1").append("&");
                bodySign.append("account=").append(floatSendBean.getUserId()).append("&");
                bodySign.append("password=").append(floatSendBean.getPassword()).append("&");
                bodySign.append("loginType=").append("1").append("&");
                bodySign.append("version=").append("0").append("&");
                bodySign.append("ip=").append("").append("&");
                bodySign.append("mac=").append("").append("&");
                bodySign.append("imei=").append("");
                bodySign.append("||").append(floatSendBean.getAppKey());
                String sign = GameMD5Tool.calcMD5(bodySign.toString(), "UTF-8");

                StringBuilder paramStr = new StringBuilder();
                paramStr.append("appId=").append(floatSendBean.getAppId()).append("&");
                paramStr.append("userId=").append(floatSendBean.getUserId()).append("&");
                paramStr.append("password=").append(floatSendBean.getPassword()).append("&");
                paramStr.append("sign=").append(sign);
                intent.putExtra("paramStr", paramStr.toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("floatSendBean", floatSendBean);
                intent.putExtras(bundle);
                intent.setClass(context, GameFloatWindowActivity.class);
                context.startActivity(intent);
            }
        });
        layout2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backSmall();
                FloatSendBean floatSendBean = getFloatSendBean();
                Intent intent = new Intent();
                intent.putExtra("mUrl", GameSdkConstants.getAPPIdwebMyGiftBags());
                StringBuilder bodySign = new StringBuilder();
                bodySign.append("appId=").append(floatSendBean.getAppId()).append("&");
                bodySign.append("type=").append("1").append("&");
                bodySign.append("account=").append(floatSendBean.getUserId()).append("&");
                bodySign.append("loginType=").append("1").append("&");
                bodySign.append("version=").append("0").append("&");
                bodySign.append("ip=").append("").append("&");
                bodySign.append("mac=").append("").append("&");
                bodySign.append("imei=").append("");
                bodySign.append("||").append(floatSendBean.getAppKey());
                String sign = GameMD5Tool.calcMD5(bodySign.toString(), "UTF-8");

                StringBuilder paramStr = new StringBuilder();
                paramStr.append("appId=").append(floatSendBean.getAppId()).append("&");
                paramStr.append("userId=").append(floatSendBean.getUserId()).append("&");
                paramStr.append("password=").append(floatSendBean.getPassword()).append("&");
                paramStr.append("sign=").append(sign);
                intent.putExtra("paramStr", paramStr.toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("floatSendBean", floatSendBean);
                intent.putExtras(bundle);
                intent.setClass(context, GameFloatWindowActivity.class);
                context.startActivity(intent);
            }
        });
        layout3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backSmall();
                FloatSendBean floatSendBean = getFloatSendBean();
                Intent intent = new Intent();
                intent.putExtra("mUrl", GameSdkConstants.getAPPIdwebHelpMessage());
                StringBuilder bodySign = new StringBuilder();
                bodySign.append("appId=").append(floatSendBean.getAppId()).append("&");
                bodySign.append("type=").append("1").append("&");
                bodySign.append("account=").append(floatSendBean.getUserId()).append("&");
                bodySign.append("loginType=").append("1").append("&");
                bodySign.append("version=").append("0").append("&");
                bodySign.append("ip=").append("").append("&");
                bodySign.append("mac=").append("").append("&");
                bodySign.append("imei=").append("");
                bodySign.append("||").append(floatSendBean.getAppKey());
                String sign = GameMD5Tool.calcMD5(bodySign.toString(), "UTF-8");

                StringBuilder paramStr = new StringBuilder();
                paramStr.append("appId=").append(floatSendBean.getAppId()).append("&");
                paramStr.append("userId=").append(floatSendBean.getUserId()).append("&");
                paramStr.append("password=").append(floatSendBean.getPassword()).append("&");
                paramStr.append("sign=").append(sign);
                intent.putExtra("paramStr", paramStr.toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("floatSendBean", floatSendBean);
                intent.putExtras(bundle);
                intent.setClass(context, GameFloatWindowActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private FloatSendBean getFloatSendBean() {
        final IDataService operatData = Dispatcher.getInstance().getIdaoFactory(context);
        JSONObject user = operatData.readCurntUid(IDataService.UidType.account);
        FloatSendBean floatSendBean = new FloatSendBean();
        try {
            String username = user.getString("username");
            String password = user.getString("password");
            String appId = GameSdkConstants.APPINFO.appId;
            String appKey = GameSdkConstants.APPINFO.appKey;
            floatSendBean.setUserId(username);
            floatSendBean.setPassword(password);
            floatSendBean.setAppId(appId);
            floatSendBean.setAppKey(appKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return floatSendBean;
    }

    private void backSmall() {
        GameFloatWindowMgr.winStatus = GameFloatWindowMgr.WIN_SMALL;
        GameFloatWindowMgr.removeBigWindow(context);
        GameFloatWindowMgr.createSmallWindow(context);
    }


    /**
     * @param str
     */
    protected void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
