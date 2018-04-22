package com.cily.wlcms.web.controller;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.utils.TokenUtils;
import com.jfinal.core.Controller;

/**
 * Created by admin on 2018/2/22.
 */
public class BaseController extends Controller {

    protected String getDeviceImei(){
        return getAttr(Param.DEVICE_IMEI);
    }

    protected String getParam(String paramKey, String defValue){
        return getPara(paramKey, defValue);
    }
    protected String getParam(String paramKey){
        return getParam(paramKey, null);
    }

    protected String getToken(){
        return getHeader(SQLParam.TOKEN);
    }

    protected String getUserId(){
        return getHeader(SQLParam.USER_ID);
    }

    protected String createTokenByOs(){
        return TokenUtils.createToken(getUserId(), getDeviceImei(), getToken());
    }

    @Override
    public void renderJson(Object object) {
//        getResponse().setHeader("Access-Control-Allow-Origin", "*");
//        getResponse().setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
        getResponse().setHeader("Access-Control-Allow-Origin", "*");
        super.renderJson(object);
    }
}
