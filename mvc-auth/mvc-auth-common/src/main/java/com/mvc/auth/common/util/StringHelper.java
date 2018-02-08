package com.mvc.auth.common.util;

/**
 * @author qyc
 */
public class StringHelper {
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
