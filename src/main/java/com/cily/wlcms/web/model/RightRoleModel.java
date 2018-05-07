package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by admin on 2018/2/22.
 */
public class RightRoleModel extends Model<RightRoleModel> {
    private static RightRoleModel dao = new RightRoleModel().dao();

    public static boolean insert(String rightId, String roleId){
        if (StrUtils.isEmpty(rightId) || StrUtils.isEmpty(roleId)){
            return false;
        }
        RightRoleModel m = new RightRoleModel();
        m.set(SQLParam.RIGHT_ID, rightId);
        m.set(SQLParam.ROLE_ID, roleId);
        return m.save();
    }

    public static boolean delByRightId(String rightId){
        if (StrUtils.isEmpty(rightId)){
            return false;
        }
        return Db.delete("delete from ", SQLParam.T_RIGHT_ROLE,
                " where ", SQLParam.RIGHT_ID, " = '", rightId, "';") > 0;
    }

    public static boolean delByRoleId(String roleId){
        if (StrUtils.isEmpty(roleId)){
            return false;
        }
        return Db.delete(StrUtils.join("delete from ",
                SQLParam.T_RIGHT_ROLE,
                " where ", SQLParam.ROLE_ID, " = '", roleId, "';")) > 0;
    }

    public static boolean delByRightIdAndRoleId(String rightId, String roleId){
        if (StrUtils.isEmpty(rightId) || StrUtils.isEmpty(roleId)) {
            return false;
        }
        return Db.delete(StrUtils.join("delete from ",
                SQLParam.T_RIGHT_ROLE, " where ",
                SQLParam.RIGHT_ID, " = '", rightId, "' and ",
                SQLParam.ROLE_ID, " = '", roleId, "';")) > 0;
    }

    public static Page<RightRoleModel> getRoleRightByRoleId(
            String roleId, int pageNumber, int pageSize){
        if (StrUtils.isEmpty(roleId)){
            return null;
        }
        if (pageSize < 1){
            pageSize = 10;
        }
        if (pageNumber < 1){
            pageNumber = 1;
        }

        return dao.paginate(pageNumber, pageSize, "select * ",
                StrUtils.join(" from " , SQLParam.T_RIGHT_ROLE,
                        " left join ", SQLParam.T_RIGHT, " on ",
                        SQLParam.T_RIGHT_ROLE, ".", SQLParam.RIGHT_ID,
                        " = ", SQLParam.T_ROLE, ".", SQLParam.RIGHT_ID,
                        " where ", SQLParam.T_RIGHT_ROLE, ".", SQLParam.ROLE_ID,
                        " = '", roleId, "';"));
    }
}
