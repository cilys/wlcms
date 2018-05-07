package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by admin on 2018/2/22.
 */
public class RoleModel extends Model<RoleModel> {
    private static RoleModel dao = new RoleModel().dao();

    public static boolean insert(String roleName){
        if (StrUtils.isEmpty(roleName)){
            return false;
        }
        RoleModel m = new RoleModel();
        m.set(SQLParam.ROLE_ID, UUIDUtils.getUUID());
        m.set(SQLParam.ROLE_NAME, roleName);

        return m.save();
    }

    public static boolean del(String roleId){
        if (StrUtils.isEmpty(roleId)){
            return false;
        }
        return dao.deleteById(roleId);
    }

    public static boolean roleNameIsExist(String roleName){
        if (StrUtils.isEmpty(roleName)){
            return true;
        }
        return dao.findFirst(StrUtils.join("select * from ",
                SQLParam.T_ROLE, " where ", SQLParam.ROLE_NAME,
                " = '", roleName, "';")) != null;
    }

    public static boolean roleIsExist(String roleId){
        if (StrUtils.isEmpty(roleId)){
            return false;
        }
        return dao.findFirst(StrUtils.join(
                "select * from ", SQLParam.T_ROLE,
                " where ", SQLParam.ROLE_ID, " = '", roleId, "';")) != null;
    }

    public static List<RoleModel> getRoles(){
        return dao.find(StrUtils.join("select * from ", SQLParam.T_ROLE));
    }
}