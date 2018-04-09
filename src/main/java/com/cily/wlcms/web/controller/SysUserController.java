package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.wlcms.web.utils.UserUtils;

/**
 * Created by admin on 2018/2/5.
 */
public class SysUserController extends BaseController {

    public void addUser(){
        UserUtils.regist(this, getUserId(),
                getParam(SQLParam.STATUS, SQLParam.STATUS_ENABLE));
    }

    public void updateUserInfo(){
        UserUtils.updateUserInfo(this, getParam(SQLParam.USER_ID),
                getParam(SQLParam.STATUS));
    }

    public void getUsers(){
        String token = getToken();
        String deviceImei = getDeviceImei();

        int pageNumber = getParaToInt(Param.PAGE_NUMBER, 1);
        int pageSize = getParaToInt(Param.PAGE_SIZE, 10);



        renderJson(ResUtils.success(createTokenByOs(),
                UserModel.getUsersByStatus(getParam(SQLParam.STATUS), pageNumber, pageSize)));
    }
}