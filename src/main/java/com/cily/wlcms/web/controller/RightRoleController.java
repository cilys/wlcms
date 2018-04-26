package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.RightIdInterceptor;
import com.cily.wlcms.web.interceptor.RoleIdInterceptor;
import com.cily.wlcms.web.model.RightRoleModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Before;

/**
 * Created by admin on 2018/2/23.
 */
@Before({RoleIdInterceptor.class})
public class RightRoleController extends BaseController {

    /**
     * 获取角色的权限
     */
    public void getRoleRights(){
        String roleId = getParam(SQLParam.ROLE_ID);
        renderJsonSuccess(RightRoleModel.getRoleRightByRoleId(roleId,
                        getParaToInt(Param.PAGE_NUMBER, 1),
                        getParaToInt(Param.PAGE_SIZE, 10)));
    }

    /**
     * 添加角色权限
     */
    @Before({RightIdInterceptor.class})
    public void addRoleRight(){
        String roleId = getParam(SQLParam.ROLE_ID);
        String rightId = getParam(SQLParam.RIGHT_ID);
        if (RightRoleModel.insert(rightId, roleId)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_RIGHT_ROLE_ADD_FAILED, null);
        }
    }

    /**
     * 删除角色权限
     */
    @Before({RightIdInterceptor.class})
    public void delRoleRight(){
        String roleId = getParam(SQLParam.ROLE_ID);
        String rightId = getParam(SQLParam.RIGHT_ID);
        if (RightRoleModel.delByRightIdAndRoleId(rightId, roleId)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_RIGHT_ROLE_DEL_FAILED, null);
        }
    }
}