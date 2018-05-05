package com.cily.wlcms.web.interceptor;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.utils.IdCardUtils;
import com.jfinal.aop.Invocation;

/**
 * Created by 123 on 2018/5/5.
 */
public class IdCardInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String idcrad = getParam(inv, SQLParam.ID_CARD);
        if (!StrUtils.isEmpty(idcrad)){
            if (IdCardUtils.isIDCard(idcrad)){
                inv.invoke();
            }else {
                renderJson(inv, Param.C_ID_CARD_ILLAGLE, createTokenByOs(inv), null);
            }
        }else {
            inv.invoke();
        }
    }
}
