package com.github.ittalks.commons.example.webservice.cxf.interceptor.client;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/31.
 */
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private String username;
    private String password;

    public AuthInterceptor(String username, String password) {
        super(Phase.PREPARE_SEND); // 准备发送soap消息的时候调用拦截器
        this.username = username;
        this.password = password;
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        List<Header> headers = message.getHeaders();
        Document doc = DOMUtils.createDocument();

        // 定义三个对象
        Element elm = doc.createElement("AuthHeader");
        Element userElm = doc.createElement("username");
        Element pwdElm = doc.createElement("password");

        // 给用户名和密码对象赋值
        userElm.setTextContent(username);
        pwdElm.setTextContent(password);

        // 将用户名和密码的对象添加到elm中
        elm.appendChild(userElm);
        elm.appendChild(pwdElm);

        headers.add(new Header(new QName("head"), elm));// 往soap消息头部中添加这个elm元素
    }
}
