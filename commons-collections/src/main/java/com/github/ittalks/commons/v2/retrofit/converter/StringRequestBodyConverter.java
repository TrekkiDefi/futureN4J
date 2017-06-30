package com.github.ittalks.commons.v2.retrofit.converter;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * Created by 刘春龙 on 2017/6/7.
 */
public class StringRequestBodyConverter implements Converter<String, RequestBody> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public RequestBody convert(String s) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        writer.write(s);
        writer.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
