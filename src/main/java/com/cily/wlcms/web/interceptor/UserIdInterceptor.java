package com.cily.wlcms.web.interceptor;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.UserModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.cily.utils.base.StrUtils;
import com.jfinal.aop.Invocation;

/**
 * Created by admin on 2018/2/23.
 * 请求参数中，userId是否null，是否存在该用户
 */
public class UserIdInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String userId = getParam(inv, SQLParam.USER_ID);
        if (StrUtils.isEmpty(userId)){
            inv.getController().renderJson(ResUtils.res(
                    Param.C_USER_ID_NULL, createTokenByOs(inv), null));
            return;
        }
        if (UserModel.getUserByUserId(userId) != null){
            inv.invoke();
        }else {
            inv.getController().renderJson(ResUtils.res(
                    Param.C_USER_NOT_EXIST, createTokenByOs(inv), null
            ));
        }
    }
}