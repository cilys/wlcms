package com.cily.wlcms.web.utils;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.cily.wlcms.web.model.TokenModel;
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
        // oneOffToken 一次失效
        if (PropKit.getBoolean("oneOffToken", false)){
            String newToken = deviceImei + UUIDUtils.getUUID();
            TokenModel.updateByUserId(userId, token);
            return newToken;
        }else {
            if (StrUtils.isEmpty(token)){
                String newToken = deviceImei + UUIDUtils.getUUID();
                TokenModel.updateByUserId(userId, newToken);
                return newToken;
            }else {
                TokenModel.updateByUserId(userId, token);
                return token;
            }
        }
    }
}