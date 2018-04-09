package com.cily.wlcms.web.interceptor;

import com.cily.wlcms.web.conf.Param;
import com.cily.utils.base.StrUtils;
import com.jfinal.aop.Invocation;

/**
 * Created by admin on 2018/2/6.
 */
public class DeviceImeiInterceptor extends BaseInterceptor {

    @Override
    public void intercept(Invocation inv) {
        String deviceImei = getDeviceImei(inv);
        if (StrUtils.isEmpty(deviceImei)){
//            deviceImei = ip
        }

        inv.getController().setAttr(Param.DEVICE_IMEI, deviceImei);
        inv.invoke();
    }
}