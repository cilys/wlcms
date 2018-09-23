package com.cily.wlcms.web.model;

import com.cily.utils.base.time.TimeUtils;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.jfinal.plugin.activerecord.Db;
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
    private static UserModel dao = new UserModel();

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

    public static long getEnableUserCount(){
        return Db.queryLong(StrUtils.join("select count(1) from ",
                SQLParam.T_USER, " where ", SQLParam.STATUS, " = '0'"));
    }
    public static long getDisableUserCount(){
        return Db.queryLong(StrUtils.join("select count(1) from ",
                SQLParam.T_USER, " where ", SQLParam.STATUS, " = '1'"));
    }

    public static UserModel getUserByUserName(String userName){
        return dao.findFirst(StrUtils.join(
                "select * from ", SQLParam.T_USER,
                " where ", SQLParam.USER_NAME, " = '" ,
                userName, "'"));
    }

    public static UserModel getUserByUserId(String userId){
        return dao.findById(userId);
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
        if (!StrUtils.isEmpty(idCard)){
            um.set(SQLParam.ID_CARD, idCard);
        }
        if (!StrUtils.isEmpty(address)){
            um.set(SQLParam.ADDRESS, address);
        }
        if (!StrUtils.isEmpty(status)){
            if (SQLParam.STATUS_ENABLE.equals(status) || SQLParam.STATUS_DISABLE.equals(status)){
                um.set(SQLParam.STATUS, status);
            }
        }
        um.set(SQLParam.CREATE_TIME, TimeUtils.milToStr(System.currentTimeMillis(), null));
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

    public static Page<UserModel> getUsersByStatus(String status, int pageNumber,
                                                   int pageSize, String desc, boolean encoder){
        if (pageSize < 1){
            pageSize = 10;
        }
        if (pageNumber < 1){
            pageNumber = 1;
        }

        if (StrUtils.isEmpty(desc)){
            desc = "DESC";
        }

        Page<UserModel> result = null;

        if (SQLParam.STATUS_ENABLE.equals(status) || SQLParam.STATUS_DISABLE.equals(status)){
            String sql = StrUtils.join(" from ", SQLParam.T_USER," where ", SQLParam.STATUS, " = '", status, "' order by ", SQLParam.CREATE_TIME, " ", desc);
            result = dao.paginate(pageNumber, pageSize, "select * ", sql);
        }else {
            result = dao.paginate(pageNumber, pageSize, "select * ", " from " + SQLParam.T_USER + " order by " + SQLParam.CREATE_TIME + " " + desc);
        }
        if (result != null){
            if (result.getList() != null && result.getList().size() > 0){
                for (UserModel um : result.getList()) {
                    um.remove(SQLParam.PWD);
                    if (encoder) {
                        um.set(SQLParam.REAL_NAME, formcatRealName(um.get(SQLParam.REAL_NAME, null)));
                        um.set(SQLParam.PHONE, formcatPhone(um.get(SQLParam.PHONE, null)));
                        um.set(SQLParam.ID_CARD, formcatIdCard(um.get(SQLParam.ID_CARD, null)));
                        um.set(SQLParam.ADDRESS, formcatAddress(um.get(SQLParam.ADDRESS, null)));
                    }
                }
            }
        }
        return result;
    }

    public static boolean delByUserId(String userId){
        return dao.deleteById(userId);
    }

    public static  Page<UserModel> searchUser(int pageNumber, int pageSize,
                                              String searchText, boolean encoder){
        if (pageSize < 1){
            pageSize = 10;
        }
        if (pageNumber < 1){
            pageNumber = 1;
        }

        String sql = StrUtils.join(" from ", SQLParam.T_USER," where ",
                SQLParam.USER_ID, " like '%", searchText, "%' or ",
                SQLParam.USER_NAME, " like '%", searchText, "%' or ",
                SQLParam.REAL_NAME, " like '%", searchText, "%' order by ", SQLParam.CREATE_TIME, " DESC");

        Page<UserModel> result = dao.paginate(pageNumber, pageSize, "select * ", sql);
        if (result != null){
            if (result.getList() != null && result.getList().size() > 0){
                for (UserModel um : result.getList()) {
                    um.remove(SQLParam.PWD);
                    if (encoder) {
                        um.set(SQLParam.PHONE, formcatPhone(um.get(SQLParam.PHONE, null)));
                        um.set(SQLParam.ID_CARD, formcatIdCard(um.get(SQLParam.ID_CARD, null)));
                        um.set(SQLParam.ADDRESS, formcatAddress(um.get(SQLParam.ADDRESS, null)));
                        um.set(SQLParam.REAL_NAME, formcatRealName(um.get(SQLParam.REAL_NAME, null)));
                    }
                }
            }
        }
        return result;
    }

    public static String formcatPhone(String phone){
        if (!StrUtils.isEmpty(phone)){
            char[] strs = phone.toCharArray();
            for (int i = 0; i < strs.length; i++){
                if (i > 2 && i < phone.length() - 4){
                    strs[i] = '*';
                }
            }
            return String.valueOf(strs);
        }
        return "";
    }
    public static String formcatIdCard(String idCard){
        if (!StrUtils.isEmpty(idCard)){
            char[] strs = idCard.toCharArray();
            for (int i = 0; i < strs.length; i++){
                if (i > 1){
                    strs[i] = '*';
                }
            }
            return String.valueOf(strs);
        }
        return "";
    }

    public static String formcatRealName(String realName){
        if (!StrUtils.isEmpty(realName)){
            int length = realName.length();
            if (length > 1){
                StringBuilder su = StrUtils.getStringBuilder();
                for (int i = 0; i < length - 1; i++){
                    su.append("*");
                }
                realName = realName.substring(0, 1) + su.toString();
            }
            return realName;
        }
        return "";
    }

    public static String formcatAddress(String address){
        if (!StrUtils.isEmpty(address)){
            int length = address.length();
            if (length > 2){
                StringBuilder su = new StringBuilder();
                for(int i = 0; i < length - 2; i++){
                    su.append("*");
                }
                address = address.substring(0, 2) + su.toString();
                return address;
            }
        }
        return "";
    }

    public static List<UserModel> searchUsers(List<String> userIds){
        if (userIds == null || userIds.size() < 1){
            return null;
        }
        StringBuilder su = StrUtils.getStringBuilder();
        for (String s : userIds){
            su.append("or ");
            su.append(SQLParam.USER_ID);
            su.append(" = '");
            su.append(s);
            su.append("' ");
        }
        String str = su.toString();
        if (str.startsWith("or")){
            str = str.substring(2);
        }

        return dao.find(StrUtils.join("select * from ", SQLParam.T_USER, " where ", str));
    }
}