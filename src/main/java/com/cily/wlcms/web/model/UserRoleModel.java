package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by admin on 2018/2/22.
 */
public class UserRoleModel extends Model<UserRoleModel> {
    private static UserRoleModel dao = new UserRoleModel().dao();

    public static boolean insertUserRole(String userId, String roleId){
        if (StrUtils.isEmpty(userId) || StrUtils.isEmpty(roleId)){
            return false;
        }
        UserRoleModel m = new UserRoleModel();
        m.set(SQLParam.USER_ID, userId);
        m.set(SQLParam.ROLE_ID, roleId);
        return m.save();
    }

    public static boolean delByUserId(String userId){
        if (StrUtils.isEmpty(userId)){
            return false;
        }
        return Db.delete(StrUtils.join("delete from ",
                SQLParam.T_USER_ROLE, " where ",
                SQLParam.USER_ID, " = '", userId, "';")) > 0;

    }

    public static boolean delByRoleId(String roleId){
        if (StrUtils.isEmpty(roleId)){
            return false;
        }
        return Db.delete(StrUtils.join("delete from ",
                SQLParam.T_USER_ROLE, " where ",
                SQLParam.ROLE_ID, " = '", roleId, "';")) > 0;

    }

    public static boolean delByUserIdAndRoleId(String userId,
                                               String roleId){
        if (StrUtils.isEmpty(userId) || StrUtils.isEmpty(roleId)){
            return false;
        }
        return Db.delete(StrUtils.join("delete from ",
                SQLParam.T_USER_ROLE, " where ",
                SQLParam.ROLE_ID, " = '", roleId, "' and ",
                SQLParam.USER_ID, "= '", userId, "';")) > 0;

    }

    public static List<UserRoleModel> getUserRolesByUserId(String userId){
        if (StrUtils.isEmpty(userId)){
            return null;
        }
        return dao.find(StrUtils.join(
                "select * from ", SQLParam.T_USER_ROLE,
                " left join ", SQLParam.T_ROLE, " on ",
                SQLParam.T_USER_ROLE, ".", SQLParam.ROLE_ID, " = ",
                SQLParam.T_ROLE, ".", SQLParam.ROLE_ID,
                " where ", SQLParam.T_USER_ROLE, ".", SQLParam.USER_ID, " = '", userId, "';"
        ));
    }

    public static boolean isAccessToUrl(String userId, String accessUrl){
        if (StrUtils.isEmpty(userId) || StrUtils.isEmpty(accessUrl)){
            return false;
        }
        return getUserRoleRight(userId, accessUrl) != null;
    }

    public static UserRoleModel getUserRoleRight(String userId, String accessUrl){
        if (StrUtils.isEmpty(userId) || StrUtils.isEmpty(accessUrl)){
            return null;
        }
        return dao.findFirst(StrUtils.join(
                "select * from ", SQLParam.T_USER_ROLE,
                " left join ", SQLParam.T_RIGHT_ROLE ,
                " on ", SQLParam.T_USER_ROLE, ".", SQLParam.ROLE_ID, " = ", SQLParam.T_RIGHT_ROLE, ".", SQLParam.ROLE_ID,
                " left join ", SQLParam.T_RIGHT,
                " on ", SQLParam.T_RIGHT_ROLE, ".", SQLParam.RIGHT_ID, " = ", SQLParam.T_RIGHT, ".", SQLParam.RIGHT_ID,
                " where ", SQLParam.T_USER_ROLE, ".", SQLParam.USER_ID, " = '", userId, "' and ",
                SQLParam.T_RIGHT, ".", SQLParam.ACCESS_URL, "'", accessUrl, "';"
        ));
    }
}