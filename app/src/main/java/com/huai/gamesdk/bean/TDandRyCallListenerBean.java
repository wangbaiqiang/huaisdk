package com.huai.gamesdk.bean;

public final class TDandRyCallListenerBean {
//	
	public static String tdKey;
	public static String ryKey;
	public static String isOpenTd;
	public static String isOpenRy;
//	手机注册
	public static String phoneRegister;
//	帐号注册
	public static String accountRegister;
//	注册类型(手机注册/帐号注册？)  统一字段 :account,mobile
	public static String registerStyle;
//	登录模块--》登录帐号 sid
	public static String loginAccount;
	public static String sid;
//	支付模块--》pay
	public static float  currencyAmount;//货币金额
	public static String transactionId; //交易流水号
	public static String paymentType; //支付类型    统一字段  alipay;unionpay;weixinpay;daijinquan
	public static String currencyType;//货币类型.人名币：CNY
	public static String response; //支付回调结果：支付成功/支付失败？
	public static String other;//预备扩展字段

	
	
}
