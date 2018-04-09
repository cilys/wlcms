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
            renderJson(ResUtils.res(Param.C_ROLE_NAME_NULL,
                    createTokenByOs(), null));
            return;
        }
        if (RoleModel.roleNameIsExist(roleName)){
            renderJson(ResUtils.res(Param.C_ROLE_NAME_EXISTS,
                    createTokenByOs(),
                    null));
            return;
        }
        if (RoleModel.insert(roleName)){
            renderJson(ResUtils.success(
                    createTokenByOs(),null));
            return;
        }else {
            renderJson(ResUtils.res(Param.C_ROLE_ADD_FAILED,
                    createTokenByOs(), null));
            return;
        }
    }

    public void delRole(){
        String roleId = getParam(SQLParam.ROLE_ID);
        if (RoleModel.del(roleId)){
            renderJson(ResUtils.success(
                    createTokenByOs(), null));
            return;
        }else {
            renderJson(ResUtils.res(Param.C_ROLE_DEL_FAILED,
                    createTokenByOs(), null));
            return;
        }
    }

    public void getRoles(){
        renderJson(ResUtils.success(createTokenByOs(), RoleModel.getRoles()));
    }
}