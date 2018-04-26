package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.RoleModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.utils.base.StrUtils;

/**
 * Created by admin on 2018/2/22.
 */
public class RoleController extends BaseController {

    public void addRole(){
        String roleName = getParam(SQLParam.ROLE_NAME);
        if (StrUtils.isEmpty(roleName)){
            renderJsonFailed(Param.C_ROLE_NAME_NULL,null);
            return;
        }
        if (RoleModel.roleNameIsExist(roleName)){
            renderJsonFailed(Param.C_ROLE_NAME_EXISTS,null);
            return;
        }
        if (RoleModel.insert(roleName)){
            renderJsonSuccess(null);
            return;
        }else {
            renderJsonFailed(Param.C_ROLE_ADD_FAILED,null);
            return;
        }
    }

    public void delRole(){
        String roleId = getParam(SQLParam.ROLE_ID);
        if (RoleModel.del(roleId)){
            renderJsonSuccess(null);
            return;
        }else {
            renderJsonFailed(Param.C_ROLE_DEL_FAILED,null);
            return;
        }
    }

    public void getRoles(){
        renderJsonSuccess(RoleModel.getRoles());
    }
}