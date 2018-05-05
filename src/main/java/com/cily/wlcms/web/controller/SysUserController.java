package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.*;
import com.cily.wlcms.web.model.RecordModel;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.wlcms.web.utils.UserUtils;
import com.jfinal.aop.Before;

/**
 * Created by admin on 2018/2/5.
 */
@Before({SysUserInterceptor.class})
public class SysUserController extends BaseController {

    @Before({UserNameInterceptor.class, PwdInterceptor.class,
            PhoneInterceptor.class, IdCardInterceptor.class})
    public void addUser(){
        UserUtils.regist(this, getUserId(),
                getParam(SQLParam.STATUS, SQLParam.STATUS_ENABLE));
    }

    @Before({UserIdInterceptor.class, UserNameInterceptor.class, PwdInterceptor.class,
            PhoneInterceptor.class, IdCardInterceptor.class})
    public void updateUserInfo(){
        UserUtils.updateUserInfo(this, getParam(SQLParam.USER_ID),
                getParam(SQLParam.STATUS));
    }

    public void getUsers(){
        renderJsonSuccess(UserModel.getUsersByStatus(getParam(SQLParam.STATUS),
                getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null));
    }

    @Before({UserIdInterceptor.class})
    public void delUser() {
        String userId = getParam(SQLParam.USER_ID);
        if (UserModel.delByUserId(userId)) {
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_USER_DEL_FAILED, null);
        }
    }

    public void getRecords(){
        renderJsonSuccess(RecordModel.getRecordsByUserId(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null));
    }
}