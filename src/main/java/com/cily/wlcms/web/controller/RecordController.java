package com.cily.wlcms.web.controller;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.Param;
import com.cily.wlcms.web.conf.SQLParam;
import com.cily.wlcms.web.interceptor.RecordAddInterceptor;
import com.cily.wlcms.web.model.RecordModel;
import com.cily.wlcms.web.utils.ResUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;

import java.io.File;
import java.util.Map;

/**
 * Created by 123 on 2018/4/22.
 */
public class RecordController extends BaseImagesController {

    @Before({RecordAddInterceptor.class})
    public void add(){
        try {
            Map<String, String> param = getImages(PropKit.get("images_dirs_report", "/"));
            if (param != null && param.size() > 0){
                String recordName = param.getOrDefault(SQLParam.RECORD_NAME, null);
                String recordNum = param.getOrDefault(SQLParam.RECORD_NUM, null);
                String recordLevel = param.getOrDefault(SQLParam.RECORD_LEVEL, null);
                String recordContent = param.getOrDefault(SQLParam.RECORD_CONTENT, null);

                String img0 = param.getOrDefault(SQLParam.RECORD_IMG_URL_0, null);
                String img1 = param.getOrDefault(SQLParam.RECORD_IMG_URL_1, null);
                String img2 = param.getOrDefault(SQLParam.RECORD_IMG_URL_2, null);
                String img3 = param.getOrDefault(SQLParam.RECORD_IMG_URL_3, null);
                String img4 = param.getOrDefault(SQLParam.RECORD_IMG_URL_4, null);
                String img5 = param.getOrDefault(SQLParam.RECORD_IMG_URL_5, null);
                String img6 = param.getOrDefault(SQLParam.RECORD_IMG_URL_6, null);
                String img7 = param.getOrDefault(SQLParam.RECORD_IMG_URL_7, null);
                String img8 = param.getOrDefault(SQLParam.RECORD_IMG_URL_8, null);

                String url = join(img0, img1, img2, img3, img4, img5, img6, img7, img8);

                if (RecordModel.insert(recordName, recordNum, recordLevel, recordContent, url)){
                    renderJson(ResUtils.success(createTokenByOs(), null));
                    return;
                }else {
                    delFile(img0, img1, img2, img3, img4, img5, img6, img7, img8);
                    renderJson(ResUtils.res(Param.C_RECORD_ADD_FAILED, createTokenByOs(), null));
                    return;
                }
            }else {
                renderJson(ResUtils.res(Param.C_RECORD_ADD_PARAM_NULL, createTokenByOs(), null));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            renderJson(ResUtils.res(Param.C_SERVER_ERROR, createTokenByOs(), null));
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
                if (s.endsWith(",")){
                    s.substring(0, s.length() - 1);
                }
                return s;
            }
        }
        return null;
    }

    private void delFile(String... files){
        if (files != null && files.length > 0){
            for (String file : files){
                if (!StrUtils.isEmpty(file)){
                    File f = new File(file);
                    if (f != null && f.exists() && f.isFile()){
                        f.delete();
                    }
                }
            }
        }
    }

}