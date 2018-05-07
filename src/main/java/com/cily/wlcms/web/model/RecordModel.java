package com.cily.wlcms.web.model;

import com.cily.utils.base.StrUtils;
import com.cily.utils.base.UUIDUtils;
import com.cily.wlcms.web.conf.SQLParam;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by admin on 2018/4/16.
 */
public class RecordModel extends Model<RecordModel> {
    private static RecordModel dao = new RecordModel().dao();

    public static boolean insert(String recordName, String recordNum,
                                 String recordLevel, String recordContent,
                                 String recordImgUrl, String recordCreateUserId){
        if (StrUtils.isEmpty(recordName)){
            return false;
        }
        if (StrUtils.isEmpty(recordNum)){
            return false;
        }
        if (StrUtils.isEmpty(recordLevel)){
            return false;
        }
        if (StrUtils.isEmpty(recordCreateUserId)){
            return false;
        }

        RecordModel m = new RecordModel();
        m.set(SQLParam.RECORD_ID, UUIDUtils.getUUID());
        m.set(SQLParam.RECORD_NAME, recordName);
        m.set(SQLParam.RECORD_NUM, recordNum);
        m.set(SQLParam.RECORD_LEVEL, recordLevel);
        m.set(SQLParam.RECORD_CREATE_USER_ID, recordCreateUserId);
        if (!StrUtils.isEmpty(recordContent)){
            m.set(SQLParam.RECORD_CONTENT, recordContent);
        }
        if (!StrUtils.isEmpty(recordImgUrl)){
            m.set(SQLParam.RECORD_IMG_URL, recordImgUrl);
        }
        return m.save();
    }

    public static boolean updateRecord(String recordId, String recordName,
                                       String recordNum, String recordLevel,
                                       String recordContent, String recordImgUrl){
        RecordModel m = getRecordById(recordId);
        if (m == null){
            return false;
        }
        m.set(SQLParam.RECORD_ID, UUIDUtils.getUUID());
        m.set(SQLParam.RECORD_NAME, recordName);
        m.set(SQLParam.RECORD_NUM, recordNum);
        m.set(SQLParam.RECORD_LEVEL, recordLevel);
        if (!StrUtils.isEmpty(recordContent)){
            m.set(SQLParam.RECORD_CONTENT, recordContent);
        }
        if (!StrUtils.isEmpty(recordImgUrl)){
            m.set(SQLParam.RECORD_IMG_URL, recordImgUrl);
        }
        return m.update();
    }

    public static RecordModel getRecordById(String recordId){
        return dao.findById(recordId);
    }

    public static boolean delById(String recordId){
        return dao.deleteById(recordId);
    }

    public static boolean delByRecordNum(String recordNum){
        return Db.update(StrUtils.join("delete from ",
                SQLParam.T_RECORD, " where ", SQLParam.RECORD_NUM,
                " = '", recordNum, "';")) > 0;
    }

    public static Page<RecordModel> searchRecord(String searchText, String userId,
                                                 int pageNumber, int pageSize){
        if (StrUtils.isEmpty(userId)) {
            //未查询创建者
//            return dao.paginate(pageNumber, pageSize, "select * ",
//                    StrUtils.join(" from ", SQLParam.T_RECORD));
            return dao.paginate(pageNumber, pageSize, StrUtils.join("select ", SQLParam.T_USER, ".", SQLParam.USER_NAME, ", ", SQLParam.T_RECORD, ".* "),
                    StrUtils.join(" from ", SQLParam.T_RECORD, " left join ", SQLParam.T_USER, " on ",
                            SQLParam.T_USER, ".", SQLParam.USER_ID, " = ",
                            SQLParam.T_RECORD, ".", SQLParam.RECORD_CREATE_USER_ID,
                            " where ", SQLParam.RECORD_ID, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_NUM, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_NAME, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_CONTENT, " like '%", searchText, "%'"
                    ));
        }else {
            return dao.paginate(pageNumber, pageSize, StrUtils.join("select ", SQLParam.T_USER, ".", SQLParam.USER_NAME, ", ", SQLParam.T_RECORD, ".* "),
                    StrUtils.join(" from ", SQLParam.T_RECORD, " left join ", SQLParam.T_USER, " on ",
                            SQLParam.T_USER, ".", SQLParam.USER_ID, " = ",
                            SQLParam.T_RECORD, ".", SQLParam.RECORD_CREATE_USER_ID,
                            " where ", SQLParam.USER_ID, " = ", userId,
                            SQLParam.RECORD_ID, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_NUM, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_NAME, " like '%", searchText, "%' or ",
                            SQLParam.RECORD_CONTENT, " like '%", searchText, "%'"
                    ));
        }
    }

    public static Page<RecordModel> getRecordsByUserId(int pageNumber,
                                                       int pageSize, String userId) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (StrUtils.isEmpty(userId)) {
            //未查询创建者
//            return dao.paginate(pageNumber, pageSize, "select * ",
//                    StrUtils.join(" from ", SQLParam.T_RECORD));
            return dao.paginate(pageNumber, pageSize, StrUtils.join("select ", SQLParam.T_USER, ".", SQLParam.USER_NAME, ", ", SQLParam.T_RECORD, ".* "),
                    StrUtils.join(" from ", SQLParam.T_RECORD, " left join ", SQLParam.T_USER, " on ",
                            SQLParam.T_USER, ".", SQLParam.USER_ID, " = ", SQLParam.T_RECORD, ".", SQLParam.RECORD_CREATE_USER_ID
                            ));
        } else {
//            return dao.paginate(pageNumber, pageSize, "select * ",
//                    StrUtils.join(" from ", SQLParam.T_RECORD,
//                            " where ", SQLParam.RECORD_CREATE_USER_ID, " = '",
//                            userId, "'"));

            return dao.paginate(pageNumber, pageSize, StrUtils.join("select ", SQLParam.T_USER, ".", SQLParam.USER_NAME, ", ", SQLParam.T_RECORD, ".* "),
                    StrUtils.join(" from ", SQLParam.T_RECORD, " left join ", SQLParam.T_USER, " on ",
                            SQLParam.T_USER, ".", SQLParam.USER_ID, " = ", SQLParam.T_RECORD, ".", SQLParam.RECORD_CREATE_USER_ID,
                            " where ", SQLParam.RECORD_CREATE_USER_ID, " = '", userId, "'"
                    ));
        }
    }

}
