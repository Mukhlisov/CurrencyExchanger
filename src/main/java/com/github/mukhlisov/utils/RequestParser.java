package com.github.mukhlisov.utils;

import com.github.mukhlisov.exceptions.EmptyCurrencyException;
import jakarta.servlet.ServletInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RequestParser {

    public static String getCodeFromRequestUri(String uri) throws EmptyCurrencyException {
        String code = null;
        int index = uri.lastIndexOf("/");
        if (index != 0){
            code = uri.substring(index + 1);
            if (code.isEmpty()){
                throw new EmptyCurrencyException("There is no currency name in the request URI");
            }
        }
        return code;
    }

    public static String getJSONData(ServletInputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line;
        while((line = reader.readLine()) != null){
            builder.append(line);
        }

        reader.close();
        return builder.toString();
    }
}
