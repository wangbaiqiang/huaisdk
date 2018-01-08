package com.huai.gamesdk.mvpbase;


/**
 * <p>Description:
 * @author tzw
 * 
 * 绑定View
 * 解绑View
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T t);
    void detachView();
}
