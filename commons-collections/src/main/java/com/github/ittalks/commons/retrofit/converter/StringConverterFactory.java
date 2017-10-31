package com.github.ittalks.commons.retrofit.converter;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by liuchunlong on 2017/7/15.
 */
public class StringConverterFactory extends Converter.Factory  {

    private static final StringConverterFactory factory = new StringConverterFactory();

    public static StringConverterFactory create() {
        return factory;
    }

    private StringConverterFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return new StringResponseBodyConverter();
        }
        //其它类型我们不处理，返回null
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == String.class) {
            return new StringRequestBodyConverter();
        }
        //其它类型我们不处理，返回null
        return null;
    }
}
