package com.sxjs.jd.composition.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.app_common.service.ITokenService;
import com.sxjs.common.util.PrefUtils;
import com.sxjs.common.widget.bottomnavigation.BadgeItem;
import com.sxjs.common.widget.bottomnavigation.BottomNavigationBar;
import com.sxjs.common.widget.bottomnavigation.BottomNavigationItem;
import com.sxjs.jd.MainDataManager;
import com.sxjs.jd.R;
import com.sxjs.common.base.BaseActivity;
import com.sxjs.jd.R2;
import com.sxjs.jd.composition.main.classificationfragment.ClassificationFragment;
import com.sxjs.jd.composition.main.findfragment.FindFragment;
import com.sxjs.jd.composition.main.homefragment.MainHomeFragment;
import com.sxjs.jd.composition.main.my.MyFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity implements MainContract.View, BottomNavigationBar.OnTabSelectedListener {

    @Inject
    MainPresenter       presenter;
    @BindView(R2.id.bottom_navigation_bar1)
    BottomNavigationBar bottomNavigationBar;
    @BindView(R2.id.main_container)
    FrameLayout         mainContainer;
    private MainHomeFragment       mMainHomeFragment;
    private ClassificationFragment mClassificationFragment;
    private FragmentManager        mFragmentManager;
    private FindFragment           mFindFragment;
    private MyFragment             mMyFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        initView();
        initData();


    }

    public void initView() {
        mMainHomeFragment = (MainHomeFragment) mFragmentManager.findFragmentByTag("home_fg");
        mClassificationFragment = (ClassificationFragment) mFragmentManager.findFragmentByTag("class_fg");
        mFindFragment = (FindFragment) mFragmentManager.findFragmentByTag("find_fg");
        mMyFragment = (MyFragment) mFragmentManager.findFragmentByTag("my_fg");

        if (mMainHomeFragment == null) {
            mMainHomeFragment = MainHomeFragment.newInstance();
            addFragment(R.id.main_container, mMainHomeFragment, "home_fg");
        }
        //更改位置,初始化在  点击底部导航栏 position 为1  初始化
        /*if(mClassificationFragment == null){
            mClassificationFragment = ClassificationFragment.newInstance();
            addFragment(R.id.main_container, mClassificationFragment, "class_fg");
        }*/

        //更改位置,初始化在  点击底部导航栏 position 为2  初始化
        /*if(mFindFragment == null){
            mFindFragment = FindFragment.newInstance();
            addFragment(R.id.main_container, mFindFragment, "find_fg");
        }*/

        //更改位置,初始化在  点击底部导航栏 position 为3  初始化
        /*if(mMyFragment == null){
            mMyFragment = MyFragment.newInstance();
            addFragment(R.id.main_container, mMyFragment, "my_fg");
        }*/

        mFragmentManager.beginTransaction().show(mMainHomeFragment)
                .commitAllowingStateLoss();
        if (mClassificationFragment != null) {
            mFragmentManager.beginTransaction().hide(mClassificationFragment).commitAllowingStateLoss();
        }
        if (mFindFragment != null) {
            mFragmentManager.beginTransaction().hide(mFindFragment).commitAllowingStateLoss();
        }
        if (mMyFragment != null) {
            mFragmentManager.beginTransaction().hide(mMyFragment).commitAllowingStateLoss();
        }

        DaggerMainActivityComponent.builder()
                .appComponent(getAppComponent())
                .mainPresenterModule(new MainPresenterModule(this, MainDataManager.getInstance(mDataManager)))
                .build()
                .inject(this);
        initBottomNavigation();

    }

    @Override
    public void onTabSelected(int position) {
        if (position == 0) {
            if (mMainHomeFragment == null) {
                mMainHomeFragment = MainHomeFragment.newInstance();
                addFragment(R.id.main_container, mMainHomeFragment, "home_fg");
            }
            mFragmentManager.beginTransaction()

                    .show(mMainHomeFragment)
                    .commitAllowingStateLoss();

            if (mClassificationFragment != null) {
                mFragmentManager.beginTransaction().hide(mClassificationFragment).commitAllowingStateLoss();
            }
            if (mFindFragment != null) {
                mFragmentManager.beginTransaction().hide(mFindFragment).commitAllowingStateLoss();
            }
            if (mMyFragment != null) {
                mFragmentManager.beginTransaction().hide(mMyFragment).commitAllowingStateLoss();
            }
        } else if (position == 1) {
            if (mClassificationFragment == null) {
                mClassificationFragment = ClassificationFragment.newInstance();
                addFragment(R.id.main_container, mClassificationFragment, "class_fg");
            }
            mFragmentManager.beginTransaction()
                    .show(mClassificationFragment)
                    .commitAllowingStateLoss();


            if (mMainHomeFragment != null) {
                mFragmentManager.beginTransaction().hide(mMainHomeFragment).commitAllowingStateLoss();
            }

            if (mFindFragment != null) {
                mFragmentManager.beginTransaction().hide(mFindFragment).commitAllowingStateLoss();
            }
            if (mMyFragment != null) {
                mFragmentManager.beginTransaction().hide(mMyFragment).commitAllowingStateLoss();
            }

        } else if (position == 2) {
            if (mFindFragment == null) {
                mFindFragment = FindFragment.newInstance();
                addFragment(R.id.main_container, mFindFragment, "find_fg");
            }
            mFragmentManager.beginTransaction()
                    .show(mFindFragment)
                    .commitAllowingStateLoss();

            if (mMainHomeFragment != null) {
                mFragmentManager.beginTransaction().hide(mMainHomeFragment).commitAllowingStateLoss();
            }
            if (mClassificationFragment != null) {
                mFragmentManager.beginTransaction().hide(mClassificationFragment).commitAllowingStateLoss();
            }

            if (mMyFragment != null) {
                mFragmentManager.beginTransaction().hide(mMyFragment).commitAllowingStateLoss();
            }
        } else if (position == 3) {

            if (mMyFragment == null) {
                mMyFragment = MyFragment.newInstance();
                addFragment(R.id.main_container, mMyFragment, "my_fg");
            }
            mFragmentManager.beginTransaction()
                    .show(mMyFragment)
                    .commitAllowingStateLoss();

            if (mMainHomeFragment != null) {
                mFragmentManager.beginTransaction().hide(mMainHomeFragment).commitAllowingStateLoss();
            }
            if (mClassificationFragment != null) {
                mFragmentManager.beginTransaction().hide(mClassificationFragment).commitAllowingStateLoss();
            }
            if (mFindFragment != null) {
                mFragmentManager.beginTransaction().hide(mFindFragment).commitAllowingStateLoss();
            }

        }
    }

    private void initBottomNavigation() {
        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.colorAccent)
                .setText("99+")
                .setHideOnSelect(false);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //bottomNavigationBar.setAutoHideEnabled(true);


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.axh, "").setInactiveIconResource(R.drawable.axg).setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.drawable.axd, "").setInactiveIconResource(R.drawable.axc).setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.drawable.axf, "").setInactiveIconResource(R.drawable.axe).setActiveColorResource(R.color.colorAccent))
                //                .addItem(new BottomNavigationItem(R.drawable.axb, "").setInactiveIconResource(R.drawable.axa).setActiveColorResource(R.color.colorAccent).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.axj, "").setInactiveIconResource(R.drawable.axi).setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
    }

    private static final String TAG = "MainActivity";


    public void initData() {
        presenter.getText();
        presenter.getLoginData();
    }

    private String text;
    private String loginData;

    @Override
    public void setLoginData(String loginData) {
        this.loginData = loginData;
        Toast.makeText(this, "loginData:" + loginData, Toast.LENGTH_SHORT).show();


        Log.e(TAG, "loginData: " + loginData);

        ITokenService service = ARouter.getInstance().navigation(ITokenService.class);
        if (service != null) {
            //            String xinGeToken = service.getToken();
            //            PrefUtils.writeXinGeToken(xinGeToken, this.getApplicationContext());
            String xinGeToken = PrefUtils.readXinGeToken(this.getApplicationContext());
            Log.e(TAG, "xinGeToken: " + xinGeToken);
            PrefUtils.writeXinGeToken("1", this.getApplicationContext());
            String xinGeToken1 = PrefUtils.readXinGeToken(this.getApplicationContext());
            Log.e(TAG, "xinGeToken1: " + xinGeToken1);
        } else {
            Toast.makeText(this, "该服务所在模块未参加编译", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setText(String text) {

        this.text = text;

        Toast.makeText(this, "text:" + text, Toast.LENGTH_SHORT).show();


        Log.e(TAG, "initData: " + text);


    }

    @Override
    public void showProgressDialogView() {
        showProgressDialog();
    }

    @Override
    public void hiddenProgressDialogView() {
        hiddenProgressDialog();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", text);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String text = savedInstanceState.getString("text");
            this.text = text;

        }
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.destory();
        }


    }

}
