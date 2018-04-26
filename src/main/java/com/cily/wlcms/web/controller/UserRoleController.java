package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.RoleIdInterceptor;
import com.cily.wlcms.web.interceptor.UserIdInterceptor;
import com.cily.wlcms.web.model.UserRoleModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Before;

/**
 * Created by admin on 2018/2/23.
 */
public class UserRoleController extends BaseController {

    /**
     * 获取用户全部角色
     */
    @Before({UserIdInterceptor.class})
    public void getUserRoles(){
        String userId = getParam(SQLParam.USER_ID);
        renderJsonSuccess(UserRoleModel.getUserRolesByUserId(userId));
    }

    /**
     * 给用户分配角色
     */
    @Before({UserIdInterceptor.class, RoleIdInterceptor.class})
    public void addUserRole(){
        String userId = getParam(SQLParam.USER_ID);
        String roleId = getParam(SQLParam.ROLE_ID);

        if (UserRoleModel.insertUserRole(userId, roleId)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_USER_ROLE_ADD_FAILED, null);
        }
    }

    /**
     * 删除用户角色
     */
    @Before({UserIdInterceptor.class, RoleIdInterceptor.class})
    public void delUserRole(){
        String userId = getParam(SQLParam.USER_ID);
        String roleId = getParam(SQLParam.ROLE_ID);

        if (UserRoleModel.delByUserIdAndRoleId(userId, roleId)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_USER_ROLE_DEL_FAILED, null);
        }
    }
}
