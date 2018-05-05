package com.cily.wlcms.web.interceptor;

import com.cily.utils.secret.MD5Utils;
import com.cily.utils.web.base.IpUtils;
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
            deviceImei = IpUtils.getRealIp(inv.getController().getRequest());
        }
        deviceImei = MD5Utils.getMD5Str(deviceImei);

        inv.getController().setAttr(Param.DEVICE_IMEI, deviceImei);
        inv.invoke();
    }
}