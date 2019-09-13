package com.sxjs.jd.composition.login;

import java.util.Map;

/**
 * @Auther: xp
 * @Date: 2019/9/13 22:10
 * @Description:
 */
public class LoginContract {

    interface View {


        void setLoginData(String loginData);

        void showProgressDialogView();

        void hiddenProgressDialogView();
    }

    interface Presenter {


        void destory();

        void saveData();

        Map getData();

        void getLoginData(Map<String, String> mapHeaders, Map<String, Object> mapParameters);
    }
}
