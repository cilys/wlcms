package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.*;
import com.cily.wlcms.web.model.MsgModel;
import com.cily.wlcms.web.model.RecordModel;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.wlcms.web.utils.UserUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

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
        String osType = getHeader("osType");

        renderJsonSuccess(UserModel.getUsersByStatus(getParam(SQLParam.STATUS),
                getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), null, !"1".equals(osType)));
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
                getParaToInt(Param.PAGE_SIZE, 10), null, recordStatus, false));
    }

    @Before({RecordIdInterceptor.class})
    public void updateRecordStatus(){
        String recordId = getParam(SQLParam.RECORD_ID);
        String recordStatus = getPara(SQLParam.RECORD_STATUS, SQLParam.STATUS_DISABLE);

        if (RecordModel.updateRecord(recordId, null,
                null, null, null,
                null, recordStatus)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_RECORD_STATUS_UPDATE_FAILED, null);
        }
    }

    @Before({RecordIdInterceptor.class})
    public void updateContent(){
        String recordId = getParam(SQLParam.RECORD_ID);
        String recordContent = getParam(SQLParam.RECORD_CONTENT, "");
        if (RecordModel.updateRecord(recordId, null,
                null, null,
                recordContent, null, null)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_RECORD_CONTENT_UPDATE_FAILED, null);
        }
    }

    public void getMsgs(){
        Page<MsgModel> m = MsgModel.getMsgs(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), getParam(Param.SEARCH_TEXT));

        if (m != null && m.getList() != null && m.getList().size() > 0){
            Set<String> userIds = new HashSet<>();
            for (MsgModel msg : m.getList()){
                String fromUserId = msg.get(SQLParam.FROM_USER_ID);
                String toUserId = msg.get(SQLParam.TO_USER_ID);
//                if (userId.equals(fromUserId)){
                userIds.add(toUserId);
//                }else {
                userIds.add(fromUserId);
//                }
            }
            List<UserModel> us = UserModel.searchUsers(new ArrayList<>(userIds));
            Map<String, UserModel> map = new HashMap<>();
            for (UserModel u : us){
                map.put(u.get(SQLParam.USER_ID), u);
            }

            for (MsgModel msg : m.getList()){
                String fromUserId = msg.get(SQLParam.FROM_USER_ID);
                String toUserId = msg.get(SQLParam.TO_USER_ID);

//                if (userId.equals(fromUserId)){
                UserModel mmToUser = map.get(toUserId);
                if (mmToUser != null){
                    msg.put("toUserName", mmToUser.get(SQLParam.USER_NAME));
                    msg.put("toRealName", mmToUser.get(SQLParam.REAL_NAME));
                }
//                }else {
                UserModel mmFromUser = map.get(fromUserId);
                if (mmFromUser != null){
                    msg.put("fromUserName", mmFromUser.get(SQLParam.USER_NAME));
                    msg.put("fromRealName", mmFromUser.get(SQLParam.REAL_NAME));
                }
//                }
            }
        }
        renderJsonSuccess(m);
    }

    public void delMsg(){
        String msgId = getParam(SQLParam.MSG_ID);
        if (MsgModel.del(msgId)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_MSG_DEL_FAILED, null);
        }
    }

    public void getMsgCount(){
        Map<String, Object> result = new HashMap<>();
        result.put("msgTotalCounts", MsgModel.allMsgCounts());
        renderJsonSuccess(result);
    }
}