package com.github.ittalks.commons.v2.retrofit.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    @Override
    public String convert(ResponseBody responseBody) throws IOException {
        try {
            return responseBody.string();
        } finally {
            responseBody.close();
        }
    }
}
