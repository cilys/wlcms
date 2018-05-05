package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.utils.TokenUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by admin on 2018/2/11.
 */
public class TokenModel extends Model<TokenModel> {
    private static TokenModel dao = new TokenModel();

    public static boolean updateByUserId(String userId, String token){
        TokenModel m = dao.findById(SQLParam.USER_ID);
        if (m == null){
            m = new TokenModel();
            m.set(SQLParam.USER_ID, userId);
            m.set(SQLParam.TOKEN, token);
            m.set(SQLParam.UPDATE_TIME, System.currentTimeMillis());
            return m.save();
        }else {
            m.set(SQLParam.TOKEN, token);
            m.set(SQLParam.UPDATE_TIME, System.currentTimeMillis());
            return m.update();
        }
    }

    public static boolean deleteByUserId(String userId){
        if (StrUtils.isEmpty(userId)){
            return false;
        }
        return dao.deleteById(userId);
    }

    public static String checkTokenByUserId(String userId,
                                            String token){
        TokenModel m = dao.findById(SQLParam.USER_ID);

        if (m == null){
            return Param.C_USER_NOT_LOGIN;
        }else {
            String to = m.get(SQLParam.TOKEN, null);
            if (StrUtils.isEmpty(to)){
                return Param.C_USER_NOT_LOGIN;
            }else {
                long updateTime = m.get(SQLParam.UPDATE_TIME, 0);
                if (System.currentTimeMillis() - updateTime >= PropKit.getLong("tokenOutTime", 180000L)){
                    //已超时
                    deleteByUserId(userId);
                    return Param.C_USER_NOT_LOGIN;
                }else {
                    if (to.equals(token)){
                        updateByUserId(userId, token);
                        return Param.C_SUCCESS;
                    }else {
                        deleteByUserId(userId);
                        return Param.C_USER_LOGIN_ON_OTHER;
                    }
                }
            }
        }
    }
}