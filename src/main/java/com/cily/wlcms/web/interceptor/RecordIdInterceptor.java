package com.cily.wlcms.web.interceptor;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.RecordModel;
import com.jfinal.aop.Invocation;

/**
 * Created by 123 on 2018/5/5.
 */
public class RecordIdInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String recordId = getParam(inv, SQLParam.RECORD_ID);
        if (StrUtils.isEmpty(recordId)) {
            renderJson(inv, Param.C_RECORD_ID_NULL, createTokenByOs(inv), null);
            return;
        } else {
            if (RecordModel.getRecordById(recordId) == null){
                renderJson(inv, Param.C_RECORD_ID_NOT_EXIST, createTokenByOs(inv), null);
            }else {
                inv.invoke();
            }
        }
    }
}
