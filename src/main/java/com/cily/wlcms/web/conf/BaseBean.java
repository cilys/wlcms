package com.cily.wlcms.web.conf;

import com.cily.utils.base.StrUtils;

import java.io.Serializable;


/**
 * Created by admin on 2018/2/1.
 */
public class BaseBean<T> implements Serializable {
    private String code;
    private String msg;
    private String token;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <D>BaseBean getBaseBean(String code,
                                          String token, D t){
        if (StrUtils.isEmpty(code)){
            code = Param.C_SERVER_ERROR;
        }

        BaseBean b = new BaseBean();
        b.setCode(code);

        b.setMsg(Param.getMsg(code));

        if (t != null){
            b.setData(t);
        }
        if (!StrUtils.isEmpty(token)){
            b.setToken(token);
        }

        return b;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}