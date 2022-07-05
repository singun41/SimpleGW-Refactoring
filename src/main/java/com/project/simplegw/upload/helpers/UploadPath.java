package com.project.simplegw.upload.helpers;

import java.io.File;
import java.time.LocalDate;

import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.upload.vos.UploadType;

public class UploadPath {
    public static String daily() {   // yyyy/mm/dd/ 형식으로 리턴하되, 월(m)과 일(d)는 1자리 또는 2자리이다.
        LocalDate now = LocalDate.now();
        return new StringBuilder()
            .append(now.getYear()).append(File.separator)
            .append(now.getMonthValue()).append(File.separator)
            .append(now.getDayOfMonth()).append(File.separator).toString();
    }


    public static String get(UploadType type) {
        switch(type) {
            case ATTACHMENTS:
                return new StringBuilder(Constants.ATTACHMENTS_UPLOAD_PATH).append(daily()).toString();
            
            case IMAGE:
                return new StringBuilder(Constants.IMAGE_UPLOAD_PATH).append(daily()).toString();

            default:
                return "";
        }
    }
}
