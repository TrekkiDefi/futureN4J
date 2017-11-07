package com.github.ittalks.commons.example.webservice.cxf.restful.server;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
@Component
public class EncodingLoggingInInterceptor extends LoggingInInterceptor {

    public EncodingLoggingInInterceptor() {
        super();
    }

    /**
     * @see org.apache.cxf.interceptor.LoggingInInterceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message message) throws Fault {
        String encoding = StringUtils.isEmpty(System.getProperty("file.encoding")) ? "UTF-8" : System.getProperty("file.encoding");
        message.put(Message.ENCODING, encoding);
        super.handleMessage(message);
    }
}
