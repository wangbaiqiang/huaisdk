package com.huai.gamesdk.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huai.gamesdk.R;

/**
 * Created by Administrator on 2018/1/2.
 */

public class GameSurePayActivity extends Activity{
    final String TAG = "SurePayActivity";
    private TextView mCommodityDescribe;
    private TextView mCommodityPrice;
    private TextView mCommodityDiscounts;
    private TextView mNeedPayMoney;
    private Button mPayButton;
    //    暂无优惠
    private TextView mTemporarynodiscounts;
    private RadioGroup mRadioGroup;
    private RadioButton mAlipay;
    private RadioButton mWechatpay;
    private RadioButton mPlatCoinpay;
    private String payTAG = "1";
    private boolean isUnionWeixinload = false;
    private String weixin_pre_id;
    private String toastMsg = "";// 微信回调消息
    private String bundleVoucher;
    private String bundlePrice;
    private String bundleUserName;
    private String bundleGameId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamesdk_layout_surepay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle mBundle = this.getIntent().getExtras();
        if (mBundle != null){
            bundleVoucher = mBundle.getString("voucher");
            bundlePrice = mBundle.getString("price");
            bundleUserName = mBundle.getString("userName");
            bundleGameId = mBundle.getString("gameId");
        }else {
            Log.i(TAG, "surePayActivity onCreate: bundle == null");
        }

        mCommodityDescribe = (TextView) findViewById(R.id.commodityDescribe);
        mCommodityPrice = (TextView) findViewById(R.id.commodityPrice);
        mCommodityDiscounts = (TextView) findViewById(R.id.commodityDiscounts);
        mNeedPayMoney = (TextView) findViewById(R.id.needPayMoney);
        mTemporarynodiscounts = (TextView) findViewById(R.id.temporarynodiscounts);
        mPayButton = (Button) findViewById(R.id.mPayButton);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGrouppay);
        mAlipay = (RadioButton) findViewById(R.id.rbalipay);
        mWechatpay = (RadioButton) findViewById(R.id.rbwechatpay);
        mPlatCoinpay = (RadioButton) findViewById(R.id.rbplatcoinpay);


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (mAlipay.getId() == checkedId) {
                    payTAG = "1";
                } else if (mWechatpay.getId() == checkedId) {
                    payTAG = "2";
                } else if (mPlatCoinpay.getId() == checkedId) {
                    payTAG = "3";
                } else {

                }
            }
        });


// !----这里完成具体的支付逻辑
//      支付请求参数以及支付回调根据对应渠道后台设置即可----!
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (payTAG.equals("1")) {
                    alipayMethod();
                } else if (payTAG.equals("2")) {
                    wechatpayMethod();
                } else if (payTAG.equals("3")) {
                    coinpayMethod();
                } else {
                    Log.i(TAG, "onClick: surepaytype == null ");
                }
            }



        });
    }

    private void coinpayMethod() {
        Toast.makeText(GameSurePayActivity.this,"点击平台币支付",Toast.LENGTH_LONG).show();

    }

    private void wechatpayMethod() {
        Toast.makeText(GameSurePayActivity.this,"点击微信支付",Toast.LENGTH_LONG).show();

    }

    private void alipayMethod() {
        Toast.makeText(GameSurePayActivity.this,"点击支付宝支付",Toast.LENGTH_LONG).show();

    }
}
