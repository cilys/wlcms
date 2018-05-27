package com.cily.wlcms.web.conf;

/**
 * Created by admin on 2018/1/30.
 */
public interface SQLParam {
    String T_USER = "t_user";
    String T_ROLE = "t_role";
    String T_RIGHT = "t_right";
    String T_USER_ROLE = "t_user_role";
    String T_RIGHT_ROLE = "t_right_role";
    String T_PROJECT = "t_project";
    String T_USER_PROJECT = "t_user_project";
    String T_RECORD = "t_record";
    String T_TOKEN = "t_token";
    String T_COMMENT = "t_comment";
    String T_MSG = "t_msg";


    String USER_ID = "userId";
    String USER_NAME = "userName";
    String PWD = "pwd";
    String REAL_NAME = "realName";
    String SEX = "sex";
    String PHONE = "phone";
    String ADDRESS = "address";
    String ID_CARD = "idCard";
    String STATUS = "status";
    String CREATE_TIME = "createTime";
    String TOKEN = "token";
    String UPDATE_TIME = "updateTime";

    String ROLE_ID = "roleId";
    String ROLE_NAME = "roleName";

    String RIGHT_ID = "rightId";
    String RIGHT_NAME = "rightName";
    String ACCESS_URL = "accessUrl";

    String PRO_ID = "proId";
    String P_ID = "pId";
    String PRO_NAME = "proName";
    String PRO_CONTENT = "proContent";
    String LAST_EDIT_USER_ID = "lastEditUserId";


    String STATUS_ENABLE = "0";
    String STATUS_DISABLE = "1";

    String RECORD_ID = "recordId";
    String RECORD_NAME = "recordName";
    String RECORD_NUM = "recordNum";
    String RECORD_LEVEL = "recordLevel";
    String RECORD_CONTENT = "recordContent";
    String RECORD_IMG_URL = "recordImgUrl";
    String RECORD_CREATE_USER_ID = "recordCreateUserId";
    String RECORD_STATUS = "recordStatus";

    //最多9张图片，name按照这个顺序来命名
    String RECORD_IMG_URL_0 = "recordImgUrl0";
    String RECORD_IMG_URL_1 = "recordImgUrl1";
    String RECORD_IMG_URL_2 = "recordImgUrl2";
    String RECORD_IMG_URL_3 = "recordImgUrl3";
    String RECORD_IMG_URL_4 = "recordImgUrl4";
    String RECORD_IMG_URL_5 = "recordImgUrl5";
    String RECORD_IMG_URL_6 = "recordImgUrl6";
    String RECORD_IMG_URL_7 = "recordImgUrl7";
    String RECORD_IMG_URL_8 = "recordImgUrl8";

    String COMMENT_ID = "commentId";
    String COMMENT_P_ID = "pCommentId";
    String COMMENT = "comment";
    String COMMENT_CREATE_USER_ID = "commentCreateUserId";
    String COMMENT_STATUS = "commentStatus";

    String MSG_ID = "msgId";
    String FROM_USER_ID = "fromUserId";
    String TO_USER_ID = "toUserId";
    String MSG = "msg";
}