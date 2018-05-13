package com.cily.wlcms.web.interceptor;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.jfinal.aop.Invocation;

/**
 * Created by 123 on 2018/5/4.
 */
public class PwdInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String pwd = getParam(inv, SQLParam.PWD);
        if (StrUtils.isEmpty(pwd)){
//            renderJson(inv, Param.C_PWD_NULL, createTokenByOs(inv), null);
            inv.invoke();
            return;
        }
        if (pwd.length() > 32){
            renderJson(inv, Param.C_PWD_ILLAGLE, createTokenByOs(inv), null);
            return;
        }
        inv.invoke();
    }
}
