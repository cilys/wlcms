package com.cily.wlcms.web.controller;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.*;
import com.cily.wlcms.web.model.RecordModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.PropKit;

import java.io.File;
import java.util.Map;

/**
 * Created by 123 on 2018/4/22.
 */
@Clear({DeviceImeiInterceptor.class, LogInterceptor.class})
public class RecordController extends BaseImagesController {
    String fileDir = PropKit.get("images_dirs_report", "/");

//    @Before({RecordAddInterceptor.class})
    public void add(){

//        String img0 = null, img1 = null, img2 = null,
//                img3 = null, img4 = null, img5 = null,
//                img6 = null, img7 = null, img8 = null;
        String fileUrl = null;
        try {
            Map<String, String> param = getImages(fileDir);
            if (param != null && param.size() > 0){
                String recordName = param.getOrDefault(SQLParam.RECORD_NAME, null);
                String recordNum = param.getOrDefault(SQLParam.RECORD_NUM, null);
                String recordLevel = param.getOrDefault(SQLParam.RECORD_LEVEL, null);
                String recordContent = param.getOrDefault(SQLParam.RECORD_CONTENT, null);

//                img0 = param.getOrDefault(SQLParam.RECORD_IMG_URL_0, null);
//                img1 = param.getOrDefault(SQLParam.RECORD_IMG_URL_1, null);
//                img2 = param.getOrDefault(SQLParam.RECORD_IMG_URL_2, null);
//                img3 = param.getOrDefault(SQLParam.RECORD_IMG_URL_3, null);
//                img4 = param.getOrDefault(SQLParam.RECORD_IMG_URL_4, null);
//                img5 = param.getOrDefault(SQLParam.RECORD_IMG_URL_5, null);
//                img6 = param.getOrDefault(SQLParam.RECORD_IMG_URL_6, null);
//                img7 = param.getOrDefault(SQLParam.RECORD_IMG_URL_7, null);
//                img8 = param.getOrDefault(SQLParam.RECORD_IMG_URL_8, null);
//
//                String url = join(img0, img1, img2, img3, img4, img5, img6, img7, img8);
                fileUrl = param.getOrDefault(SQLParam.RECORD_IMG_URL, null);

//                String userId = "670b14728ad9902aecba32e22fa4f6bd";

                if (RecordModel.insert(recordName, recordNum, recordLevel, recordContent, fileUrl, getUserId())){
                    renderJsonSuccess( null);
                    return;
                }else {
                    delFile(fileDir, fileUrl);
                    renderJsonFailed(Param.C_RECORD_ADD_FAILED,  null);
                    return;
                }
            }else {
                delFile(fileDir, fileUrl);
                renderJsonFailed(Param.C_RECORD_ADD_PARAM_NULL,  null);
                return;
            }
        } catch (Exception e) {
            delFile(fileDir, fileUrl);
            e.printStackTrace();
            renderJsonFailed(Param.C_SERVER_ERROR,  null);
        }
    }

    public void getRecordsByUserId(){
        String userId = getParam(SQLParam.USER_ID);
        String recordStatus = getPara(SQLParam.RECORD_STATUS, null);
        renderJsonSuccess(RecordModel.getRecordsByUserId(getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10), userId, recordStatus));
    }

    //userId不为空，查自己的。为空，则查任何人的
    @Before({SearchInterceptor.class})
    public void searchRecords(){
        renderJsonSuccess(RecordModel.searchRecord(getParam(Param.SEARCH_TEXT),
                getParam(SQLParam.USER_ID),
                getParaToInt(Param.PAGE_NUMBER, 1),
                getParaToInt(Param.PAGE_SIZE, 10)));
    }

    @Before({RecordIdInterceptor.class, RecordDelInterceptor.class})
    public void del() {
        String recordId = getParam(SQLParam.RECORD_ID);
        delFile(fileDir, RecordModel.getRecordById(recordId).getStr(SQLParam.RECORD_IMG_URL));
        if (RecordModel.delById(recordId)) {
            renderJsonSuccess(null);
        } else {
            renderJsonFailed(Param.C_RECORD_DEL_FAILED, null);
        }
    }

    private String join(String... strs){
        if (strs != null && strs.length > 0){
            StringBuilder su = StrUtils.getStringBuilder();
            for (String s : strs){
                if (!StrUtils.isEmpty(s)){
                    su.append(s);
                    su.append(",");
                }
            }
            if (su != null && su.length() > 0){
                String s = su.toString();
                if (s.endsWith(",") && s.length() > 1){
                    s.substring(0, s.length() - 2);
                }
                return s;
            }
        }
        return null;
    }

    private void delFile(String filaDir, String fileUrl){
        if (fileUrl != null && fileUrl.length() > 0){
            String[] files = fileUrl.split(",");
            if (files != null) {
                for (String file : files) {
                    if (!StrUtils.isEmpty(file)) {
                        File f = new File(filaDir + File.separator + file);
                        if (f != null && f.exists() && f.isFile()) {
                            f.delete();
                        }
                    }
                }
            }
        }
    }
}