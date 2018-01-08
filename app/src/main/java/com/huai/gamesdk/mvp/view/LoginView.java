package com.huai.gamesdk.mvp.view;

import com.huai.gamesdk.mvpbase.BaseView;

public interface LoginView extends BaseView{
	    void loginSuccess(String msg) ;
	    void loginFailed(String msg) ;

}
