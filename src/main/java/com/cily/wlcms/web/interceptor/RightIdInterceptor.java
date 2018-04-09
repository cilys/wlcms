package com.cily.wlcms.web.interceptor;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.RightModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.utils.base.StrUtils;
import com.jfinal.aop.Invocation;

/**
 * Created by admin on 2018/2/23.
 * 请求参数中，权限id是否为空，是否存在该权限
 */
public class RightIdInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String rightId = getParam(inv, SQLParam.RIGHT_ID);
        if (StrUtils.isEmpty(rightId)){
            inv.getController().renderJson(ResUtils.res(Param.C_RIGHT_ID_NULL,
                    createTokenByOs(inv), null));
            return;
        }
        if (RightModel.rightIsExist(rightId)){
            inv.invoke();
        }else {
            inv.getController().renderJson(ResUtils.res(Param.C_RIGHT_NOT_EXIST,
                    createTokenByOs(inv), null));
            return;
        }
    }
}
