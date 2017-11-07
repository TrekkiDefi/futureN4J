package com.github.ittalks.commons.example.webservice.cxf.interceptor.server;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Created by 刘春龙 on 2017/10/31.
 * <p>
 * 自定义拦截器
 */
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    public AuthInterceptor() {
        super(Phase.PRE_INVOKE); // 在调用方法之前调用自定义拦截器

    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        List<Header> headers = message.getHeaders(); // 根据soap消息获取头部
        if (headers == null || headers.size() == 0) {
            throw new Fault(new IllegalArgumentException("没有Header,拦截器实施拦截"));
        }

        QName qName = new QName("AuthHeader");
        Header header = message.getHeader(qName);
        Element elm = (Element) header.getObject();// 将该头部转成一个Element对象

        NodeList userList = elm.getElementsByTagName("username"); // 根据标签获取值
        NodeList pwdList = elm.getElementsByTagName("password");

        // 进行身份认证
        if (userList.getLength() != 1) {// 只有一个用户
            throw new Fault(new IllegalArgumentException("用户名格式不对"));
        }
        if (pwdList.getLength() != 1) {// 只有一个密码
            throw new Fault(new IllegalArgumentException("密码格式不对"));
        }

        String username = userList.item(0).getTextContent(); // 因为就一个,所以获取第一个即可
        String password = pwdList.item(0).getTextContent();

        if (!username.equals("admin") || !password.equals("123")) {
            throw new Fault(new IllegalArgumentException("用户名或者密码错误"));
        }
    }
}
