package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.*;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.UserUtils;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.wlcms.web.utils.TokenUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;

/**
 * Created by admin on 2018/1/30.
 */

public class UserController extends BaseController {

    @Clear({LoginedInterceptor.class})
    @Before({UserNameInterceptor.class, PwdInterceptor.class})
    public void login(){
        String userName = getParam(SQLParam.USER_NAME);
        String pwd = getParam(SQLParam.PWD);

        String deviceImei = getDeviceImei();

        UserModel um = UserModel.getUserByUserName(userName);
        if (um == null){
            renderJson(ResUtils.res(Param.C_USER_NOT_EXIST, null, null));
            return;
        }
        if (pwd.equals(um.get(SQLParam.PWD))){
            um.remove(SQLParam.PWD);
            String token = TokenUtils.createToken(um.get(SQLParam.USER_ID), deviceImei, null);
            renderJson(ResUtils.success(token, um));
            return;
        }else {
            renderJson(ResUtils.res(Param.C_USER_OR_PWD_ERROR, null, null));
            return;
        }
    }

    @Clear({LoginedInterceptor.class})
    @Before({UserNameInterceptor.class, PwdInterceptor.class,
            PhoneInterceptor.class, IdCardInterceptor.class})
    public void regist(){
        UserUtils.regist(this, null, null);
    }

    @Before({UserIdInterceptor.class, PwdInterceptor.class,
            PhoneInterceptor.class, IdCardInterceptor.class})
    public void updateUserInfo(){
        UserUtils.updateUserInfo(this, getUserId(), null);
    }

    @Before({SearchInterceptor.class})
    public void search(){
        String searchText = getParam(Param.SEARCH_TEXT);
        renderJsonSuccess(UserModel.searchUser(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), searchText));
    }
}