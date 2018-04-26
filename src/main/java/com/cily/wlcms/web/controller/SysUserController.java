package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.RecordModel;
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
        renderJsonSuccess(UserModel.getUsersByStatus(getParam(SQLParam.STATUS),
                getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10)));
    }

    public void getRecords(){
        renderJsonSuccess(RecordModel.getRecordsByUserId(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null));
    }
}