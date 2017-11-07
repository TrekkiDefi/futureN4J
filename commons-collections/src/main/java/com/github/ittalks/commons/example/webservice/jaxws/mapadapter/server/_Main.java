package com.github.ittalks.commons.example.webservice.jaxws.mapadapter.server;

import javax.xml.ws.Endpoint;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
class _Main {

    public static void main(String[] args) {
        String address = "http://127.0.0.1:9999/webservice";
        Endpoint.publish(address, new FnWebServiceImpl());
    }
}
