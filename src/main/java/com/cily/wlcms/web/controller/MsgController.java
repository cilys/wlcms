package com.cily.wlcms.web.controller;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.MsgModel;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.RootUserIdUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;

import java.util.*;

/**
 * Created by 123 on 2018/5/25.
 */
public class MsgController extends BaseController {

    public void insert(){
        String fromUserId = getUserId();
        String toUserId = getParam(SQLParam.TO_USER_ID);
        String msg = getParam(SQLParam.MSG);
        String msgType = getPara(SQLParam.MSG_TYPE);
        if (StrUtils.isEmpty(msgType)){
            if (RootUserIdUtils.isRootUser(fromUserId)){
                msgType = "0";
            }
        }
        if (StrUtils.isEmpty(toUserId)){
            renderJsonFailed(Param.C_MSG_TO_USER_ID_NULL, null);
            return;
        }
        if (StrUtils.isEmpty(msg)){
            renderJsonFailed(Param.C_MSG_NULL, null);
            return;
        }

        UserModel um = UserModel.getUserByUserId(toUserId);
        if (um == null){
            renderJsonFailed(Param.C_MSG_TO_USER_ID_NOT_EXIST, null);
            return;
        }
        MsgModel m = MsgModel.createModel(fromUserId, toUserId, msg, msgType);
        if (m == null){
            renderJsonFailed(Param.C_MSG_NULL, null);
            return;
        }
        if (MsgModel.insert(m)){
            renderJsonSuccess(m);
            return;
        }else {
            renderJsonFailed(Param.C_MSG_INSERT_FAILED, null);
            return;
        }
    }

    public void search(){
        String fromUserId = getUserId();
        String toUserId = getParam(SQLParam.TO_USER_ID);
        String searchText = getParam(Param.SEARCH_TEXT);
        if (StrUtils.isEmpty(toUserId)){
            renderJsonFailed(Param.C_MSG_TO_USER_ID_NULL, null);
            return;
        }
        renderJsonSuccess(MsgModel.searchMsg(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 5), fromUserId, toUserId,
                searchText));
    }

    public void searchMsgGroup(){
        String userId = getUserId();
        int pageNumber = getParaToInt(Param.PAGE_NUMBER, 1);
        int pageSize = getParaToInt(Param.PAGE_SIZE, 20);
        Page<MsgModel> m = MsgModel.searchMsgGroup(pageNumber, pageSize, userId);
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
}
