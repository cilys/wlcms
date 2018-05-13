package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.*;
import com.cily.wlcms.web.model.RecordModel;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.wlcms.web.utils.UserUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;

import java.util.HashMap;
import java.util.Map;

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
        UserUtils.updateUserInfo(this, getUserId(), getParam(SQLParam.USER_ID),
                getParam(SQLParam.STATUS));
    }

    public void getUsers(){
        renderJsonSuccess(UserModel.getUsersByStatus(getParam(SQLParam.STATUS),
                getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null));
    }

    public void getUserCount(){
        long enableUserCount = UserModel.getEnableUserCount();
        long disableUserCount = UserModel.getDisableUserCount();

        Map<String, Long> map = new HashMap<>();
        map.put("enableUserCount", enableUserCount);
        map.put("disableUserCount", disableUserCount);
        renderJsonSuccess(map);
    }

    public void getRecordCount(){
        long enableRecordCount = RecordModel.getEnableRecordCount();
        long disenableRecordCount = RecordModel.getDisenableRecordCount();

        Map<String, Long> map = new HashMap<>();
        map.put("enableRecordCount", enableRecordCount);
        map.put("disenableRecordCount", disenableRecordCount);
        renderJsonSuccess(map);
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
        String recordStatus = getPara(SQLParam.RECORD_STATUS, null);
        renderJsonSuccess(RecordModel.getRecordsByUserId(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null, recordStatus));
    }

    @Before({RecordIdInterceptor.class})
    public void updateRecordStatus(){
        String recordId = getParam(SQLParam.RECORD_ID);
        String recordStatus = getPara(SQLParam.RECORD_STATUS, SQLParam.STATUS_DISABLE);

        renderJsonSuccess(RecordModel.updateRecord(recordId, null,
                null, null, null,
                null, recordStatus));
    }
}