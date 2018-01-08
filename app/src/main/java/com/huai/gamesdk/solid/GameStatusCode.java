package com.huai.gamesdk.solid;

public final class GameStatusCode {
	private GameStatusCode(){}
	public static final int SUCCESS = 0;
	public static final int NET_UNAVAILABLE = 1;
	public static final int SERVER_ERROR = 2;
	public static final int RESULT_FORMAT_ERROR = 3;
	public static final int CLIENT_INVALID = 101;
	public static final int INIT_FAIL = 102;
	public static final int ACCOUNT_LOGIN_FAIL = 201;
	public static final int PHONE_LOGIN_FAIL = 202;
	public static final int VERIFY_CODE_ERROR = 203;
	public static final int REG_SUCCESS = 300;
	public static final int REG_FAIL = 301;
	public static final int PAY_SUCCESS = 600;
	public static final int PAY_FAIL = 601;
	public static final int PAY_CANCEL = 602;
	public static final int PAY_CONFIRM = 603;
}
