package com.cily.wlcms.web.model;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.cily.wlcms.web.conf.SQLParam;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 123 on 2018/5/20.
 */
public class CommentModel extends Model<CommentModel> {
    private static CommentModel dao = new CommentModel();

    public static boolean insert(String pCommentId, String recordId, String comment,
                          String commentCreateUserId){
        CommentModel m = new CommentModel();
        m.set(SQLParam.COMMENT_ID, UUIDUtils.getUUID());
        if (!StrUtils.isEmpty(pCommentId)){
            m.set(SQLParam.COMMENT_P_ID, pCommentId);
        }
        m.set(SQLParam.RECORD_ID, recordId);
        m.set(SQLParam.COMMENT, comment);
        m.set(SQLParam.COMMENT_CREATE_USER_ID, commentCreateUserId);
        return m.save();
    }

    public static boolean del(String commentId){
        return dao.deleteById(commentId);
    }

    public static Page<CommentModel> searchCommentByRecordId(String recordId,
                                                             int pageNumber, int pageSize){
        return dao.paginate(pageNumber, pageSize,
                StrUtils.join("select ", SQLParam.T_COMMENT, ".*, ",
                SQLParam.T_USER, ".", SQLParam.USER_NAME, ", ", SQLParam.T_USER, ".", SQLParam.REAL_NAME),
                StrUtils.join(" from ", SQLParam.T_COMMENT, " left join ", SQLParam.T_USER, " on ",
                        SQLParam.T_COMMENT, ".", SQLParam.COMMENT_CREATE_USER_ID, " = ", SQLParam.T_USER, ".", SQLParam.USER_ID,
                        " where ", SQLParam.T_COMMENT, ".", SQLParam.RECORD_ID, " = '", recordId,
                        "' order by ", SQLParam.T_COMMENT, ".", SQLParam.UPDATE_TIME, " desc")
                );
    }


}
