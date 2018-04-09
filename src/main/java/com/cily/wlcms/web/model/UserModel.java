package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import javax.swing.plaf.PanelUI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/1/30.
 */
public class UserModel extends Model<UserModel> {
    private static UserModel dao = new UserModel().dao();

    public static boolean insert(String userName, String pwd,
                                 String realName, String sex,
                                 String phone, String address,
                                 String idCard, String status){
        UserModel um = new UserModel();
        um.set(SQLParam.USER_ID, UUIDUtils.getUUID());
        um.set(SQLParam.USER_NAME, userName);
        um.set(SQLParam.PWD, pwd);
        if (!StrUtils.isEmpty(realName)){
            um.set(SQLParam.REAL_NAME, realName);
        }
        if (!StrUtils.isEmpty(sex)){
            um.set(SQLParam.SEX, sex);
        }
        if (!StrUtils.isEmpty(phone)){
            um.set(SQLParam.PHONE, phone);
        }
        if (!StrUtils.isEmpty(address)){
            um.set(SQLParam.ADDRESS, address);
        }
        if (!StrUtils.isEmpty(idCard)){
            um.set(SQLParam.ID_CARD, idCard);
        }
        if (!StrUtils.isEmpty(status)){
            um.set(SQLParam.STATUS, status);
        }
        return um.save();
    }

    public static UserModel getUserByUserName(String userName){
        return dao.findFirst(StrUtils.join(
                "select * from ", SQLParam.T_USER,
                " where ", SQLParam.USER_NAME, " = '" ,
                userName, "'"));
    }

    public static UserModel getUserByUserId(String userId){
        return dao.findById(SQLParam.USER_ID);
    }

    public final static int USER_INFO_UPDATE_SUCCESS = 0;
    public final static int USER_NOT_EXIST = -1;
    public final static int USER_INFO_UPDATE_FAILURE = -2;
    public final static int USER_INFO_NO_UPDATE = -3;
    public static int updateUserInfo(String userId, String pwd,
                                         String realName, String sex,
                                         String phone, String address,
                                         String idCard, String status){
        UserModel um = getUserByUserId(userId);
        if (um == null){
            return USER_NOT_EXIST;
        }
        if (!StrUtils.isEmpty(pwd)){
            um.set(SQLParam.PWD, pwd);
        }
        if (!StrUtils.isEmpty(realName)){
            um.set(SQLParam.REAL_NAME, realName);
        }
        if (!StrUtils.isEmpty(sex)){
            um.set(SQLParam.SEX, sex);
        }
        if (!StrUtils.isEmpty(phone)){
            um.set(SQLParam.PHONE, phone);
        }
        if (!StrUtils.isEmpty(address)){
            um.set(SQLParam.ADDRESS, address);
        }
        if (!StrUtils.isEmpty(status)){
            if (SQLParam.STATUS_ENABLE.equals(status) || SQLParam.STATUS_DISABLE.equals(status)){
                um.set(SQLParam.STATUS, status);
            }
        }
        if (isEmpty(pwd, realName, sex, phone, address, status)){
            return USER_INFO_NO_UPDATE;
        }
        if (um.update()){
            return USER_INFO_UPDATE_SUCCESS;
        }else {
            return USER_INFO_UPDATE_FAILURE;
        }
    }

    private static boolean isEmpty(String... strs){
        if (strs == null){
            return true;
        }
        if (strs.length < 1){
            return true;
        }
        for (String s : strs){
            if (!StrUtils.isEmpty(s)){
                return false;
            }
        }
        return true;
    }

    public static Page<UserModel> getUsersByStatus(String status, int pageNumber, int pageSize){
        if (pageSize < 1){
            pageSize = 10;
        }
        if (pageNumber < 1){
            pageNumber = 1;
        }

        if (SQLParam.STATUS_ENABLE.equals(status) || SQLParam.STATUS_DISABLE.equals(status)){
            String sql = StrUtils.join(" from ", SQLParam.T_USER," where ", SQLParam.STATUS, " = '" + status + "'");
            return dao.paginate(pageNumber, pageSize, "select * ", sql);
        }else {
            return dao.paginate(pageNumber, pageSize, "select * ", " from " + SQLParam.T_USER);
        }
    }
}