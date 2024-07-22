package com.github.mukhlisov.utils;

public class RequestParser {

    public static String getCodeFromRequestUri(String uri) {
        String code = null;
        if (uri.lastIndexOf("/") != 0){
            code = uri.substring(uri.lastIndexOf("/") + 1);
        }
        return code;
    }
}
