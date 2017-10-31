package com.github.ittalks.commons.retrofit.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

/**
 * Created by liuchunlong on 2017/7/15.
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
