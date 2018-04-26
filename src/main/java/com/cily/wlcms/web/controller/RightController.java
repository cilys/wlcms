package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.RightIdInterceptor;
import com.cily.wlcms.web.model.RightModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Before;

/**
 * Created by admin on 2018/2/23.
 */
public class RightController extends BaseController {
    /**
     * 根据权限状态，获取所有权限
     */
    public void getRights(){
        int pageNumber = getParaToInt(Param.PAGE_NUMBER, 1);
        int pageSize = getParaToInt(Param.PAGE_SIZE, 10);
        String status = getParam(SQLParam.STATUS);
        renderJsonSuccess(RightModel.getRights(pageNumber, pageSize, status));
    }

    /**
     * 修改权限状态信息
     */
    @Before({RightIdInterceptor.class})
    public void updateRight(){
        String rightId = getParam(SQLParam.RIGHT_ID);
        String status = getParam(SQLParam.STATUS);
        String rightName = getParam(SQLParam.RIGHT_NAME);
        String accessUrl = getParam(SQLParam.ACCESS_URL);

        if (SQLParam.STATUS_ENABLE.equals(status)
                || SQLParam.STATUS_DISABLE.equals(status)){

        }else {
            status = SQLParam.STATUS_DISABLE;
        }
        if (RightModel.update(rightId, null, null, status)){
            renderJsonSuccess(null);
        }else {
            renderJsonFailed(Param.C_RIGHT_STATUS_CHANGE_FAILED, null);
        }
    }
}