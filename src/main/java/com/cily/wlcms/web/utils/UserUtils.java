package com.cily.wlcms.web.utils;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.UserModel;
import com.cily.utils.base.StrUtils;
import com.jfinal.core.Controller;

/**
 * Created by admin on 2018/2/5.
 */
public class UserUtils {

    public static void regist(Controller c, String userId, String status){
        if(c == null){
            throw new NullPointerException("The Controller is null.");
        }

        String token = c.getHeader(SQLParam.TOKEN);
        String deviceImei = c.getAttr(Param.DEVICE_IMEI);

        String userName = c.getPara(SQLParam.USER_NAME);
        String pwd = c.getPara(SQLParam.PWD);
        String realName = c.getPara(SQLParam.REAL_NAME, null);
        String sex = c.getPara(SQLParam.SEX, null);
        String phone = c.getPara(SQLParam.PHONE, null);
        String address = c.getPara(SQLParam.ADDRESS, null);
        String idCard = c.getPara(SQLParam.ID_CARD, null);

        if (StrUtils.isEmpty(status)){
            status = SQLParam.STATUS_DISABLE;
        }

        if (UserModel.getUserByUserName(userName) != null){
            c.renderJson(ResUtils.res(Param.C_RESIGT_USER_EXISTS,
                    TokenUtils.createToken(userId, deviceImei, token), null));
            return;
        }

        if (UserModel.insert(userName, pwd, realName,
                sex, phone, address, idCard, status)){
            c.renderJson(ResUtils.success(TokenUtils.createToken(
                    userId, deviceImei, token), null));
        }else {
            c.renderJson(ResUtils.res(Param.C_REGIST_FAILURE,
                    TokenUtils.createToken(userId, deviceImei, token), null));
        }
    }

    public static void updateUserInfo(Controller c, String userId, String status){
        if(c == null){
            throw new NullPointerException("The Controller is null.");
        }
        String token = c.getHeader(SQLParam.TOKEN);
        String deviceImei = c.getAttr(Param.DEVICE_IMEI);

        String pwd = c.getPara(SQLParam.PWD);
        String realName = c.getPara(SQLParam.REAL_NAME, null);
        String sex = c.getPara(SQLParam.SEX, null);
        String phone = c.getPara(SQLParam.PHONE, null);
        String address = c.getPara(SQLParam.ADDRESS, null);
        String idCard = c.getPara(SQLParam.ID_CARD, null);

        if (StrUtils.isEmpty(userId)){
            c.renderJson(ResUtils.res(Param.C_USER_ID_NULL, TokenUtils.createToken(userId, deviceImei, token), null));
            return;
        }

        int updateResult = UserModel.updateUserInfo(userId, pwd, realName,
                sex, phone, address, idCard, status);
        if (updateResult == UserModel.USER_INFO_UPDATE_SUCCESS){
            c.renderJson(ResUtils.success(TokenUtils.createToken(
                    userId, deviceImei, token), null));
        }else if (updateResult == UserModel.USER_NOT_EXIST){
            c.renderJson(ResUtils.res(Param.C_USER_NOT_EXIST,
                    TokenUtils.createToken(userId, deviceImei, token), null));
        }else if (updateResult == UserModel.USER_INFO_NO_UPDATE){
            c.renderJson(ResUtils.res(Param.C_USER_INFO_NO_UPDATE,
                    TokenUtils.createToken(userId, deviceImei, token), null));
        }else {
            c.renderJson(ResUtils.res(Param.C_USER_INFO_UPDATE_FAILURE,
                    TokenUtils.createToken(userId, deviceImei, token), null));
        }
    }
}
