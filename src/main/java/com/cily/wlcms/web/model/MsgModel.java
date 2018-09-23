package com.cily.wlcms.web.model;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.cily.utils.base.time.TimeUtils;
import com.cily.wlcms.web.conf.SQLParam;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 123 on 2018/5/20.
 */
public class MsgModel extends Model<MsgModel> {
    private static MsgModel dao = new MsgModel();

    public static MsgModel createModel(String fromUserId, String toUserId, String msg, String msgType){
        if (StrUtils.isEmpty(fromUserId) || StrUtils.isEmpty(toUserId) || StrUtils.isEmpty(msg)){
            return null;
        }
        MsgModel m = new MsgModel();
        m.set(SQLParam.MSG_ID, UUIDUtils.getUUID());
        m.set(SQLParam.FROM_USER_ID, fromUserId);
        m.set(SQLParam.TO_USER_ID, toUserId);
        m.set(SQLParam.MSG, msg);
        if ("0".equals(msgType)){
            m.set(SQLParam.MSG_TYPE, "0");
        }else {
            m.set(SQLParam.MSG_TYPE, "1");
        }
        m.set(SQLParam.CREATE_TIME, TimeUtils.milToStr(System.currentTimeMillis(), null));

        return m;
    }

    public static boolean insert(MsgModel m){
        if (m != null){
            boolean flag = m.save();
            if (flag){
                String userId1 = m.get(SQLParam.FROM_USER_ID);
                String userId2 = m.get(SQLParam.TO_USER_ID);
                String msgId = m.get(SQLParam.MSG_ID);
                boolean n = FriendModel.insertOrUpdate(userId1, userId2, msgId);
                if (!n){
                    m.delete();
                }
                return n;
            }
            return flag;
        }else {
            return false;
        }
    }

    public static List<MsgModel> getAllMsgs(String userId1, String userId2){
        if (StrUtils.isEmpty(userId1) || StrUtils.isEmpty(userId2)){
            return null;
        }
        return dao.find(StrUtils.join(
                "select * from ", SQLParam.T_MSG, " where ((",
                SQLParam.FROM_USER_ID, " = '", userId1, "' and ",
                SQLParam.TO_USER_ID, " = '", userId2, "') or (",

                SQLParam.FROM_USER_ID, " = '", userId2, "' and ",
                SQLParam.TO_USER_ID, " = '", userId1, "')) order by ", SQLParam.UPDATE_TIME, " desc"));
    }

    public static boolean del(String msgId){
        MsgModel m = dao.findById(msgId);
        if (m == null){
            return false;
        }else {
            String userId1 = m.get(SQLParam.FROM_USER_ID);
            String userId2 = m.get(SQLParam.TO_USER_ID);

            boolean flag = m.delete();
            if (flag){
                List<MsgModel> ls = getAllMsgs(userId1, userId2);
                if (ls == null || ls.size() < 1){
                    FriendModel.updateMsgCount(userId1, userId2, null, 1);
                }else {
                    MsgModel lastMsg = ls.get(0);
                    if (lastMsg == null){
                        FriendModel.updateMsgCount(userId1, userId2, null, 1);
                    }else {
                        String newMsgId = lastMsg.get(SQLParam.MSG_ID);
                        FriendModel.updateMsgCount(userId1, userId2, newMsgId, ls.size());
                    }
                }

                return true;
            }else {
                return false;
            }
        }
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

    public static Page<MsgModel> getMsgs(int pageNumber, int pageSize, String searchText){
        if (StrUtils.isEmpty(searchText)) {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG,
                            " order by ",
                            SQLParam.UPDATE_TIME, " desc"));
        }else {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG, " where ",
                            SQLParam.MSG, " like '%", searchText, "%'", " order by ",
                            SQLParam.UPDATE_TIME, " desc"));
        }
    }

    public static Page<MsgModel> searchMsg(int pageNumber, int pageSize, String fromUserId,
                                                String toUserId, String searchText){
        if (StrUtils.isEmpty(searchText)) {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG, " where ",
                            "((", SQLParam.FROM_USER_ID, " = '", fromUserId, "' and ", SQLParam.TO_USER_ID, " = '", toUserId, "'", ") or (",
                            SQLParam.FROM_USER_ID, " = '", toUserId, "' and ", SQLParam.TO_USER_ID, " = '", fromUserId, "'))",
                            " order by ",
                            SQLParam.UPDATE_TIME, " desc"));
        }else {
            return dao.paginate(pageNumber, pageSize, " select * ",
                    StrUtils.join(" from ", SQLParam.T_MSG, " where ",
                            "((", SQLParam.FROM_USER_ID, " = '", fromUserId, "' and ", SQLParam.TO_USER_ID, " = '", toUserId, "'", ") or (",
                            SQLParam.FROM_USER_ID, " = '", toUserId, "' and ", SQLParam.TO_USER_ID, " = '", fromUserId, "'))",
                            " and ",
                            SQLParam.MSG, " like '%", searchText, "%'", " order by ",
                            SQLParam.UPDATE_TIME, " desc"));
        }
    }

    public static Page<MsgModel> searchMsgGroup(int pageNumber, int pageSize, String userId){
//        return dao.paginate(pageNumber, pageSize, " select * ",
//                StrUtils.join(" from (", "select * from ", SQLParam.T_MSG, " where ", SQLParam.FROM_USER_ID, " = '", userId,
//                        "' or ", SQLParam.TO_USER_ID, "= '", userId, "'", " order by ",
//                        SQLParam.UPDATE_TIME, " DESC) temp group by ", SQLParam.FROM_USER_ID, " and ", SQLParam.TO_USER_ID));



        return dao.paginate(pageNumber, pageSize,"select distinct " + SQLParam.T_MSG + ".* ",
                StrUtils.join(" from ", SQLParam.T_FRIEND, " left join ", SQLParam.T_MSG, " on ",
                        SQLParam.T_FRIEND, ".", SQLParam.MSG_ID, " = ", SQLParam.T_MSG, ".", SQLParam.MSG_ID,
                        " where (", SQLParam.T_FRIEND, ".", SQLParam.USER_ID_1, " = '", userId, "' or ", SQLParam.T_FRIEND, ".", SQLParam.USER_ID_2, " = '", userId, "')",
                        " order by ", SQLParam.T_FRIEND, ".", SQLParam.UPDATE_TIME, " desc"
                ));

    }

    public static long allMsgCounts(){
        return Db.queryLong("select count(1) from " + SQLParam.T_MSG);
    }

}