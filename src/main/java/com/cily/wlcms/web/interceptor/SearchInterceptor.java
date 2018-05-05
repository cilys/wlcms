package com.cily.wlcms.web.interceptor;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;

/**
 * Created by 123 on 2018/5/5.
 */
public class SearchInterceptor extends BaseInterceptor {
    @Override
    public void intercept(Invocation inv) {
        String searchText = getParam(inv, Param.SEARCH_TEXT);
        if (StrUtils.isEmpty(searchText)){
            renderJson(inv, Param.C_SEARCH_TEXT_NULL, createTokenByOs(inv), null);
        }else {
            if (searchText.length() > PropKit.getInt("searchTextMaxLength", 20)){
                renderJson(inv, Param.C_SEARCH_TEXT_TOO_LONG, createTokenByOs(inv), null);
            }else {
                inv.invoke();
            }
        }
    }
}
