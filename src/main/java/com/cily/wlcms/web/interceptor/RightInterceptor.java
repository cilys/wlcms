package com.cily.wlcms.web.interceptor;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.model.UserRoleModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Invocation;
/**
 * Created by admin on 2018/2/22.
 */
public class RightInterceptor extends BaseInterceptor {

    @Override
    public void intercept(Invocation inv) {
        String userId = getUserId(inv);

        String accessUrl = inv.getActionKey();

        UserRoleModel m = UserRoleModel.getUserRoleRight(userId, accessUrl);
        if (m == null){
            inv.getController().renderJson(ResUtils.res(
                    Param.C_RIGHT_LOW, createTokenByOs(inv), null));
            return;
        }
        if (SQLParam.STATUS_ENABLE.equals(m.get(SQLParam.STATUS))){
            inv.invoke();
        }else {
            inv.getController().renderJson(ResUtils.res(
                    Param.C_RIGHT_REFUSE, createTokenByOs(inv), null));
        }
    }
}
