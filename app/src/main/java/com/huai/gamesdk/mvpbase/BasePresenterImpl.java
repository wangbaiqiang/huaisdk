package com.huai.gamesdk.mvpbase;

/**
 * <p>Description:
 *
 * P层实现类
 */

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {



    protected T mPresenterView ;

    @Override
    public void attachView(T t) {
        this.mPresenterView = t ;
    }

    @Override
    public void detachView() {
        this.mPresenterView = null ;
    }
}
