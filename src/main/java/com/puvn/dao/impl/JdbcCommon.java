package com.puvn.dao.impl;

public class JdbcCommon {
    public static String parseDate(String date) {
        return date.substring(0, date.length() - 5);
    }
}
