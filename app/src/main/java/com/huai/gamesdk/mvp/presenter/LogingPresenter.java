package com.huai.gamesdk.mvp.presenter;

import android.content.Context;

import com.huai.gamesdk.mvp.model.MVPLogingBean;
import com.huai.gamesdk.mvp.view.LoginView;
import com.huai.gamesdk.mvpbase.BasePresenter;

public interface LogingPresenter extends BasePresenter<LoginView>{
	 void login(MVPLogingBean user ,Context context) ;
}
