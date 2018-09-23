package com.cily.wlcms.web;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.controller.*;
import com.cily.wlcms.web.interceptor.DeviceImeiInterceptor;
import com.cily.wlcms.web.interceptor.LogInterceptor;
import com.cily.wlcms.web.interceptor.LoginedInterceptor;
import com.cily.wlcms.web.model.*;
import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.template.Engine;

/**
 * Created by admin on 2018/1/17.
 */
public class Conf extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        PropKit.use("conf.properties");
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add("user", UserController.class);
        me.add("sysUser", SysUserController.class);
        me.add("role", RoleController.class);
        me.add("record", RecordController.class);
        me.add("msg", MsgController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        C3p0Plugin c3p0 = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("pwd"));
        me.add(c3p0);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0);
        arp.setShowSql(PropKit.getBoolean("showSql"));
        me.add(arp);
        arp.addMapping(SQLParam.T_USER, SQLParam.USER_ID, UserModel.class);

//        arp.addMapping(SQLParam.T_RIGHT, SQLParam.RIGHT_ID, RightModel.class);
//        arp.addMapping(SQLParam.T_RIGHT_ROLE,
//                StrUtils.join(SQLParam.RIGHT_ID, ",",
//                        SQLParam.ROLE_ID), RightRoleModel.class);
//        arp.addMapping(SQLParam.T_ROLE, SQLParam.ROLE_ID, RoleModel.class);
//        arp.addMapping(SQLParam.T_USER_ROLE, SQLParam.USER_ID + "," + SQLParam.ROLE_ID, UserRoleModel.class);
        arp.addMapping(SQLParam.T_RECORD, SQLParam.RECORD_ID, RecordModel.class);
        arp.addMapping(SQLParam.T_TOKEN, SQLParam.USER_ID, TokenModel.class);
        arp.addMapping(SQLParam.T_MSG, SQLParam.MSG_ID, MsgModel.class);
        arp.addMapping(SQLParam.T_FRIEND,
                SQLParam.USER_ID_1 + "," + SQLParam.USER_ID_2, FriendModel.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
//        me.add(new LogInterceptor());
//        me.add(new DeviceImeiInterceptor());
//        me.add(new LoginedInterceptor());
//        me.add(new RightInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {

    }
}