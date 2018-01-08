package com.huai.gamesdk.activity;

public enum ActivityFactory {
	PHONE_REGISTER_ACTIVITY,
	ACCOUNT_REGISTER_ACTIVITY,
	AGREEMENT_ACTIVITY,
	ACCOUNT_LOGIN_ACTIVITY,
	PHONE_VERIFY_ACTIVITY,
	FIND_PWD_ACTIVITY,
	FIND_PWD_TYPE_ACTIVITY,
	FIND_PWD_VCODE_ACTIVITY,
	FIND_PWD_FINISH_ACTIVITY,
	PWD_ERROR_ACTIVITY,
	PAY_ACTIVITY,
	CARDPAY_ACTIVITY,
	GUILD_PAY_ACTIVITY;

	public ActivityUI getService() {
		switch (this) {
			case ACCOUNT_LOGIN_ACTIVITY:
				return new GameLoginActivity();
			case PHONE_REGISTER_ACTIVITY:
				return new GamePhoneRegisterActivity();
			case ACCOUNT_REGISTER_ACTIVITY:
				return new GameAccountRegisterActivity();
			case AGREEMENT_ACTIVITY:
				return new GameAgreementActivity();
			case PHONE_VERIFY_ACTIVITY:
				return new GamePhoneVerifyCodeActivity();
			case FIND_PWD_ACTIVITY:
				return new GameFindPwdActivity();
			case FIND_PWD_TYPE_ACTIVITY:
				return new GameFindPwdTypeActivity();
			case FIND_PWD_VCODE_ACTIVITY:
				return new GameFindPwdVcodeActivity();
			case FIND_PWD_FINISH_ACTIVITY:
				return new GameFindPwdFinishActivity();
			case PAY_ACTIVITY:
				return new GamePayActivity();
			case CARDPAY_ACTIVITY:
				return new GameCardPayActivity();
			case PWD_ERROR_ACTIVITY:
				return new GamePasswordErrorActivity();
			case GUILD_PAY_ACTIVITY:
				return new GameGuildPayActivity();
			default:
				break;
			}
		return null;
	}
	

}
