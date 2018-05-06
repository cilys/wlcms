package com.cily.wlcms.web.utils;

import com.cily.utils.base.StrUtils;
import com.jfinal.kit.PropKit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 123 on 2018/5/6.
 */
public class RootUserIdUtils {
    private static Map<String, String> map;

    public static boolean isRootUser(String userId){
        if (StrUtils.isEmpty(userId)){
            return false;
        }

        if (map == null){
            map = new HashMap<>();
            String rootUserIds = PropKit.get("rootUserId", "670b14728ad9902aecba32e22fa4f6bd");
            if (rootUserIds.contains(",")){
                String[] ids = rootUserIds.split(",");
                for (String s : ids){
                    map.put(s, s);
                }
            }else {
                map.put(rootUserIds, rootUserIds);
            }
        }
        if (StrUtils.isEmpty(map.get(userId))){
            return false;
        }else {
            return true;
        }
    }
}
