package com.project.simplegw.system.vos;

import java.util.Arrays;

public class Constants {
    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- column size ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //
    public static final int COLUMN_LENGTH_ROLE = 10;
    public static final int COLUMN_LENGTH_MENU = 20;
    public static final int COLUMN_LENGTH_RWD = 4;   // NONE, R, W, D, RW, RD, WD, RWD

    public static final int COLUMN_LENGTH_USER_ID = 30;
    public static final int COLUMN_LENGTH_PW = 70;
    
    public static final int COLUMN_LENGTH_TEAM = 50;
    public static final int COLUMN_LENGTH_NAME = 20;
    public static final int COLUMN_LENGTH_JOB_TITLE = 30;
    public static final int COLUMN_LENGTH_MOBILE_NO = 13;   // format: 000-0000-0000
    public static final int COLUMN_LENGTH_MAIL_ADDRESS = 50;
    
    public static final int COLUMN_LENGTH_DOCU_TYPE = 20;

    public static final int COLUMN_LENGTH_DOCU_TITLE = 200;
    public static final int COLUMN_LENGTH_COMMENT = 1000;

    public static final int COLUMN_LENGTH_REMARKS = 500;

    public static final int COLUMN_LENGTH_BASECODE_TYPE = 20;
    public static final int COLUMN_LENGTH_BASECODE_CODE = 3;
    public static final int COLUMN_LENGTH_BASECODE_VALUE = 50;

    public static final int COLUMN_LENGTH_APPROVAL_SIGN_TYPE = 10;

    public static final int COLUMN_LENGTH_FILE_NAME = 200;
    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- column size ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //





    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- column define ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //
    public static final String COLUMN_DEFINE_TITLE = "nvarchar(" + COLUMN_LENGTH_DOCU_TITLE + ")";
    public static final String COLUMN_DEFINE_CONTENT = "nvarchar(max)";
    public static final String COLUMN_DEFINE_COMMENT = "nvarchar(" + COLUMN_LENGTH_COMMENT + ")";

    public static final String COLUMN_DEFINE_TEAM = "nvarchar(" + COLUMN_LENGTH_TEAM + ")";
    public static final String COLUMN_DEFINE_NAME = "nvarchar(" + COLUMN_LENGTH_NAME + ")";
    public static final String COLUMN_DEFINE_JOB_TITLE = "nvarchar(" + COLUMN_LENGTH_JOB_TITLE + ")";
    
    public static final String COLUMN_DEFINE_DATE = "date";
    public static final String COLUMN_DEFINE_TIME = "time";
    public static final String COLUMN_DEFINE_DATETIME = "datetime";

    public static final String COLUMN_DEFINE_BASECODE_VALUE = "nvarchar(" + COLUMN_LENGTH_BASECODE_VALUE + ")";

    public static final String COLUMN_DEFINE_REMARKS = "nvarchar(" + COLUMN_LENGTH_REMARKS + ")";

    public static final String COLUMN_DEFINE_FILE_NAME = "nvarchar(" + COLUMN_LENGTH_FILE_NAME + ")";

    public static final String COLUMN_DEFINE_UNIQUE_IDENTIFIER = "uniqueidentifier";
    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- column define ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //





    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- System ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //
    public static final String USERNAME_PARAM = "userId";
    public static final String PASSWORD_PARAM = "userPw";

    public static final String LOGIN_PAGE = "/login";
    public static final String ERROR_PAGE_403 = "error/403";
    public static final String ERROR_PAGE_403_MODIFY = "error/403-modify";
	public static final String ERROR_PAGE_410 = "error/410";


    public static final String STRING_SYSTEM = "System";

    public static final String SYSTEM_PATH = "C:/WebApp/SpringBoot/SimpleGW/";
    public static final String UPLOAD_ROOT_PATH = "D:/WebApp/SpringBoot/SimpleGW/attachments/";


    public static final String ATTACHMENTS_UPLOAD_PATH = UPLOAD_ROOT_PATH + "files/";
    public static final long ATTACHMENTS_UPLOAD_MAX_SIZE = 1024 * 1024 * 50;   // 50MB


    public static final String IMAGE_UPLOAD_PATH = UPLOAD_ROOT_PATH + "images/";
    public static final long IMAGE_UPLOAD_MAX_SIZE = 1024 * 1024 * 20;   // 20MB

    public static final String IMAGE_EXTENSION_PATTERNS = "jpg|jpeg|png|gif|bmp|tiff|tif";
    public static final String IMAGE_EXTENSIONS = Arrays.toString(IMAGE_EXTENSION_PATTERNS.split("[|]"));   // 특수문자를 구분자로 할 땐 []로 감싸주자.

    public static final float IMAGE_UPLOAD_QUALITY = 0.5f;   // 업로드하는 이미지 용량 압축을 위해 추가. 화질 감소는 거의 없고 용량이 많이 절약된다.

    public static final String IMAGE_GET_URL = "/images/";


    public static final String MEMBER_PORTRAIT_PATH = SYSTEM_PATH + "member_portrait/";
    public static final String PORTRAIT_IMAGE_EXTENSION = ".png";

    
    public static final String EDITOR_FORMS_PATH = SYSTEM_PATH + "editor_forms";


    public static final long SSE_EMITTER_TIME_OUT = 1_000L * 60 * 15;   // 15 min


    public static final long NOTIFICATION_STORED_DURATION = 30L;


    public static final String CACHE_MANAGER = "cacheManager";

    public static final String CACHE_NOTICE = "notice";
    public static final String CACHE_FREEBOARD = "freeboard";
    public static final String CACHE_POST_IT = "postIt";
    public static final String CACHE_HOLIDAYS = "holidays";
    public static final String CACHE_TEMPDOCS_COUNT = "tempdocsCount";
    public static final String CACHE_ALARMS = "alarms";
    public static final String CACHE_EDITOR_FORMS = "editorForms";

    public static final String CACHE_APPROVAL_PROCEED_COUNT = "cntProceed";
    public static final String CACHE_APPROVAL_APPROVER_COUNT = "cntApprover";
    public static final String CACHE_APPROVAL_REFERRER_COUNT = "cntReferrer";
    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- System ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //





    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- others ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //
    public static final int PW_UPDATE_AT_LEAST_LENGTH = 8;
    public static final String REGEXP_PW = "((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{" + PW_UPDATE_AT_LEAST_LENGTH + ",})";

    public static final String REGEXP_MOBILE_NO = "\\d{3}-\\d{3,4}-\\d{4}";   // format: 000-0000-0000
    public static final String REGEXP_EMAIL = "\\w+@\\w+\\.\\w+(\\.\\w+)*";
    public static final String REGEXP_TIME = "([01]?[0-9]|2[0-3]):[0-5][0-9]";   // 00:00 ~ 23:59
    // ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- others ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- //
}
