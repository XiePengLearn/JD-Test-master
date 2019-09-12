package com.example.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.app.utils.PrefUtils;
import com.example.app.utils.StatusBarUtil;
import com.sxjs.common.base.BaseActivity;
import com.sxjs.jd.composition.login.LoginActivity;
import com.sxjs.jd.composition.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xiepeng
 * @date 2018/12/1
 * @description
 */
public class SplashActivity extends BaseActivity {


    public static final String  PREF_IS_USER_GUIDE_SHOWED = "is_user_guide_showed";//标记新手引导是否已经展示过
    public static final String  PREF_IS_ENTER_LOG_OR_MAIN = "is_enter_login_or_main";//标记进入登录页还是主页
    private             Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        ButterKnife.bind(this);
        StatusBarUtil.setImmersiveStatusBar(this, true);
//        initViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BuildConfig.DEBUG && !BuildConfig.DEBUG_ALL) {
                    ARouter.getInstance().build("/app/ModuleTestActivity").greenChannel().navigation(SplashActivity.this);
                } else {
                    ARouter.getInstance().build("/main/MainActivity").greenChannel().navigation(SplashActivity.this);
                }
                finish();
            }
        }, 2000);
    }

    // 初始化欢迎页面的动画
    private void initViews() {
        RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        // 渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(1500);
        alpha.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);// 初始化动画集合
        set.addAnimation(alpha);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画结束的回调
            @Override
            public void onAnimationEnd(Animation animation) {
                // 判断新手引导是否展示过
                boolean showed = PrefUtils.getBoolean(
                        SplashActivity.this, PREF_IS_USER_GUIDE_SHOWED, false);

                if (showed) {
                    // 已经展示过, 进入主页面
                    //enterLogin ture 进登录界面 false 进main界面
                    boolean enterLogin = PrefUtils.getBoolean(SplashActivity.this, PREF_IS_ENTER_LOG_OR_MAIN, true);
                    if (enterLogin) {

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.putExtra("title", "登录");
                        startActivity(intent);
                        finish();
                    } else {
                        String token = PrefUtils.readToken(SplashActivity.this);
                        if (!TextUtils.isEmpty(token)) {
                            String isPhone = PrefUtils.readIsPhone(SplashActivity.this);

                            if (!TextUtils.isEmpty(isPhone)) {
                                if ("1".equals(isPhone)) {

                                    Intent mIntent = new Intent(mContext, MainActivity.class);
                                    mIntent.putExtra("title", "首页");
                                    startActivity(mIntent);

                                    finish(); //结束当前的activity的生命周期


                                }
                            }

                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.putExtra("title", "登录");
                            startActivity(intent);
                            finish();

                        }
                    }

                } else {
                    // 没展示, 进入新手引导页面
                    startActivity(new Intent(SplashActivity.this,
                            GuideActivity.class));
                }

                finish();
            }
        });

        rlRoot.startAnimation(set);
    }

}
