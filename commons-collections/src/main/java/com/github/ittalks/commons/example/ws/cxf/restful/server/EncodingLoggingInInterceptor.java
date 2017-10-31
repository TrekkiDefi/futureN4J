package com.github.ittalks.commons.example.ws.cxf.restful.server;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public class EncodingLoggingInInterceptor extends LoggingInInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    public EncodingLoggingInInterceptor() {
        // TODO Auto-generated constructor stub
        super();
    }

    /**
     * @see org.apache.cxf.interceptor.LoggingInInterceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message message) throws Fault {

        // TODO Auto-generated method stub
        String encoding = System.getProperty("file.encoding");
        encoding = StringUtils.isEmpty(encoding) ? "UTF-8" : encoding;
        log.debug("encoding : " + encoding);

        message.put(Message.ENCODING, encoding);
        super.handleMessage(message);
    }
}
