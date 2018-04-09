package com.cily.wlcms.web.utils;

import com.cily.wlcms.web.conf.BaseBean;
import com.cily.wlcms.web.conf.Param;
import com.jfinal.plugin.activerecord.Model;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2018/2/1.
 */
public class ResUtils {
    public static <D>BaseBean res(String code, String token, D d){
        if (d != null){
            if (d instanceof Model){
                removeNullValue((Model) d);
            }
        }

        return BaseBean.getBaseBean(code, token, d);
    }

    private static Model removeNullValue(Model d){
        if (d == null){
            return null;
        }
        Iterator<Map.Entry<String, Object>> it = d._getAttrsEntrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> e = it.next();
            if (e.getValue() == null){
                it.remove();
            }
        }
        return d;
    }

    public static <D>BaseBean success(String token, D d){
        return res(Param.C_SUCCESS,  token, d);
    }
}