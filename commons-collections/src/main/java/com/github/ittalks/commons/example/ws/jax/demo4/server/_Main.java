package com.github.ittalks.commons.example.ws.jax.demo4.server;

import javax.xml.ws.Endpoint;

/**
 * Created by 刘春龙 on 2017/10/30.
 */
class _Main {

    public static void main(String[] args) {
        String address = "http://127.0.0.1:6666/ws";
        Endpoint.publish(address, new MyWebServiceImpl());
    }
}
