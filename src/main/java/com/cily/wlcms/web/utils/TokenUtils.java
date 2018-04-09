package com.cily.wlcms.web.utils;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.jfinal.kit.PropKit;

/**
 * Created by admin on 2018/2/1.
 */
public class TokenUtils {

    public static String createToken(String userId, String deviceImei, String token){
        if (StrUtils.isEmpty(userId)){
            return null;
        }

        //TODO token存储到数据库里

        if (PropKit.getBoolean("oneOffToken", false)){
            return deviceImei + UUIDUtils.getUUID();
        }else {
            if (StrUtils.isEmpty(token)){
                return deviceImei + UUIDUtils.getUUID();
            }else {
                return token;
            }
        }
    }
}