package com.cily.wlcms.web.interceptor;


import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Invocation;

/**
 * Created by 123 on 2018/4/22.
 */
public class RecordAddInterceptor extends BaseInterceptor{
    @Override
    public void intercept(Invocation inv) {
        String recordName = getParam(inv, SQLParam.RECORD_NAME);
        String recordNum = getParam(inv, SQLParam.RECORD_NUM);
        String recordLevel = getParam(inv, SQLParam.RECORD_LEVEL);

        if (StrUtils.isEmpty(recordName)){
            renderJson(inv, Param.C_RECORD_NAME_NULL, createTokenByOs(inv), null);
            return;
        }

        if (StrUtils.isEmpty(recordNum)){
            renderJson(inv, Param.C_RECORD_NUM_NULL, createTokenByOs(inv), null);
            return;
        }

        if (StrUtils.isEmpty(recordLevel)){
            renderJson(inv, Param.C_RECORD_LEVEL_NULL, createTokenByOs(inv), null);
            return;
        }
        inv.invoke();

    }
}
