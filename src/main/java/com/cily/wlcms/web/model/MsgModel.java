package com.cily.wlcms.web.model;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.cily.utils.base.time.TimeUtils;
import com.cily.wlcms.web.conf.SQLParam;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 123 on 2018/5/20.
 */
public class MsgModel extends Model<MsgModel> {
    private static MsgModel dao = new MsgModel();

    public static MsgModel createModel(String fromUserId, String toUserId, String msg){
        if (StrUtils.isEmpty(fromUserId) || StrUtils.isEmpty(toUserId) || StrUtils.isEmpty(msg)){
            return null;
        }
        MsgModel m = new MsgModel();
        m.set(SQLParam.MSG_ID, UUIDUtils.getUUID());
        m.set(SQLParam.FROM_USER_ID, fromUserId);
        m.set(SQLParam.TO_USER_ID, toUserId);
        m.set(SQLParam.MSG, msg);
        m.set(SQLParam.CREATE_TIME, TimeUtils.milToStr(System.currentTimeMillis(), null));

        return m;
    }

    public static boolean insert(MsgModel m){
        return m != null && m.save();
    }

    public static boolean del(long msgId){
        return dao.deleteById(msgId);
    }

    public static boolean enableMsg(long msgId, String status){
        MsgModel m = dao.findById(msgId);
        if (m == null){
            return false;
        }
        if (SQLParam.STATUS_ENABLE.equals(status)){
            m.set(SQLParam.STATUS, status);
        }else {
            m.set(SQLParam.STATUS, SQLParam.STATUS_DISABLE);
        }
        return m.update();
    }

    public static Page<MsgModel> searchMsg(int pageNumber, int pageSize, String fromUserId,
                                                String toUserId, String searchText){
        if (StrUtils.isEmpty(searchText)) {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG, " where ",
                            "((", SQLParam.FROM_USER_ID, " = '", fromUserId, "' and ", SQLParam.TO_USER_ID, " = '", toUserId, "'", ") or (",
                            SQLParam.FROM_USER_ID, " = '", toUserId, "' and ", SQLParam.TO_USER_ID, " = '", fromUserId, "'))",
                            " order by ",
                            SQLParam.CREATE_TIME, " desc"));
        }else {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG, " where ",
                            "((", SQLParam.FROM_USER_ID, " = '", fromUserId, "' and ", SQLParam.TO_USER_ID, " = '", toUserId, "'", ") or (",
                            SQLParam.FROM_USER_ID, " = '", toUserId, "' and ", SQLParam.TO_USER_ID, " = '", fromUserId, "'))",
                            " and ",
                            SQLParam.MSG, " like '%", searchText, "%'", " order by ",
                            SQLParam.CREATE_TIME, " desc"));
        }
    }

    public static Page<MsgModel> searchMsgGroup(int pageNumber, int pageSize, String userId){
        return dao.paginate(pageNumber, pageSize, " select * ",
                StrUtils.join(" from (", "select * from ", SQLParam.T_MSG, " where ", SQLParam.FROM_USER_ID, " = '", userId,
                        "' or ", SQLParam.TO_USER_ID, "= '", userId, "'", " order by ",
                        SQLParam.UPDATE_TIME, " DESC) temp group by ", SQLParam.FROM_USER_ID, " and ", SQLParam.TO_USER_ID));
    }
}