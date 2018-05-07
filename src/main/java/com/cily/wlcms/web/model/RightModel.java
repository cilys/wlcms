package com.cily.wlcms.web.model;

import com.cily.wlcms.web.conf.SQLParam;
import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by admin on 2018/2/22.
 */
public class RightModel extends Model<RightModel> {
    private static RightModel dao = new RightModel().dao();

    public static boolean insert(String rightName, String accessUrl,
            String status){
        if (StrUtils.isEmpty(rightName) || StrUtils.isEmpty(accessUrl)){
            return false;
        }
        RightModel m = new RightModel();
        m.set(SQLParam.RIGHT_ID, UUIDUtils.getUUID());
        m.set(SQLParam.RIGHT_NAME, rightName);
        m.set(SQLParam.ACCESS_URL, accessUrl);

        if (StrUtils.isEmpty(status)){
            m.set(SQLParam.STATUS, SQLParam.STATUS_ENABLE);
        }else {
            m.set(SQLParam.STATUS, status);
        }
        return m.save();
    }

    public static boolean del(String rightId){
        if (StrUtils.isEmpty(rightId)){
            return false;
        }
        return dao.deleteById(rightId);
    }

    public static boolean rightNameIsExist(String rightName){
        if (StrUtils.isEmpty(rightName)){
            return true;
        }
        return dao.findFirst(StrUtils.join("select * from ",
                SQLParam.RIGHT_NAME, " = '", rightName, "';")) != null;
    }

    public static boolean rightIsExist(String rightId){
        if (StrUtils.isEmpty(rightId)){
            return false;
        }
        return dao.findById(rightId) != null;
    }

    public static boolean accessUrlIsExist(String accessUrl){
        if (StrUtils.isEmpty(accessUrl)){
            return true;
        }
        return dao.findFirst(StrUtils.join("select * from ",
                SQLParam.T_RIGHT, " where ", SQLParam.ACCESS_URL,
                " = '", accessUrl, "';")) != null;
    }


    public static boolean update(String rightId, String rightName,
                                 String accessUrl, String status){
        if (StrUtils.isEmpty(rightId)){
            return false;
        }

        RightModel m = dao.findById(rightId);
        if (m == null){
            return false;
        }
        if (!StrUtils.isEmpty(rightName)){
            m.set(SQLParam.RIGHT_NAME, rightName);
        }
        if (!StrUtils.isEmpty(accessUrl)){
            m.set(SQLParam.ACCESS_URL, accessUrl);
        }
        if (!StrUtils.isEmpty(status)){
            m.set(SQLParam.STATUS, status);
        }

        return m.update();
    }

    public static Page<RightModel> getRights(int pageNumber, int pageSize, String status){
        if (pageSize < 1){
            pageSize = 10;
        }
        if (pageNumber < 1){
            pageNumber = 1;
        }

        if (SQLParam.STATUS_ENABLE.equals(status) || SQLParam.STATUS_DISABLE.equals(status)){
            String sql = StrUtils.join(" from ", SQLParam.T_RIGHT," where ", SQLParam.STATUS, " = '" + status + "'");
            return dao.paginate(pageNumber, pageSize, "select * ", sql);
        }else {
            return dao.paginate(pageNumber, pageSize, "select * ", " from " + SQLParam.T_RIGHT);
        }
    }
}