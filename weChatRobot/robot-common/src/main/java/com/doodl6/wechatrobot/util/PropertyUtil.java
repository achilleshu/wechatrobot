package com.doodl6.wechatrobot.util;

import org.apache.commons.lang3.StringUtils;

public class PropertyUtil {

    public static String getProperty(String key) {
        String value = "";
        if(StringUtils.isNotBlank(key)){
            value  = System.getProperty(key);
        }
//        if (value == null) {
//            value = System.getenv(key);
//        }

        return value;
    }
}
