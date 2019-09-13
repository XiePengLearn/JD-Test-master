package com.sxjs.jd.composition.login;

import android.util.Log;

import com.sxjs.common.base.rxjava.ErrorDisposableObserver;
import com.sxjs.jd.MainDataManager;
import com.sxjs.jd.composition.BasePresenter;
import com.sxjs.jd.composition.main.MainContract;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * @Auther: xp
 * @Date: 2019/9/13 22:09
 * @Description:
 */
public class LoginPresenter extends BasePresenter implements LoginContract.Presenter{
    private MainDataManager mDataManager;
    private LoginContract.View mLoginView;
    private static final String TAG = "MainPresenter";

    @Inject
    public LoginPresenter(MainDataManager mDataManager, LoginContract.View view) {
        this.mDataManager = mDataManager;
        this.mLoginView = view;

    }

    @Override
    public void destory() {
        if(disposables != null){
            disposables.clear();
        }
    }

    @Override
    public void saveData() {

    }

    @Override
    public Map getData() {
        return null;
    }

    @Override
    public void getLoginData(Map<String, String> mapHeaders, Map<String, Object> mapParameters) {
        mLoginView.showProgressDialogView();
        Disposable disposable = mDataManager.getLoginData( mapHeaders,  mapParameters, new ErrorDisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    mLoginView.setLoginData(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //如果需要发生Error时操作UI可以重写onError，统一错误操作可以在ErrorDisposableObserver中统一执行
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mLoginView.hiddenProgressDialogView();
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: " );
                mLoginView.hiddenProgressDialogView();
            }
        });
        addDisposabe(disposable);
    }
}
