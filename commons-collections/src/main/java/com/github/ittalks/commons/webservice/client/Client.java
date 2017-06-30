package com.github.ittalks.commons.webservice.client;

import com.github.ittalks.commons.jackson.JsonUtils;
import com.github.ittalks.commons.webservice.IHelloWebService;
import com.github.ittalks.commons.webservice.entity.User;
import com.github.ittalks.commons.webservice.entity.Users;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Created by 刘春龙 on 2017/5/2.
 */
public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        Client client = new Client();
//        client.runJaxWS();
        client.runJaxRS();
    }

    public void runJaxWS() {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(IHelloWebService.class);
        factoryBean.setAddress("http://localhost:9090/ws_demo_server/webservice/ws/hello");
        IHelloWebService helloWebService = (IHelloWebService) factoryBean.create();

        HashMap<String, User> map = new HashMap<String, User>();
        User tom = new User();
        tom.setName("tom");
        map.put("1", tom);
        User Dave = new User();
        Dave.setName("Dave");
        map.put("2", Dave);

        Users users = new Users();
        users.setMaps(map);
        logger.info(">> 响应：\n" + JsonUtils.toNonEmptyJson(helloWebService.conveyMap(users, 1)));
    }

    public void runJaxRS() {
        try {
            queryUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryUser() throws Exception {
        GetMethod get = new GetMethod(
                "http://localhost:9090/ws_demo_server/webservice/rest/user/queryUser");
        get.setRequestHeader("accept", "application/xml");

        HttpClient hc = new HttpClient();
        hc.getParams().setContentCharset("UTF-8"); // 设置编码

        int code = hc.executeMethod(get);
        logger.info("Get方式返回的状态码：" + code);
        if (code == 200) {
            String result = get.getResponseBodyAsString();
            logger.info(">> 响应：\n" + result);
        }
        get.releaseConnection();
    }
}
