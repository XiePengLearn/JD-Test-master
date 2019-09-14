package com.sxjs.jd.composition.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.app_common.service.ITokenService;
import com.sxjs.common.base.BaseActivity;
import com.sxjs.common.base.rxjava.ErrorDisposableObserver;
import com.sxjs.common.util.ToastUtil;
import com.sxjs.common.util.statusbar.StatusBarUtil;
import com.sxjs.common.view.ClearEditText;
import com.sxjs.jd.MainDataManager;
import com.sxjs.jd.R;
import com.sxjs.jd.R2;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * @author xiepeng
 */
@Route(path = "/test/login")
public class LoginActivity extends BaseActivity implements LoginContract.View , View.OnClickListener {
    @Inject
    LoginPresenter presenter;


    @BindView(R2.id.fake_status_bar)
    View           fakeStatusBar;
    @BindView(R2.id.jkx_title_left)
    TextView       jkxTitleLeft;
    @BindView(R2.id.jkx_title_left_btn)
    Button         jkxTitleLeftBtn;
    @BindView(R2.id.jkx_title_center)
    TextView       jkxTitleCenter;
    @BindView(R2.id.jkx_title_right_btn)
    TextView       jkxTitleRightBtn;
    @BindView(R2.id.new_message)
    TextView       newMessage;
    @BindView(R2.id.rl_new_message)
    RelativeLayout rlNewMessage;
    @BindView(R2.id.jkx_title_right)
    TextView       jkxTitleRight;
    @BindView(R2.id.edit_account)
    ClearEditText  editAccount;
    @BindView(R2.id.edit_password)
    ClearEditText  editPassword;
    @BindView(R2.id.login_remember_passwords)
    CheckBox       loginRememberPasswords;
    @BindView(R2.id.login_find_password)
    TextView       loginFindPassword;
    @BindView(R2.id.login_entry)
    Button         loginEntry;
    @BindView(R2.id.register)
    TextView       register;
    private String mXinGeToken;
    private static final String TAG = "LoginActivity";
    private Button mLoginEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        StatusBarUtil.setImmersiveStatusBar(this, true);
        unbinder = ButterKnife.bind(this);

        initView();


    }

    private void initView() {
//        mLoginEntry = findViewById(R.id.login_entry);
        loginEntry.setOnClickListener(this);

        DaggerLoginActivityComponent.builder()
                .appComponent(getAppComponent())
                .loginPresenterModule(new LoginPresenterModule(this, MainDataManager.getInstance(mDataManager)))
                .build()
                .inject(this);
    }

    public void initData(Map<String, String> mapHeaders, Map<String, Object> mapParameters) {
        presenter.getLoginData(mapHeaders, mapParameters);
    }
    /**
     * 模拟登录网络请求，只做演示无返回
     */
    public void login() {
        addDisposable(MainDataManager.getInstance(mDataManager).login(new ErrorDisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

            }

            @Override
            public void onComplete() {

            }
        }, "mobile", "code"));
    }

//    @OnClick({R2.id.jkx_title_left_btn, R2.id.edit_account, R2.id.edit_password,
//            R2.id.login_remember_passwords, R2.id.login_find_password, R2.id.login_entry, R2.id.register})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R2.id.jkx_title_left_btn:
//                break;
//            case R2.id.edit_account:
//                break;
//            case R2.id.edit_password:
//                break;
//            case R2.id.login_remember_passwords:
//                break;
//            case R2.id.login_find_password:
//                break;
//            case R2.id.login_entry:
//                //登录
//
//                LoginMethod();
//                break;
//            case R2.id.register:
//                break;
//        }
//    }

    private void LoginMethod() {
        String lAccount = editAccount.getText().toString().trim();

        if (TextUtils.isEmpty(lAccount)) {

            ToastUtil.showToast(mContext, mContext.getResources().getString(
                    R.string.username_not_empty), Toast.LENGTH_SHORT);
            return;
        }

        String lPassword = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(lPassword)) {
            ToastUtil.showToast(
                    mContext,
                    mContext.getResources().getString(
                            R.string.password_not_empty), Toast.LENGTH_SHORT);
            return;
        }

        //密码加密 并没有用到 我给注释了
        //                String lPasswordMD5 = Tool.encryption2(lPassword);

        ITokenService service = ARouter.getInstance().navigation(ITokenService.class);
        if (service != null) {
            mXinGeToken = service.getToken();

        } else {
            Toast.makeText(this, "该服务所在模块未参加编译", Toast.LENGTH_LONG).show();
        }

        Map<String, Object> mapParameters = new HashMap<>(6);
        mapParameters.put("MOBILE", lAccount);
        mapParameters.put("PASSWORD", lPassword);
        mapParameters.put("SIGNIN_TYPE", "1");
        mapParameters.put("USER_TYPE", "1");
        mapParameters.put("MOBILE_TYPE", "1");
        mapParameters.put("XINGE_TOKEN", mXinGeToken);

        Map<String, String> mapHeaders = new HashMap<>(1);
        mapHeaders.put("ACTION", "S002");
        //        mapHeaders.put("SESSION_ID", TaskManager.SESSION_ID);

        initData(mapHeaders,mapParameters);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.destory();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("loginData", loginData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String loginData = savedInstanceState.getString("loginData");
            this.loginData = loginData;

        }
    }
    private String loginData;
    @Override
    public void setLoginData(String loginData) {
        this.loginData = loginData;

        Toast.makeText(this, "loginData:" + loginData, Toast.LENGTH_SHORT).show();


        Log.e(TAG, "loginData: " + loginData);
    }

    @Override
    public void showProgressDialogView() {

    }

    @Override
    public void hiddenProgressDialogView() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_entry) {
            Log.e(TAG, "loginData: " + "login_entry");
            LoginMethod();
        }

    }
}
