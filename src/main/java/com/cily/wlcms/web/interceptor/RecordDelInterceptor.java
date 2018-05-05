package com.cily.wlcms.web.interceptor;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.RecordModel;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;

/**
 * Created by 123 on 2018/5/5.
 */
public class RecordDelInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String recordId = getParam(inv, SQLParam.RECORD_ID);
        //用户自己可以删除自己的，超管可以删除任意的发布
        String userId = getUserId(inv);
        String rootUserId = PropKit.get("rootUserId", "670b14728ad9902aecba32e22fa4f6bd");
        if (rootUserId.equals(userId)){
            inv.invoke();
        }else {
            RecordModel m = RecordModel.getRecordById(recordId);
            if (userId.equals(m.get(SQLParam.RECORD_CREATE_USER_ID))){
                inv.invoke();
            }else {
                renderJson(inv, Param.C_NO_RIGHT_DEL_OTHER_USER_ROCORD, createTokenByOs(inv), null);
                return;
            }
        }
    }
}
